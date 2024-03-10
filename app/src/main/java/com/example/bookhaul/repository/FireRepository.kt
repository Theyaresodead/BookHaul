package com.example.bookhaul.repository

import com.example.bookhaul.data.DataOrEception
import com.example.bookhaul.model.MBooks
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireRepository @Inject constructor(private val query:Query){
    suspend fun getAllBooksFromDatabase():DataOrEception<List<MBooks>,Boolean,Exception>{
        val dataOrEception=DataOrEception<List<MBooks>,Boolean,Exception>()
        try{
            dataOrEception.loading=true
            dataOrEception.data=query.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(MBooks::class.java)!!
            }
               if(!dataOrEception.data.isNullOrEmpty())  dataOrEception.loading=false
        }catch (e:FirebaseFirestoreException){
            dataOrEception.e=e
        }
        return dataOrEception
    }
}