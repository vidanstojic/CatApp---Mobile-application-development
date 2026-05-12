package com.example.proba2.quiz

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    onBackToHome: () -> Unit
) {
    val context = LocalContext.current

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
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


