package com.example.echolex.core.domain.data.repository

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonBlueprintMemoryStore @Inject constructor() {
    private val _lessonBlueprints = MutableStateFlow<List<LessonBlueprint>>(emptyList())
    val lessonBlueprints: StateFlow<List<LessonBlueprint>> = _lessonBlueprints

    fun addLessonBlueprint(lessonBlueprint: LessonBlueprint) {
        _lessonBlueprints.value = listOf(lessonBlueprint) + _lessonBlueprints.value //TODO create use-case for creating blueprint (add use-case already created)
    }

    fun saveLessonBlueprint() {
        //TODO create json saver for blueprint
    }
}

