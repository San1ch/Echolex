package com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.domain.data.model.lesson.LearningStage
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.model.lesson.LessonSettings
import com.example.echolex.core.domain.data.model.lesson.LessonStage
import com.example.echolex.core.domain.data.model.lesson.RepeatingStage
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.blueprint.CreateBlueprintUseCase
import com.example.echolex.core.domain.useCase.deck.GetDecksFlowUseCase
import com.example.echolex.core.domain.useCase.blueprint.GetFlowBlueprintListUseCase
import com.example.echolex.core.domain.useCase.lesson.GetFlowLessonListUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase

@HiltViewModel
class LessonMenuViewModel @Inject constructor(
    private val getFlowBlueprintListUseCase: GetFlowBlueprintListUseCase,
    private val getFlowLessonListUseCase: GetFlowLessonListUseCase,
    private val getDecksFlowUseCase: GetDecksFlowUseCase,
    private val createBlueprintUseCase: CreateBlueprintUseCase,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(LessonMenuUiState())
    val uiState: State<LessonMenuUiState> get() = _uiState

    val blueprintList = getFlowBlueprintListUseCase()
    val lessonList = getFlowLessonListUseCase()
    val deckList = getDecksFlowUseCase()

    val dialogCenter = DialogCenter()

    fun openSettings() {
        _uiState.value = _uiState.value.copy(
            lessonMenuScreenUiState = LessonMenuScreenUiState.Settings
        )
    }

    fun createLessonButtonClick() {
        if (blueprintList.value.isEmpty()) {
            openAppNotificationUseCase(AppNotification.Business.BlueprintsDoNotExist)
        } else {
            dialogCenter.openCreateLessonDialog()
        }
    }

    private inline fun updateCurrentStage(transform: (LessonStage) -> LessonStage) {
        _uiState.value = _uiState.value.copy(
            currentCreatingStage = transform(uiState.value.currentCreatingStage)
        )
    }

    fun updateStageCardsValue(value: Int) {
        updateCurrentStage { stage ->
            when (stage) {
                is LearningStage -> stage.copy(cards = value)
                is RepeatingStage -> stage.copy(cards = value)
            }
        }
    }


    fun updateCyclesValue(value: Int) {
        updateCurrentStage { stage ->
            when (stage) {
                is LearningStage -> stage.copy(cycles = value)
                is RepeatingStage -> stage.copy(cycles = value)
            }
        }
    }

    fun updatePriorityLevel(value: Int) {
        val stage = uiState.value.currentCreatingStage as RepeatingStage
        _uiState.value = _uiState.value.copy(
            currentCreatingStage = stage.copy(
                basePriorityRepeatingLevel = value
            )
        )
    }

    fun createBlueprint() {
        if (createBlueprintUseCase(uiState.value.currentCreatingBlueprint)) {
            _uiState.value = _uiState.value.copy(
                currentCreatingBlueprint = LessonBlueprint(
                    name = "",
                    stages = listOf(),
                    settings = LessonSettings(
                        isLoop = false
                    )
                )
            )
            dialogCenter.closeDialog()
        }
    }

    fun createStage() {
        _uiState.value = _uiState.value.copy(
            currentCreatingBlueprint = uiState.value.currentCreatingBlueprint.copy(
                stages = uiState.value.currentCreatingBlueprint.stages + uiState.value.currentCreatingStage
            )
        )
    }

    fun createBlueprintButtonClick() {
        dialogCenter.openCreateBlueprintDialog()
    }

    fun openLessons() {
        _uiState.value = _uiState.value.copy(
            lessonMenuScreenUiState = LessonMenuScreenUiState.Lessons
        )
    }

    fun updateCurrentCreatingBlueprint(
        name: String? = null,
        stages: List<LessonStage>? = null,
        isLoop: Boolean? = null
    ) {
        val current = uiState.value.currentCreatingBlueprint
        val newSettings = current.settings.copy(
            isLoop = isLoop ?: current.settings.isLoop
        )
        val updated = current.copy(
            name = name ?: current.name,
            stages = stages ?: current.stages,
            settings = newSettings
        )
        _uiState.value = uiState.value.copy(
            currentCreatingBlueprint = updated
        )
    }

    fun createAndAddStage(stage: LessonStage) {
        _uiState.value = uiState.value.copy(
            currentCreatingBlueprint = uiState.value.currentCreatingBlueprint.copy(
                stages = uiState.value.currentCreatingBlueprint.stages + stage
            )
        )
        dialogCenter.openCreateBlueprintDialog()
    }

    fun deleteStage() {
        val stage = uiState.value.stageToDelete ?: return
        _uiState.value = uiState.value.copy(
            currentCreatingBlueprint = uiState.value.currentCreatingBlueprint.copy(
                stages = uiState.value.currentCreatingBlueprint.stages - stage
            )
        )
        _uiState.value = uiState.value.copy(
            stageToDelete = null
        )
        dialogCenter.openCreateBlueprintDialog()
    }

    fun openDeleteStageDialog(stage: LessonStage) {
        _uiState.value = uiState.value.copy(
            stageToDelete = stage
        )
        dialogCenter.openDeleteStageDialog()
    }

    fun chooseSelectionMode(selectionMode: CardSelectionMode) {
        val stage = uiState.value.currentCreatingStage as RepeatingStage
        _uiState.value = uiState.value.copy(
            currentCreatingStage = stage.copy(
                cardSelectionMode = selectionMode
            )
        )
        dialogCenter.openCurrentCreatingStageDialog()
    }

    inner class DialogCenter {
        fun openCreateLessonDialog() {
            openAnyDialog(LessonMenuDialogState.CreateLesson)
        }

        fun openCreateBlueprintDialog() {
            openAnyDialog(LessonMenuDialogState.CreateBlueprint)
        }

        fun openChooseStageModeDialog() {
            openAnyDialog(LessonMenuDialogState.ChoseStageMode)
        }

        fun openDeleteStageDialog() {
            openAnyDialog(LessonMenuDialogState.DeleteStage)
        }

        fun closeDialog() {
            openAnyDialog(LessonMenuDialogState.Closed)
        }

        fun openCurrentCreatingStageDialog() {
            openCreateBlueprintStageDialog(uiState.value.currentCreatingStage)
        }

        fun openCreateBlueprintStageDialog(stage: LessonStage) {
            openAnyDialog(LessonMenuDialogState.CreateBlueprintStage)
            _uiState.value = uiState.value.copy(
                currentCreatingStage = stage
            )
        }

        fun openChooseSelectionModeDialog(){
            openAnyDialog(LessonMenuDialogState.ChoseSelectionMode)
        }

        private fun openAnyDialog(dialog: LessonMenuDialogState) {
            _uiState.value = _uiState.value.copy(
                lessonMenuDialogUiState = dialog
            )
        }

        fun openSelectionModeInfoDialog() {
            openAnyDialog(LessonMenuDialogState.SelectionModeInfo)
        }
    }
}


sealed class LessonMenuDialogState {
    object Closed : LessonMenuDialogState()
    object CreateBlueprint : LessonMenuDialogState()
    object ChoseStageMode : LessonMenuDialogState()
    object ChoseSelectionMode : LessonMenuDialogState()
    object SelectionModeInfo : LessonMenuDialogState()
    object CreateBlueprintStage : LessonMenuDialogState()
    object DeleteStage : LessonMenuDialogState()
    object CreateLesson : LessonMenuDialogState()
}

data class LessonMenuUiState(
    val lessonMenuScreenUiState: LessonMenuScreenUiState = LessonMenuScreenUiState.Lessons,
    //
    val lessonMenuDialogUiState: LessonMenuDialogState = LessonMenuDialogState.Closed,
    val currentCreatingBlueprint: LessonBlueprint = LessonBlueprint(
        name = "",
        stages = emptyList(),
        settings = LessonSettings(
            isLoop = false,
        ),
    ),
    val currentCreatingStage: LessonStage = LearningStage(),
    val stageToDelete: LessonStage? = null
)


enum class LessonMenuScreenUiState() {
    Settings,
    Lessons
}

