package com.example.demilingua;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.LoginResponse;
import com.example.demilingua.controller.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
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
        setContentView(R.layout.activity_register);

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
        btnRegistrar.setEnabled(false);
        ApiService apiService = RetrofitClient.getApiService();
        
        // Se pasan los parámetros individualmente como requiere la interfaz ApiService
        apiService.register(nombre, correo, contrasena).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                btnRegistrar.setEnabled(true);
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, String> bodyMap = response.body();

                    if ("ok".equals(bodyMap.get("status"))) {
                        // Crear LoginResponse a partir del Map para mantener compatibilidad
                        LoginResponse loginResponse = new LoginResponse();
                        loginResponse.setStatus("ok");
                        loginResponse.setNombre(bodyMap.get("nombre"));
                        String userIdStr = bodyMap.get("user_id");
                        if (userIdStr != null) {
                            try {
                                loginResponse.setUser_id(Integer.parseInt(userIdStr));
                            } catch (NumberFormatException e) {
                                Log.e("RegisterActivity", "Error al parsear user_id", e);
                            }
                        }

                        guardarSesion(loginResponse);
                        
                        // Guardar en UsuarioPrefs como en el código original
                        getSharedPreferences("UsuarioPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("nombre", nombre)
                                .putString("correo", correo)
                                .apply();

                        Toast.makeText(RegisterActivity.this, "Registro exitoso para: " + nombre, Toast.LENGTH_SHORT).show();
                        finish(); // Regresar a Login
                    } else {
                        String errorMsg = bodyMap.get("message") != null ?
                                bodyMap.get("message") : "Error en el registro";
                        Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try (ResponseBody errorBody = response.errorBody()) {
                        String errorBodyString = errorBody != null ? errorBody.string() : "Error desconocido";
                        Toast.makeText(RegisterActivity.this, "Error: " + errorBodyString, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("RegisterActivity", "Error al leer errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Map<String, String>> call, @NonNull Throwable t) {
                btnRegistrar.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarSesion(LoginResponse response) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("isLoggedIn", true);
        editor.putInt("userId", response.getUser_id());
        editor.putString("userEmail", etCorreo.getText().toString().trim());
        editor.putString("userName", response.getNombre());

        editor.apply();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){ finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}