package com.example.echolex.core.domain.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedDataMemoryStore @Inject constructor() {
    private val _state = MutableStateFlow<Map<String, Any>>(emptyMap())
    val state: StateFlow<Map<String, Any>> = _state.asStateFlow()

    fun <T : Any> set(key: String, value: T) {
        _state.value = _state.value.toMutableMap().apply {
            this[key] = value
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String): T? {
        return _state.value[key] as? T
    }
}
