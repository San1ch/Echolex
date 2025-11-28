package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.useCase.validation.ValidateBlueprintStagesUseCase
import javax.inject.Inject

class CreateBlueprintUseCase @Inject constructor(
    private val nameValidate: ValidateBlueprintNameUseCase,
    private val stagesValidate: ValidateBlueprintStagesUseCase,
    private val addLessonBlueprintUseCase: AddLessonBlueprintUseCase
) {

    suspend operator fun invoke(lessonBlueprint: LessonBlueprint): Boolean {
        if (!nameValidate.invoke(lessonBlueprint.name)) return false
        if (!stagesValidate.invoke(lessonBlueprint)) return false

        addLessonBlueprintUseCase(lessonBlueprint)
        return true
    }
}