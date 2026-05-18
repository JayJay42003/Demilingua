package com.example.demilingua;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.RetrofitClient;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmigosActivity extends AppCompatActivity {

    private RecyclerView rvAmigos;
    private EditText etBuscar;
    private ImageButton btnBuscar;
    private ApiService api;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        rvAmigos = findViewById(R.id.rvAmigos);
        etBuscar = findViewById(R.id.etBuscarAmigo);
        btnBuscar = findViewById(R.id.btnBuscarAmigo);

        api = RetrofitClient.getApiService();
        usuarioId = getSharedPreferences("AppPrefs", MODE_PRIVATE).getInt("userId", -1);

        rvAmigos.setLayoutManager(new LinearLayoutManager(this));

        btnBuscar.setOnClickListener(v -> buscarUsuarios());

        cargarAmigos();
    }

    private void cargarAmigos() {
        api.getFriends(usuarioId).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rvAmigos.setAdapter(new AmigosAdapter(response.body(), usuarioId, false));
                }
            }
            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Toast.makeText(AmigosActivity.this, "Error al cargar amigos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buscarUsuarios() {
        String query = etBuscar.getText().toString().trim();
        if (query.isEmpty()) {
            cargarAmigos();
            return;
        }

        api.searchUsers(query).enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    rvAmigos.setAdapter(new AmigosAdapter(response.body(), usuarioId, true));
                }
            }
            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                Toast.makeText(AmigosActivity.this, "Error en la búsqueda", Toast.LENGTH_SHORT).show();
            }
        });
    }
}