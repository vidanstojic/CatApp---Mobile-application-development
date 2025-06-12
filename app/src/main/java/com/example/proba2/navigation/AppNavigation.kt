package com.example.proba2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.example.proba2.ui.screens.BreedDetailsScreen
import com.example.proba2.ui.screens.BreedListScreen
import androidx.compose.runtime.getValue
import com.example.proba2.ui.screens.SearchScreen

private fun NavController.navigateToDetails(breedId: String) {
    this.navigate(route = "details/$breedId")
}
//
//private fun NavController.navigateToEditor(passwordId: Int? = null) {
//    if (passwordId != null) {
//        this.navigate(route = "editor?$PASSWORD_ID_ARG=$passwordId")
//    } else {
//        this.navigate(route = "editor")
//    }
//}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Važan korak – koristimo NavHost sa početnom rutom
    NavHost(
        navController = navController,
        startDestination = "breeds"
    ) {
        // Ekran sa listom rasa
        composable(route = "breeds") { navBackStackEntry ->
            // Deljeni ViewModel koji se koristi i u details ekranu
            val sharedViewModel = hiltViewModel<CatBreedsViewModel>(navBackStackEntry)

            val state by sharedViewModel.state.collectAsState()

            BreedListScreen(
                state = state,
                onBreedClick = { breedId ->
                    navController.navigate("details/$breedId")
                },
                navController = navController
            )
        }

        // Ekran sa detaljima o rasi
        composable(
            route = "details/{breedId}",
            arguments = listOf(
                navArgument("breedId") { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val parentEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry("breeds")
            }
            val sharedViewModel = hiltViewModel<CatBreedsViewModel>(parentEntry)

            val breedId = navBackStackEntry.arguments?.getString("breedId") ?: ""

            BreedDetailsScreen(
                breedId = breedId,
                onClose = {
                    navController.navigateUp()
                },
                viewModel = sharedViewModel,
                navController = navController
            )
        }
        composable(
            route = "search/{query}",
            arguments = listOf(
                navArgument("query") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            SearchScreen(
                query = query,
                onBreedClick = { breedId ->
                    navController.navigate("details/$breedId")
                }
            )
        }

    }
}
