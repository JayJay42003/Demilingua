package com.example.demilingua.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demilingua.data.DemilinguaRepository;
import com.example.demilingua.model.Idioma;
import com.example.demilingua.model.RankingItem;
import com.example.demilingua.model.StatusVidas;

import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final DemilinguaRepository repository;

    private final MutableLiveData<List<Idioma>> _idiomas = new MutableLiveData<>();
    public LiveData<List<Idioma>> idiomas = _idiomas;

    private final MutableLiveData<List<RankingItem>> _ranking = new MutableLiveData<>();
    public LiveData<List<RankingItem>> ranking = _ranking;

    private final MutableLiveData<StatusVidas> _status = new MutableLiveData<>();
    public LiveData<StatusVidas> status = _status;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    @Inject
    public HomeViewModel(DemilinguaRepository repository) {
        this.repository = repository;
    }

    public void loadData(int usuarioId) {
        _isLoading.setValue(true);
        loadIdiomas();
        loadRanking();
        loadStatus(usuarioId);
    }

    private void loadIdiomas() {
        repository.getIdiomas().enqueue(new Callback<List<Idioma>>() {
            @Override
            public void onResponse(Call<List<Idioma>> call, Response<List<Idioma>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _idiomas.setValue(response.body());
                }
                checkLoadingFinished();
            }

            @Override
            public void onFailure(Call<List<Idioma>> call, Throwable t) {
                checkLoadingFinished();
            }
        });
    }

    private void loadRanking() {
        repository.getGlobalRanking().enqueue(new Callback<List<RankingItem>>() {
            @Override
            public void onResponse(Call<List<RankingItem>> call, Response<List<RankingItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _ranking.setValue(response.body());
                }
                checkLoadingFinished();
            }

            @Override
            public void onFailure(Call<List<RankingItem>> call, Throwable t) {
                checkLoadingFinished();
            }
        });
    }

    private void loadStatus(int usuarioId) {
        repository.getGamificationStatus(usuarioId).enqueue(new Callback<StatusVidas>() {
            @Override
            public void onResponse(Call<StatusVidas> call, Response<StatusVidas> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _status.setValue(response.body());
                }
                checkLoadingFinished();
            }

            @Override
            public void onFailure(Call<StatusVidas> call, Throwable t) {
                checkLoadingFinished();
            }
        });
    }

    private void checkLoadingFinished() {
        _isLoading.setValue(false);
    }
}