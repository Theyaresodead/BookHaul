package com.example.bookhaul.screens.details

import androidx.lifecycle.ViewModel
import com.example.bookhaul.data.Resource
import com.example.bookhaul.model.Item
import com.example.bookhaul.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository):ViewModel() {
    suspend fun getBookInfo(bookId:String):Resource<Item>{
         return repository.getBookInfo(bookId)
    }

}