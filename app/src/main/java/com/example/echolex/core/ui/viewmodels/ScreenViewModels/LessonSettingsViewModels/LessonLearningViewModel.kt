package com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.lesson.LessonLearningUseCase
import com.example.echolex.core.domain.useCase.lesson.GetCurrentLessonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonLearningViewModel @Inject constructor(
    private val lessonLearningUseCase: LessonLearningUseCase,
    private val getCurrentLessonUseCase: GetCurrentLessonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LessonLearningUiState>(LessonLearningUiState.Loading)
    val uiState: StateFlow<LessonLearningUiState> = _uiState.asStateFlow()

    private var currentLesson: Lesson? = null
    private var currentCard: Card? = null

    init {
        loadCurrentLesson()
    }

    private fun loadCurrentLesson() {
        viewModelScope.launch {
            val lesson = getCurrentLessonUseCase()
            if (lesson != null) {
                startLesson(lesson)
            } else {
                _uiState.value = LessonLearningUiState.Error(
                    AppNotification.Error.Generic("Урок не знайдено")
                )
            }
        }
    }

    // Функція для ініціалізації з параметрами навігації
    fun initializeWithLesson(lesson: Lesson) {
        startLesson(lesson)
    }

    fun startLesson(lesson: Lesson) {
        viewModelScope.launch {
            _uiState.value = LessonLearningUiState.Loading
            
            // Валідуємо урок перед початком
            val validationResult = lessonLearningUseCase.validateLesson(lesson)
            when (validationResult) {
                is com.example.echolex.core.domain.service.lesson.LessonResult.Error -> {
                    _uiState.value = LessonLearningUiState.Error(validationResult.notification)
                    return@launch
                }
                is com.example.echolex.core.domain.service.lesson.LessonResult.UpdatedLesson -> {
                    currentLesson = validationResult.lesson
                }
                else -> {
                    _uiState.value = LessonLearningUiState.Error(AppNotification.Error.Generic("Invalid lesson"))
                    return@launch
                }
            }
            
            loadNextCard()
        }
    }

    fun loadNextCard() {
        val lesson = currentLesson ?: return
        
        viewModelScope.launch {
            val (card, error) = lessonLearningUseCase.getNextCardOrError(lesson)
            
            if (error != null) {
                _uiState.value = LessonLearningUiState.Error(error)
                return@launch
            }
            
            if (card == null) {
                _uiState.value = LessonLearningUiState.Completed
                return@launch
            }
            
            currentCard = card
            
            val (stageInfo, stageError) = lessonLearningUseCase.getStageInfoOrError(lesson)
            val (progress, progressError) = lessonLearningUseCase.getProgressOrError(lesson)
            
            if (stageError != null) {
                _uiState.value = LessonLearningUiState.Error(stageError)
                return@launch
            }
            
            if (progressError != null) {
                _uiState.value = LessonLearningUiState.Error(progressError)
                return@launch
            }
            
            _uiState.value = LessonLearningUiState.ShowCard(
                card = card,
                stageInfo = stageInfo ?: "Невідомий етап",
                progress = progress ?: 0f
            )
        }
    }

    fun onCardCorrect() {
        val lesson = currentLesson ?: return
        val card = currentCard ?: return

        viewModelScope.launch {
            val result = lessonLearningUseCase.markCardAsCorrect(lesson, card)
            
            when (result) {
                is com.example.echolex.core.domain.service.lesson.LessonResult.UpdatedLesson -> {
                    currentLesson = result.lesson
                    loadNextCard()
                }
                is com.example.echolex.core.domain.service.lesson.LessonResult.Error -> {
                    _uiState.value = LessonLearningUiState.Error(result.notification)
                }
                is com.example.echolex.core.domain.service.lesson.LessonResult.Completed -> {
                    _uiState.value = LessonLearningUiState.Completed
                }
                else -> {
                    _uiState.value = LessonLearningUiState.Error(AppNotification.Error.Generic("Unexpected result"))
                }
            }
        }
    }

    fun onCardIncorrect() {
        val lesson = currentLesson ?: return
        val card = currentCard ?: return

        viewModelScope.launch {
            val result = lessonLearningUseCase.markCardAsIncorrect(lesson, card)
            
            when (result) {
                is com.example.echolex.core.domain.service.lesson.LessonResult.UpdatedLesson -> {
                    currentLesson = result.lesson
                    loadNextCard()
                }
                is com.example.echolex.core.domain.service.lesson.LessonResult.Error -> {
                    _uiState.value = LessonLearningUiState.Error(result.notification)
                }
                is com.example.echolex.core.domain.service.lesson.LessonResult.Completed -> {
                    _uiState.value = LessonLearningUiState.Completed
                }
                else -> {
                    _uiState.value = LessonLearningUiState.Error(AppNotification.Error.Generic("Unexpected result"))
                }
            }
        }
    }

    fun flipCard() {
        val currentState = _uiState.value
        if (currentState is LessonLearningUiState.ShowCard) {
            _uiState.value = currentState.copy(
                card = currentState.card.flipCard()
            )
        }
    }
    
    fun dismissError() {
        val currentState = _uiState.value
        if (currentState is LessonLearningUiState.Error) {
            loadNextCard()
        }
    }
}

sealed class LessonLearningUiState {
    object Loading : LessonLearningUiState()
    data class ShowCard(
        val card: Card,
        val stageInfo: String,
        val progress: Float
    ) : LessonLearningUiState()
    data class Error(val notification: AppNotification) : LessonLearningUiState()
    object Completed : LessonLearningUiState()
}