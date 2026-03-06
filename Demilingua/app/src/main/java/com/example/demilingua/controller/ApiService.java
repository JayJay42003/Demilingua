package com.example.demilingua.controller;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("api/login")
    Call<LoginResponse> login(@Body Map<String, String> credentials);

    @POST("api/register")
    Call<LoginResponse> register(@Body Map<String,String> creds);

    @GET("api/courses")
    Call<List<Map<String,String>>> course(@Query("idiomaId") int idiomaId);

    @POST("api/user/update")
    Call<Map<String, String>> updateUser(@Body Map<String, String> body);

    @GET("api/test")
    Call<Map<String,Object>> randomTest(@Query("cursoId") int cursoId);

    @GET("api/exercises")
    Call<List<Map<String,Object>>> exercises(@Query("testId") int testId);

    @GET("api/ranking")
    Call<List<Map<String,String>>> getRanking();

    @POST("api/puntos")
    Call<Map<String,String>> insertPoints(@Body Map<String,Integer> body);


}
