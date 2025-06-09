// CursoActivity.java
package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.RetrofitClient;
import com.example.demilingua.model.Curso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra la lista de cursos de un idioma (id y nombre
 * recibidos por Intent extras:  "id"  y  "nombre").
 */
public class CursoActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener{

    private RecyclerView rvCursos;
    private TextView tvEmpty;
    private CourseAdapter adapter;
    private final List<Curso> cursoList = new ArrayList<>();
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
        adapter  = new CourseAdapter(cursoList,this);
        rvCursos.setAdapter(adapter);

        // Texto vacío
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
        Call<List<Map<String,String>>> call = apiService.course(idiomaId);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Map<String,String>>> call,
                                   Response<List<Map<String,String>>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String,String>> raw = response.body();
                    cursoList.clear();

                    for (Map<String,String> m : raw) {
                        cursoList.add(new Curso(
                                Integer.parseInt(m.get("id")),
                                m.get("nombre"),
                                m.get("descripcion"),
                                m.get("dificultad"),
                                Integer.parseInt(m.get("idioma_id"))
                        ));
                    }
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
            public void onFailure(@NonNull Call<List<Map<String,String>>> call,
                                  @NonNull Throwable t) {
                Log.e("CursoActivity", "Fallo Retrofit", t);
                Toast.makeText(CursoActivity.this,
                        "Sin conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onCourseClick(Curso curso) {
        Intent i = new Intent(this, TestActivity.class);
        i.putExtra("cursoId", curso.getId());
        i.putExtra("cursoName", curso.getNombre());
        startActivity(i);
    }
}
