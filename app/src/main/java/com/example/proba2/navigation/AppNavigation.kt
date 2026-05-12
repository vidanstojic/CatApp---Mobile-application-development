package com.example.proba2.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.example.proba2.ui.screens.*
import com.example.proba2.user.viewmodel.UserProfileViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val userProfileViewModel = hiltViewModel<UserProfileViewModel>()
    val userProfile by userProfileViewModel.userProfile.collectAsState(initial = null)

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userProfile) {
        startDestination = if (userProfile != null) "breeds" else "profile_setup"
    }

    startDestination?.let { start ->
        NavHost(
            navController = navController,
            startDestination = start
        ) {

            composable("profile_setup") {
                ProfileSetupScreen(
                    onProfileCreated = {
                        navController.navigate("breeds") {
                            popUpTo("profile_setup") { inclusive = true }
                        }
                    }
                )
            }

            composable(route = "breeds") { navBackStackEntry ->
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

            composable(
                route = "details/{breedId}",
                arguments = listOf(navArgument("breedId") { type = NavType.StringType })
            ) { navBackStackEntry ->
                val parentEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry("breeds")
                }
                val sharedViewModel = hiltViewModel<CatBreedsViewModel>(parentEntry)
                val breedId = navBackStackEntry.arguments?.getString("breedId") ?: ""

                BreedDetailsScreen(
                    breedId = breedId,
                    onClose = { navController.navigateUp() },
                    viewModel = sharedViewModel,
                    navController = navController
                )
            }

            composable(
                route = "gallery/{breedId}",
                arguments = listOf(
                    navArgument("breedId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val breedId = backStackEntry.arguments?.getString("breedId") ?: ""
                GalleryScreen(
                    breedId = breedId,
                    navController = navController
                )
            }

            composable(
                route = "viewer/{breedId}/{encodedImageUrl}",
                arguments = listOf(
                    navArgument("breedId") { type = NavType.StringType },
                    navArgument("encodedImageUrl") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val breedId = backStackEntry.arguments?.getString("breedId") ?: ""
                val encodedImageUrl = backStackEntry.arguments?.getString("encodedImageUrl") ?: ""
                val decodedUrl = URLDecoder.decode(encodedImageUrl, StandardCharsets.UTF_8.toString())

                PhotoViewerScreen(
                    breedId = breedId,
                    selectedImageUrl = decodedUrl,
                    navController = navController
                )
            }

            composable(
                route = "search/{query}",
                arguments = listOf(navArgument("query") { type = NavType.StringType })
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
}
