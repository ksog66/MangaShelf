package com.example.mangashelfassignment.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mangashelfassignment.presentation.detail.MangaDetailRoute
import com.example.mangashelfassignment.presentation.home.HomeRoute
import com.example.mangashelfassignment.presentation.navigation.AppScreens
import com.example.mangashelfassignment.presentation.navigation.MANGA_ID_KEY

@Composable
fun MangaShelfApp(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        MangaNavHost(
            navController = navController
        )
    }
}

@Composable
fun MangaNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomeFeed.route
    ) {
        composable(route = AppScreens.HomeFeed.route) {
            HomeRoute(
                modifier = modifier,
            ) {
                navController.navigate(
                    route = AppScreens.MangaDetail.passMangaId(it)
                )
            }
        }

        composable(
            route = AppScreens.MangaDetail.route,
            arguments = listOf(navArgument(MANGA_ID_KEY) { type = NavType.StringType })
        ) {
            MangaDetailRoute(
                modifier = modifier
            ) {
                navController.navigateUp()
            }
        }
    }
}