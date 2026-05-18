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

    @GET("api/users")
    Call<List<Map<String, String>>> searchUsers(@Query("query") String query);

    @GET("api/users/{id}")
    Call<Map<String, String>> getUserById(@Path("id") int id);

    @PUT("api/users/{id}")
    Call<Map<String, String>> updateUser(
            @Path("id") int id, 
            @Query("nombre") String nombre, 
            @Query("correo") String correo,
            @Query("contrasena") String contrasena
    );

    @DELETE("api/users/{id}")
    Call<Map<String, String>> deleteUser(@Path("id") int id);

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

    @POST("api/friends")
    Call<Map<String, String>> sendFriendRequest(@Query("usuarioId1") int usuarioId1, @Query("usuarioId2") int usuarioId2);

    @PUT("api/friends")
    Call<Map<String, String>> acceptFriend(@Query("usuarioId1") int usuarioId1, @Query("usuarioId2") int usuarioId2);

    @DELETE("api/friends")
    Call<Map<String, String>> deleteFriend(@Query("usuarioId1") int usuarioId1, @Query("usuarioId2") int usuarioId2);

    // --- CURSOS Y CONTENIDO ---
    @GET("api/idiomas")
    Call<List<Map<String, String>>> getIdiomas();

    @GET("api/courses")
    Call<List<Map<String, String>>> course(@Query("idiomaId") int idiomaId);

    @GET("api/test")
    Call<Map<String, Object>> randomTest(@Query("cursoId") int cursoId);

    @GET("api/exercises")
    Call<List<Map<String, Object>>> exercises(@Query("testId") int testId);

    @GET("api/ranking")
    Call<List<Map<String, String>>> getRanking();
}