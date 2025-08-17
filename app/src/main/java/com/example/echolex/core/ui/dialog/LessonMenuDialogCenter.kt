package com.example.echolex.core.ui.dialog

import com.example.echolex.core.ui.viewmodels.ScreenViewModels.LessonSettingsViewModels.LessonMenuDialogState

class LessonMenuDialogCenter(
    private val onDialogRequested: (LessonMenuDialogState) -> Unit
) {
    private fun open(dialog: LessonMenuDialogState) = onDialogRequested(dialog)
    fun createLessonDialog() = open(LessonMenuDialogState.CreateLesson)
    fun createBlueprintDialog() = open(LessonMenuDialogState.CreateBlueprint)
    fun chooseStageModeDialog() = open(LessonMenuDialogState.ChooseStageMode)
    fun deleteStageDialog() = open(LessonMenuDialogState.DeleteStage)
    fun deleteBlueprintDialog() = open(LessonMenuDialogState.DeleteBlueprint)
    fun deleteLessonDialog() = open(LessonMenuDialogState.DeleteLesson)
    fun openBlueprintDeleteDialog() = open(LessonMenuDialogState.DeleteBlueprint)
    fun closeDialog() = open(LessonMenuDialogState.Closed)
    fun openChooseStageModeDialog() = open(LessonMenuDialogState.ChooseStageMode)
    fun openCreateBlueprintStageDialog() = open(LessonMenuDialogState.CreateBlueprintStage)
    fun openChooseSelectionModeDialog() = open(LessonMenuDialogState.ChooseSelectionMode)
    fun openSelectionModeInfoDialog() = open(LessonMenuDialogState.SelectionModeInfo)
    fun openCurrentCreatingStageDialog() = open(LessonMenuDialogState.CreateBlueprintStage)
}