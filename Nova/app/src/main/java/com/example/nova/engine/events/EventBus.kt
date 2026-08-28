package com.example.nova.engine.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class NovaEvent {
    data class FlightCommand(val targetX: Float, val targetY: Float) : NovaEvent()
    data class HighlightCommand(val targetId: String, val message: String) : NovaEvent()
    data class ValidationSuccess(val stepId: String) : NovaEvent()
    data class ValidationFailure(val stepId: String, val reason: String) : NovaEvent()
}

object EventBus {
    private val _events = MutableSharedFlow<NovaEvent>(extraBufferCapacity = 10)
    val events = _events.asSharedFlow()

    suspend fun publish(event: NovaEvent) {
        _events.emit(event)
    }
}
