package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.databinding.ActivityCourseBinding;
import com.example.demilingua.model.Curso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class CourseActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {

    private ActivityCourseBinding binding;
    private CourseAdapter adapter;
    private final List<Curso> cursoList = new ArrayList<>();

    @Inject
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String idiomaNombre = getIntent().getStringExtra("nombre");
        int idiomaId = getIntent().getIntExtra("idiomaId", 0);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(idiomaNombre != null ? idiomaNombre : "Cursos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupRecyclerView();
        cargarCursosPorIdioma(idiomaId);
    }

    private void setupRecyclerView() {
        binding.rvModulos.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CourseAdapter(cursoList, this);
        binding.rvModulos.setAdapter(adapter);
    }

    private void cargarCursosPorIdioma(int idiomaId) {
        binding.pbCursos.setVisibility(View.VISIBLE);
        binding.tvEmpty.setVisibility(View.GONE);

        apiService.getCursos(idiomaId).enqueue(new Callback<List<Curso>>() {
            @Override
            public void onResponse(@NonNull Call<List<Curso>> call, @NonNull Response<List<Curso>> response) {
                binding.pbCursos.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    cursoList.clear();
                    cursoList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    binding.tvEmpty.setVisibility(cursoList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    Toast.makeText(CourseActivity.this, "Error al obtener cursos", Toast.LENGTH_SHORT).show();
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Curso>> call, @NonNull Throwable t) {
                binding.pbCursos.setVisibility(View.GONE);
                Toast.makeText(CourseActivity.this, "Sin conexión", Toast.LENGTH_SHORT).show();
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCourseClick(Curso curso) {
        Intent i = new Intent(this, TestActivity.class);
        i.putExtra("cursoId", curso.getId());
        i.putExtra("cursoName", curso.getNombre());
        i.putExtra("idiomaId", curso.getIdiomaId());
        startActivity(i);
    }
}