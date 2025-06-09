package com.example.demilingua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.LoginResponse;
import com.example.demilingua.controller.RetrofitClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Click en Registro
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Click en Login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validarCampos(email, password)) {
                realizarLogin(email, password);
            }
        });


    }

    private boolean validarCampos(String email, String password) {
        boolean valido = true;

        if (email.isEmpty()) {
            etEmail.setError("Ingresa tu email");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email inválido");
            valido = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingresa tu contraseña");
            valido = false;
        } else if (password.length() < 6) {
            etPassword.setError("Mínimo 6 caracteres");
            valido = false;
        }

        return valido;
    }

    private void realizarLogin(String email, String password) {
        btnLogin.setEnabled(false);

        // Crear el mapa correctamente (username/password en lugar de correo/contrasena)
        Map<String, String> data = new HashMap<>();
        data.put("username", email);  // Cambiado a "username" para coincidir con tu endpoint
        data.put("password", password);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.login(data).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        // Login exitoso
                        guardarSesion(loginResponse);
                        iniciarMainActivity();
                    } else {
                        String errorMsg = loginResponse.getMessage() != null ?
                                loginResponse.getMessage() : "Credenciales incorrectas";
                        Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejo de errores HTTP (4xx, 5xx)
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Error desconocido";
                        Toast.makeText(LoginActivity.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarSesion(LoginResponse response) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("isLoggedIn", true);
        editor.putInt("userId", response.getUserId());
        editor.putString("userEmail", etEmail.getText().toString().trim());
        editor.putString("userName", response.getNombre());

        editor.apply();
    }

    private void iniciarMainActivity() {
        Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}