package com.example.flashfun.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.flashfun.data.FlashCard
import com.example.flashfun.data.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class StudyUiState(
    val currentCard: FlashCard? = null,
    val isAnswerVisible: Boolean = false,
    val userAnswer: String = "",
    val currentIndex: Int = 0,
    val totalCards: Int = 0,
    val correctCount: Int = 0,
    val isFinished: Boolean = false,
    val setTitle: String = ""
)

class StudyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StudyUiState())
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    private var cards: List<FlashCard> = emptyList()

    fun loadSet(setId: String) {
        val set = Repository.getSetById(setId) ?: return
        cards = set.cards.shuffled()
        _uiState.update {
            StudyUiState(
                currentCard = cards.firstOrNull(),
                isAnswerVisible = false,
                userAnswer = "",
                currentIndex = 0,
                totalCards = cards.size,
                correctCount = 0,
                isFinished = false,
                setTitle = set.title
            )
        }
    }

    fun onUserAnswerChange(text: String) {
        _uiState.update { it.copy(userAnswer = text) }
    }

    fun showAnswer() {
        _uiState.update { it.copy(isAnswerVisible = true) }
    }

    fun onKnow() = handleAnswer(correct = true)

    fun onDontKnow() = handleAnswer(correct = false)

    private fun handleAnswer(correct: Boolean) {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1
        val newCorrect = if (correct) state.correctCount + 1 else state.correctCount

        if (nextIndex >= cards.size) {
            _uiState.update { it.copy(correctCount = newCorrect, isFinished = true) }
        } else {
            _uiState.update {
                it.copy(
                    currentCard = cards[nextIndex],
                    currentIndex = nextIndex,
                    isAnswerVisible = false,
                    userAnswer = "",
                    correctCount = newCorrect
                )
            }
        }
    }
}
