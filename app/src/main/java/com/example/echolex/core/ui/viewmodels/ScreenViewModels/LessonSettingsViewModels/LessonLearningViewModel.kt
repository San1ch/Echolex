package com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.data.repository.DeckRepository
import com.example.echolex.core.domain.data.repository.LessonRepository
import com.example.echolex.core.domain.useCase.lesson.GetCurrentLessonUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.BackToPreviousScreenUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonLearningViewModel @Inject constructor(
    private val getCurrentLessonUseCase: GetCurrentLessonUseCase,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase,
    private val backToPreviousScreenUseCase: BackToPreviousScreenUseCase,
    private val saveLessonUseCase: SaveLessonUseCase
) : ViewModel() {

    private var lesson: Lesson? = null

    private val _uiState = MutableStateFlow(LessonLearningUiState.initial())
    val uiState: StateFlow<LessonLearningUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<LessonLearningEvent>(extraBufferCapacity = 1)
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            getCurrentLessonUseCase()
                .onSuccess { loaded ->
                    lesson = loaded
                    step(wasIncorrect = false)
                }
                .onFailure { e ->
                    openAppNotificationUseCase(
                        AppNotification.Error.Generic(
                            e.message ?: "Lesson load error"
                        )
                    )
                }
        }
    }

    fun onKnow() = step(wasIncorrect = false)
    fun onDoNotKnow() = step(wasIncorrect = true)

    private fun step(wasIncorrect: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val l = lesson ?: return@launch

            val result = l.validation(wasIncorrectAnswer = wasIncorrect)


            result.notification?.let { n ->
                when (n) {
                    is AppNotification.Lesson.CardsAreNotExist -> {
                        step(wasIncorrect = false)
                    }

                    is AppNotification.Lesson.NextStage -> {
                        saveLessonUseCase(l)
                    }

                    is AppNotification.Lesson.LessonFinished -> {
                        if (result.ui == null) {
                            saveLessonUseCase(l)
                            openAppNotificationUseCase(AppNotification.Lesson.LessonFinished)

                        } else {
                            saveLessonUseCase(l)
                            _event.emit(LessonLearningEvent.ShowToast(AppNotification.Lesson.LessonRestart))
                        }
                        backToPreviousScreenUseCase()
                    }

                    AppNotification.Null -> Unit
                    else ->
                        _event.emit(LessonLearningEvent.ShowToast(n))
                }
            }
            if(result.ui != null) {
                result.ui.let { nextUi ->
                    _uiState.value = nextUi.copy(isFlipped = false)
                }
            }
            else {
                step(wasIncorrect = false)
            }
        }
    }

    fun onExit() {
        backToPreviousScreenUseCase()
    }

    fun flipCard() {
        _uiState.value = _uiState.value.copy(
            isFlipped = !_uiState.value.isFlipped
        )
    }
}

data class LessonLearningUiState(
    val isFlipped: Boolean = false,

    val card: Card,
    val remainingCards: Int,
    val remainingCycles: Int,
    val wasIncorrect: Boolean,
    val currentIndexStage: Int,
    val stageCount: Int,
) {
    companion object {
        fun initial(): LessonLearningUiState =
            LessonLearningUiState(
                card = Card("", ""),
                remainingCards = 0,
                remainingCycles = 0,
                wasIncorrect = false,
                currentIndexStage = 0,
                stageCount = 0,
            )
    }
}

sealed interface LessonLearningEvent {
    data class ShowToast(val notification: AppNotification) : LessonLearningEvent
}

class SaveLessonUseCase @Inject constructor(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(lesson: Lesson) {
        lessonRepository.upsert(lesson)
    }
}

class IncrementRepeatedCardsUseCase @Inject constructor(
    private val deckRepository: DeckRepository
) {
    suspend operator fun invoke(cards: List<Card>, deckNames: List<String>) {
        deckRepository.incrementCardsRepeatingInDeck(cards, deckNames)
    }
}

class MarkCardsAsPreLearnedUseCase @Inject constructor(
    private val deckRepository: DeckRepository
) {
    suspend operator fun invoke(cards: List<Card>, deckNames: List<String>) {
        deckRepository.markCardsAsPreLearned(cards, deckNames)
    }
}