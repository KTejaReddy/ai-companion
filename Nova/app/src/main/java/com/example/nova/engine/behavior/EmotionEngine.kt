package com.example.nova.engine.behavior

import com.example.nova.engine.events.EventBus
import com.example.nova.engine.events.NovaEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

enum class RobotEmotion {
    NEUTRAL,
    HAPPY,
    CURIOUS,
    PATIENT,
    CELEBRATING,
    SLEEPY,
    THINKING
}

class EmotionEngine(private val scope: CoroutineScope) {
    private val _currentEmotion = MutableStateFlow(RobotEmotion.NEUTRAL)
    val currentEmotion: StateFlow<RobotEmotion> = _currentEmotion.asStateFlow()

    private var failureCount = 0

    init {
        scope.launch {
            EventBus.events.collect { event ->
                when (event) {
                    is NovaEvent.ValidationSuccess -> handleSuccess()
                    is NovaEvent.ValidationFailure -> handleFailure()
                    is NovaEvent.FlightCommand -> setEmotion(RobotEmotion.CURIOUS)
                    else -> {}
                }
            }
        }
    }

    private fun handleSuccess() {
        failureCount = 0
        setEmotion(RobotEmotion.CELEBRATING)
        // Reset to neutral after a while
    }

    private fun handleFailure() {
        failureCount++
        if (failureCount >= 2) {
            setEmotion(RobotEmotion.PATIENT)
        } else {
            setEmotion(RobotEmotion.THINKING)
        }
    }

    private fun setEmotion(emotion: RobotEmotion) {
        _currentEmotion.value = emotion
    }
}
