package com.example.proba2.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.breeds.list.CatBreedsListState
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.theme.CatalistOnSurface
import com.example.proba2.ui.theme.CatalistPrimary
import com.example.proba2.ui.theme.CatalistSecondary
import kotlinx.coroutines.launch
import rs.edu.raf.rma.R

@Composable
fun BreedListScreen(
    state: CatBreedsListState,
    onBreedClick: (String) -> Unit,
    navController: NavController
) {
    val uiScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    val logo = painterResource(id = R.drawable.catalist2)

    Scaffold(
        containerColor = CatalistPrimary,
        topBar = {
            AppTopBar(
                logoPainter = logo,
                onMenuClick = { /* ako treba */ },
                onSearchSubmit = { query ->
                    navController.navigate("search/${query}")
                }
            )
        },
        floatingActionButton = {
            if (showScrollToTop) {
                FloatingActionButton(
                    onClick = {
                        uiScope.launch { listState.scrollToItem(index = 0) }
                    }
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to Top")
                }
            }
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(
                    items = state.breeds,
                    key = { breed -> breed.id },
                    contentType = { "BreedListItem" },
                ) {
                    BreedListItem(
                        model = it,
                        onBreedClick = onBreedClick,
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreedListItem(
    model: CatBreedUiModel,
    onBreedClick: (String) -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CatalistSecondary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onBreedClick(model.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            SubcomposeAsyncImage(
                model = model.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
                loading = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeWidth = 2.dp)
                    }
                },
                error = {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Error, contentDescription = null, tint = Color.Red)
                    }
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = model.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = CatalistOnSurface
                )

                model.alternativeName?.takeIf { it.isNotBlank() }?.let {
                    Text(
                        text = "aka: $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = CatalistOnSurface
                    )
                }

                Text(
                    text = model.description.take(200) + if (model.description.length > 200) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 6.dp),
                    color = CatalistOnSurface
                )

                if (model.temperament.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        model.temperament
                            .split(",")
                            .map { it.trim() }
                            .take(5)
                            .forEach { trait ->
                                AssistChip(
                                    onClick = { },
                                    label = {
                                        Text(
                                            text = trait,
                                            color = Color.White
                                        )
                                    },
                                    border = BorderStroke(1.dp, Color.White),
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color.Transparent
                                    )
                                )
                            }
                    }
                }
            }
        }
    }
}

