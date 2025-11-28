package com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.domain.data.local.GetSharedDataUseCase
import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.useCase.deck.AddCardsToDeckUseCase
import com.example.echolex.core.domain.useCase.deck.ChangeDeckNameUseCase
import com.example.echolex.core.domain.useCase.deck.GetDeckByNameUseCase
import com.example.echolex.core.domain.useCase.deck.RemoveCardInDeckUseCase
import com.example.echolex.core.domain.useCase.deck.RemoveDeckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.example.echolex.core.constants.SharedDataDeckItemNameKey
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.domain.service.DataDeck
import com.example.echolex.core.domain.useCase.deck.GetCardExportStringUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.BackToPreviousScreenUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

@HiltViewModel
class DeckItemViewModel @Inject constructor(
    private val changeDeckNameUseCase: ChangeDeckNameUseCase,
    private val removeDeckUseCase: RemoveDeckUseCase,
    private val getDeckByNameUseCase: GetDeckByNameUseCase,
    private val removeCardInDeckUseCase: RemoveCardInDeckUseCase,
    private val addCardsToDeckUseCase: AddCardsToDeckUseCase,
    private val getSharedDataUseCase: GetSharedDataUseCase,
    private val backToPreviousScreenUseCase: BackToPreviousScreenUseCase,
    private val getCardExportStringUseCase: GetCardExportStringUseCase,
    private val openAppNotificationUseCase: OpenAppNotificationUseCase
) : ViewModel() {
    val deckName: String = getSharedDataUseCase<String>(SharedDataDeckItemNameKey)!!
    val screenDeck: StateFlow<Deck> =
        getDeckByNameUseCase(deckName).stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            Deck("TEMP_TEST")
        )

    private val _localDeck = mutableStateOf(Deck(name = deckName))
    val localDeck: State<Deck> = _localDeck


    val changedNameTextField = mutableStateOf("")
    val importTextField = mutableStateOf("")
    val isMarkingLikePreLearned = mutableStateOf(false)
    val withFlipCards = mutableStateOf(false)

    var currentCardToRemove: Card? = null

    val dialogMode = mutableStateOf<DeckItemDialogMode>(DeckItemDialogMode.Null)


    init {
        viewModelScope.launch {
            copyToLocal()
        }
    }

    fun toggleWithFlipCards() {
        withFlipCards.value = !withFlipCards.value
    }

    fun backScreen() {
        viewModelScope.launch {
            backToPreviousScreenUseCase()
        }
    }

    fun onImportChange(text: String) {
        importTextField.value = text
    }

    fun onChangeNameChange(text: String) {
        changedNameTextField.value = text
    }

    private fun openDialogMode(dialogMode: DeckItemDialogMode) {
        this.dialogMode.value = dialogMode
    }

    fun openDeletingDialog(card: Card) {
        openDialogMode(DeckItemDialogMode.RemoveCard)
        currentCardToRemove = card
    }

    fun flipCard(card: Card) {
        val oldCards: List<Card> = localDeck.value.cards
        val newCards: List<Card> = oldCards.map {
            if (it == card) it.flipCard() else it
        }
        _localDeck.value = localDeck.value.copy(cards = newCards)
    }

    fun closeDialogMode() {
        dialogMode.value = DeckItemDialogMode.Null
    }

    fun deleteDeck() {
        closeDialogMode()
        viewModelScope.launch(Dispatchers.IO) {
            removeDeckUseCase(localDeck.value.name)
            backScreen()
        }
    }

    fun deleteCard() {
        viewModelScope.launch(Dispatchers.IO) {
            val card = currentCardToRemove
            if (card != null){
                removeCardInDeckUseCase(screenDeck.value.name, card)
                copyToLocal()
            }
        }
        closeDialogMode()
    }

    fun cancelDeleting() {
        closeDialogMode()
        currentCardToRemove = null
    }

    fun openImportDialog() {
        dialogMode.value = DeckItemDialogMode.ImportCards
    }

    fun openChangeNameDialog() {
        dialogMode.value = DeckItemDialogMode.ChangeDeckName
    }

    fun openDeleteDeckDialog() {
        dialogMode.value = DeckItemDialogMode.RemoveDeck
    }

    fun addImportCards() {
        viewModelScope.launch(Dispatchers.IO) {
            val changed = addCardsToDeckUseCase(
                screenDeck.value.name,
                DataDeck("", importTextField.value, isMarkingLikePreLearned.value),withFlipCards.value
            )
            if (changed) {
                backToPreviousScreenUseCase()
            }
        }
        closeDialogMode()
    }

    fun changeDeckName() {
        val currentName = screenDeck.value.name
        val newName = changedNameTextField.value
        viewModelScope.launch(Dispatchers.IO) {
            val changed = changeDeckNameUseCase(newName, currentName)
            if (changed) {
                backToPreviousScreenUseCase()
            }
        }
    }

    private fun buildDeckData(): DataDeck {
        return DataDeck("", importTextField.value, isMarkingLikePreLearned.value)
    }

    suspend fun copyToLocal() {
        val deck = screenDeck.first()
        _localDeck.value = deck.copy()
    }

    fun exportDeck(onExportReady: (String) -> Unit) {
        viewModelScope.launch {
            val text: String = getCardExportStringUseCase(screenDeck.value.name)
            onExportReady(text)
            openAppNotificationUseCase(AppNotification.Success.DeckExported)
        }
    }

    sealed class DeckItemDialogMode() {
        data object Null : DeckItemDialogMode()
        data object RemoveCard : DeckItemDialogMode()
        data object ChangeDeckName : DeckItemDialogMode()
        data object ImportCards : DeckItemDialogMode()
        data object RemoveDeck : DeckItemDialogMode()
    }
    sealed class ScreenUiLaunching() {
        data object Loading : ScreenUiLaunching()
        data class Success(val deck: Deck) : ScreenUiLaunching()
        data class Error(val message: String) : ScreenUiLaunching()
    }
}
