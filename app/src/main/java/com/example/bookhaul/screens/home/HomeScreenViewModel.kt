package com.example.bookhaul.screens.home

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookhaul.data.DataOrEception
import com.example.bookhaul.model.MBooks
import com.example.bookhaul.repository.FireRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val repository: FireRepository): ViewModel(){
   val data:MutableState<DataOrEception<List<MBooks>,Boolean,Exception>>
    = mutableStateOf(DataOrEception(listOf(),true,Exception("")))
    init{
        getAllBooksFromDatabase()
    }

     private fun getAllBooksFromDatabase() {
        viewModelScope.launch {
            data.value.loading=true
            data.value=repository.getAllBooksFromDatabase()
            if(!data.value.data.isNullOrEmpty()) data.value.loading=false
        }

    }
}