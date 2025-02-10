package com.fetch.exercise.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fetch.exercise.ui.details.FetchItemsDetailsScreen
import com.fetch.exercise.ui.list.FetchItemsListScreen
import com.fetch.exercise.ui.theme.FetchCodingExerciseTheme
import com.fetch.exercise.util.Constants.KEY_ID

@Composable
fun FetchAppRoot() {
    val navController = rememberNavController()
    FetchCodingExerciseTheme {
        Box(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
            NavHost(navController = navController, startDestination = "list") {
                composable(
                    route = "list",
                ) {
                    FetchItemsListScreen(viewModel()) { id ->
                        navController.navigate("details/$id")
                    }
                }
                composable(
                    route = "details/{${KEY_ID}}",
                    arguments = listOf(navArgument(KEY_ID) {
                        defaultValue = -1
                        type = NavType.LongType
                    })
                ) {
                    FetchItemsDetailsScreen(viewModel()) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}