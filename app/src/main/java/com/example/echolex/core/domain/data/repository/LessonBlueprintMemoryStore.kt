package com.example.echolex.core.domain.data.repository

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.useCase.lesson.LoadBlueprintsUseCases
import com.example.echolex.core.domain.useCase.lesson.SaveBlueprintsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonBlueprintMemoryStore @Inject constructor(
    private val saveBlueprintsUseCase: SaveBlueprintsUseCase,
    private val loadBlueprintsUseCases: LoadBlueprintsUseCases
) {
    private val _lessonBlueprints = MutableStateFlow<List<LessonBlueprint>>(emptyList())
    val lessonBlueprints: StateFlow<List<LessonBlueprint>> = _lessonBlueprints

    init {
        _lessonBlueprints.value = loadBlueprintsUseCases()
    }

    fun addLessonBlueprint(lessonBlueprint: LessonBlueprint) {
        _lessonBlueprints.value = listOf(lessonBlueprint) + _lessonBlueprints.value //TODO create use-case for creating blueprint (add use-case already created)
        saveLessonBlueprint()
    }

    fun saveLessonBlueprint() {
        saveBlueprintsUseCase(_lessonBlueprints.value)
    }

    fun getBlueprintByName(name: String): LessonBlueprint? {
        return _lessonBlueprints.value.find { it.name == name }
    }

    fun removeBlueprintByName(name: String) {
        _lessonBlueprints.value = _lessonBlueprints.value.filter { it.name != name }
    }
}

