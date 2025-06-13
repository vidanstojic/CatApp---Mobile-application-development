package com.example.proba2.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.google.accompanist.pager.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.theme.CatalistOnSurface
import com.example.proba2.ui.theme.CatalistPrimary
import rs.edu.raf.rma.R

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    breedId: String,
    onClose: () -> Unit,
    viewModel: CatBreedsViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val logoPainter: Painter = painterResource(id = R.drawable.catalist2)

    // Učitavanje detalja i slika
    LaunchedEffect(breedId) {
        viewModel.fetchBreedDetails(breedId)
        viewModel.loadBreedImages(breedId)
    }

    val breed by viewModel.selectedBreed.collectAsState()
    val images by viewModel.breedImages.collectAsState()

    Scaffold(
        containerColor = CatalistPrimary,
        topBar = {
            AppTopBar(
                logoPainter = logoPainter,
                onMenuClick = { /* ne koristiš sada */ },
                onSearchSubmit = { query ->
                    navController.navigate("search/$query")
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                breed == null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(padding)
                            .padding(16.dp)
                    ) {
                        // 🔁 Slider sa slikama
                        if (images.isNotEmpty()) {
                            ImageSlider(imageUrls = images)
//                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Prikaz detalja
//                        SubcomposeAsyncImage(
//                            model = breed!!.imageUrl,
//                            contentDescription = null,
//                            contentScale = ContentScale.Inside,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .wrapContentHeight()
//                                .clip(MaterialTheme.shapes.medium)
//                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Name: ${breed!!.name}", style = MaterialTheme.typography.titleLarge, color = CatalistOnSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Description: ${breed!!.description}", style = MaterialTheme.typography.bodyMedium, color = CatalistOnSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Origin: ${breed!!.originCountries}", style = MaterialTheme.typography.bodyMedium, color = CatalistOnSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Temperament: ${breed!!.temperament}", style = MaterialTheme.typography.bodyMedium, color = CatalistOnSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Life Span: ${breed!!.lifespan}", style = MaterialTheme.typography.bodyMedium, color = CatalistOnSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Weight: ${breed!!.weight.metric} kg", style = MaterialTheme.typography.bodyMedium, color = CatalistOnSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Rare: ${if (breed!!.isRare == 1) "Yes" else "No"}", style = MaterialTheme.typography.bodyMedium, color = CatalistOnSurface)
                        Spacer(modifier = Modifier.height(16.dp))

                        TraitRating("Child Friendly", breed!!.childFriendly)
                        TraitRating("Dog Friendly", breed!!.dogFriendly)
                        TraitRating("Energy Level", breed!!.energyLevel)
                        TraitRating("Intelligence", breed!!.intelligence)
                        TraitRating("Vocalisation", breed!!.vocalisation)
                        Spacer(modifier = Modifier.height(24.dp))

                        breed!!.wikipediaUrl?.let { url ->
                            Button(
                                onClick = {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                            ) {
                                Text("Open on Wikipedia", color = Color.Black)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                navController.navigate("gallery/${breedId}")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
//                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Gallery", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSlider(
    imageUrls: List<String>
) {
    val uniqueImages = remember(imageUrls) { imageUrls.distinct() }

    if (uniqueImages.isEmpty()) {
        Text("No images available", modifier = Modifier.padding(16.dp))
        return
    }

    LazyRow(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(uniqueImages) { url ->
            Card(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .heightIn(max = 240.dp), // ograniči maksimalnu visinu
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                SubcomposeAsyncImage(
                    model = url,
                    contentDescription = "Breed image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.6f) // ili 16:10 — prilagodi kako ti odgovara
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop,
            // zadrzava aspect ratio bez deformacije
                    loading = {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(strokeWidth = 2.dp)
                        }
                    },
                    error = {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = "Error loading image",
                                tint = Color.Red
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
fun TraitRating(label: String, value: Int) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = CatalistOnSurface)
        Row {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .padding(end = 2.dp)
                        .background(
                            if (index < value) Color(0xFF4CAF50) else Color.LightGray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
