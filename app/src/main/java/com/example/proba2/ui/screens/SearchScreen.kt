package com.example.proba2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.theme.CatalistPrimary
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    query: String,
    onBreedClick: (String) -> Unit,
    viewModel: CatBreedsViewModel = hiltViewModel()
) {
    val results by viewModel.results.collectAsState()
    val uiScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    LaunchedEffect(query) {
        viewModel.search(query)
    }

    Scaffold(
        containerColor = CatalistPrimary,
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
                    items = results.breeds,
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
