package com.example.demilingua;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.data.TokenManager;
import com.example.demilingua.databinding.ActivityAmigosBinding;
import com.example.demilingua.model.Amistad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class AmigosActivity extends AppCompatActivity {

    private ActivityAmigosBinding binding;
    private int usuarioId;

    @Inject
    ApiService api;

    @Inject
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAmigosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarAmigos);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mis Amigos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        usuarioId = getSharedPreferences("demilingua_prefs", MODE_PRIVATE).getInt("userId", 0);

        binding.rvAmigos.setLayoutManager(new LinearLayoutManager(this));

        binding.btnBuscarAmigo.setOnClickListener(v -> buscarUsuarios());

        cargarAmigos();
    }

    private void cargarAmigos() {
        api.getFriends(usuarioId).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(@NonNull Call<List<Map<String, String>>> call, @NonNull Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Amistad> amistades = new ArrayList<>();
                    for (Map<String, String> map : response.body()) {
                        Amistad a = new Amistad();
                        a.setAmigoId(Integer.parseInt(map.getOrDefault("amigo_id", "0")));
                        a.setNombre(map.get("nombre"));
                        a.setEstado(map.get("estado"));
                        a.setPuntos(Integer.parseInt(map.getOrDefault("puntos", "0")));
                        amistades.add(a);
                    }
                    binding.rvAmigos.setAdapter(new AmigosAdapter(amistades, usuarioId, false, api));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Map<String, String>>> call, @NonNull Throwable t) {
                Toast.makeText(AmigosActivity.this, "Error al cargar amigos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarUsuarios() {
        String query = binding.etBuscarAmigo.getText().toString().trim();
        if (query.isEmpty()) {
            cargarAmigos();
            return;
        }

        api.searchUsers(query).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(@NonNull Call<List<Map<String, String>>> call, @NonNull Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Amistad> amistades = new ArrayList<>();
                    for (Map<String, String> map : response.body()) {
                        Amistad a = new Amistad();
                        // El endpoint de búsqueda devuelve objetos Usuario, mapeamos a Amistad para el adapter
                        a.setAmigoId(Integer.parseInt(map.getOrDefault("id", "0")));
                        a.setNombre(map.get("nombre"));
                        a.setPuntos(0); // Búsqueda no suele traer puntos
                        amistades.add(a);
                    }
                    binding.rvAmigos.setAdapter(new AmigosAdapter(amistades, usuarioId, true, api));
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Map<String, String>>> call, @NonNull Throwable t) {
                Toast.makeText(AmigosActivity.this, "Error en la búsqueda", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}