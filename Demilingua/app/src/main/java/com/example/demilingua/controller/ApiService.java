package com.example.demilingua.controller;

import com.example.demilingua.controller.LoginResponse;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // --- AUTENTICACIÓN Y USUARIOS ---
    @POST("api/login")
    Call<LoginResponse> login(@Body Map<String, String> credentials);

    @POST("api/users")
    Call<Map<String, String>> register(@Query("nombre") String nombre, @Query("correo") String correo, @Query("contrasena") String contrasena);

    @GET("api/users/search")
    Call<List<Map<String, String>>> searchUsers(@Query("query") String query);

    @PUT("api/users/{id}")
    Call<Map<String, String>> updateUser(@Path("id") int id, @Query("nombre") String nombre, @Query("correo") String correo);

    // --- GAMIFICACIÓN ---
    @GET("api/gamification/status/{usuarioId}")
    Call<Map<String, String>> getStatusVidas(@Path("usuarioId") int usuarioId);

    @POST("api/gamification/perder-vida")
    Call<Map<String, String>> perderVida(@Query("usuarioId") int usuarioId);

    @POST("api/gamification/add-xp")
    Call<Map<String, String>> insertPoints(@Query("usuarioId") int usuarioId, @Query("idiomaId") int idiomaId, @Query("puntos") int puntos);

    // --- SISTEMA DE AMIGOS ---
    @GET("api/friends/{usuarioId}")
    Call<List<Map<String, String>>> getFriends(@Path("usuarioId") int usuarioId);

    @POST("api/friends/request")
    Call<Map<String, String>> sendFriendRequest(@Query("usuarioId") int usuarioId, @Query("amigoId") int amigoId);

    // --- CURSOS Y CONTENIDO ---
    @GET("api/idiomas")
    Call<List<Map<String, String>>> getIdiomas();

    @GET("api/courses")
    Call<List<Map<String, String>>> course(@Query("idiomaId") int idiomaId);

    @GET("api/test")
    Call<Map<String, Object>> randomTest(@Query("cursoId") int cursoId);

    @GET("api/exercises")
    Call<List<Map<String, Object>>> exercises(@Query("testId") int testId);

    @GET("api/ranking/global")
    Call<List<Map<String, String>>> getRanking();
}