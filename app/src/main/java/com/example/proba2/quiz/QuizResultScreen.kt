package com.example.proba2.quiz

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.compose.AppDrawerContent
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import rs.edu.raf.rma.R
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    onBackToHome: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val logo = painterResource(id = R.drawable.catalist2)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val uiScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = com.example.proba2.ui.theme.CatalistPrimary,
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
            containerColor = MaterialTheme.colorScheme.primary,
            topBar = {
                AppTopBar(
                    logoPainter = logo,
                    onMenuClick = { uiScope.launch { drawerState.open() } },
                    onSearchSubmit = { }
                )
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.fillMaxSize().padding(paddingValues), color = MaterialTheme.colorScheme.primary) {
                Column(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Quiz finished", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Score: $score", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Out of: ${totalQuestions * com.example.proba2.quiz.viewmodel.QuizViewModel.POINTS_PER_QUESTION}", color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        // share via intent (placeholder for Leaderboard API)
                        val share = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, "I scored $score points on Cat Quiz!")
                            type = "text/plain"
                        }
                        startActivity(context, Intent.createChooser(share, "Share your result"), null)
                    }) {
                        Text("Share / Post to leaderboard")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(onClick = { onBackToHome() }) {
                        Text("Back to breeds")
                    }
                }
            }
        }
    }
}


