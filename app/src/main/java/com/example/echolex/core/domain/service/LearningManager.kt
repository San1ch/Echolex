package com.example.echolex.core.domain.service

import com.example.echolex.core.domain.data.model.deck.Card
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.service.lesson.LessonCommand
import com.example.echolex.core.domain.service.lesson.LessonCommandHandler
import com.example.echolex.core.domain.service.lesson.LessonResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningManager @Inject constructor(
    private val commandHandler: LessonCommandHandler
) {
    
    fun getNextCard(lesson: Lesson): LessonResult {
        return commandHandler.handle(LessonCommand.GetNextCard(lesson))
    }
    
    fun markCardAsCorrect(lesson: Lesson, card: Card): LessonResult {
        return commandHandler.handle(LessonCommand.MarkCardAsCorrect(lesson, card))
    }
    
    fun markCardAsIncorrect(lesson: Lesson, card: Card): LessonResult {
        return commandHandler.handle(LessonCommand.MarkCardAsIncorrect(lesson, card))
    }
    
    fun validateLesson(lesson: Lesson): LessonResult {
        return commandHandler.handle(LessonCommand.ValidateLesson(lesson))
    }
    
    fun getProgress(lesson: Lesson): LessonResult {
        return commandHandler.handle(LessonCommand.GetProgress(lesson))
    }
    
    fun getStageInfo(lesson: Lesson): LessonResult {
        return commandHandler.handle(LessonCommand.GetStageInfo(lesson))
    }
    
    fun isLessonCompleted(lesson: Lesson): Boolean {
        return lesson.isCompleted()
    }
}
