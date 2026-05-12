package com.example.proba2.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.SubcomposeAsyncImage
import com.example.proba2.quiz.model.QuizQuestion
import com.example.proba2.quiz.viewmodel.QuizViewModel
import androidx.compose.material3.AlertDialog

@Composable
fun QuizScreen(
    onFinish: (score: Int, total: Int) -> Unit,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val questions by viewModel.questions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val remaining by viewModel.remainingSeconds.collectAsState()
    val finished by viewModel.finished.collectAsState()
    val answers by viewModel.answers.collectAsState()

    var showCancelDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        showCancelDialog = true
    }

    if (showCancelDialog) {
        AlertDialog(onDismissRequest = { showCancelDialog = false }, confirmButton = {
                    TextButton(onClick = {
                // cancel quiz, do not count result: signal cancellation with negative score
                showCancelDialog = false
                onFinish(-1, questions.size)
            }) {
                Text("Yes, exit")
            }
        }, dismissButton = {
            TextButton(onClick = { showCancelDialog = false }) {
                Text("No")
            }
        }, title = { Text("Exit quiz?") }, text = { Text("Are you sure you want to quit the quiz? Your progress will be lost.") })
    }

    LaunchedEffect(finished) {
        if (finished) {
            val score = viewModel.computeScore()
            onFinish(score, questions.size)
        }
    }

    if (questions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions available", color = Color.White)
        }
        return
    }

    val q: QuizQuestion = questions[currentIndex]

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Question ${currentIndex + 1} / ${questions.size}", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Text(text = "Time left: ${remaining}s", style = MaterialTheme.typography.bodySmall, color = Color.White)
                Spacer(modifier = Modifier.height(12.dp))

                q.imageUrl?.let { url ->
                    SubcomposeAsyncImage(model = url, contentDescription = null, modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp), contentScale = ContentScale.Crop)
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(text = q.questionText, style = MaterialTheme.typography.headlineSmall, color = Color.White)
                Spacer(modifier = Modifier.height(16.dp))

                val answeredAlready = answers.containsKey(currentIndex)
                q.options.forEachIndexed { idx, option ->
                    val selected = answers[currentIndex] == idx
                    Button(onClick = { viewModel.answerCurrent(idx) }, enabled = !answeredAlready, modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp), colors = ButtonDefaults.buttonColors(containerColor = if (selected) Color.DarkGray else Color.White)) {
                        Text(option, color = if (selected) Color.White else Color.Black, textAlign = TextAlign.Start, modifier = Modifier.fillMaxWidth())
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { showCancelDialog = true }) {
                    Text("Exit", color = Color.White)
                }

                Button(onClick = { viewModel.nextQuestion() }, enabled = answers.containsKey(currentIndex)) {
                    Text(if (currentIndex == questions.size - 1) "Finish" else "Next")
                }
            }
        }
    }
}



