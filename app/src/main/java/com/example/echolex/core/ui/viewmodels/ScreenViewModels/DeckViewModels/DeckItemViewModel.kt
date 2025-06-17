package com.example.echolex.core.ui.viewmodels.ScreenViewModels.DeckViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.echolex.core.data.local.useCases.GetSharedDeckNameUseCase
import com.example.echolex.core.data.model.dataclass.Card
import com.example.echolex.core.data.model.dataclass.Deck
import com.example.echolex.core.domain.service.centralScreenService.NavigationCenter
import com.example.echolex.core.domain.useCase.deckStore.AddCardsToDeckUseCase
import com.example.echolex.core.domain.useCase.deckStore.ChangeDeckNameUseCase
import com.example.echolex.core.domain.useCase.deckStore.GetDeckByNameUseCase
import com.example.echolex.core.domain.useCase.deckStore.RemoveCardInDeckUseCase
import com.example.echolex.core.domain.useCase.deckStore.RemoveDeckUseCase
import com.example.echolex.core.navigation.NavigationTarget.DeckScreens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import com.example.echolex.core.domain.service.DataDeck
import kotlinx.coroutines.flow.first

@HiltViewModel
class DeckItemViewModel @Inject constructor(
    private val changeDeckNameUseCase: ChangeDeckNameUseCase,
    private val removeDeckUseCase: RemoveDeckUseCase,
    private val getDeckByNameUseCase: GetDeckByNameUseCase,
    private val removeCardInDeckUseCase: RemoveCardInDeckUseCase,
    private val addCardsToDeckUseCase: AddCardsToDeckUseCase,
    private val navigationCenter: NavigationCenter,
    private val getSharedDeckNameUseCase: GetSharedDeckNameUseCase
) : ViewModel() {
    private val deckName: StateFlow<String> = getSharedDeckNameUseCase()

    val screenDeck: StateFlow<Deck> =
        getDeckByNameUseCase(deckName.value).stateIn(viewModelScope, SharingStarted.Eagerly, Deck("TEMP_TEST"))

    private val _localDeck = mutableStateOf(Deck(name = deckName.value))
    val localDeck: State<Deck> = _localDeck


    val changedNameTextField = mutableStateOf("")
    val importTextField = mutableStateOf("")
    val isMarkingLikePreLearned = mutableStateOf(false)

    var currentCardToRemove: Card? = null

    val dialogMode = mutableStateOf<DeckItemDialogMode>(DeckItemDialogMode.Null)


    var screenState = mutableStateOf<ScreenUiLaunching>(ScreenUiLaunching.Loading)
        private set

    init {
        viewModelScope.launch {
            copyToLocal()
        }
    }


    fun navigateDeckMenu() {
        navigationCenter.navigate(DeckScreens.DecksMenu)

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
        viewModelScope.launch {
            removeDeckUseCase(localDeck.value.name)
            navigateDeckMenu()
        }
    }

    fun deleteCard() {
        viewModelScope.launch {
            removeCardInDeckUseCase(screenDeck.value.name, currentCardToRemove)
            copyToLocal()
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
        viewModelScope.launch {
            val changed = addCardsToDeckUseCase(screenDeck.value.name, DataDeck("", importTextField.value, isMarkingLikePreLearned.value))
            if(changed){
                navigationCenter.navigate(DeckScreens.DecksMenu)
            }
        }
        closeDialogMode()
    }

    fun changeDeckName() {
        val currentName = screenDeck.value.name
        val newName = changedNameTextField.value
        viewModelScope.launch {
            val changed = changeDeckNameUseCase(newName, currentName)
            if(changed){
                navigationCenter.navigate(DeckScreens.DecksMenu)
            }
        }
    }



    suspend fun copyToLocal(){
        val deck = screenDeck.first()
        _localDeck.value = deck.copy()
    }

    sealed class DeckItemDialogMode() {
        data object Null : DeckItemDialogMode()
        data object RemoveCard : DeckItemDialogMode()
        data object ChangeDeckName : DeckItemDialogMode()
        data object ImportCards : DeckItemDialogMode()
        data object RemoveDeck : DeckItemDialogMode()
    }

    sealed class ScreenUiLaunching(){
        data object Loading : ScreenUiLaunching()
        data class Success(val deck: Deck) : ScreenUiLaunching()
        data class Error(val message: String) : ScreenUiLaunching()
    }

}
