package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.repository.LessonBlueprintRepository
import com.example.echolex.core.domain.data.repository.LessonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetFlowBlueprintListUseCase @Inject constructor(
    private val lessonBlueprintRepository: LessonBlueprintRepository
){
    operator fun invoke(): Flow<List<LessonBlueprint>> = lessonBlueprintRepository.blueprints
}