package com.example.echolex.core.domain.data.local

import com.example.echolex.core.domain.data.repository.SharedDataMemoryStore
import javax.inject.Inject

class GetSharedDataUseCase @Inject constructor(
    private val sharedDataMemoryStore: SharedDataMemoryStore
){
    operator fun <T : Any> invoke(key: String): T? {
        return sharedDataMemoryStore.get(key)
    }
}