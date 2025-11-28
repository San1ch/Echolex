package com.example.echolex.core.ui.dialog

class LessonMenuDialogCenter(
    private val onDialogRequested: (LessonMenuDialogState) -> Unit
) {
    private fun open(dialog: LessonMenuDialogState) = onDialogRequested(dialog)
    fun closeDialog() = open(LessonMenuDialogState.Closed)
    fun createLessonDialog() = open(LessonMenuDialogState.CreateLesson)
    fun createBlueprintDialog() = open(LessonMenuDialogState.CreateBlueprint)
    fun chooseStageModeDialog() = open(LessonMenuDialogState.ChooseStageMode)
    fun deleteStageDialog() = open(LessonMenuDialogState.DeleteStage)
    fun deleteBlueprintDialog() = open(LessonMenuDialogState.DeleteBlueprint)
    fun deleteLessonDialog() = open(LessonMenuDialogState.DeleteLesson)
    fun openBlueprintDeleteDialog() = open(LessonMenuDialogState.DeleteBlueprint)
    fun openChooseStageModeDialog() = open(LessonMenuDialogState.ChooseStageMode)
    fun openCreateBlueprintStageDialog() = open(LessonMenuDialogState.CreateBlueprintStage)
    fun openChooseSelectionModeDialog() = open(LessonMenuDialogState.ChooseSelectionMode)
    fun openSelectionModeInfoDialog() = open(LessonMenuDialogState.SelectionModeInfo)
    fun openCurrentCreatingStageDialog() = open(LessonMenuDialogState.CreateBlueprintStage)
}

sealed class LessonMenuDialogState {
    object Closed : LessonMenuDialogState()
    object CreateLesson : LessonMenuDialogState()
    object CreateBlueprint : LessonMenuDialogState()
    object ChooseStageMode : LessonMenuDialogState()
    object DeleteStage : LessonMenuDialogState()
    object DeleteBlueprint : LessonMenuDialogState()
    object DeleteLesson : LessonMenuDialogState()
    object ChooseSelectionMode : LessonMenuDialogState()
    object SelectionModeInfo : LessonMenuDialogState()
    object CreateBlueprintStage : LessonMenuDialogState()
    object CurrentCreatingStage : LessonMenuDialogState()
}