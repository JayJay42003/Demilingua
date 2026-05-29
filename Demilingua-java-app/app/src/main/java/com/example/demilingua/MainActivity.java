package com.example.demilingua;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demilingua.data.TokenManager;
import com.example.demilingua.databinding.ActivityMainBinding;
import com.example.demilingua.model.Idioma;
import com.example.demilingua.ui.home.HomeViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements LanguageAdapter.OnLanguageClickListener {
    private ActivityMainBinding binding;
    private HomeViewModel viewModel;
    private LanguageAdapter languageAdapter;
    private RankingAdapter rankingAdapter;
    private int usuarioId;

    @Inject
    TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        usuarioId = tokenManager.getUserId();

        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        setupRecyclerViews();
        setupListeners();
        observeViewModel();

        viewModel.loadData(usuarioId);
    }

    private void setupRecyclerViews() {
        binding.rvLanguages.setLayoutManager(new LinearLayoutManager(this));
        languageAdapter = new LanguageAdapter(new ArrayList<>(), this);
        binding.rvLanguages.setAdapter(languageAdapter);

        binding.rvRanking.setLayoutManager(new LinearLayoutManager(this));
        rankingAdapter = new RankingAdapter(new ArrayList<>());
        binding.rvRanking.setAdapter(rankingAdapter);
    }

    private void setupListeners() {
        binding.btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, PerfilActivity.class));
        });

        binding.btnAmigos.setOnClickListener(v -> {
            startActivity(new Intent(this, AmigosActivity.class));
        });

        binding.swipeRefresh.setOnRefreshListener(() -> viewModel.loadData(usuarioId));
    }

    private void observeViewModel() {
        viewModel.idiomas.observe(this, idiomas -> {
            languageAdapter.updateData(idiomas);
        });

        viewModel.ranking.observe(this, ranking -> {
            rankingAdapter.updateData(ranking);
        });

        viewModel.status.observe(this, status -> {
            if (status != null) {
                binding.tvVidas.setText("❤️ " + status.getVidas());
                binding.tvRacha.setText("🔥 " + status.getRacha());
            }
        });

        viewModel.isLoading.observe(this, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
        });
    }

    @Override
    public void onLanguageClick(Idioma idioma) {
        Intent intent = new Intent(this, CourseActivity.class);
        intent.putExtra("idiomaId", idioma.getId());
        intent.putExtra("nombre", idioma.getNombre());
        startActivity(intent);
    }
}