package com.example.biometricapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object {
        private const val BASE_URL = "https://api.nalimadan.com"

        fun getInstance(): ApiService {
            return Retrofit.Builder()
                  .baseUrl(BASE_URL)
                  .addConverterFactory(GsonConverterFactory.create())
                  .build()
                  .create(ApiService::class.java)
        }
    }

}