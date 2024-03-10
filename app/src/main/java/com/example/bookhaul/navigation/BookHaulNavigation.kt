package com.example.bookhaul.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.bookhaul.screens.details.BookDetailScreen
import com.example.bookhaul.screens.home.BookHomeScreen
import com.example.bookhaul.screens.home.HomeScreenViewModel
import com.example.bookhaul.screens.login.BookLoginScreen
import com.example.bookhaul.screens.search.BookSearchScreen
import com.example.bookhaul.screens.search.BookSearchViewModel
import com.example.bookhaul.screens.splashscreen.BookSplashScreen
import com.example.bookhaul.screens.stats.BookStatsScreen
import com.example.bookhaul.screens.update.BookUpdateScreen

@Composable
fun BookHaulNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = BookHaulScreens.BookSplashScreen.name){
        composable(BookHaulScreens.BookSplashScreen.name){
            BookSplashScreen(navController=navController)
        }
        composable(BookHaulScreens.BookLoginScreen.name){
            BookLoginScreen(navController=navController)
        }
        composable(BookHaulScreens.BookHomeScreen.name){
            val homeViewModel= hiltViewModel<HomeScreenViewModel>()
            BookHomeScreen(navController=navController, viewModel =homeViewModel)
        }
        val detailName=BookHaulScreens.BookDetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type= NavType.StringType
        })){ backStackEntry ->
           backStackEntry.arguments?.getString("bookId").let{
                BookDetailScreen(navController = navController, bookId = it.toString())

            }
        }
        composable(BookHaulScreens.BookStatsScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            BookStatsScreen(navController=navController, viewModel=homeViewModel)
        }
        composable(BookHaulScreens.BookSearchScreen.name){
            val searchViewModel= hiltViewModel<BookSearchViewModel>()
            BookSearchScreen(navController=navController, viewModel= searchViewModel)
        }
        val updateName=BookHaulScreens.BookUpdateScreen.name
        composable("$updateName/{bookItemId}",arguments= listOf(navArgument("bookItemId"){
            type= NavType.StringType
        })){ navBackStackEntry ->
            navBackStackEntry.arguments?.getString("bookItemId").let{
                BookUpdateScreen(navController=navController,bookItemId=it.toString())
            }

        }

    }
}