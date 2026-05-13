package com.example.proba2.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Scaffold
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.compose.AppDrawerContent
import com.example.proba2.ui.theme.CatalistPrimary
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import rs.edu.raf.rma.R
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(
    breedId: String,
    navController: NavController,
    viewModel: CatBreedsViewModel = hiltViewModel()
) {
    val images by viewModel.breedImages.collectAsState()
    val logo = painterResource(id = R.drawable.catalist2)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val uiScope = rememberCoroutineScope()

    LaunchedEffect(breedId) {
        if (images.isEmpty()) {
            viewModel.loadBreedImages(breedId)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CatalistPrimary,
                drawerTonalElevation = 0.dp,
            ) {
                AppDrawerContent(
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        uiScope.launch { drawerState.close() }
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            containerColor = CatalistPrimary,
            topBar = {
                AppTopBar(
                    logoPainter = logo,
                    onMenuClick = { uiScope.launch { drawerState.open() } },
                    onSearchSubmit = { query -> navController.navigate("search/$query") }
                )
            }
        ) { paddingValues ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(images) { imageUrl ->
                    SubcomposeAsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                val encodedUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString())
                                navController.navigate("viewer/${breedId}/${encodedUrl}")
                            }
                    )
                }
            }
        }
    }
}
