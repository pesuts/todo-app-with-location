package com.daniel.todoapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.daniel.todoapp.data.api.ApiConfig
//import com.example.fixingproject1.models.ResponseDeleteTodo
//import com.example.fixingproject1.models.ResponseStoreTodo
import com.daniel.todoapp.data.models.ResponsePlace
//import com.example.fixingproject1.models.ResponseUpdateTodo
import com.daniel.todoapp.data.models.Place
import retrofit2.Call
import retrofit2.Response

class MainViewModel: ViewModel() {
    var result = 0

    fun multiply(data: Int){
        result = data * data
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listPlace = MutableLiveData<List<Place>?>()
    val listPlace: LiveData<List<Place>?> = _listPlace

    private val _todo = MutableLiveData<Place?>()
    val todo: LiveData<Place?> = _todo

    private val _listError = MutableLiveData<List<String>?>()
    val listError: LiveData<List<String>?> = _listError

    private val _isFinish = MutableLiveData<Boolean>()
    val isFinish: LiveData<Boolean> = _isFinish

    fun setIsLoading(data: Boolean){
        _isLoading.value = data
    }

    fun setTargetTodo(data: Place?){
        _todo.value = data
    }

    fun setIsFinish(data: Boolean){
        _isFinish.value = data
    }

    fun loadDatas(long: String, lat: String, category: String){
        setIsLoading(true)
        val client = ApiConfig.getApiService().getPlaces(long, lat, category)

        client.enqueue(object: retrofit2.Callback<ResponsePlace>{
            override fun onFailure(call: Call<ResponsePlace>, t: Throwable) {
                Log.e("error", t.message!!)
                setIsLoading(false)
            }
            override fun onResponse(call: Call<ResponsePlace>, response: Response<ResponsePlace>) {
                if(response.body() === null){
                    Log.e("error", "Your data API is empty")
                } else {
                    _listPlace.value = response.body()?.data
                }
                setIsLoading(false)
            }
        })
    }
}