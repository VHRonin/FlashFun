package com.example.flashfun.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashfun.ui.viewmodel.CardDraft
import com.example.flashfun.ui.viewmodel.CreateDeckViewModel

private val EMOJI_OPTIONS = listOf(
    "📚", "🎯", "🧠", "⚡", "🏗️", "🖌️", "🔬", "🌍", "🎵", "🍕",
    "💡", "🚀", "🌱", "🔑", "🏆", "💎", "🎲", "🌊", "🔥", "🤖"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: CreateDeckViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    var showEmojiPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Новый набор",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.save() }) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Сохранить",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = viewModel::addCard,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Добавить карточку") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 12.dp,
                bottom = 120.dp // space for FAB
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Deck info card
            item {
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Информация о наборе",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.SemiBold
                        )

                        // Emoji + Title row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Emoji button
                            Surface(
                                onClick = { showEmojiPicker = !showEmojiPicker },
                                shape = MaterialTheme.shapes.medium,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(56.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = state.emoji.ifBlank { "📚" },
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = state.title,
                                onValueChange = viewModel::onTitleChange,
                                modifier = Modifier.weight(1f),
                                label = { Text("Название набора *") },
                                isError = "title" in state.errors,
                                supportingText = if ("title" in state.errors) {
                                    { Text("Обязательное поле") }
                                } else null,
                                singleLine = true,
                                shape = MaterialTheme.shapes.large
                            )
                        }

                        // Emoji picker
                        AnimatedVisibility(visible = showEmojiPicker) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "Выбери иконку",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                // 2 rows of 10
                                EMOJI_OPTIONS.chunked(10).forEach { row ->
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        row.forEach { emoji ->
                                            Surface(
                                                onClick = {
                                                    viewModel.onEmojiChange(emoji)
                                                    showEmojiPicker = false
                                                },
                                                shape = MaterialTheme.shapes.small,
                                                color = if (state.emoji == emoji)
                                                    MaterialTheme.colorScheme.primaryContainer
                                                else
                                                    MaterialTheme.colorScheme.surfaceContainerHigh,
                                                modifier = Modifier.size(36.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(emoji)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = state.description,
                            onValueChange = viewModel::onDescriptionChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Описание (необязательно)") },
                            singleLine = true,
                            shape = MaterialTheme.shapes.large
                        )
                    }
                }
            }

            // Section header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Карточки",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = "${state.cards.size} шт.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Card editors
            itemsIndexed(
                items = state.cards,
                key = { _, card -> card.id }
            ) { index, card ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically()
                ) {
                    CardEditor(
                        index = index + 1,
                        card = card,
                        isError = "card_${card.id}" in state.errors,
                        canDelete = state.cards.size > 1,
                        onQuestionChange = { viewModel.onQuestionChange(card.id, it) },
                        onAnswerChange = { viewModel.onAnswerChange(card.id, it) },
                        onDelete = { viewModel.removeCard(card.id) }
                    )
                }
            }

            // Save button at bottom
            item {
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.save() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Сохранить набор",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun CardEditor(
    index: Int,
    card: CardDraft,
    isError: Boolean,
    canDelete: Boolean,
    onQuestionChange: (String) -> Unit,
    onAnswerChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (isError)
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "Карточка $index",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                if (canDelete) {
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Удалить карточку",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (isError) {
                Text(
                    text = "Заполни вопрос и ответ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedTextField(
                value = card.question,
                onValueChange = onQuestionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Вопрос *") },
                minLines = 2,
                maxLines = 3,
                isError = isError && card.question.isBlank(),
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = card.answer,
                onValueChange = onAnswerChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Ответ *") },
                minLines = 2,
                maxLines = 4,
                isError = isError && card.answer.isBlank(),
                shape = MaterialTheme.shapes.large
            )
        }
    }
}
