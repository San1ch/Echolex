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
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.domain.useCase.lesson.SetCurrentLessonUseCase
import com.example.echolex.core.ui.dialog.LessonMenuDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

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
            val decks = deckList.first()

            when {
                blueprints.isEmpty() -> openAppNotificationUseCase(AppNotification.Business.BlueprintsDoNotExist)
                decks.isEmpty() -> openAppNotificationUseCase(AppNotification.Business.DecksDoNotExist)
                else -> dialogCenter.createLessonDialog()
            }
        }
    }



    fun createLesson(lessonBlueprint: LessonBlueprint, decks: List<Deck>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (uiState.value.lessonNameTextField.isBlank()) {
                openAppNotificationUseCase(AppNotification.Validation.NameIsEmpty)
                return@launch
            }
            if (decks.isEmpty()) {
                openAppNotificationUseCase(AppNotification.Business.LessonDecksAreEmpty)
                return@launch
            }

            val chosenDeckNames = decks.map { it.name }
            val result =
                createLessonUseCase(lessonBlueprint, chosenDeckNames, uiState.value.lessonNameTextField)
            if (result) {
                dialogCenter.closeDialog()
                updateUiState {
                    it.copy(
                        lessonNameTextField = ""
                    )
                }
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
        viewModelScope.launch {
            val blueprint = uiState.value.currentCreatingLessonBlueprint
            if (blueprint.name.isBlank()) {
                openAppNotificationUseCase(AppNotification.Validation.NameIsEmpty)
                return@launch
            }
            if (blueprint.stages.isEmpty()) {
                openAppNotificationUseCase(AppNotification.Business.BlueprintsStagesAreEmpty)
                return@launch
            }

            if (createBlueprintUseCase(blueprint)) {
                updateUiState {
                    it.copy(
                        currentCreatingLessonBlueprint = LessonBlueprint(
                            name = "",
                            stages = emptyList(),
                            settings = LessonSettings()
                        )
                    )
                }
                dialogCenter.closeDialog()
            }
        }
    }

    fun deleteBlueprint() {
        viewModelScope.launch{

            val blueprint = uiState.value.currentLessonBlueprintToDelete
            if (blueprint == null) {
                openAppNotificationUseCase(AppNotification.Business.BlueprintsDoNotExist)
            } else {
                deleteBlueprintFromStoreUseCase(blueprint.name)
            }
            dialogCenter.closeDialog()
        }
    }

    fun createStage() {
        updateUiState {
            it.copy(
                currentCreatingLessonBlueprint = uiState.value.currentCreatingLessonBlueprint.copy(
                    stages = uiState.value.currentCreatingLessonBlueprint.stages + uiState.value.currentCreatingStage
                ),
                currentCreatingStage = LessonStage(
                    type = StageType.LEARNING,
                    cards = 10,
                    cycles = 5,
                    priority = 1,
                    cardSelectionMode = CardSelectionMode.Random
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
        viewModelScope.launch {
            val lesson = uiState.value.currentLessonToDelete
            if (lesson == null) {
                openAppNotificationUseCase(AppNotification.Business.LessonDoesNotExist)
            } else {
                deleteLessonFromStoreUseCase(lesson)
            }
            dialogCenter.closeDialog()
        }
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
                currentCreatingLessonBlueprint = uiState.value.currentCreatingLessonBlueprint.copy(
                    name = name
                )
            )
        }
    }
    
    fun updateCurrentCreatingBlueprint(
        name: String? = null,
        isLoop: Boolean? = null
    ) {
        val current = uiState.value.currentCreatingLessonBlueprint
        val newSettings = current.settings.copy(
            isLoop = isLoop ?: current.settings.isLoop
        )
        val updated = current.copy(
            name = name ?: current.name,
            settings = newSettings
        )
        updateUiState {
            it.copy(
                currentCreatingLessonBlueprint = updated
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
                    currentCreatingLessonBlueprint = uiState.value.currentCreatingLessonBlueprint.copy(
                        stages = uiState.value.currentCreatingLessonBlueprint.stages.filter { stage ->
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
                priority = 0,
                cardSelectionMode = _uiState.value.currentCreatingStage.cardSelectionMode
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
                currentCreatingLessonBlueprint = uiState.value.currentCreatingLessonBlueprint.copy(
                    stages = uiState.value.currentCreatingLessonBlueprint.stages + currentStage
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

    fun openDeleteBlueprintDialog(lessonBlueprint: LessonBlueprint) {
        updateUiState {
            it.copy(
                currentLessonBlueprintToDelete = lessonBlueprint
            )
        }
        dialogCenter.deleteBlueprintDialog()
    }
}

data class LessonMenuUiState(
    val lessonMenuScreenUiState: LessonMenuScreenUiState = LessonMenuScreenUiState.Lessons,
    val lessonMenuDialogUiState: LessonMenuDialogState = LessonMenuDialogState.Closed,
    val currentCreatingLessonBlueprint: LessonBlueprint = LessonBlueprint(
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

    val currentLessonBlueprintToDelete: LessonBlueprint? = null,

    val currentLessonToLearn: Lesson? = null,
    val currentLessonToDelete: Lesson? = null,
    val lessonNameTextField: String = ""
)


enum class LessonMenuScreenUiState() {
    Settings,
    Lessons
}

