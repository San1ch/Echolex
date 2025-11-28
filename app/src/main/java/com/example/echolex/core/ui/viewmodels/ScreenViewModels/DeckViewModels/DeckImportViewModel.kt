package com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.useCase.screensUseCases.BackToPreviousScreenUseCase
import com.example.echolex.core.domain.useCase.deck.CreateImportDeckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeckImportUiState(
    val nameText: String = "",
    val importText: String = "",
    val markAsPreLearned: Boolean = false,
    val withFlipCards: Boolean = false
)
@HiltViewModel
class DeckImportViewModel @Inject constructor(
    private val createImportDeckUseCase: CreateImportDeckUseCase,
    private val backToPreviousScreenUseCase: BackToPreviousScreenUseCase
) : ViewModel() {

    private val _uiState = mutableStateOf(DeckImportUiState())
    val uiState: State<DeckImportUiState> get() = _uiState

    fun onNameTextChanged(newName: String) {
        _uiState.value = _uiState.value.copy(nameText = newName)
    }

    fun onImportTextChanged(newText: String) {
        _uiState.value = _uiState.value.copy(importText = newText)
    }

    fun toggleMarkAsPreLearned() {
        _uiState.value = _uiState.value.copy(
            markAsPreLearned = !_uiState.value.markAsPreLearned
        )
    }

    fun toggleWithFlipCards() {
        _uiState.value = _uiState.value.copy(
            withFlipCards = !_uiState.value.withFlipCards
        )
    }

    fun pasteImportText(newText: String) {
        _uiState.value = _uiState.value.copy(importText = newText)
    }

    fun clearImportText() {
        _uiState.value = _uiState.value.copy(importText = "")
    }

    fun startCreatingDeck() {
        viewModelScope.launch(Dispatchers.IO) {
            if (createImportDeckUseCase(buildData(), _uiState.value.withFlipCards)) {
                backToPreviousScreenUseCase()
            }
        }
    }

    private fun buildData(): DataDeck {
        val state = _uiState.value
        return DataDeck(
            name = state.nameText,
            words = state.importText,
            isPreLearned = state.markAsPreLearned
        )
    }

    fun openDeckMenu() {
        backToPreviousScreenUseCase()
    }
}
