package com.example.bookhaul.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookhaul.components.BookAppBar
import com.example.bookhaul.components.FABcontent
import com.example.bookhaul.components.ListCard
import com.example.bookhaul.components.TitleSection
import com.example.bookhaul.model.MBooks
import com.example.bookhaul.navigation.BookHaulScreens
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BookHomeScreen(navController: NavController, viewModel: HomeScreenViewModel= hiltViewModel()) {
    Scaffold(topBar = {
          BookAppBar(title = "Book Haul", navController = navController)
    }, floatingActionButton = {
        FABcontent{
              navController.navigate(BookHaulScreens.BookSearchScreen.name)
        }
    }) {
        //content
        Surface(modifier = Modifier.fillMaxWidth()) {
            HomeContent(navController,viewModel)
        }
        
    }
}

@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel) {

    var listOfBooks= emptyList<MBooks>()
    val currentUser=FirebaseAuth.getInstance().currentUser
    if(!viewModel.data.value.data.isNullOrEmpty()){
        listOfBooks=viewModel.data.value.data!!.toList().filter { mBooks ->  
            mBooks.userId == currentUser!!.uid
        }
    }


    val currentUserName = if(!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty())
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    else "N/A"
    Column(modifier = Modifier.padding(2.dp), verticalArrangement = Arrangement.SpaceEvenly) {
        Row(modifier = Modifier.align(Alignment.Start)) {
             TitleSection(label = "Your Reading \n"+"activity right now..")
            Spacer(modifier = Modifier.fillMaxWidth(0.8f))
            Column {
                Icon(imageVector =Icons.Default.Person, contentDescription = "profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(BookHaulScreens.BookStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant)
                Text(text = currentUserName.toString(),
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.overline, color = Color.Red, fontSize = 15.sp,
                maxLines = 1, overflow = TextOverflow.Clip)
                Divider()
            }
        }
        ReadingRightNowArea(books = listOfBooks, navController = navController)
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks= listOfBooks,navController=navController)
    }

}

@Composable
fun BookListArea(listOfBooks: List<MBooks>, navController: NavController) {
    val addedBooks = listOfBooks.filter { mBook ->
        mBook.startedReading == null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(addedBooks){
        navController.navigate(BookHaulScreens.BookUpdateScreen.name +"/$it")

    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBooks>,
                                  viewModel: HomeScreenViewModel = hiltViewModel(),
                                  onCardPressed: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState)) {
        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator()

        }else {
            if (listOfBooks.isNullOrEmpty()){
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No books found. Add a Book",
                        style = TextStyle(
                            color = Color.Red.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    )

                }
            }else {
                for (book in listOfBooks) {
                    ListCard(book) {
                        onCardPressed(book.googleBookId.toString())

                    }
                }
            }
        }
    }
}



@Composable
fun ReadingRightNowArea(books: List<MBooks>, navController: NavController){
    val readingNowList = books.filter { mBook ->
        Log.d("Hell","${mBook.startedReading}")
        mBook.startedReading != null && mBook.finishedReading == null
    }

    HorizontalScrollableComponent(readingNowList){
        navController.navigate(BookHaulScreens.BookUpdateScreen.name + "/$it")
    }
}



