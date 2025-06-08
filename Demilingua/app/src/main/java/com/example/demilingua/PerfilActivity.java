package com.example.demilingua;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Activity de perfil sencillo que muestra avatar, nombre y correo,
 * y ofrece opciones para editar datos o cerrar sesión.
 */
public class PerfilActivity extends AppCompatActivity {

    private static final String PREFS = "AppPrefs";

    private ImageView ivProfile;
    private TextView tvName, tvEmail;
    private Button btnEdit, btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);     // usa el ScrollView modelo

        // ───── Vistas ─────
        ivProfile = findViewById(R.id.ivProfile);
        tvName    = findViewById(R.id.tvName);
        tvEmail   = findViewById(R.id.tvEmail);
        btnEdit   = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // ───── Cargar datos del usuario ─────
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        String nombre = prefs.getString("userName", "Usuario Ejemplo");
        String correo = prefs.getString("userEmail", "usuario@example.com");
        tvName.setText(nombre);
        tvEmail.setText(correo);

        // (Opcional) si guardas la URI de la foto, cárgala con Glide/Picasso.
        // Drawable placeholder incluido en res/drawable/profile.png
        Drawable avatar = getResources().getDrawable(R.drawable.profile, getTheme());
        ivProfile.setImageDrawable(avatar);

        // ───── Editar perfil ─────
        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(this, EditProfileActivity.class);
            startActivity(i);
        });

        // ───── Cerrar sesión ─────
        btnLogout.setOnClickListener(v -> mostrarDialogoLogout());
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
