package com.example.echolex.core.navigation

import com.example.echolex.ui.screens.DeckImportScreenRoute
import com.example.echolex.ui.screens.DeckItemScreenRoute
import com.example.echolex.ui.screens.DecksMenuScreenRoute
import com.example.echolex.ui.screens.LessonMenuScreenRoute
import com.example.echolex.ui.screens.LessonSettingsMenuScreenRoute
import com.example.echolex.ui.screens.MainScreenRoute

sealed class NavigationTarget(open val route: String) {

    object NullScreen : NavigationTarget(route = "")

    sealed class MainScreens(route: String) : NavigationTarget(route) {
        object Main : MainScreens(route = MainScreenRoute)
    }

    sealed class DeckScreens(route: String) : NavigationTarget(route) {
        object DecksMenu : DeckScreens(route = DecksMenuScreenRoute)

        object DeckItem : DeckScreens(DeckItemScreenRoute)

        object DeckImport : DeckScreens(route = DeckImportScreenRoute)
    }

    sealed class LessonSettingsScreens(route: String) : NavigationTarget(route) {
        object LessonSettings : LessonSettingsScreens(route = LessonSettingsMenuScreenRoute)
    }

    sealed class LessonScreens(route: String) : NavigationTarget(route) {
        object Lesson : LessonScreens(route = LessonMenuScreenRoute)
    }
}