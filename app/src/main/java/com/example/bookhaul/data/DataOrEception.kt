package com.example.bookhaul.data

data class DataOrEception<T,Boolean,E:Exception?>(
    var data:T?=null,
    var loading:Boolean?=null,
    var e:E?=null
)