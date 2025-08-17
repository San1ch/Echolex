package com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.model.lesson.LessonSettings
import com.example.echolex.core.domain.data.model.lesson.LessonStage
import com.example.echolex.core.domain.data.model.lesson.StageType
import com.example.echolex.core.domain.data.model.lesson.CardSelectionMode
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.useCase.blueprint.CreateBlueprintUseCase
import com.example.echolex.core.domain.useCase.blueprint.DeleteBlueprintFromStoreUseCase
import com.example.echolex.core.domain.useCase.deck.GetDecksFlowUseCase
import com.example.echolex.core.domain.useCase.blueprint.GetFlowBlueprintListUseCase
import com.example.echolex.core.domain.useCase.lesson.CreateLessonUseCase
import com.example.echolex.core.domain.useCase.lesson.DeleteLessonFromStore
import com.example.echolex.core.domain.useCase.lesson.GetFlowLessonListUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.NavigateToScreenUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import com.example.echolex.core.navigation.NavigationTarget
import com.example.echolex.core.ui.dialog.LessonMenuDialogCenter
import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuDialogState
import com.example.echolex.core.domain.useCase.lesson.SetCurrentLessonUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

@HiltViewModel
class LessonMenuViewModel @Inject constructor(
    private val getFlowBlueprintListUseCase: GetFlowBlueprintListUseCase,
    private val getFlowLessonListUseCase: GetFlowLessonListUseCase,
    private val getDecksFlowUseCase: GetDecksFlowUseCase,
    private val createBlueprintUseCase: CreateBlueprintUseCase,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase,
    private val deleteBlueprintFromStoreUseCase: DeleteBlueprintFromStoreUseCase,
    private val createLessonUseCase: CreateLessonUseCase,
    private val deleteLessonFromStoreUseCase: DeleteLessonFromStore,
    private val navigateToScreenUseCase: NavigateToScreenUseCase,
    private val setCurrentLessonUseCase: SetCurrentLessonUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(LessonMenuUiState())
    val uiState: State<LessonMenuUiState> get() = _uiState

    val blueprintList = getFlowBlueprintListUseCase()
    val lessonList = getFlowLessonListUseCase()
    val deckList = getDecksFlowUseCase()

    private inline fun updateUiState(transform: (LessonMenuUiState) -> LessonMenuUiState) {
        _uiState.value = transform(_uiState.value)
    }

    val dialogCenter = LessonMenuDialogCenter { dialog ->
        updateUiState { it.copy(lessonMenuDialogUiState = dialog) }
    }

    fun openSettings() {
        updateUiState {
            it.copy(
                lessonMenuScreenUiState = LessonMenuScreenUiState.Settings
            )
        }
    }

    fun openCreatingLessonDialog() {
        viewModelScope.launch {
            val blueprints = blueprintList.first()
            if (blueprints.isEmpty()) {
                openAppNotificationUseCase(AppNotification.Business.BlueprintsDoNotExist)
            } else {
                dialogCenter.createLessonDialog()
            }
        }
    }

    fun createLesson(blueprint: LessonBlueprint, decks: List<Deck>) {
        if (uiState.value.lessonNameTextField.isBlank()) {
            openAppNotificationUseCase(AppNotification.Validation.NameIsEmpty)
            return
        }
        if (decks.isEmpty()) {
            openAppNotificationUseCase(AppNotification.Business.LessonDecksAreEmpty)
            return
        }
        
        val chosenDeckNames = decks.map { it.name }
        val result =
            createLessonUseCase(blueprint, chosenDeckNames, uiState.value.lessonNameTextField)
        if (result) {
            dialogCenter.closeDialog()
            updateUiState {
                it.copy(
                    lessonNameTextField = ""
                )
            }
        }
    }

    fun onLessonNameChange(name: String) {
        updateUiState {
            it.copy(
                lessonNameTextField = name
            )
        }
    }

    private inline fun updateCurrentStage(transform: (LessonStage) -> LessonStage) {
        updateUiState {
            it.copy(
                currentCreatingStage = transform(uiState.value.currentCreatingStage)
            )
        }
    }

    fun updateCurrentLesson(lesson: Lesson) {
        updateUiState {
            it.copy(
                currentLessonToLearn = lesson
            )
        }
    }

    fun updateStageCardsValue(value: Int) {
        updateCurrentStage { stage ->
            stage.copy(cards = value)
        }
    }

    fun updateCyclesValue(value: Int) {
        updateCurrentStage { stage ->
            stage.copy(cycles = value)
        }
    }

    fun updatePriorityLevel(value: Int) {
        updateCurrentStage { stage ->
            if (stage.type == StageType.REPEATING) {
                stage.copy(priority = value)
            } else {
                stage
            }
        }
    }

    fun updateStageType(type: StageType) {
        updateCurrentStage { stage ->
            stage.copy(type = type)
        }
    }

    fun createBlueprint() {
        val blueprint = uiState.value.currentCreatingBlueprint
        if (blueprint.name.isBlank()) {
            openAppNotificationUseCase(AppNotification.Validation.NameIsEmpty)
            return
        }
        if (blueprint.stages.isEmpty()) {
            openAppNotificationUseCase(AppNotification.Business.BlueprintsStagesAreEmpty)
            return
        }
        
        if (createBlueprintUseCase(blueprint)) {
            updateUiState {
                it.copy(
                    currentCreatingBlueprint = LessonBlueprint(
                        name = "",
                        stages = listOf(),
                        settings = LessonSettings()
                    )
                )
            }
            dialogCenter.closeDialog()
        }
    }

    fun deleteBlueprint() {
        val blueprint = uiState.value.currentBlueprintToDelete
        if (blueprint == null) {
            openAppNotificationUseCase(AppNotification.Business.BlueprintsDoNotExist)
        } else {
            deleteBlueprintFromStoreUseCase(blueprint.name)
        }
        dialogCenter.closeDialog()
    }

    fun createStage() {
        updateUiState {
            it.copy(
                currentCreatingBlueprint = uiState.value.currentCreatingBlueprint.copy(
                    stages = uiState.value.currentCreatingBlueprint.stages + uiState.value.currentCreatingStage
                ),
                currentCreatingStage = LessonStage(
                    type = StageType.LEARNING,
                    cards = 10,
                    cycles = 5
                )
            )
        }
    }

    fun createBlueprintButtonClick() {
        dialogCenter.createBlueprintDialog()
    }

    fun openLessons() {
        updateUiState {
            it.copy(
                lessonMenuScreenUiState = LessonMenuScreenUiState.Lessons
            )
        }
    }

    fun openDeleteLessonDialog(lesson: Lesson) {
        updateUiState {
            it.copy(
                currentLessonToDelete = lesson
            )
        }
        dialogCenter.deleteLessonDialog()
    }

    fun deleteLesson() {
        val lesson = uiState.value.currentLessonToDelete
        if (lesson == null) {
            openAppNotificationUseCase(AppNotification.Business.LessonDoesNotExist)
        } else {
            deleteLessonFromStoreUseCase(lesson)
        }
        dialogCenter.closeDialog()
    }

    fun openLearningScreen(lesson: Lesson) {
        viewModelScope.launch {
            setCurrentLessonUseCase(lesson.name)
            updateCurrentLesson(lesson)
            navigateToScreenUseCase(NavigationTarget.LessonScreens.LessonProcess)
        }
    }

    fun updateBlueprintName(name: String) {
        updateUiState {
            it.copy(
                currentCreatingBlueprint = uiState.value.currentCreatingBlueprint.copy(
                    name = name
                )
            )
        }
    }
    
    fun updateCurrentCreatingBlueprint(
        name: String? = null,
        isLoop: Boolean? = null
    ) {
        val current = uiState.value.currentCreatingBlueprint
        val newSettings = current.settings.copy(
            isLoop = isLoop ?: current.settings.isLoop
        )
        val updated = current.copy(
            name = name ?: current.name,
            settings = newSettings
        )
        updateUiState {
            it.copy(
                currentCreatingBlueprint = updated
            )
        }
    }
    
    fun openDeleteStageDialog(stage: LessonStage) {
        updateUiState {
            it.copy(
                stageToDelete = stage
            )
        }
        dialogCenter.deleteStageDialog()
    }
    
    fun deleteStage() {
        val stageToDelete = uiState.value.stageToDelete
        if (stageToDelete != null) {
            updateUiState {
                it.copy(
                    currentCreatingBlueprint = uiState.value.currentCreatingBlueprint.copy(
                        stages = uiState.value.currentCreatingBlueprint.stages.filter { stage ->
                            stage != stageToDelete
                        }
                    ),
                    stageToDelete = null
                )
            }
        }
        dialogCenter.closeDialog()
    }
    
    fun addStage(type: StageType) {
        val newStage = when (type) {
            StageType.LEARNING -> LessonStage(
                type = StageType.LEARNING,
                cards = 10,
                cycles = 5
            )
            StageType.REPEATING -> LessonStage(
                type = StageType.REPEATING,
                cards = 10,
                cycles = 5,
                priority = 0
            )
        }
        
        updateUiState {
            it.copy(
                currentCreatingStage = newStage
            )
        }
        dialogCenter.openCreateBlueprintStageDialog()
    }

    fun chooseSelectionMode(mode: CardSelectionMode) {
        updateCurrentStage { stage ->
            stage.copy(cardSelectionMode = mode)
        }
        dialogCenter.openCurrentCreatingStageDialog()
    }

    fun createAndAddStage() {
        val currentStage = uiState.value.currentCreatingStage
        updateUiState {
            it.copy(
                currentCreatingBlueprint = uiState.value.currentCreatingBlueprint.copy(
                    stages = uiState.value.currentCreatingBlueprint.stages + currentStage
                ),
                currentCreatingStage = LessonStage(
                    type = StageType.LEARNING,
                    cards = 10,
                    cycles = 5
                )
            )
        }
        dialogCenter.createBlueprintDialog()
    }

    fun openDeleteBlueprintDialog(blueprint: LessonBlueprint) {
        updateUiState {
            it.copy(
                currentBlueprintToDelete = blueprint
            )
        }
        dialogCenter.deleteBlueprintDialog()
    }
}

data class LessonMenuUiState(
    val lessonMenuScreenUiState: LessonMenuScreenUiState = LessonMenuScreenUiState.Lessons,
    val lessonMenuDialogUiState: LessonMenuDialogState = LessonMenuDialogState.Closed,
    val currentCreatingBlueprint: LessonBlueprint = LessonBlueprint(
        name = "",
        stages = emptyList(),
        settings = LessonSettings()
    ),
    val currentCreatingStage: LessonStage = LessonStage(
        type = StageType.LEARNING,
        cards = 10,
        cycles = 5
    ),
    val stageToDelete: LessonStage? = null,

    val currentBlueprintToDelete: LessonBlueprint? = null,

    val currentLessonToLearn: Lesson? = null,
    val currentLessonToDelete: Lesson? = null,
    val lessonNameTextField: String = ""
)


enum class LessonMenuScreenUiState() {
    Settings,
    Lessons
}

