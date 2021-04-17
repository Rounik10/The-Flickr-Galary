package com.example.theflickrgalary.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.theflickrgalary.model.ApiModel
import com.example.theflickrgalary.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : ViewModel() {
    val myResponse: MutableLiveData<ApiModel> = MutableLiveData()

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap {
        repository.getSearchResPaging(it).cachedIn(viewModelScope)
    }

    fun getPagedResults(query: String) {
        currentQuery.value = query
    }

    fun getApiModel() {
        viewModelScope.launch {
            myResponse.value = repository.getApiModel()
        }
    }
    companion object {
        private const val DEFAULT_QUERY = "recent"
    }

}