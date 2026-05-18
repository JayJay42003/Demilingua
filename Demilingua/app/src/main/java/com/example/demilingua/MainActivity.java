package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
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

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements LanguageAdapter.OnLanguageClickListener {
    private RecyclerView rvLanguages;
    private ImageButton btnProfile;
    private RankingAdapter rankingAdapter;

    private TextView tvVidas, tvRacha;
    private ImageButton btnAmigos;
    private ProgressBar pbRanking;
    private SwipeRefreshLayout swipeRefresh;
    private final List<RankingItem> rankingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, PerfilActivity.class));
        });

        tvVidas = findViewById(R.id.tvVidas);
        tvRacha = findViewById(R.id.tvRacha);
        btnAmigos = findViewById(R.id.btnAmigos);
        pbRanking = findViewById(R.id.pbRanking);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        if (btnAmigos != null) {
            btnAmigos.setOnClickListener(v -> startActivity(new Intent(this, AmigosActivity.class)));
        }

        if (swipeRefresh != null) {
            swipeRefresh.setOnRefreshListener(this::refreshData);
        }

        /*Idiomas*/
        rvLanguages = findViewById(R.id.rvLanguages);
        rvLanguages.setLayoutManager(new LinearLayoutManager(this));
        
        cargarIdiomas();

        /*Ranking*/
        RecyclerView rvRank = findViewById(R.id.rvRanking);
        rvRank.setLayoutManager(new LinearLayoutManager(this));
        rankingAdapter = new RankingAdapter(rankingList);
        rvRank.setAdapter(rankingAdapter);

        refreshData();
    }

    private void refreshData() {
        cargarIdiomas();
        cargarRanking();
        comprobarEstadoVidas();
    }

    private void cargarIdiomas() {
        ApiService api = RetrofitClient.getApiService();
        api.getIdiomas().enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<Idioma> languages = new ArrayList<>();
                    for (Map<String, String> m : response.body()) {
                        int id = Integer.parseInt(m.get("id"));
                        String nombre = m.get("nombre");
                        // El adaptador ya gestiona las banderas dinámicamente, pasamos 0 o logo
                        languages.add(new Idioma(id, nombre, R.drawable.logo));
                    }
                    LanguageAdapter adapter = new LanguageAdapter(languages, MainActivity.this);
                    rvLanguages.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void cargarRanking() {
        if (pbRanking != null) pbRanking.setVisibility(View.VISIBLE);
        ApiService api = RetrofitClient.getApiService();
        api.getRanking().enqueue(new Callback<>() {
            @Override public void onResponse(Call<List<Map<String,String>>> c,
                                             Response<List<Map<String,String>>> r) {
                if (pbRanking != null) pbRanking.setVisibility(View.GONE);
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                if (r.isSuccessful() && r.body()!=null) {
                    rankingList.clear();
                    for (Map<String,String> m : r.body()) {
                        rankingList.add(new RankingItem(
                                m.get("nombre"),
                                m.get("racha"),
                                m.get("division")));
                    }
                    rankingAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onFailure(Call<List<Map<String,String>>> c, Throwable t) {
                if (pbRanking != null) pbRanking.setVisibility(View.GONE);
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                Toast.makeText(MainActivity.this,
                        "No se pudo cargar el ranking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void comprobarEstadoVidas() {
        int userId = getSharedPreferences("AppPrefs", MODE_PRIVATE).getInt("userId", -1);
        if (userId == -1) {
            if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            return;
        }

        ApiService api = RetrofitClient.getApiService();
        api.getStatusVidas(userId).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    String vidas = response.body().get("vidas");
                    String racha = response.body().get("racha");

                    tvVidas.setText("❤️ " + vidas);
                    tvRacha.setText("🔥 " + racha);
                }
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                if (swipeRefresh != null) swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){ finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarRanking();
        comprobarEstadoVidas(); // Actualiza vidas y racha cada vez que vuelves al menú
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