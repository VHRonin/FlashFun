package com.example.flashfun.ui.viewmodel

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.example.flashfun.data.FlashCard
import com.example.flashfun.data.FlashCardSet
import com.example.flashfun.data.Repository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class CardDraft(
    val id: String = UUID.randomUUID().toString(),
    val question: String = "",
    val answer: String = ""
)

data class CreateDeckUiState(
    val title: String = "",
    val description: String = "",
    val emoji: String = "📚",
    val cards: List<CardDraft> = listOf(CardDraft(), CardDraft()),
    val isSaved: Boolean = false,
    val errors: Set<String> = emptySet() // field keys with errors
)

class CreateDeckViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(CreateDeckUiState())
    val uiState: StateFlow<CreateDeckUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) =
        _uiState.update { it.copy(title = value, errors = it.errors - "title") }

    fun onDescriptionChange(value: String) =
        _uiState.update { it.copy(description = value) }

    fun onEmojiChange(value: String) =
        _uiState.update { it.copy(emoji = value.take(2)) }

    fun onQuestionChange(cardId: String, value: String) =
        _uiState.update { state ->
            state.copy(
                cards = state.cards.map { if (it.id == cardId) it.copy(question = value) else it },
                errors = state.errors - "card_$cardId"
            )
        }

    fun onAnswerChange(cardId: String, value: String) =
        _uiState.update { state ->
            state.copy(
                cards = state.cards.map { if (it.id == cardId) it.copy(answer = value) else it },
                errors = state.errors - "card_$cardId"
            )
        }

    fun addCard() =
        _uiState.update { it.copy(cards = it.cards + CardDraft()) }

    fun removeCard(cardId: String) {
        val current = _uiState.value.cards
        if (current.size <= 1) return
        _uiState.update { it.copy(cards = it.cards.filter { c -> c.id != cardId }) }
    }

    fun save(): Boolean {
        val state = _uiState.value
        val errors = mutableSetOf<String>()

        if (state.title.isBlank()) errors.add("title")

        state.cards.forEach { card ->
            if (card.question.isBlank() || card.answer.isBlank()) {
                errors.add("card_${card.id}")
            }
        }

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return false
        }

        val newSet = FlashCardSet(
            id = UUID.randomUUID().toString(),
            title = state.title.trim(),
            description = state.description.trim().ifBlank { "Пользовательский набор" },
            emoji = state.emoji.ifBlank { "📚" },
            cards = state.cards.map { FlashCard(it.question.trim(), it.answer.trim()) }
        )
        Repository.addSet(newSet)
        _uiState.update { it.copy(isSaved = true) }

        Repository.save()
        return true
    }
}
