package com.example.demilingua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre, etCorreo, etContrasena, etConfirmarContrasena;
    private TextInputLayout tilNombre, tilCorreo, tilContrasena, tilConfirmarContrasena;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializar vistas
        tilNombre = findViewById(R.id.tilNombre);
        tilCorreo = findViewById(R.id.tilCorreo);
        tilContrasena = findViewById(R.id.tilContrasena);
        tilConfirmarContrasena = findViewById(R.id.tilConfirmarContrasena);

        etNombre = findViewById(R.id.etNombre);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena = findViewById(R.id.etContrasena);
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> validarYRegistrar());
    }

    private void validarYRegistrar() {
        // Resetear errores
        tilNombre.setError(null);
        tilCorreo.setError(null);
        tilContrasena.setError(null);
        tilConfirmarContrasena.setError(null);

        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();
        String confirmarContrasena = etConfirmarContrasena.getText().toString().trim();

        boolean valido = true;

        // Validación de nombre
        if (nombre.isEmpty()) {
            tilNombre.setError("Ingresa tu nombre completo");
            valido = false;
        } else if (nombre.length() > 100) {
            tilNombre.setError("Máximo 100 caracteres");
            valido = false;
        }

        // Validación de correo
        if (correo.isEmpty()) {
            tilCorreo.setError("Ingresa tu correo");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.setError("Correo inválido");
            valido = false;
        } else if (correo.length() > 100) {
            tilCorreo.setError("Máximo 100 caracteres");
            valido = false;
        }

        // Validación de contraseña
        if (contrasena.isEmpty()) {
            tilContrasena.setError("Ingresa una contraseña");
            valido = false;
        } else if (contrasena.length() < 6) {
            tilContrasena.setError("Mínimo 6 caracteres");
            valido = false;
        } else if (contrasena.length() > 50) {
            tilContrasena.setError("Máximo 50 caracteres");
            valido = false;
        }

        // Validación de confirmación
        if (confirmarContrasena.isEmpty()) {
            tilConfirmarContrasena.setError("Confirma tu contraseña");
            valido = false;
        } else if (!contrasena.equals(confirmarContrasena)) {
            tilConfirmarContrasena.setError("Las contraseñas no coinciden");
            valido = false;
        }

        if (valido) {
            registrarUsuario(nombre, correo, contrasena);
        }
    }

    private void registrarUsuario(String nombre, String correo, String contrasena) {
        // Aquí iría la conexión con tu base de datos
        HashMap<String,String> datos=new HashMap<>();
        datos.put("nombre",nombre);
        datos.put("correo",correo);
        datos.put("contrasena",contrasena);

        ApiService apiService = RetrofitClient.getApiService();
        apiService.register(datos).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {
                        guardarSesion(loginResponse);
                    } else {
                        String errorMsg = loginResponse.getMessage() != null ?
                                loginResponse.getMessage() : "Credenciales no válidas";
                        Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejo de errores HTTP (4xx, 5xx)
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Error desconocido";
                        Toast.makeText(RegisterActivity.this, "Error: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnRegistrar.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(this, "Registro exitoso para: " + nombre, Toast.LENGTH_SHORT).show();

        // Ejemplo de cómo guardaríamos en SharedPreferences

        getSharedPreferences("UsuarioPrefs", MODE_PRIVATE)
            .edit()
            .putString("nombre", nombre)
            .putString("correo", correo)
            .apply();


        finish(); // Regresar a Login
    }

    private void guardarSesion(LoginResponse response) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("isLoggedIn", true);
        editor.putString("token", response.getToken());
        editor.putInt("userId", response.getUserId());
        editor.putString("userEmail", etCorreo.getText().toString().trim()); // del EditText
        editor.putString("userName", response.getNombre());

        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
