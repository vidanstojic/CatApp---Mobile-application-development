package com.example.proba2.breeds.list

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.breeds.list.model.CatBreedUiModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UserListScreen(
    state: CatBreedsListState,
    onBreedClick: (String) -> Unit,
) {
    val uiScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }

    Log.d("test", "{${listState.firstVisibleItemIndex}}")

    LaunchedEffect(showScrollToTop) {
        Log.w("test", "shouldShow = $showScrollToTop")
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(title = { Text(text = "Breeds") })
        },
        floatingActionButton = {
            if (showScrollToTop) {
                FloatingActionButton(
                    onClick = {
                        uiScope.launch { listState.scrollToItem(index = 0) }
                    },
                ) {

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

                item(
                    key = "1",
                    contentType = "Plain Header",
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = Color.Red)
                            .fillMaxWidth()
                            .height(128.dp),
                        contentAlignment = Alignment.Center,
                    ) {

                        Text(text = "This is a text in an item.", color = Color.White)
                    }
                }

                stickyHeader(
                    contentType = "Sticky Header"
                ) {
                    Text(
                        modifier = Modifier
                            .background(color = Color.Blue)
                            .fillMaxWidth()
                            .padding(all = 16.dp),
                        text = "This is a sticky header.",
                        color = Color.White,
                    )
                }

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

                items(count = 5) {

                }
            }
        }
    )
}

@Composable
private fun BreedListItem(
    model: CatBreedUiModel,
    onBreedClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(CardDefaults.shape)
            .clickable { onBreedClick(model.id) },
    ) {
        Row {
            SubcomposeAsyncImage(
                modifier = Modifier.size(100.dp),
                model = model.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
//                                contentScale = ContentScale.Fit,
//                                contentScale = ContentScale.FillWidth,
//                                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            imageVector = Icons.Default.Error,
                            contentDescription = null
                        )
                    }
                }
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                text = "@${model.id}\n${model.name}",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}
