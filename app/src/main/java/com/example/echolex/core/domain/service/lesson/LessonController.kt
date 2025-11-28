package com.example.echolex.core.domain.service.lesson

import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.lesson.LessonStepResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonController @Inject constructor() {
    private lateinit var lesson: Lesson

    fun setLesson(lesson: Lesson) {
        this.lesson = lesson
    }

    suspend fun getNextStep(wasIncorrectAnswer: Boolean): LessonStepResult {
        return lesson.validation(wasIncorrectAnswer)
    }
}

