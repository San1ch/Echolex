package com.example.echolex.core.domain.data.repository

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.LessonSettings
import com.example.echolex.core.domain.data.model.lesson.LessonStage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonMemoryStore @Inject constructor() {
    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> get() = _lessons


    fun addLesson(lesson: Lesson) {
        _lessons.value = _lessons.value + lesson
    }
}

data class Lesson(
    val name: String,
    val decks: List<Deck>,
    val lessonStages: List<LessonStage> = emptyList(),
    val lessonSettings: LessonSettings
)
