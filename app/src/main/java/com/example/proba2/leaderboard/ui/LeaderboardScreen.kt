package com.example.proba2.leaderboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.proba2.leaderboard.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
	viewModel: LeaderboardViewModel = hiltViewModel()
) {
	val entries by viewModel.entries.collectAsState()

	Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
		Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
			Text("Global Leaderboard", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(8.dp))
			Divider()
			LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp)) {
				itemsIndexed(entries) { index, entry ->
					LeaderboardRow(rank = index + 1, nickname = entry.nickname, score = entry.score, games = entry.gamesPlayed)
					Divider(modifier = Modifier.padding(vertical = 6.dp))
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


