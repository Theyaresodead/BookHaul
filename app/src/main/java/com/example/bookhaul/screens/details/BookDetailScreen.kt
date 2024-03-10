package com.example.bookhaul.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookhaul.components.BookAppBar
import com.example.bookhaul.components.RoundedButton
import com.example.bookhaul.data.Resource
import com.example.bookhaul.model.Item
import com.example.bookhaul.model.MBooks
import com.example.bookhaul.navigation.BookHaulScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("ProduceStateDoesNotAssignValue")
@Composable
fun BookDetailScreen(navController: NavController, bookId:String,viewModel: DetailsViewModel= hiltViewModel()) {
    Scaffold(topBar = {
        BookAppBar(title = "Book Details",
            icon = Icons.Default.ArrowBack, showProfile = false,navController = navController){
            navController.navigate(BookHaulScreens.BookSearchScreen.name)
        }
    }) {
        Surface(modifier = Modifier
            .padding(3.dp)
            .fillMaxSize()) {
            Column(
                modifier = Modifier.padding(top = 12.dp),
                verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {
                 val bookInfo= produceState<Resource<Item>>(initialValue =Resource.Loading()){
                     value = viewModel.getBookInfo(bookId)
                 }.value
                 if(bookInfo.data==null) {
                     Row() {
                         LinearProgressIndicator()
                         Text(text = "Loading..")
                     }
                 }
                 else {
                     ShowBookDetails(navController=navController,bookInfo)
                 }
            }
        }
    }


}

@Composable
fun ShowBookDetails(navController: NavController, bookInfo: Resource<Item>) {
   val bookData=bookInfo.data!!.volumeInfo
    val googleBookId=bookInfo.data.id
    Card(modifier = Modifier.padding(34.dp), shape = CircleShape, elevation = 4.dp) {
        Image(painter = rememberImagePainter(data = bookData.imageLinks.thumbnail.toString()),
            contentDescription = "Book Image", modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(1.dp))
    }
    Text(text = bookData.title, style = MaterialTheme.typography.h6, overflow = TextOverflow.Ellipsis, maxLines = 2)
    Text(text = "Authors: ${bookData.authors}")
    Text(text = "PageCount: ${bookData.pageCount}")
    Text(text = "Categories: ${bookData.categories}", style = MaterialTheme.typography.subtitle1,
        maxLines = 3, overflow = TextOverflow.Ellipsis)
    Text(text = "Published: ${bookData.publishedDate}", style = MaterialTheme.typography.subtitle2)
    Spacer(modifier = Modifier.height(5.dp))
    val localDims= LocalContext.current.resources.displayMetrics
    val cleanDescription= HtmlCompat.fromHtml(bookData.description,HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    Surface(modifier = Modifier
        .height(localDims.heightPixels.dp.times(0.09f))
        .padding(4.dp), shape = RectangleShape,
    border = BorderStroke(1.dp, Color.DarkGray)) {
        LazyColumn(modifier = Modifier.padding(3.dp)) {
            item {
                Text(text = cleanDescription)
            }
        }
    }

            Row(modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.SpaceAround) {
                RoundedButton(label = "SAVE") {
                    val book = MBooks(
                        title = bookData.title,
                        authors = bookData.authors.toString(),
                        description = bookData.description,
                        categories = bookData.categories.toString(),
                        notes = "",
                        photoUrl = bookData.imageLinks.thumbnail,
                        publishedDate = bookData.publishedDate,
                        pageCount = bookData.pageCount.toString(),
                        rating = 0.0,
                        googleBookId = googleBookId,
                        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                    )
                    saveToFireStore(book, navController = navController)
                }
                Spacer(modifier = Modifier.width(25.dp))
                RoundedButton(label = "CANCEL") {
                    navController.popBackStack()
                }
            }
        }




fun saveToFireStore(book: MBooks, navController: NavController) {
    val db=FirebaseFirestore.getInstance()
    val dbCollection=db.collection("books")
    if(book.toString().isNotEmpty()){
         dbCollection.add(book).addOnSuccessListener { documentRef ->
             val docId=documentRef.id
             dbCollection.document(docId).update(hashMapOf("id" to docId) as Map<String, Any>).addOnCompleteListener{ task ->
                  if(task.isSuccessful){
                      navController.popBackStack()
                  }

             }.addOnFailureListener {
                 Log.d("Tag","SaveToFirestore :Error",it)
             }

         }
    }else{

    }

}
