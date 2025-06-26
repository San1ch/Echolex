package com.example.echolex.core.domain.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedDataMemoryStore @Inject constructor(){
    private val state = MutableStateFlow<SharedDataMemoryStoreState>(SharedDataMemoryStoreState())

    suspend fun setDeckName(name: String) {
        state.value = state.value.copy(
            deckNameForItemDeckInfo = name
        )
    }

    fun getState(): StateFlow<SharedDataMemoryStoreState> {
        return state.asStateFlow()
    }
}

data class SharedDataMemoryStoreState(
    val deckNameForItemDeckInfo: String = ""

)