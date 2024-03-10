package com.example.bookhaul.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bookhaul.components.BookAppBar
import com.example.bookhaul.model.Item
import com.example.bookhaul.navigation.BookHaulScreens
import com.example.bookhaul.screens.login.InputField

@Composable
fun BookSearchScreen(
    navController: NavController, viewModel: BookSearchViewModel = hiltViewModel(),
){
 Scaffold(topBar = {
     BookAppBar(title = "Search Books",
         icon=Icons.Default.ArrowBack ,
         navController = navController, showProfile = false){
         navController.popBackStack()
         navController.navigate(BookHaulScreens.BookHomeScreen.name)
     } }) {
         Surface() {
             Column {
                  SearchForm(modifier = Modifier
                      .fillMaxWidth()
                      .padding(16.dp)){ query->
                      viewModel.searchBooks(query)
                  }
                 Spacer(modifier = Modifier.height(13.dp))
                 BookList(navController=navController,viewModel)
             }
             
         }
 }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(modifier: Modifier=Modifier,
               loading: Boolean = false,
               hint:String="Search",
               onSearch:(String) -> Unit={}) {
    Column() {
        val searchQueryState = rememberSaveable { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState) { searchQueryState.value.trim().isNotEmpty() }

        InputField(valueState =searchQueryState, enabled = true, labelId = "Search", onAction = KeyboardActions {
            if(!valid) return@KeyboardActions
            onSearch(searchQueryState.value.trim())
            searchQueryState.value =""
            keyboardController!!.hide()
        })
    }
}

@Composable
fun BookList(navController: NavController,viewModel: BookSearchViewModel= hiltViewModel()) {

    val listOfBooks = viewModel.list
     if(viewModel.isLoading) LinearProgressIndicator()
    else {
         LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
             items(items = listOfBooks) { book ->
                 BookRow(book, navController = navController)
             }
         }
     }
}



@Composable
fun BookRow(book: Item, navController: NavController) {
    Card(modifier = Modifier
        .clickable {
            navController.navigate(BookHaulScreens.BookDetailScreen.name+"/${book.id}")
        }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        elevation = 7.dp) {
        Row(modifier = Modifier.padding(5.dp), verticalAlignment = Alignment.Top) {
            val imageUrl=book.volumeInfo.imageLinks.smallThumbnail

            Image(painter = rememberImagePainter(data= imageUrl), contentDescription = "Image",
                modifier = Modifier
                    .width(80.dp)
                    .fillMaxHeight()
                    .padding(end = 4.dp))
            Column {
                Text(text = book.volumeInfo.title, overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption)

                Text(text = "Date: ${book.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption)

                Text(text = "Author: ${book.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption)
            }
        }
    }
}



