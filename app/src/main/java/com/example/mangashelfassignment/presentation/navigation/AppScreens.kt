package com.example.mangashelfassignment.presentation.navigation


const val MANGA_ID_KEY = "manga_id"
sealed class AppScreens(val route: String) {

    object HomeFeed: AppScreens(route = "home_screen")

    object MangaDetail : AppScreens(route = "manga_detail?manga_id={$MANGA_ID_KEY}") {
        fun passMangaId(
            id: String
        ): String {
            return "manga_detail?manga_id=$id"
        }
    }
}