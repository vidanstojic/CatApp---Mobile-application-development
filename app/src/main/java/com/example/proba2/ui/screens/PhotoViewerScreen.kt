package com.example.proba2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.google.accompanist.pager.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.theme.CatalistPrimary
import rs.edu.raf.rma.R

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PhotoViewerScreen(
    breedId: String,
    selectedImageUrl: String,
    navController: NavController,
    viewModel: CatBreedsViewModel = hiltViewModel()
) {
    val images by viewModel.breedImages.collectAsState()
    val logo = painterResource(id = R.drawable.catalist2)

    val startIndex = images.indexOfFirst { it == selectedImageUrl }.coerceAtLeast(0)

    val pagerState = rememberPagerState(initialPage = startIndex)

    LaunchedEffect(breedId) {
        if (images.isEmpty()) {
            viewModel.loadBreedImages(breedId)
        }
    }

    Scaffold(
        containerColor = CatalistPrimary,
        topBar = {
            AppTopBar(
                logoPainter = logo,
                onMenuClick = { navController.navigate("breeds") },
                onSearchSubmit = { query -> navController.navigate("search/$query") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            if (images.isNotEmpty()) {
                HorizontalPager(
                    count = images.size,
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    SubcomposeAsyncImage(
                        model = images[page],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                HorizontalPagerIndicator(
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    activeColor = Color.White
                )
            } else {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

