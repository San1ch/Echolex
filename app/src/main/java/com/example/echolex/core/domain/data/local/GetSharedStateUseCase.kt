package com.example.echolex.core.domain.data.local

import com.example.echolex.core.domain.data.repository.SharedDataMemoryStore
import javax.inject.Inject

class GetSharedStateUseCase @Inject constructor(
    private val sharedDataMemoryStore: SharedDataMemoryStore
){
    operator fun invoke() = sharedDataMemoryStore.getState()
}