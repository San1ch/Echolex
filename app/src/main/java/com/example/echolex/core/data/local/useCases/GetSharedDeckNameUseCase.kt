package com.example.echolex.core.data.local.useCases

import com.example.echolex.core.data.local.SharedDataStore
import javax.inject.Inject

class GetSharedDeckNameUseCase @Inject constructor(
    private val sharedDataStore: SharedDataStore
){
    operator fun invoke() = sharedDataStore.deckName
}