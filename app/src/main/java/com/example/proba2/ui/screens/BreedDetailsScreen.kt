package com.example.proba2.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.breeds.list.CatBreedsViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.theme.CatalistOnSurface
import com.example.proba2.ui.theme.CatalistPrimary
import rs.edu.raf.rma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedDetailsScreen(
    breedId: String,
    onClose: () -> Unit,
    viewModel: CatBreedsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val logo = painterResource(id = R.drawable.catalist2)

    // 👇 Breed se traži i kešira kada se promeni lista
    val breed by remember(state.breeds) {
        derivedStateOf {
            state.breeds.find { it.id == breedId }
        }
    }

    Scaffold(
        containerColor = CatalistPrimary,
        topBar = {
            AppTopBar(
                logoPainter = logo,
                onMenuClick = { /* TODO */ },
                onSearchClick = { /* TODO */ }
            )
        }
    ) { padding ->
        when {
            // ⏳ Prikaz loading indikatora dok se lista još učitava
            state.breeds.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            // ✅ Prikaz detalja ako je pronađena rasa
            breed != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    SubcomposeAsyncImage(
                        model = breed!!.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clip(MaterialTheme.shapes.medium),
                        contentScale = ContentScale.Inside
                    )

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

                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(breed!!.wikipediaUrl))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Open on Wikipedia", color = Color.Black)
                    }
                }
            }

            // ❌ Ako stvarno nema rase — prikaz poruke
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Breed not found", style = MaterialTheme.typography.bodyLarge, color = CatalistOnSurface)
                }
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
                val color = if (index < value) Color(0xFF4CAF50) else Color.LightGray
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .padding(end = 2.dp)
                        .background(color = color, shape = CircleShape)
                )
            }
        }
    }
}
