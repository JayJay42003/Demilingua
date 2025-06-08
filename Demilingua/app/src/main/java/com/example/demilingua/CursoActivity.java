// CursoActivity.java
package com.example.demilingua;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.CourseResponse;
import com.example.demilingua.controller.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra la lista de cursos de un idioma (id y nombre
 * recibidos por Intent extras:  "id"  y  "nombre").
 */
public class CursoActivity extends AppCompatActivity {

    private RecyclerView rvCursos;
    private TextView     tvEmpty;
    private CourseAdapter adapter;
    private final List<CourseResponse> cursoList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        // ───────────────────────── Toolbar ─────────────────────────
        String idiomaNombre = getIntent().getStringExtra("nombre");
        int    idiomaId     = getIntent().getIntExtra("id", 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(idiomaNombre != null ? idiomaNombre : "Cursos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ───────────────────────── RecyclerView ─────────────────────────
        rvCursos = findViewById(R.id.rvModulos);
        rvCursos.setLayoutManager(new GridLayoutManager(this, 2));   // ← cuadrícula 2×N
        adapter  = new CourseAdapter(cursoList);
        rvCursos.setAdapter(adapter);

        // Texto “vacío” (añádelo al XML debajo del RecyclerView o superpuesto)
        tvEmpty = findViewById(R.id.tvEmpty);

        // ───────────────────────── Retrofit ─────────────────────────
        apiService = RetrofitClient.getApiService();

        // Cargar cursos por idioma
        cargarCursosPorIdioma(idiomaId);
    }

    /**
     * Llama a la API, recibe la lista y actualiza el RecyclerView.
     */
    private void cargarCursosPorIdioma(int idiomaId) {
        Call<List<CourseResponse>> call = apiService.course(idiomaId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CourseResponse>> call,
                                   Response<List<CourseResponse>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    cursoList.clear();
                    cursoList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    tvEmpty.setVisibility(cursoList.isEmpty() ? View.VISIBLE : View.GONE);

                } else {  // HTTP 4xx / 5xx
                    String err = "Código: " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            err += " - " + response.errorBody().string();
                        }
                    } catch (IOException ignored) {
                    }

                    Toast.makeText(CursoActivity.this,
                            "Error al obtener cursos: " + err,
                            Toast.LENGTH_LONG).show();
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CourseResponse>> call,
                                  @NonNull Throwable t) {
                Log.e("CursoActivity", "Fallo Retrofit", t);
                Toast.makeText(CursoActivity.this,
                        "Sin conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}
