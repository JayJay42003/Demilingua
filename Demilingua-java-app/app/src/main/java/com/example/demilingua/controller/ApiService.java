package com.example.demilingua.controller;

import com.example.demilingua.model.Curso;
import com.example.demilingua.model.Ejercicio;
import com.example.demilingua.model.GenericResponse;
import com.example.demilingua.model.Idioma;
import com.example.demilingua.model.LoginRequest;
import com.example.demilingua.model.LoginResponse;
import com.example.demilingua.model.RankingItem;
import com.example.demilingua.model.StatusVidas;
import com.example.demilingua.model.Test;
import com.example.demilingua.model.Usuario;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // --- AUTENTICACIÓN Y USUARIOS ---
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest credentials);

    @POST("api/users")
    Call<GenericResponse> register(
            @Query("nombre") String nombre, 
            @Query("correo") String correo, 
            @Query("contrasena") String contrasena
    );

    @GET("api/users")
    Call<List<Map<String, String>>> searchUsers(@Query("query") String query);

    @GET("api/users/{id}")
    Call<Usuario> getUserById(@Path("id") int id);

    @PUT("api/users/{id}")
    Call<GenericResponse> updateUser(
            @Path("id") int id, 
            @Query("nombre") String nombre, 
            @Query("correo") String correo,
            @Query("contrasena") String contrasena
    );

    @DELETE("api/users/{id}")
    Call<GenericResponse> deleteUser(@Path("id") int id);

    // --- GAMIFICACIÓN ---
    @GET("api/gamification/status/{usuarioId}")
    Call<StatusVidas> getStatusVidas(@Path("usuarioId") int usuarioId);

    @POST("api/gamification/perder-vida")
    Call<GenericResponse> perderVida(@Query("usuarioId") int usuarioId);

    @POST("api/gamification/add-xp")
    Call<GenericResponse> insertPoints(@Query("usuarioId") int usuarioId, @Query("idiomaId") int idiomaId, @Query("puntos") int puntos);

    // --- SISTEMA DE AMIGOS ---
    @GET("api/friends/{usuarioId}")
    Call<List<Map<String, String>>> getFriends(@Path("usuarioId") int usuarioId);

    @POST("api/friends")
    Call<GenericResponse> sendFriendRequest(@Query("usuarioId1") int usuarioId1, @Query("usuarioId2") int usuarioId2);

    @PUT("api/friends")
    Call<GenericResponse> acceptFriend(@Query("usuarioId1") int usuarioId1, @Query("usuarioId2") int usuarioId2);

    @DELETE("api/friends")
    Call<GenericResponse> deleteFriend(@Query("usuarioId1") int usuarioId1, @Query("usuarioId2") int usuarioId2);

    // --- CURSOS Y CONTENIDO ---
    @GET("api/idiomas")
    Call<List<Idioma>> getIdiomas();

    @GET("api/cursos/{idiomaId}")
    Call<List<Curso>> getCursos(@Path("idiomaId") int idiomaId);

    @GET("api/tests/{cursoId}")
    Call<List<Test>> getTests(@Path("cursoId") int cursoId);

    @GET("api/tests/ejercicios/{testId}")
    Call<List<Map<String, Object>>> getEjercicios(@Path("testId") int testId);

    @GET("api/ranking")
    Call<List<RankingItem>> getRanking();
}
