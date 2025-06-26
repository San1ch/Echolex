package com.example.echolex.core.domain.validation.newValdations

import com.example.echolex.core.domain.data.model.notification.AppNotification

interface ValidationRule<T> {
    fun validate(data: T): AppNotification
}