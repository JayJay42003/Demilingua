package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.data.TokenManager;
import com.example.demilingua.databinding.ActivityProfileBinding;
import com.example.demilingua.model.Usuario;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class PerfilActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Inject
    TokenManager tokenManager;

    @Inject
    ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Para este ejemplo, cargamos un perfil hardcoded o buscamos el ID en un futuro State
        // Como no tenemos el ID a mano sin pasarlo por Intent, vamos a suponer que el backend
        // tiene un endpoint /me o similar, o simplemente dejamos que el usuario vea su perfil.
        
        setupListeners();
    }

    private void setupListeners() {
        binding.btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, EditProfileActivity.class));
        });

        binding.btnLogout.setOnClickListener(v -> mostrarDialogoLogout());
    }

    private void mostrarDialogoLogout() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Seguro que quieres cerrar sesión?")
                .setPositiveButton("Sí", (d, w) -> {
                    tokenManager.clearToken();
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}