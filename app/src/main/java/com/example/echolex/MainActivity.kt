package com.example.echolex

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.echolex.core.domain.data.model.notification.AppNotification
import com.example.echolex.core.navigation.NavigationTarget
import com.example.echolex.core.ui.viewmodels.CenterProgramViewModel
import com.example.echolex.ui.customDesign.NotificationWindow
import com.example.echolex.ui.screens.DeckImportScreenRoute
import com.example.echolex.ui.screens.DeckItemScreenRoute
import com.example.echolex.ui.screens.DecksMenuScreenRoute
import com.example.echolex.ui.screens.LessonMenuScreenRoute
import com.example.echolex.ui.screens.MainScreen.DeckMenuScreen.DeckImportScreen.DeckImportScreen
import com.example.echolex.ui.screens.MainScreen.DeckMenuScreen.DeckItemScreen.DeckItemScreen
import com.example.echolex.ui.screens.MainScreen.DeckMenuScreen.DecksScreen
import com.example.echolex.ui.screens.MainScreen.LessonSettingsScreen.LessonScreen
import com.example.echolex.ui.screens.MainScreen.MainScreen
import com.example.echolex.ui.screens.MainScreenRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        transperentHotbar()
        setContent {
            CenterProgramScreen()
        }
    }

    fun transperentHotbar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }


}

@Composable
fun CenterProgramScreen(viewModel: CenterProgramViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MainScreenRoute,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        composable(MainScreenRoute) { MainScreen() }
        composable(DecksMenuScreenRoute) { DecksScreen() }
        composable(DeckItemScreenRoute) { DeckItemScreen() }
        composable(DeckImportScreenRoute) { DeckImportScreen() }
        composable(LessonMenuScreenRoute) { LessonScreen() }
    }

    CenterProgramNotification(viewModel)
    CenterProgramNavigation(viewModel, navController)
}

@Composable
private fun CenterProgramNotification(viewModel: CenterProgramViewModel) {
    val notificationState = viewModel.notificationState.collectAsState()

    if (notificationState.value !is AppNotification.Null) {
        val notification = notificationState.value
        NotificationWindow(notification.title, notification.message, onDismiss = {
            viewModel.closeNotification()
        })
    }
}

@Composable
fun CenterProgramNavigation(viewModel: CenterProgramViewModel, navController: NavController) {
    val navigationTarget = viewModel.navigationTarget.collectAsState()
    val shouldNavigateBack = viewModel.shouldNavigateBack.collectAsState()

    if (navigationTarget.value !is NavigationTarget.NullScreen) {
        navController.navigate(navigationTarget.value.route)
        viewModel.resetNavigationTarget()
    }

    if (shouldNavigateBack.value) {
        navController.popBackStack()
        viewModel.resetBackMode()
    }
}

