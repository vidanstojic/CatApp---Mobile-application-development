package com.example.proba2.leaderboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proba2.leaderboard.viewmodel.LeaderboardViewModel
import com.example.proba2.ui.compose.AppTopBar
import com.example.proba2.ui.compose.AppDrawerContent
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import rs.edu.raf.rma.R
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import kotlinx.coroutines.launch

@Composable
fun LeaderboardScreen(
	viewModel: LeaderboardViewModel = hiltViewModel(),
	navController: NavController
) {
	val entries by viewModel.entries.collectAsState()
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
			containerColor = com.example.proba2.ui.theme.CatalistPrimary,
			topBar = {
				AppTopBar(
					logoPainter = logo,
					onMenuClick = { uiScope.launch { drawerState.open() } },
					onSearchSubmit = { }
				)
			}
		) { paddingValues ->
			Surface(modifier = Modifier.fillMaxSize().padding(paddingValues), color = com.example.proba2.ui.theme.CatalistPrimary) {
				Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
					Text("Global Leaderboard", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(8.dp))
					HorizontalDivider()
					LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
						itemsIndexed(entries) { index, entry ->
							LeaderboardRow(rank = index + 1, nickname = entry.nickname, score = entry.score, games = entry.gamesPlayed)
							HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
						}
					}
				}
			}
		}
	}
}

@Composable
fun LeaderboardRow(rank: Int, nickname: String, score: Int, games: Int) {
	Row(modifier = Modifier.fillMaxWidth().padding(4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
		Row {
			Text("#${rank}", color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.width(56.dp))
			Column {
				Text(nickname, color = MaterialTheme.colorScheme.onPrimary)
				Text("Games: $games", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
			}
		}
		Text("${score}", color = MaterialTheme.colorScheme.onPrimary)
	}
}


