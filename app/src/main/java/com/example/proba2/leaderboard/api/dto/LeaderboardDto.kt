package com.example.proba2.leaderboard.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardDto(
    val nickname: String,
    val score: Int,
    val gamesPlayed: Int
)

