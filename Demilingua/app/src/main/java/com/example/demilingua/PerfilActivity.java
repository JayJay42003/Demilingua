package com.example.demilingua;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.RetrofitClient;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Activity de perfil sencillo que muestra avatar, nombre y correo,
 * y ofrece opciones para editar datos o cerrar sesión.
 */
public class PerfilActivity extends AppCompatActivity {

    private static final String PREFS = "AppPrefs";

    private ImageView ivProfile;
    private TextView tvName, tvEmail;
    private ProgressBar pbProfile;
    private Button btnEdit, btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);     // usa el ScrollView modelo

        // ───── Vistas ─────
        ivProfile = findViewById(R.id.ivProfile);
        tvName    = findViewById(R.id.tvName);
        tvEmail   = findViewById(R.id.tvEmail);
        pbProfile = findViewById(R.id.pbProfile);
        btnEdit   = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // ───── Cargar datos iniciales del usuario (caché) ─────
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String nombre = prefs.getString("userName", "Usuario Ejemplo");
        String correo = prefs.getString("userEmail", "usuario@example.com");
        int userId = prefs.getInt("userId", -1);
        
        tvName.setText(nombre);
        tvEmail.setText(correo);


        Drawable avatar = getResources().getDrawable(R.drawable.profile, getTheme());
        ivProfile.setImageDrawable(avatar);

        // ───── Actualizar datos desde el servidor ─────
        if (userId != -1) {
            cargarDatosServidor(userId);
        }

        // ───── Editar perfil ─────
        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, EditProfileActivity.class);
            startActivity(i);
        });

        // ───── Cerrar sesión ─────
        btnLogout.setOnClickListener(v -> mostrarDialogoLogout());
    }

    private void cargarDatosServidor(int userId) {
        if (pbProfile != null) pbProfile.setVisibility(View.VISIBLE);
        
        ApiService api = RetrofitClient.getApiService();
        api.getUserById(userId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (pbProfile != null) pbProfile.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, String> user = response.body();
                    if ("ok".equals(user.get("status"))) {
                        String nombre = user.get("nombre");
                        String correo = user.get("correo");
                        
                        tvName.setText(nombre);
                        tvEmail.setText(correo);
                        
                        // Actualizar caché
                        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                                .putString("userName", nombre)
                                .putString("userEmail", correo)
                                .apply();
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                if (pbProfile != null) pbProfile.setVisibility(View.GONE);
                Toast.makeText(PerfilActivity.this, "Error al sincronizar perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDialogoLogout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage("¿Seguro que quieres cerrar sesión?")
                .setPositiveButton(R.string.yes, (d, w) -> {
                    getSharedPreferences(PREFS, MODE_PRIVATE)
                            .edit()
                            .putBoolean("isLoggedIn", false)
                            .apply();
                    startActivity(new Intent(this, LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
