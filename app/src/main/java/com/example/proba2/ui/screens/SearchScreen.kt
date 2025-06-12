package com.example.proba2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.ui.theme.CatalistPrimary

@Composable
fun SearchScreen(
    query: String,
    onBreedClick: (String) -> Unit,
    viewModel: CatBreedsViewModel = hiltViewModel()
) {
    val results by viewModel.results.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val scope = rememberCoroutineScope()

    // Lokalno stanje za prikaz sa ažuriranim slikama
    var itemsWithImages by remember { mutableStateOf<List<CatBreedUiModel>>(emptyList()) }

    // Prvo pozivamo pretragu po query-ju
    LaunchedEffect(query) {
        viewModel.search(query)
    }

    // Kada se stignu rezultati, asinhrono pokupi slike za njih
    LaunchedEffect(results) {
        itemsWithImages = results // privremeno bez slika

        val updated = results.map { breed ->
            val url = viewModel.fetchBreedImage(breed.imageId)
            breed.copy(imageUrl = url.toString())
        }
        itemsWithImages = updated
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CatalistPrimary)
    ) {
        when {
            loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            itemsWithImages.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No breeds found.", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(itemsWithImages, key = { it.id }) { breed ->
                        BreedListItem(
                            model = breed,
                            onBreedClick = { onBreedClick(breed.id) }
                        )
                    }
                }
            }
        }
    }
}
