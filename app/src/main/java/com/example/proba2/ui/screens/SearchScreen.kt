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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proba2.breeds.list.CatBreedsViewModel
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.compose.AppDrawerContent
import com.example.proba2.ui.theme.CatalistPrimary
import kotlinx.coroutines.launch
import rs.edu.raf.rma.R

@Composable
fun SearchScreen(
    query: String,
    onBreedClick: (String) -> Unit,
    navController: androidx.navigation.NavController,
    viewModel: CatBreedsViewModel = hiltViewModel()
) {
    val results by viewModel.results.collectAsState()
    val uiScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val logo = painterResource(id = R.drawable.catalist2)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    LaunchedEffect(query) {
        viewModel.search(query)
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
                    onSearchSubmit = { newQuery -> viewModel.search(newQuery) }
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
}
