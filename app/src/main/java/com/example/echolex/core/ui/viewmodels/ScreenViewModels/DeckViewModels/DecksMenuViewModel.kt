package com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.data.local.useCases.SetSharedDeckNameUseCase
import com.example.echolex.core.data.model.dataclass.Deck
import com.example.echolex.core.data.model.dataclass.DeckCardsLearningStatus
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.service.centralScreenService.NavigationCenter
import com.example.echolex.core.domain.useCase.CreateEmptyDeckUseCase
import com.example.echolex.core.domain.useCase.deckStore.GetDecksFlowUseCase
import com.example.echolex.core.navigation.NavigationTarget.DeckScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DecksMenuViewModel @Inject constructor(
    private val getDecksFlowUseCase: GetDecksFlowUseCase,
    private val createEmptyDeckUseCase: CreateEmptyDeckUseCase,
    private val navigationCenter: NavigationCenter,
    private val setSharedDeckNameUseCase: SetSharedDeckNameUseCase
) : ViewModel() {
    val decks: StateFlow<List<Deck>> = getDecksFlowUseCase()

    private val _dialogState = MutableStateFlow(DialogState())
    val dialogState: StateFlow<DialogState> = _dialogState

    private fun updateDialogState(update: DialogState.() -> DialogState) {
        _dialogState.value = _dialogState.value.update()
    }

    fun onDeckNameChanged(newName: String) {
        updateDialogState {
            copy(deckName = newName)
        }
    }

    fun getAllItemDeckData(): List<ItemDeckBoardData> {
        return decks.value.map { deck ->
            ItemDeckBoardData(
                name = deck.name,
                learningStatusCounts = deck.getCountOfLearningStatus()
            )
        }
    }

    val countOfDecks: Int
        get() = decks.value.size

    fun navigateDeckInfoScreen(name: String) {
        viewModelScope.launch {
            setSharedDeckNameUseCase(name)
            navigationCenter.navigate(DeckScreens.DeckItem)
        }
    }

    fun navigateImportScreen() {
        navigationCenter.navigate(DeckScreens.DeckImport)
    }

    fun openChooseModeDialog() {
        updateDialogState {
            copy(type = DialogType.ChooseMode)
        }
    }

    fun openEmptyCreatorDialog() {
        updateDialogState {
            copy(type = DialogType.EmptyCreator)
        }
    }

    fun closeDialog() {
        updateDialogState {
            copy(
                type = DialogType.None,
                error = null
            )
        }
    }

    fun createDeck() {
        viewModelScope.launch {
            updateDialogState {
                copy(isLoading = true)
            }
            try {
                val isCreated = createEmptyDeckUseCase(buildData())
                if (isCreated) {
                    closeDialog()
                } else {
                    updateDialogState {
                        copy(
                            error = "Failed to create deck",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                updateDialogState {
                    copy(
                        error = "Failed to create deck.: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun buildData(): DataDeck {
        return DataDeck(
            dialogState.value.deckName,
            "",
            false
        )
    }
}


data class DialogState(
    val type: DialogType = DialogType.None,
    val deckName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class DialogType {
    None,
    ChooseMode,
    EmptyCreator
}


data class ItemDeckBoardData(
    val name: String,
    val learningStatusCounts: DeckCardsLearningStatus
)