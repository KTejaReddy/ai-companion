package com.example.nova.engine.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class RobotState {
    SLEEPING,
    IDLE,
    LISTENING,
    THINKING,
    TALKING,
    POINTING,
    FLYING,
    CELEBRATING,
    CONFUSED,
    WAITING,
    PROCESSING
}

class RobotStateMachine {
    private val _currentState = MutableStateFlow(RobotState.SLEEPING)
    val currentState: StateFlow<RobotState> = _currentState.asStateFlow()

    fun transitionTo(newState: RobotState) {
        // Enforce valid transitions here
        if (_currentState.value == RobotState.SLEEPING && newState != RobotState.IDLE) {
            // Must wake up first
            return
        }
        _currentState.value = newState
    }
}
