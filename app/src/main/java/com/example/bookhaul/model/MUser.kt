package com.example.bookhaul.model

data class MUser(
    val id: String?,
    val userId:String,
    val avatarUrl:String,
    val displayName:String,
    val profession:String,
    val quote:String){
    fun toMap():MutableMap<String,Any>{
        return mutableMapOf(
            "user_Id" to this.userId,
            "avatar_Url" to this.avatarUrl,
            "display_Name" to this.displayName,
            "quote" to this.quote,
            "profession" to this.profession
        )
    }
}
