package com.example.flashfun.data

data class FlashCard(
    val question: String,
    val answer: String
)

data class FlashCardSet(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val cards: List<FlashCard>
)
