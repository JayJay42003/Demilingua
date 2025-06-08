package com.example.demilingua.controller;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @POST("api/login") // Relativo a tu base URL
    Call<LoginResponse> login(@Body Map<String, String> credentials);

    @POST("api/register")
    Call<LoginResponse> register(@Body Map<String,String> creds);

    @GET("api/courses")
    Call<List<CourseResponse>> course(int id);

    @POST("api/user/update")
    Call<Map<String, String>> updateUser(@Body Map<String, String> body);
}
