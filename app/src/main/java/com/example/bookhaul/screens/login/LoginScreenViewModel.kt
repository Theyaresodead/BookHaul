package com.example.bookhaul.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookhaul.model.MUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth =Firebase.auth
    private val _loading=MutableLiveData(false)
    val loading:LiveData<Boolean> =_loading

    fun signInUserWithEmailAndPassword(email:String,password:String,home: () -> Unit)
    =viewModelScope.launch{
         try {
             auth.signInWithEmailAndPassword(email, password)
                 .addOnCompleteListener{ task ->
                     if(task.isSuccessful){
                         home()}
                     else{
                         Log.d("FB", task.result.toString())
                     }

                 }
         }catch (e:Exception){
             Log.d("FB","signInWithEmailAndPassword: ${e.message}")
         }
    }

    fun createUserWithEmailAndPassword(email:String,password: String,home: () -> Unit){
         if(_loading.value==false){
             _loading.value=true
             auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     val displayName = task.result?.user?.email?.split('@')?.get(0)
                     home()
                     createUser(displayName)
                 } else {
                     Log.d("FB", "createUserWithEmailAndPassword: ${email.toString()}")
                 }
                 _loading.value = true
             }
         }
    }

    private fun createUser(displayName: String?) {
        val userId= auth.currentUser?.uid
        val user= MUser(userId =userId.toString(), displayName = displayName.toString(),
            avatarUrl = "", quote = "Live Young", profession = "Android Developer", id =null).toMap()
        // Adding user to the database of the firebase
        FirebaseFirestore.getInstance().collection("users").add(user)

    }

}