package com.daniel.todoapp.data.api

//import com.example.fixingproject1.models.ResponseDeleteTodo
//import com.example.fixingproject1.models.ResponseStoreTodo
import com.daniel.todoapp.data.models.ResponsePlace
//import com.example.fixingproject1.models.ResponseUpdateTodo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
//    @GET("todo")
//    fun getPlaces(): Call<ResponseTodo>

    @GET("geocode/{long}/{lat}/{category}")
    fun getPlaces(@Path("long") long: String, @Path("lat") lat: String, @Path("category") category: String): Call<ResponsePlace>

//    @GET("todo")
//    fun getTodos(): Call<ResponseTodo>

//    @POST("todo")
//    fun storeTodo(@Body params: RequestBody): Call<ResponseStoreTodo>
//
//    @PUT("todo/{id}")
//    fun updateTodo(@Path("id") id: String, @Body params: RequestBody): Call<ResponseUpdateTodo>
//
//    @DELETE("todo/{id}")
//    fun deleteTodo(@Path("id") id: String): Call<ResponseDeleteTodo>
}