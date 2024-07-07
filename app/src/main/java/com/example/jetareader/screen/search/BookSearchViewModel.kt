package com.example.jetareader.screen.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetareader.data.DataOrException
import com.example.jetareader.model.Item
import com.example.jetareader.repository.BookRepository2
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository2):
    ViewModel() {
        val listOfBooks: MutableState<DataOrException<List<Item>, Boolean, Exception>>
     = mutableStateOf(DataOrException(listOf(), true, Exception("")))

    init {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                return@launch
            }

            listOfBooks.value.loading = true
            listOfBooks.value = repository.getBooks(query)
            Log.d("DATA" , "searchBooks: ${listOfBooks.value.data.toString()}")
            if (listOfBooks.value.data.toString().isNotEmpty()) {
                listOfBooks.value.loading = false
            }
        }
    }
}