package com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.service.centralScreenService.NavigationCenter
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import com.example.echolex.core.domain.useCase.CreateImportDeckUseCase
import com.example.echolex.core.navigation.NavigationTarget.DeckScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckImportViewModel @Inject constructor(
    private val createImportDeckUseCase: CreateImportDeckUseCase,
    private val notificationCenter: NotificationCenter,
    private val navigationCenter: NavigationCenter
) : ViewModel() {
    private var _nameTextField = mutableStateOf("")
    val nameTextField: State<String> get() = _nameTextField

    private val _importTextField = mutableStateOf("")
    val importTextField: State<String> get() = _importTextField

    private val _isMarkingLikePreLearned = mutableStateOf(false)
    val isMarkingLikePreLearned: State<Boolean> get() = _isMarkingLikePreLearned

    fun openDeckMenu() {
        navigationCenter.navigate(DeckScreens.DecksMenu)
    }

    fun onNameTextChanged(newName: String) {
        _nameTextField.value = newName
    }

    fun onImportTextChanged(newText: String) {
        _importTextField.value = newText
    }

    fun toggleMarkAsPreLearned() {
        _isMarkingLikePreLearned.value = !isMarkingLikePreLearned.value
    }

     fun startCreatingDeck() {
        viewModelScope.launch {
            val isCreated = createImportDeckUseCase(buildData())
            if(isCreated) {
                openDeckMenu()
            }
        }
    }


    private fun buildData(): DataDeck {
        return DataDeck(
            _nameTextField.value,
            _importTextField.value,
            _isMarkingLikePreLearned.value
        )
    }

    fun pasteImportText(newText: String) {
        _importTextField.value = newText
    }

    fun clearImportText() {
        _importTextField.value = ""
    }

}

