package com.example.jetareader.repository

import com.example.jetareader.data.DataOrException
import com.example.jetareader.model.Item
import com.example.jetareader.network.BooksApi
import javax.inject.Inject
import kotlin.Exception

class BookRepository2 @Inject constructor(private val api: BooksApi) {
    private val dataOrException =
        DataOrException<List<Item>, Boolean, Exception>()

    private val bookInfoDataOrException =
        DataOrException<Item, Boolean, Exception>()
    suspend fun getBooks(searchQuery: String): DataOrException<List<Item>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllBooks(searchQuery).items
            if (dataOrException.data!!.isNotEmpty()) {
                dataOrException.loading = false
            }

        } catch (ex: Exception) {
            dataOrException.e = ex
        }

        return dataOrException
    }

    /*suspend fun getBookInfo(bookId: String): DataOrException<Item, Boolean, Exception> {
        val response = try {
            bookInfoDataOrException.loading = true
            bookInfoDataOrException.data = api.getBookInfo(bookId)
            if (bookInfoDataOrException.data.toString().isNotEmpty()) {
                bookInfoDataOrException.loading = false
            } else {}

        } catch (ex: Exception) {
            bookInfoDataOrException.e = ex
        }

        return response
    }*/
}