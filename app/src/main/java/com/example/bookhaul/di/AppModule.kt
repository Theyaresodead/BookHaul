package com.example.bookhaul.di

import com.example.bookhaul.network.BooksApi
import com.example.bookhaul.repository.BookRepository
import com.example.bookhaul.repository.FireRepository
import com.example.bookhaul.utils.Constants
import com.example.bookhaul.utils.Constants.BASE_URL
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBookRepository(api:BooksApi)=BookRepository(api)

    @Singleton
    @Provides
    fun provideFireBookRepository()=FireRepository(query=FirebaseFirestore.getInstance().collection("books"))

    @Singleton
    @Provides
    fun provideBookApi():BooksApi{
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApi::class.java)
    }

}