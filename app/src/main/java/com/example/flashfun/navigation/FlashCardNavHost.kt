package com.example.flashfun.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.flashfun.ui.screens.DeckListScreen
import com.example.flashfun.ui.screens.ResultScreen
import com.example.flashfun.ui.screens.StudyScreen

@Composable
fun FlashCardNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.DeckList.route
    ) {
        composable(Screen.DeckList.route) {
            DeckListScreen(
                onDeckSelected = { setId ->
                    navController.navigate(Screen.Study.createRoute(setId))
                }
            )
        }

        composable(
            route = Screen.Study.route,
            arguments = listOf(navArgument("setId") { type = NavType.StringType })
        ) { backStackEntry ->
            val setId = backStackEntry.arguments?.getString("setId") ?: return@composable
            StudyScreen(
                setId = setId,
                onFinished = { correct, total ->
                    navController.navigate(Screen.Result.createRoute(correct, total)) {
                        popUpTo(Screen.DeckList.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("correct") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            ResultScreen(
                correctCount = correct,
                totalCount = total,
                onRestart = {
                    navController.navigate(Screen.DeckList.route) {
                        popUpTo(Screen.DeckList.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
