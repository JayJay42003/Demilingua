package com.example.demilingua;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class CursoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        // Obtener idioma seleccionado
        String idioma = getIntent().getStringExtra("nombre");
        int id = getIntent().getIntExtra("id",0);

        // Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(idioma);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Aquí cargarías los módulos/lecciones del curso
        cargarContenidoCurso(id);
    }

    private void cargarContenidoCurso(int id) {
        // Implementar lógica para cargar:
        // 1. Módulos/lecciones desde base de datos o API

        // 2. Progreso del usuario

        // 3. Adaptar contenido según nivel

    }
}