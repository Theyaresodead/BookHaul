package com.example.bookhaul.navigation


enum class BookHaulScreens {
    BookSplashScreen,
    BookLoginScreen,
    BookCreateAccountScreen,
    BookUpdateScreen,
    BookDetailScreen,
    BookHomeScreen,
    BookSearchScreen,
    BookStatsScreen;

    companion object {
        fun fromRoute(route:String):BookHaulScreens=when(route.substringBefore("/")){
            BookSplashScreen.name -> BookSplashScreen
            BookLoginScreen.name -> BookLoginScreen
            BookCreateAccountScreen.name -> BookCreateAccountScreen
            BookUpdateScreen.name -> BookUpdateScreen
            BookDetailScreen.name -> BookDetailScreen
            BookStatsScreen.name -> BookStatsScreen
            BookSearchScreen.name -> BookSearchScreen
            null -> BookHomeScreen
            else -> throw IllegalArgumentException("Route $route is not recognised")
        }
    }

}