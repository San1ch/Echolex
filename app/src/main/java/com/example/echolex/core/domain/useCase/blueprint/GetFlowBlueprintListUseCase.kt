package com.example.echolex.core.domain.useCase.blueprint

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.data.repository.LessonBlueprintMemoryStore
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetFlowBlueprintListUseCase @Inject constructor(
    private val lessonBlueprintMemoryStore: LessonBlueprintMemoryStore
){
    operator fun invoke(): StateFlow<List<LessonBlueprint>> = lessonBlueprintMemoryStore.lessonBlueprints
}