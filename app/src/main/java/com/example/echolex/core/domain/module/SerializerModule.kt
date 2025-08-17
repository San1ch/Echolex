package com.example.echolex.core.domain.module

import com.example.echolex.core.domain.data.model.deck.Deck
import com.example.echolex.core.domain.data.model.lesson.Lesson
import com.example.echolex.core.domain.data.model.lesson.LessonBlueprint
import com.example.echolex.core.domain.service.lesson.LearningStageStrategy
import com.example.echolex.core.domain.service.lesson.RepeatingStageStrategy
import com.example.echolex.core.domain.service.lesson.StageStrategy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializerModule {

    @Provides
    fun provideDeckListSerializer(): KSerializer<List<Deck>> {
        return ListSerializer(Deck.serializer())
    }

    @Provides
    fun provideLessonBlueprintListSerializer(): KSerializer<List<LessonBlueprint>> {
        return ListSerializer(LessonBlueprint.serializer())
    }

    @Provides
    fun provideLessonListSerializer(): KSerializer<List<Lesson>> {
        return ListSerializer(Lesson.serializer())
    }
}

@Module
@InstallIn(SingletonComponent::class)
object StrategyModule {
    
    @Provides
    @Singleton
    fun provideLearningStageStrategy(): LearningStageStrategy {
        return LearningStageStrategy()
    }
    
    @Provides
    @Singleton
    fun provideRepeatingStageStrategy(): RepeatingStageStrategy {
        return RepeatingStageStrategy()
    }
}