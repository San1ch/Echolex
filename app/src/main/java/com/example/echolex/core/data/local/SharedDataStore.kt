package com.example.echolex.core.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedDataStore @Inject constructor(){
    private val _deckName = MutableStateFlow<String>("")
    val deckName: StateFlow<String> = _deckName.asStateFlow()

    suspend fun setDeckName(name: String) {
        _deckName.value = name
    }
}