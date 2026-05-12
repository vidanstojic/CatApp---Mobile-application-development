package com.example.proba2.quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba2.breeds.repository.CatBreedsRepository
import com.example.proba2.quiz.model.QuizQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: CatBreedsRepository
) : ViewModel() {

    companion object {
        const val TOTAL_QUESTIONS = 20
        const val TOTAL_TIME_SECONDS = 300 // 5 minutes
        const val POINTS_PER_QUESTION = 5
    }

    private val _questions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val questions: StateFlow<List<QuizQuestion>> = _questions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _answers = MutableStateFlow<MutableMap<Int, Int>>(mutableMapOf())
    val answers: StateFlow<Map<Int, Int>> = _answers.asStateFlow()

    private val _remainingSeconds = MutableStateFlow(TOTAL_TIME_SECONDS)
    val remainingSeconds: StateFlow<Int> = _remainingSeconds.asStateFlow()

    private val _finished = MutableStateFlow(false)
    val finished: StateFlow<Boolean> = _finished.asStateFlow()

    init {
        viewModelScope.launch {
            generateQuestions()
            startTimer()
        }
    }

    private suspend fun generateQuestions() {
        val breeds = repository.observeAllBreeds().first()
        if (breeds.isEmpty()) {
            _questions.value = emptyList()
            return
        }

        val rnd = Random(System.currentTimeMillis())
        val list = mutableListOf<QuizQuestion>()
        val breedNames = breeds.map { it.name }

        repeat(TOTAL_QUESTIONS) { idx ->
            // choose a correct breed
            val correct = breeds[rnd.nextInt(breeds.size)]

            // Prepare options (distinct names)
            val options = mutableSetOf<String>()
            options += correct.name
            while (options.size < 4) {
                options += breedNames[rnd.nextInt(breedNames.size)]
            }

            val shuffled = options.shuffled(rnd)
            val correctIndex = shuffled.indexOf(correct.name)

            val qText = if (correct.imageUrl != null && rnd.nextBoolean()) {
                "Which breed is shown in the image?"
            } else {
                "Which of the following is the origin country of ${correct.name}?" // fallback, options contain names though
            }

            val imageUrl = if (qText.startsWith("Which breed is shown")) correct.imageUrl else null

            list += QuizQuestion(
                id = idx,
                questionText = qText,
                options = shuffled,
                correctIndex = correctIndex,
                imageUrl = imageUrl
            )
        }

        _questions.value = list
    }

    fun answerCurrent(selectedIndex: Int) {
        val idx = _currentIndex.value
        _answers.value[idx] = selectedIndex
    }

    fun nextQuestion() {
        if (_currentIndex.value < TOTAL_QUESTIONS - 1) {
            _currentIndex.value = _currentIndex.value + 1
        } else {
            finishQuiz()
        }
    }

    fun finishQuiz() {
        _finished.value = true
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (_remainingSeconds.value > 0 && !_finished.value) {
                delay(1000)
                _remainingSeconds.value = _remainingSeconds.value - 1
            }
            if (!_finished.value) {
                // time expired
                finishQuiz()
            }
        }
    }

    fun computeScore(): Int {
        val qs = _questions.value
        val ans = _answers.value
        var points = 0
        qs.forEachIndexed { index, question ->
            val selected = ans[index]
            if (selected != null && selected == question.correctIndex) {
                points += POINTS_PER_QUESTION
            }
        }
        return points
    }
}

