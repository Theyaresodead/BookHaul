package com.example.bookhaul.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookhaul.data.DataOrEception
import com.example.bookhaul.data.Resource
import com.example.bookhaul.model.Item
import com.example.bookhaul.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository):ViewModel(){
    var list:List<Item> by mutableStateOf(listOf())
    var isLoading:Boolean by mutableStateOf(true)
    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            isLoading=true
            if(query.isEmpty()) return@launch
             try{
                   when(val response= repository.getBooks(query)){
                       is Resource.Success ->{
                           list=response.data!!
                           if(list.isNotEmpty()) isLoading= false
                       }
                       is Resource.Error->{
                           Log.e("Error","Errror")
                       }else ->{}
                   }
             }catch (e:Exception){}
        }
    }
}