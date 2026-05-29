package com.example.demilingua.data;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.model.Idioma;
import com.example.demilingua.model.RankingItem;
import com.example.demilingua.model.LoginRequest;
import com.example.demilingua.model.LoginResponse;
import com.example.demilingua.model.GenericResponse;
import com.example.demilingua.model.StatusVidas;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import retrofit2.Call;

@Singleton
public class DemilinguaRepository {
    private final ApiService apiService;

    @Inject
    public DemilinguaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<List<Idioma>> getIdiomas() {
        return apiService.getIdiomas();
    }

    public Call<List<RankingItem>> getGlobalRanking() {
        return apiService.getRanking();
    }

    public Call<StatusVidas> getGamificationStatus(int usuarioId) {
        return apiService.getStatusVidas(usuarioId);
    }

    public Call<LoginResponse> login(String correo, String contrasena) {
        return apiService.login(new LoginRequest(correo, contrasena));
    }

    public Call<GenericResponse> register(String nombre, String correo, String contrasena) {
        return apiService.register(nombre, correo, contrasena);
    }
}