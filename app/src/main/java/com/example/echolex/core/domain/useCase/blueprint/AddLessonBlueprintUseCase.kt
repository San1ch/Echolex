package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.repository.LessonBlueprintMemoryStore
import javax.inject.Inject

class AddLessonBlueprintUseCase @Inject constructor(
    val lessonBlueprintMemoryStore: LessonBlueprintMemoryStore,
){
    operator fun invoke(blueprint: LessonBlueprint) = lessonBlueprintMemoryStore.addLessonBlueprint(blueprint)
}