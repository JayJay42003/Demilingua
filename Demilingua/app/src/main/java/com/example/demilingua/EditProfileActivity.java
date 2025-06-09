package com.example.demilingua;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.RetrofitClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Permite al usuario editar nombre, correo **y contraseña**.
 * Los datos se guardan en SharedPreferences.
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final String PREFS = "AppPrefs";

    private EditText etName, etEmail, etPassword, etConfirm;
    private Button btnSave;
    private SharedPreferences prefs;
    private ApiService api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);   // tu layout con los nuevos campos

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Referencias
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirm = findViewById(R.id.etConfirmarContrasena);
        btnSave = findViewById(R.id.btnSave);

        // Cargar datos actuales
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        etName.setText(prefs.getString("userName", ""));
        etEmail.setText(prefs.getString("userEmail", ""));

        // Retrofit
        api = RetrofitClient.getApiService();

        btnSave.setOnClickListener(v -> guardarCambios());
    }


    private void guardarCambios() {
        String nombre = etName.getText().toString().trim();
        String correo = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();
        String confirm = etConfirm.getText().toString();

        // Validar nombre / correo
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo)) {
            Toast.makeText(this, "Rellenar todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar contraseña si el usuario escribió algo
        if (!TextUtils.isEmpty(pass) || !TextUtils.isEmpty(confirm)) {
            if (!pass.equals(confirm)) {
                Toast.makeText(this, "Contraseñas no son iguales", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass.length() < 6) {
                Toast.makeText(this, "Contraseña muy corta", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        Map<String, String> body = new HashMap<>();
        body.put("id"     , String.valueOf(prefs.getInt("userId", 0)));
        body.put("nombre" , nombre);
        body.put("correo" , correo);
        body.put("contrasena", pass);   // Puede ir vacío

        // Llamada Retrofit
        Call<Map<String,String>> call = api.updateUser(body);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String,String>> call,
                                   Response<Map<String,String>> res) {

                if (res.isSuccessful() && res.body() != null
                        && "ok".equals(res.body().get("status"))) {


                    prefs.edit()
                            .putString("userName" , nombre)
                            .putString("userEmail", correo)
                            .apply();


                    Toast.makeText(EditProfileActivity.this,
                            "Perfil actualizado",
                            Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(EditProfileActivity.this,
                            "Error: " + obtenerMensaje(res),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this,
                        "Sin conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private String obtenerMensaje(Response<Map<String,String>> res) {
        if (res.body() != null && res.body().get("message") != null) {
            return res.body().get("message");
        } else {
            return "Código " + res.code();
        }
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
