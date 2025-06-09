package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.controller.RetrofitClient;
import com.example.demilingua.model.Idioma;
import com.example.demilingua.model.RankingItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LanguageAdapter.OnLanguageClickListener {
    private RecyclerView rvLanguages;
    private ImageButton btnProfile;
    private RankingAdapter rankingAdapter;
    private final List<RankingItem> rankingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Botón Perfil
        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, PerfilActivity.class));
        });

        // RecyclerView
        rvLanguages = findViewById(R.id.rvLanguages);
        rvLanguages.setLayoutManager(new LinearLayoutManager(this));

        List<Idioma> languages = new ArrayList<>();
        languages.add(new Idioma(1, "Español", R.drawable.espa_a));
        languages.add(new Idioma(2, "Ingles", R.drawable.reino_unido));
        languages.add(new Idioma(3, "Frances", R.drawable.francia));

        LanguageAdapter adapter = new LanguageAdapter(languages,this);
        rvLanguages.setAdapter(adapter);

        /* RecyclerView Ranking */
        RecyclerView rvRank = findViewById(R.id.rvRanking);
        rvRank.setLayoutManager(new LinearLayoutManager(this));
        rankingAdapter = new RankingAdapter(rankingList);
        rvRank.setAdapter(rankingAdapter);

        cargarRanking();

    }

    private void cargarRanking() {
        ApiService api = RetrofitClient.getApiService();
        api.getRanking().enqueue(new Callback<>() {
            @Override public void onResponse(Call<List<Map<String,String>>> c,
                                             Response<List<Map<String,String>>> r) {
                if (r.isSuccessful() && r.body()!=null) {
                    rankingList.clear();
                    for (Map<String,String> m : r.body()) {
                        rankingList.add(new RankingItem(
                                m.get("usuario"),
                                m.get("idioma"),
                                Integer.parseInt(m.get("puntos"))));
                    }
                    rankingAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<List<Map<String,String>>> c, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "No se pudo cargar el ranking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){ finish(); return true; }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLanguageClick(Idioma idioma) {
        // Navegar a la actividad de curso
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra("nombre", idioma.getName());
        intent.putExtra("id", idioma.getId());

        startActivity(intent);
    }
}