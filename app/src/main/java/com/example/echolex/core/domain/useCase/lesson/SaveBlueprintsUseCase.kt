package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.service.JSONSaver
import com.example.echolex.core.domain.service.blueprintsFileName
import javax.inject.Inject

class SaveBlueprintsUseCase @Inject constructor(private val blueprintSaveLoader: JSONSaver<List<LessonBlueprint>>) {
    operator fun invoke(blueprints: List<LessonBlueprint>) =
        blueprintSaveLoader.save(blueprints, blueprintsFileName)
}