package com.example.proba2.leaderboard.repository

import com.example.proba2.leaderboard.api.LeaderboardApi
import com.example.proba2.leaderboard.model.LeaderboardEntry
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val api: LeaderboardApi
) {
    suspend fun fetchAll(): List<LeaderboardEntry> {
        return try {
            val dto = api.getLeaderboard()
            dto.map { LeaderboardEntry(it.nickname, it.score, it.gamesPlayed) }
                .sortedByDescending { it.score }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

