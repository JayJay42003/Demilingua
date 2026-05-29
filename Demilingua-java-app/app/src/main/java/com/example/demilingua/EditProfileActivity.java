package com.example.demilingua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.data.TokenManager;
import com.example.demilingua.databinding.ActivityEditProfileBinding;
import com.example.demilingua.model.GenericResponse;

import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private int usuarioId;

    @Inject
    ApiService api;

    @Inject
    TokenManager tokenManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Editar Perfil");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        usuarioId = getSharedPreferences("demilingua_prefs", MODE_PRIVATE).getInt("userId", 0);

        binding.btnSave.setOnClickListener(v -> guardarCambios());
        binding.btnDeleteAccount.setOnClickListener(v -> confirmarBorradoCuenta());
    }

    private void confirmarBorradoCuenta() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("¿Borrar cuenta?")
                .setMessage("Esta acción es permanente.")
                .setPositiveButton("Borrar", (d, w) -> borrarCuenta())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void borrarCuenta() {
        binding.pbEditProfile.setVisibility(View.VISIBLE);
        api.deleteUser(usuarioId).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                if (response.isSuccessful()) {
                    tokenManager.clearToken();
                    Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    binding.pbEditProfile.setVisibility(View.GONE);
                    Toast.makeText(EditProfileActivity.this, "Error al borrar cuenta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                binding.pbEditProfile.setVisibility(View.GONE);
                Toast.makeText(EditProfileActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarCambios() {
        String nombre = binding.etName.getText().toString().trim();
        String correo = binding.etEmail.getText().toString().trim();
        String pass = binding.etPassword.getText().toString();
        String confirm = binding.etConfirmarContrasena.getText().toString();

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo)) {
            Toast.makeText(this, "Nombre y correo son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(pass) && !pass.equals(confirm)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        binding.pbEditProfile.setVisibility(View.VISIBLE);
        binding.btnSave.setEnabled(false);

        api.updateUser(usuarioId, nombre, correo, pass).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(@NonNull Call<GenericResponse> call, @NonNull Response<GenericResponse> response) {
                binding.pbEditProfile.setVisibility(View.GONE);
                binding.btnSave.setEnabled(true);

                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Perfil actualizado", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Error al actualizar", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenericResponse> call, @NonNull Throwable t) {
                binding.pbEditProfile.setVisibility(View.GONE);
                binding.btnSave.setEnabled(true);
                Toast.makeText(EditProfileActivity.this, "Error de conexión", Toast.LENGTH_LONG).show();
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
}