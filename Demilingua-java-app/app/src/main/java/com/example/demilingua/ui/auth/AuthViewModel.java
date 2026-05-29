package com.example.demilingua.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.demilingua.data.DemilinguaRepository;
import com.example.demilingua.data.TokenManager;
import com.example.demilingua.model.LoginResponse;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final DemilinguaRepository repository;
    private final TokenManager tokenManager;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private final MutableLiveData<LoginResponse> _loginResult = new MutableLiveData<>();
    public LiveData<LoginResponse> loginResult = _loginResult;

    private final MutableLiveData<Boolean> _registerSuccess = new MutableLiveData<>();
    public LiveData<Boolean> registerSuccess = _registerSuccess;

    @Inject
    public AuthViewModel(DemilinguaRepository repository, TokenManager tokenManager) {
        this.repository = repository;
        this.tokenManager = tokenManager;
    }

    public void register(String nombre, String correo, String contrasena) {
        _isLoading.setValue(true);
        repository.register(nombre, correo, contrasena).enqueue(new Callback<com.example.demilingua.model.GenericResponse>() {
            @Override
            public void onResponse(Call<com.example.demilingua.model.GenericResponse> call, Response<com.example.demilingua.model.GenericResponse> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    if ("ok".equals(response.body().getStatus())) {
                        _registerSuccess.setValue(true);
                    } else {
                        _error.setValue(response.body().getMessage());
                    }
                } else {
                    _error.setValue("Error en el registro");
                }
            }

            @Override
            public void onFailure(Call<com.example.demilingua.model.GenericResponse> call, Throwable t) {
                _isLoading.setValue(false);
                _error.setValue("Error de conexión");
            }
        });
    }

    public void login(String correo, String contrasena) {
        _isLoading.setValue(true);
        repository.login(correo, contrasena).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if ("ok".equals(loginResponse.getStatus())) {
                        tokenManager.saveSession(loginResponse.getToken(), loginResponse.getUserId(), loginResponse.getNombre());
                        _loginResult.setValue(loginResponse);
                    } else {
                        _error.setValue(loginResponse.getMessage());
                    }
                } else {
                    _error.setValue("Error en el servidor");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                _isLoading.setValue(false);
                _error.setValue("Error de conexión");
            }
        });
    }
}