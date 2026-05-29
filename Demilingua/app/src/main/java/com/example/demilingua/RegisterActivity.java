package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.demilingua.databinding.ActivityRegisterBinding;
import com.example.demilingua.ui.auth.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.btnRegistrar.setOnClickListener(v -> validarYRegistrar());
    }

    private void validarYRegistrar() {
        String nombre = binding.etNombre.getText().toString().trim();
        String correo = binding.etCorreo.getText().toString().trim();
        String contrasena = binding.etContrasena.getText().toString().trim();
        String confirmarContrasena = binding.etConfirmarContrasena.getText().toString().trim();

        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            binding.tilCorreo.setError("Correo inválido");
            return;
        }

        if (!contrasena.equals(confirmarContrasena)) {
            binding.tilConfirmarContrasena.setError("Las contraseñas no coinciden");
            return;
        }

        viewModel.register(nombre, correo, contrasena);
    }

    private void observeViewModel() {
        viewModel.isLoading.observe(this, isLoading -> {
            binding.btnRegistrar.setEnabled(!isLoading);
        });

        viewModel.error.observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });

        viewModel.registerSuccess.observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Registro exitoso. Por favor, inicia sesión.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}