package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.demilingua.databinding.ActivityLoginBinding;
import com.example.demilingua.ui.auth.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupListeners();
        observeViewModel();
    }

    private void setupListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(email, password);
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void observeViewModel() {
        viewModel.isLoading.observe(this, isLoading -> {
            binding.btnLogin.setEnabled(!isLoading);
            // Podríamos añadir un ProgressBar aquí en el futuro
        });

        viewModel.error.observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });

        viewModel.loginResult.observe(this, response -> {
            Toast.makeText(this, "¡Bienvenido, " + response.getNombre() + "!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }
}