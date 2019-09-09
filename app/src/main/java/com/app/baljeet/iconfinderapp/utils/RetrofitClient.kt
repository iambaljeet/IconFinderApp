package com.app.baljeet.iconfinderapp.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient = OkHttpClient.Builder().addInterceptor(interceptor)

    val retrofitClient: ApiService = Retrofit.Builder()
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(NetworkConstants.BASE_URL)
        .build().create(ApiService::class.java)
}