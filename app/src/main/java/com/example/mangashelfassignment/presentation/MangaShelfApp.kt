package com.example.mangashelfassignment.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
        startDestination = AppScreens.HomeFeed.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
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
            arguments = listOf(navArgument(MANGA_ID_KEY) { type = NavType.StringType }),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideIntoContainer(
                    animationSpec = tween(300, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    )
                ) + slideOutOfContainer(
                    animationSpec = tween(300, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            MangaDetailRoute(
                modifier = modifier
            ) {
                navController.navigateUp()
            }
        }
    }
}