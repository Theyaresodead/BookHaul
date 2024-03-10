package com.example.bookhaul.components

import android.content.Context
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.bookhaul.R
import com.example.bookhaul.model.MBooks
import com.example.bookhaul.navigation.BookHaulScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BookAppBar(title:String,icon:ImageVector?=null,
               showProfile:Boolean =true,navController: NavController,
onBackArrowButtonClicked: () -> Unit={}){
    TopAppBar(title={
        Row(verticalAlignment = Alignment.CenterVertically) {
            if(showProfile){
                Icon(imageVector = Icons.Default.Book,
                    contentDescription = "Logo Icon", modifier = Modifier
                        .clip(
                            RoundedCornerShape(12.dp))
                        .scale(0.9f))
            }
            if(icon!=null){
                Icon(imageVector = icon, contentDescription = "ArrowBack", tint=Color.Red.copy(alpha=0.7f),
                    modifier = Modifier.clickable { onBackArrowButtonClicked.invoke() })
            }
            Spacer(modifier = Modifier.width(50.dp))
            Text(text = title,
                color = Color.Red.copy(alpha = 0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))


        }

    },
        actions = {
            IconButton(onClick = { FirebaseAuth.getInstance().signOut().run {
                navController.navigate(BookHaulScreens.BookLoginScreen.name)
            } }) {
                if(showProfile) Row() {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Log out")
                }else Box{}
            }
        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp)
}
@Composable
fun FABcontent(onTap: (String) -> Unit) {
    FloatingActionButton(onClick = {onTap("")},
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xFF92CBDF)) {
        Icon(imageVector = Icons.Default.Add,
            contentDescription ="Adding the content",
            tint = Color.White)
    }
}

@Composable
fun TitleSection(modifier: Modifier=Modifier,label:String){
    Surface(modifier=modifier.padding(start = 5.dp,top=1.dp)) {
        Column {
            Text(text = label,
                fontSize = 19.sp,
                fontStyle = FontStyle.Normal,
                textAlign = TextAlign.Left)

        }

    }
}

@Composable
fun ListCard(book: MBooks, onPressDetails: (String) ->Unit={}){
    val context = LocalContext.current
    val resources=context.resources
    val displayMetrics =resources.displayMetrics
    val screenWidth =displayMetrics.widthPixels / displayMetrics.density
    val spacing=10.dp
    Card(shape = RoundedCornerShape(29.dp),
        backgroundColor = Color.White, elevation = 6.dp,
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable { onPressDetails.invoke(book.title.toString()) }){
        Column(modifier = Modifier.width(screenWidth.dp - (spacing*2)),
            horizontalAlignment = Alignment.Start){
            Row(horizontalArrangement = Arrangement.Center) {
                Image(painter = rememberImagePainter(data = book.photoUrl.toString()), contentDescription ="book image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp) )
                Spacer(modifier = Modifier.width(50.dp))
                Column(modifier = Modifier.padding(top=25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription ="Favorite",
                        modifier = Modifier.padding(1.dp))
                    BookRating(score =3.5)

                }

            }
                Text(text = book.title.toString(),
                    modifier = Modifier.padding(4.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
            Text(text = book.authors.toString(), modifier = Modifier.padding(4.dp),
                style= MaterialTheme.typography.caption)
        }
        Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
            RoundedButton(label = "Reading", radius = 70)

        }
    }


}

@Composable
fun RoundedButton(label: String,radius: Int=29,onPress:() -> Unit={}){
    Surface(modifier = Modifier.clip(RoundedCornerShape(
        bottomEndPercent = radius,
        topStartPercent = radius)),
        color = Color(0xFF92CBDF)) {
        Column(modifier = Modifier
            .width(90.dp)
            .heightIn(40.dp)
            .clickable { onPress.invoke() }, verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally){
            Text(text = label, style = TextStyle(color=Color.White, fontSize = 15.sp))

        }

    }

}


@Composable
fun BookRating(score: Double= 4.5) {
    Surface(modifier = Modifier
        .padding(4.dp)
        .height(70.dp), shape = RoundedCornerShape(56.dp), elevation = 6.dp, color = Color.White) {
        Column(modifier = Modifier.padding(4.dp)) {
            Icon(imageVector = Icons.Default.StarBorder, contentDescription ="Star",
                modifier = Modifier.padding(3.dp))
            Text(text = score.toString(), style = MaterialTheme.typography.subtitle1)
        }
    }


}
@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}


fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG)
        .show()
}