package com.example.echolex.core.data.local.useCases

import com.example.echolex.core.data.local.SharedDataStore
import javax.inject.Inject

class SetSharedDeckNameUseCase @Inject constructor(
    private val sharedDataStore: SharedDataStore
) {
    suspend operator fun invoke(deckName: String) {
        sharedDataStore.setDeckName(deckName)
    }
}