package com.example.demilingua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.model.Idioma;
import com.example.demilingua.model.Usuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LanguageAdapter.OnLanguageClickListener {
    private RecyclerView rvLanguages;

    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Botón Perfil
        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, PerfilActivity.class));
        });

        // Configurar RecyclerView (lista de idiomas)
        rvLanguages = findViewById(R.id.rvLanguages);
        rvLanguages.setLayoutManager(new LinearLayoutManager(this));

        List<Idioma> languages = new ArrayList<>();
        languages.add(new Idioma(1, "Español", R.drawable.espa_a));
        languages.add(new Idioma(2, "Ingles", R.drawable.reino_unido));
        languages.add(new Idioma(3, "Frances", R.drawable.francia));

        LanguageAdapter adapter = new LanguageAdapter(languages,this);
        rvLanguages.setAdapter(adapter);

    }

    @Override
    public void onLanguageClick(Idioma idioma) {
        // Navegar a la actividad de curso
        Intent intent = new Intent(this, CursoActivity.class);
        intent.putExtra("nombre", idioma.getName());
        intent.putExtra("id", idioma.getId());

        startActivity(intent);
    }
}