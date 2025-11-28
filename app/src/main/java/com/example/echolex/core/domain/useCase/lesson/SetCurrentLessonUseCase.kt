package com.example.echolex.core.domain.useCase.lesson

import com.example.echolex.core.constants.SharedDataLessonItemNameKey
import com.example.echolex.core.domain.data.local.GetSharedDataUseCase
import com.example.echolex.core.domain.data.repository.SharedDataMemoryStore
import javax.inject.Inject

class SetCurrentLessonUseCase @Inject constructor(
    private val sharedDataMemoryStore: SharedDataMemoryStore
) {
    operator fun invoke(name: String) {
        sharedDataMemoryStore.set<String>(SharedDataLessonItemNameKey, name)
    }
}