package com.example.translateapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://linguee-api.fly.dev/api/v2/";
    private static Retrofit retrofit;

    public static LingueeApi getLingueeApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(LingueeApi.class);
    }
}
