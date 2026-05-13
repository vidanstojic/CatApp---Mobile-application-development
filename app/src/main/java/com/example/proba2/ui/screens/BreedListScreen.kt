package com.example.proba2.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.breeds.list.CatBreedsListState
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.theme.CatalistOnSurface
import com.example.proba2.ui.theme.CatalistPrimary
import com.example.proba2.ui.theme.CatalistSecondary
import kotlinx.coroutines.launch
import rs.edu.raf.rma.R

data class DrawerNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val drawerNavItems = listOf(
    DrawerNavItem("Početna",    "breeds",      Icons.Filled.Home,        Icons.Outlined.Home),
    DrawerNavItem("Quiz",       "quiz",        Icons.Filled.Quiz,        Icons.Outlined.Quiz),
    DrawerNavItem("Leaderboard","leaderboard", Icons.Filled.Leaderboard, Icons.Outlined.Leaderboard),
)

@Composable
fun AppDrawerContent(
    currentRoute: String?,
    onItemClick: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(CatalistSecondary, CatalistPrimary)
                )
            ),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "🐱 Catalist",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = CatalistOnSurface
            )
            Text(
                text = "Explore cat breeds",
                fontSize = 13.sp,
                color = CatalistOnSurface.copy(alpha = 0.7f)
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    drawerNavItems.forEach { item ->
        val selected = currentRoute == item.route

        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                    contentDescription = item.label,
                )
            },
            label = {
                Text(
                    text = item.label,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            },
            selected = selected,
            onClick = { onItemClick(item.route) },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = CatalistSecondary.copy(alpha = 0.25f),
                selectedIconColor = CatalistSecondary,
                selectedTextColor = CatalistSecondary,
                unselectedIconColor = CatalistOnSurface.copy(alpha = 0.65f),
                unselectedTextColor = CatalistOnSurface.copy(alpha = 0.65f),
            )
        )
    }
}
@Composable
fun BreedListScreen(
    state: CatBreedsListState,
    onBreedClick: (String) -> Unit,
    navController: NavController,
) {
    val uiScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val showScrollToTop by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    val logo = painterResource(id = R.drawable.catalist2)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
                    onMenuClick = {
                        uiScope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    },
                    onSearchSubmit = { query ->
                        navController.navigate("search/$query")
                    }
                )
            },

            floatingActionButton = {
                if (showScrollToTop) {
                    FloatingActionButton(
                        onClick = { uiScope.launch { listState.scrollToItem(0) } },
                        containerColor = CatalistSecondary,
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = "Scroll to Top",
                            tint = CatalistOnSurface,
                        )
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
                        key = { it.id },
                        contentType = { "BreedListItem" },
                    ) {
                        BreedListItem(model = it, onBreedClick = onBreedClick)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreedListItem(
    model: CatBreedUiModel,
    onBreedClick: (String) -> Unit,
) {
    val context = LocalContext.current
    Card(
        colors = CardDefaults.cardColors(containerColor = CatalistSecondary),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onBreedClick(model.id) },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(model.imageUrl)
                    .size(200)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = model.name, style = MaterialTheme.typography.titleMedium, color = CatalistOnSurface)

                model.alternativeName?.takeIf { it.isNotBlank() }?.let {
                    Text(text = "aka: $it", style = MaterialTheme.typography.bodySmall, color = CatalistOnSurface)
                }

                Text(
                    text = model.description.take(200) + if (model.description.length > 200) "…" else "",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 6.dp),
                    color = CatalistOnSurface,
                )

                if (model.temperament.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        model.temperament.split(",").map { it.trim() }.take(5).forEach { trait ->
                            AssistChip(
                                onClick = {},
                                label = { Text(text = trait, color = Color.White) },
                                border = BorderStroke(1.dp, Color.White),
                                colors = AssistChipDefaults.assistChipColors(containerColor = Color.Transparent),
                            )
                        }
                    }
                }
            }
        }
    }
}