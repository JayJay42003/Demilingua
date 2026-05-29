package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.data.TokenManager;
import com.example.demilingua.databinding.ActivityTestBinding;
import com.example.demilingua.model.Ejercicio;
import com.example.demilingua.model.GenericResponse;
import com.example.demilingua.model.Test;
import com.example.demilingua.utils.AudioPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class TestActivity extends AppCompatActivity {

    private ActivityTestBinding binding;

    @Inject
    ApiService api;

    @Inject
    AudioPlayer audioPlayer;

    @Inject
    TokenManager tokenManager;

    private int cursoId;
    private List<Ejercicio> ejercicios = new ArrayList<>();
    private int indice = 0;
    private int puntuacion = 0;
    private int usuarioId;
    private int idiomaId;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cursoId = getIntent().getIntExtra("cursoId", 0);
        idiomaId = getIntent().getIntExtra("idiomaId", 0);
        usuarioId = tokenManager.getUserId();

        setSupportActionBar(binding.toolbarTest);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.btnNext.setOnClickListener(v -> mostrarSiguiente());

        cargarTest();
    }

    private void cargarTest() {
        api.getTests(cursoId).enqueue(new Callback<List<Test>>() {
            @Override
            public void onResponse(@NonNull Call<List<Test>> call, @NonNull Response<List<Test>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Test test = response.body().get(0);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(test.getTitulo());
                    }
                    cargarEjercicios(test.getId());
                } else {
                    Toast.makeText(TestActivity.this, "No hay tests disponibles.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Test>> call, @NonNull Throwable t) {
                Toast.makeText(TestActivity.this, "Error de conexión.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void cargarEjercicios(int testId) {
        api.getEjercicios(testId).enqueue(new Callback<List<Map<String, Object>>>() {
            @Override
            public void onResponse(@NonNull Call<List<Map<String, Object>>> call, @NonNull Response<List<Map<String, Object>>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ejercicios.clear();
                    for (Map<String, Object> eMap : response.body()) {
                        Ejercicio ej = new Ejercicio();
                        
                        if (eMap.get("id") != null) ej.setId(((Double) eMap.get("id")).intValue());
                        ej.setTipo((String) eMap.get("tipo"));
                        if (eMap.get("puntuacion") != null) ej.setPuntos(((Double) eMap.get("puntuacion")).intValue());
                        
                        List<Map<String, Object>> objetos = (List<Map<String, Object>>) eMap.get("objetos");
                        if (objetos != null && !objetos.isEmpty()) {
                            Map<String, Object> firstObj = objetos.get(0);
                            ej.setContenido((String) firstObj.get("contenido"));
                            ej.setRespuesta((String) firstObj.get("respuesta_correcta"));
                            ej.setOpciones((String) firstObj.get("opciones"));
                        }
                        ejercicios.add(ej);
                    }
                    
                    binding.pbTest.setMax(ejercicios.size());
                    indice = 0;
                    puntuacion = 0;
                    mostrarEjercicioActual();
                } else {
                    Toast.makeText(TestActivity.this, "No hay ejercicios.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Map<String, Object>>> call, @NonNull Throwable t) {
                Toast.makeText(TestActivity.this, "Error cargando ejercicios.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void mostrarEjercicioActual() {
        if (indice >= ejercicios.size()) {
            terminarTest();
            return;
        }

        Ejercicio ej = ejercicios.get(indice);
        binding.tvContenido.setText(ej.getContenido());
        binding.pbTest.setProgress(indice + 1);

        String op = ej.getOpciones();
        binding.tvOpciones.setText(TextUtils.isEmpty(op) ? "" : op.replace("[", "").replace("]", "").replace("\"", "").replace(",", "\n"));

        String audioFileName = "audio_ejercicio_" + ej.getId();
        audioPlayer.playFromRaw(audioFileName);
    }

    private void mostrarSiguiente() {
        if (indice >= ejercicios.size()) return;

        Ejercicio ej = ejercicios.get(indice);
        int puntosObtenidos = (ej.getPuntos() != null) ? ej.getPuntos() : 0;
        String correcta = ej.getRespuesta() != null ? ej.getRespuesta().trim() : "";
        String usuario = binding.etRespuesta.getText() != null ? binding.etRespuesta.getText().toString().trim() : "";

        if (usuario.equalsIgnoreCase(correcta)) {
            puntuacion += puntosObtenidos;
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            avanzarEjercicio();
        } else {
            Toast.makeText(this, "Incorrecto. Era: " + correcta, Toast.LENGTH_SHORT).show();
            restarVida();
        }
    }

    private void avanzarEjercicio() {
        binding.etRespuesta.setText("");
        indice++;
        mostrarEjercicioActual();
    }

    private void restarVida() {
        api.perderVida(usuarioId).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String status = response.body().getStatus();
                    if ("sin_vidas".equals(status)) {
                        Toast.makeText(TestActivity.this, "¡Te has quedado sin vidas!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        avanzarEjercicio();
                    }
                } else {
                    avanzarEjercicio();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                avanzarEjercicio();
            }
        });
    }

    private void terminarTest() {
        binding.tvContenido.setText("Test finalizado: " + puntuacion + " XP");
        binding.tvOpciones.setText("");
        binding.btnNext.setEnabled(false);
        binding.etRespuesta.setEnabled(false);

        api.insertPoints(usuarioId, idiomaId, puntuacion).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                Toast.makeText(TestActivity.this, "Puntuación guardada", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.stop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}