package com.example.bookhaul

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookhaul.navigation.BookHaulNavigation
import com.example.bookhaul.ui.theme.BookHaulTheme
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookHaulTheme {
                BookHaulApplication()
            }
        }
    }
}

@Composable
fun BookHaulApplication(){
    Surface(modifier = Modifier
        .fillMaxSize()
        .padding(top = 46.dp),
        color = MaterialTheme.colors.background,) {
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            BookHaulNavigation()
        }

    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BookHaulTheme {

    }
}