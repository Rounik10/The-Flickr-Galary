package com.example.theflickrgalary.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.theflickrgalary.model.ApiModel
import com.example.theflickrgalary.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<ApiModel> = MutableLiveData()

    fun getApiModel() {
        viewModelScope.launch {
            val response = repository.getApiModel()
            myResponse.value = response
        }
    }

    fun getSearchResult(tags:String) {
        viewModelScope.launch {
            val response = repository.getSearchResult(tags)
            myResponse.value = response
        }
    }
}