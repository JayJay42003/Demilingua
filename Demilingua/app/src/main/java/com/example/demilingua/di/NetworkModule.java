package com.example.demilingua.di;

import com.example.demilingua.controller.ApiService;
import com.example.demilingua.data.TokenManager;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String BASE_URL = "https://demilingua-back.onrender.com/";

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(TokenManager tokenManager) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            String token = tokenManager.getToken();
            
            if (token != null) {
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Bearer " + token)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
            
            return chain.proceed(original);
        };

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(authInterceptor)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}