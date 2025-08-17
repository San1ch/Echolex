package com.example.echolex.core.ui.viewmodels.ScreenViewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.echolex.core.domain.useCase.deck.AllCardsStats
import com.example.echolex.core.domain.useCase.deck.GetDecksCopyStatUseCase
import com.example.echolex.core.domain.useCase.lesson.ClearAllDataUseCase
import com.example.echolex.core.domain.useCase.screensUseCases.NavigateToScreenUseCase
import com.example.echolex.core.navigation.NavigationTarget.DeckScreens
import com.example.echolex.core.navigation.NavigationTarget.LessonScreens
import com.example.echolex.ui.theme.AppCardItemLearnedStatusColor
import com.example.echolex.ui.theme.AppCardItemNotLearnedStatusColor
import com.example.echolex.ui.theme.AppCardItemPreLearnedStatusColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val getDecksCopyStatUseCase: GetDecksCopyStatUseCase,
    private val navigateToScreenUseCase: NavigateToScreenUseCase,
    private val clearAllDataUseCase: ClearAllDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainMenuUiState>(MainMenuUiState.Loading)
    val uiState: StateFlow<MainMenuUiState> = _uiState.asStateFlow()

    private val _donutChartData = MutableStateFlow<PieChartData?>(null)
    val donutChartData: StateFlow<PieChartData?> = _donutChartData.asStateFlow()

    val donutChartConfig = PieChartConfig(
        isAnimationEnable = false,
        backgroundColor = Color.Transparent
    )

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = MainMenuUiState.Loading

            val result = getDecksCopyStatUseCase()

            if (result.isSuccess) {
                val stats = result.getOrNull()
                if (stats != null) {
                    updateChartData(stats)
                    _uiState.value = MainMenuUiState.Success(
                        stats = stats
                    )
                } else {
                    _uiState.value = MainMenuUiState.Error("Data is null")
                }
            } else {
                val errorMessage = result.exceptionOrNull()?.message ?: "Unknown error"
                _uiState.value = MainMenuUiState.Error(errorMessage)
            }

        }
    }

    private fun updateChartData(stats: AllCardsStats) {
        _donutChartData.value = PieChartData(
            slices = listOf(
                PieChartData.Slice(
                    "Learned",
                    stats.countOfLearnedCards,
                    AppCardItemLearnedStatusColor
                ),
                PieChartData.Slice(
                    "In process",
                    stats.countOfPreLearnedCards,
                    AppCardItemPreLearnedStatusColor
                ),
                PieChartData.Slice(
                    "Not learned",
                    stats.countOfNotLearnedCards,
                    AppCardItemNotLearnedStatusColor
                )
            ),
            plotType = PlotType.Donut
        )
    }

    fun openDecksMenu() {
        navigateToScreenUseCase(DeckScreens.DecksMenu)
    }

    fun openLessonMenu() {
        navigateToScreenUseCase(LessonScreens.Lesson)
    }
    
    fun clearAllData() {
        viewModelScope.launch {
            clearAllDataUseCase()
            loadInitialData()
        }
    }
}

sealed class MainMenuUiState {
    object Loading : MainMenuUiState()
    data class Success(
        val stats: AllCardsStats
    ) : MainMenuUiState()

    data class Error(val message: String) : MainMenuUiState()
}