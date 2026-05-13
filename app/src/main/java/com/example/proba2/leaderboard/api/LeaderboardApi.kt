package com.example.proba2.leaderboard.api

import com.example.proba2.leaderboard.api.dto.LeaderboardDto
import retrofit2.http.GET

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(): List<LeaderboardDto>
}

