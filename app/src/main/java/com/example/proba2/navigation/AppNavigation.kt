package com.example.proba2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proba2.ui.screens.BreedDetailsScreen
import com.example.proba2.ui.screens.BreedListScreen
import com.example.proba2.ui.viewmodel.BreedsListViewModel

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

    NavHost(
        navController = navController,
        startDestination = "breeds"
    ) {
        breedList(
            route = "breeds",
            onBreedClick = {
                navController.navigate(route = "users/grid/$it")
            }
        )

        composable(
            route = "details/{breedId}",
            arguments = listOf(
                navArgument("breedId") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            val breedId = navBackStackEntry.arguments?.getString("breedId") ?: ""

            BreedDetailsScreen(
                breedId = breedId,
                onClose = {
                    navController.navigateUp()
                }
            )
        }

//        composable(
//            route = "editor?$PASSWORD_ID_ARG={$PASSWORD_ID_ARG}",
//            arguments = listOf(
//                navArgument(name = PASSWORD_ID_ARG) {
//                    type = NavType.IntType
//                    nullable = false
//                    defaultValue = Password.INVALID_ID
//                },
//            ),
//        ) { navBackStackEntry ->
//            val passwordId = navBackStackEntry.passwordIdOrThrow
//
//            PasswordEditorScreen(
//                passwordId = passwordId,
//                onClose = {
//                    navController.navigateUp()
//                },
//            )
//        }
    }
}

private fun NavGraphBuilder.breedList(
    route: String,
    onBreedClick: (String) -> Unit,
) = composable(
    route = route
) {
    val breedListViewModel = hiltViewModel<BreedsListViewModel>()
    val state = breedListViewModel.state.collectAsState()
    BreedListScreen(
        state = state.value,
        onBreedClick = onBreedClick,
    )
}