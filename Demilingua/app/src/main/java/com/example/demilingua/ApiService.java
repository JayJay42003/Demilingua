package com.example.demilingua;

import com.example.demilingua.model.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("api/login") // Relativo a tu base URL
    Call<LoginResponse> login(@Body Map<String, String> credentials);

    @POST("api/register")
    Call<LoginResponse> register(@Body Map<String,String> creds);

    @GET("usuarios/{id}")
    Call<Usuario> obtenerUsuario(@Path("id") int userId);

    @POST("usuarios")
    Call<Usuario> crearUsuario(@Body Usuario usuario);

    @GET("usuarios")
    Call<List<Usuario>> listarUsuarios();

    @PUT("usuarios/{id}")
    Call<Usuario> actualizarUsuario(@Path("id") int userId, @Body Usuario usuario);
}
