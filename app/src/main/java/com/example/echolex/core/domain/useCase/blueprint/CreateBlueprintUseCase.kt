package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.repository.LessonBlueprintMemoryStore
import com.example.echolex.core.domain.service.centralScreenService.NotificationCenter
import com.example.echolex.core.domain.useCase.screensUseCases.OpenAppNotificationUseCase
import com.example.echolex.core.domain.useCase.validation.ValidateBlueprintNameUseCase
import com.example.echolex.core.domain.useCase.validation.ValidateBlueprintStagesUseCase
import javax.inject.Inject

class CreateBlueprintUseCase @Inject constructor(
    private val nameValidate: ValidateBlueprintNameUseCase,
    private val stagesValidate: ValidateBlueprintStagesUseCase,
    private val addLessonBlueprintUseCase: AddLessonBlueprintUseCase
) {

    operator fun invoke(blueprint: LessonBlueprint): Boolean {
        if (!nameValidate.invoke(blueprint)) return false
        if (!stagesValidate.invoke(blueprint)) return false

        addLessonBlueprintUseCase(blueprint)
        return true
    }
}