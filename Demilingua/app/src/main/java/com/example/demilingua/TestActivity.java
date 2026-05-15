package com.example.demilingua;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    private TextView tvContenido, tvOpciones;
    private Button btnNext;
    private TextInputEditText etRespuesta;
    private ApiService api;
    private int cursoId;

    /*lista de ejercicios cargados*/
    private List<Map<String,Object>> ejercicios = new ArrayList<>();
    private int indice = 0;
    private int puntuacion = 0;
    int usuarioId;
    int idiomaId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_test);

        cursoId  = getIntent().getIntExtra("cursoId", 0);
        usuarioId = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getInt("userId", 0);
        idiomaId  = getIntent().getIntExtra("idiomaId", 0);

        Toolbar tb = findViewById(R.id.toolbarTest);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvContenido = findViewById(R.id.tvContenido);
        tvOpciones  = findViewById(R.id.tvOpciones);
        btnNext = findViewById(R.id.btnNext);
        etRespuesta = findViewById(R.id.etRespuesta);

        api = RetrofitClient.getApiService();
        btnNext.setOnClickListener(v -> mostrarSiguiente());

        cargarTestAleatorio();
    }

    private void cargarTestAleatorio() {
        api.randomTest(cursoId).enqueue(new Callback<>() {
            @Override public void onResponse(Call<Map<String,Object>> c, Response<Map<String,Object>> r) {
                if (r.isSuccessful() && r.body()!=null) {
                    int testId = pasarInt(r.body().get("id")); // Gson devuelve double
                    String titulo = (String) r.body().get("titulo");
                    getSupportActionBar().setTitle(titulo);
                    cargarEjercicios(testId);
                } else finish();
            }
            @Override public void onFailure(Call<Map<String,Object>> c, Throwable t) { finish(); }
        });
    }

    private void cargarEjercicios(int testId) {
        api.exercises(testId).enqueue(new Callback<>() {
            @Override public void onResponse(Call<List<Map<String,Object>>> c, Response<List<Map<String,Object>>> r) {
                if (r.isSuccessful() && r.body()!=null && !r.body().isEmpty()) {
                    ejercicios.clear();
                    ejercicios.addAll(r.body());
                    indice = 0;
                    puntuacion = 0;
                    mostrarEjercicioActual();
                } else finish();
            }
            @Override public void onFailure(Call<List<Map<String,Object>>> c, Throwable t) { finish(); }
        });
    }


    private void mostrarEjercicioActual() {
        if (indice >= ejercicios.size()) {
            tvContenido.setText(getString(R.string.test_finished) + ": " + puntuacion + " puntos");
            tvOpciones.setText("");
            btnNext.setEnabled(false);
            etRespuesta.setEnabled(false);

            // Pasamos los 3 enteros directamente, sin crear el HashMap
            api.insertPoints(usuarioId, idiomaId, puntuacion).enqueue(new Callback<Map<String, String>>() {
                @Override public void onResponse(Call<Map<String,String>> c,
                                                 Response<Map<String,String>> r) {
                    if (r.isSuccessful()) {
                        Toast.makeText(TestActivity.this,
                                "Puntuación guardada", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TestActivity.this,
                                "No se pudo guardar la puntuación", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(Call<Map<String,String>> c, Throwable t) {
                    Toast.makeText(TestActivity.this,
                            "Error al enviar puntos", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }

        Map<String,Object> ej = ejercicios.get(indice);
        tvContenido.setText((String) ej.get("contenido"));

        String op = (String) ej.get("opciones");
        tvOpciones.setText(TextUtils.isEmpty(op) ? "" :
        op.replace("[","").replace("]","").replace("\"","").replace(",", "\n"));
    }


    private void mostrarSiguiente() {
        Map<String, Object> ej = ejercicios.get(indice);
        int puntos = pasarInt(ej.get("puntos"));
        String correcta = ((String) ej.get("respuesta")).trim();

        String usuario = etRespuesta.getText() != null
                ? etRespuesta.getText().toString().trim()
                : "";

        if (!usuario.isEmpty() && usuario.equalsIgnoreCase(correcta)) {
            puntuacion += puntos;
            Toast.makeText(this, "¡Correcto! +" + puntos + " puntos", Toast.LENGTH_SHORT).show();
            avanzarEjercicio(); // Si acierta, pasa al siguiente
        } else {
            Toast.makeText(this, "Incorrecto. Era: " + correcta, Toast.LENGTH_SHORT).show();
            restarVida(); // Si falla, llamamos a la API para quitar vida
        }
    }

    private void avanzarEjercicio() {
        etRespuesta.setText("");
        indice++;
        if (indice < ejercicios.size()) {
            mostrarEjercicioActual();
        } else {
            terminarTest(); // Si ya no hay ejercicios, guardamos los puntos
        }
    }

    private void restarVida() {
        api.perderVida(usuarioId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().get("status");
                    if ("sin_vidas".equals(status)) {
                        Toast.makeText(TestActivity.this, "¡Te has quedado sin vidas!", Toast.LENGTH_LONG).show();
                        finish(); // Echa al usuario si sus vidas llegan a 0
                    } else {
                        avanzarEjercicio(); // Aún le quedan vidas, avanza
                    }
                }
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                avanzarEjercicio(); // Fallback por si falla internet
            }
        });
    }

    private void terminarTest() {
        // Al terminar, sumamos la experiencia total conseguida
        api.insertPoints(usuarioId, idiomaId, puntuacion).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Toast.makeText(TestActivity.this, "Test finalizado. Total: " + puntuacion + " XP", Toast.LENGTH_LONG).show();
                finish(); // Volver al menú
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                finish();
            }
        });
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){ finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    private int pasarInt(Object o) {
        if (o instanceof Number) return ((Number) o).intValue();
        if (o instanceof String) return Integer.parseInt(o.toString());
        return 0;
    }
}

