package com.example.myapplication.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    private val _searchResults = MutableLiveData<List<String>>()
    val searchResults: LiveData<List<String>>
        get() = _searchResults

    fun search(query: String?) {
        // TODO: Make backend request with the query and update _searchResults LiveData
        // Call backend API to get search results based on query
        // Replace this with your own implementation
        val results = listOf("Result 1", "Result 2", "Result 3")
        _searchResults.value = results
    }
}