package com.example.flashfun.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashfun.ui.viewmodel.StudyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(
    setId: String,
    onFinished: (correct: Int, total: Int) -> Unit,
    onBack: () -> Unit,
    viewModel: StudyViewModel = viewModel()
) {
    LaunchedEffect(setId) { viewModel.loadSet(setId) }

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isFinished) {
        if (state.isFinished) {
            onFinished(state.correctCount, state.totalCards)
        }
    }

    val progress by animateFloatAsState(
        targetValue = if (state.totalCards > 0)
            (state.currentIndex + 1).toFloat() / state.totalCards
        else 0f,
        animationSpec = tween(400, easing = EaseOutCubic),
        label = "progress"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.setTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(8.dp))

            // Progress section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${state.currentIndex + 1} из ${state.totalCards}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "✅ ${state.correctCount}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp),
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
            )

            Spacer(Modifier.height(32.dp))

            // Card area
            state.currentCard?.let { card ->
                // var cardKey by remember { mutableIntStateOf(0) }

                // LaunchedEffect(state.currentIndex) { cardKey = state.currentIndex }

                AnimatedContent(
                    targetState = card,
                    transitionSpec = {
                        (slideInHorizontally { it / 3 } + fadeIn(tween(220)))
                            .togetherWith(slideOutHorizontally { -it / 3 } + fadeOut(tween(180)))
                    },
                    label = "card_transition"
                ) { currentCard ->
                    FlashCard(
                        question = currentCard.question,
                        answer = currentCard.answer,
                        isAnswerVisible = state.isAnswerVisible,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(32.dp))

                // Buttons
                AnimatedContent(
                    targetState = state.isAnswerVisible,
                    transitionSpec = {
                        fadeIn(tween(200)) togetherWith fadeOut(tween(150))
                    },
                    label = "buttons_transition"
                ) { showingAnswer ->
                    if (showingAnswer) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Don't know
                            OutlinedButton(
                                onClick = viewModel::onDontKnow,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text(
                                    text = "✗  Не знаю",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            // Know
                            Button(
                                onClick = viewModel::onKnow,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = MaterialTheme.shapes.large,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(
                                    text = "✓  Знаю",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = viewModel::showAnswer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text(
                                text = "Показать ответ",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashCard(
    question: String,
    answer: String,
    isAnswerVisible: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 280.dp)
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Question section
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "ВОПРОС",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Text(
                    text = question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = MaterialTheme.typography.titleMedium.lineHeight
                )
            }

            // Divider
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )

            // Answer section
            AnimatedVisibility(
                visible = isAnswerVisible,
                enter = fadeIn(tween(300)) + expandVertically(tween(300, easing = EaseOutCubic))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = "ОТВЕТ",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(
                        text = answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                }
            }

            if (!isAnswerVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Нажмите «Показать ответ»",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(0.7f)
                    )
                }
            }
        }
    }
}
