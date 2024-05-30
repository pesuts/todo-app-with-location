package com.daniel.todoapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        const val baseUrl = "https://gmaps-geocode-nearby.vercel.app/"
//        const val baseUrl = "http://192.168.166.118:8000/api/"

        fun getApiService(): ApiInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}