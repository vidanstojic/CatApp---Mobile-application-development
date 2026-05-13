package com.example.proba2.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.proba2.ui.screens.DrawerNavItem
import com.example.proba2.ui.screens.drawerNavItems
import com.example.proba2.ui.theme.CatalistOnSurface
import com.example.proba2.ui.theme.CatalistPrimary
import com.example.proba2.ui.theme.CatalistSecondary
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch

@Composable
fun AppScreenWithDrawer(
    navController: NavController,
    topBarTitle: String = "",
    content: @Composable () -> Unit
) {
    val uiScope = rememberCoroutineScope()
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
        content()
    }
}

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

