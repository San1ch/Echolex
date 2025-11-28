package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.repository.LessonBlueprintRepository
import com.example.echolex.core.domain.data.repository.LessonRepository
import javax.inject.Inject

class AddLessonBlueprintUseCase @Inject constructor(
    val lessonBlueprintRepository: LessonBlueprintRepository,
){
    suspend operator fun invoke(lessonBlueprint: LessonBlueprint) = lessonBlueprintRepository.upsert(lessonBlueprint)
}