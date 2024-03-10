package com.example.bookhaul.screens.splashscreen

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.bookhaul.R
import com.example.bookhaul.navigation.BookHaulScreens
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun BookSplashScreen(navController: NavController) {
      val animate = remember {
            Animatable(0f)
      }
      LaunchedEffect(key1 = true, block ={
            animate.animateTo(targetValue = 0.9f, animationSpec = tween(
                  durationMillis = 1000, easing = {
                        OvershootInterpolator(8f).getInterpolation(it) }))
            delay(1000)
           if(FirebaseAuth.getInstance().currentUser?.email.isNullOrBlank()){
            navController.navigate(BookHaulScreens.BookLoginScreen.name)

           }else{
                  navController.navigate(BookHaulScreens.BookHomeScreen.name)
            }

      } )
      androidx.compose.material.Surface(modifier = Modifier
            .padding(14.dp)
            .size(250.dp)
            .scale(animate.value), shape = CircleShape,
            border = BorderStroke(2.dp, color = Color.Gray), color = Color.White) {
            Column(
                  modifier = Modifier.padding(1.dp),
                  horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                  BookLogo()
                  Text(text = "Books Are the Best Weapons", style = TextStyle(fontSize = 15.sp,
                        color = Color.Black, fontStyle = FontStyle.Italic))
            }

      }
}

@Composable
 fun BookLogo(modifier: Modifier=Modifier) {
      Text(modifier = Modifier.padding(16.dp),
            text = "Book Haul", style = MaterialTheme.typography.h4, color = Color.Red.copy(alpha = 0.7f))
}