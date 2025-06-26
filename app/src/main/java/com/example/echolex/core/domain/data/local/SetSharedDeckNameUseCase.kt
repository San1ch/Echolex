package com.example.echolex.core.domain.data.local

import com.example.echolex.core.domain.data.repository.SharedDataMemoryStore
import javax.inject.Inject

class SetSharedDeckNameUseCase @Inject constructor(
    private val sharedDataMemoryStore: SharedDataMemoryStore
) {
    suspend operator fun invoke(deckName: String) {
        sharedDataMemoryStore.setDeckName(deckName)
    }
}