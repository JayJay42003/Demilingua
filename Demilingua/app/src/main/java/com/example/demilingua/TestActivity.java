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
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestActivity extends AppCompatActivity {

    private TextView tvContenido, tvOpciones;
    private Button   btnNext;
    private TextInputEditText etRespuesta;
    private ApiService api;
    private int cursoId;

    /* lista de ejercicios cargados */
    private List<Map<String,Object>> ejercicios = new ArrayList<>();
    private int indice = 0;
    private int puntuacion = 0;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_test);

        cursoId    = getIntent().getIntExtra("cursoId", 0);

        Toolbar tb = findViewById(R.id.toolbarTest);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvContenido = findViewById(R.id.tvContenido);
        tvOpciones  = findViewById(R.id.tvOpciones);
        btnNext     = findViewById(R.id.btnNext);
        etRespuesta = findViewById(R.id.etRespuesta);

        api = RetrofitClient.getApiService();
        btnNext.setOnClickListener(v -> mostrarSiguiente());

        cargarTestAleatorio();
    }

    /* 1º: pedir un test y luego sus ejercicios */
    private void cargarTestAleatorio() {
        api.randomTest(cursoId).enqueue(new Callback<>() {
            @Override public void onResponse(Call<Map<String,Object>> c, Response<Map<String,Object>> r) {
                if (r.isSuccessful() && r.body()!=null) {
                    int testId = ((Double) r.body().get("id")).intValue(); // Gson devuelve double
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

    /* 2º: mostrar ejercicio actual */
    private void mostrarEjercicioActual() {
        if (indice >= ejercicios.size()) {
            tvContenido.setText(getString(R.string.test_finished) + ": " + puntuacion + " puntos");
            tvOpciones.setText("");
            btnNext.setEnabled(false);
            return;
        }

        Map<String,Object> ej = ejercicios.get(indice);
        tvContenido.setText((String) ej.get("contenido"));

        String op = (String) ej.get("opciones");
        tvOpciones.setText(TextUtils.isEmpty(op) ? "" :
                op.replace("[","").replace("]","").replace("\"","").replace(",", "\n"));
    }

    /* 3º: avanzar y sumar puntuación (modo demo) */
    private void mostrarSiguiente() {

        // 1. Obtener puntos y respuesta correcta del ejercicio actual
        Map<String, Object> ej  = ejercicios.get(indice);
        int puntos = ((Double) ej.get("puntos")).intValue();          // puntos del ejercicio
        String correcta = ((String) ej.get("respuesta")).trim();     // respuesta_correcta

        // 2. Respuesta del usuario (lo escrito en el EditText)
        String usuario = etRespuesta.getText() != null
                ? etRespuesta.getText().toString().trim()
                : "";

        // 3. Comparar (ignorando mayúsc/minúsc y espacios finales)
        if (!usuario.isEmpty() && usuario.equalsIgnoreCase(correcta)) {
            puntuacion += puntos;
            Toast.makeText(this,
                    "¡Correcto! +" + puntos + " puntos",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,
                    "Incorrecto. Respuesta: " + correcta,
                    Toast.LENGTH_SHORT).show();
        }

        // 4. Limpiar campo para la siguiente pregunta
        etRespuesta.setText("");

        // 5. Avanzar y mostrar
        indice++;
        mostrarEjercicioActual();
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){ finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}

