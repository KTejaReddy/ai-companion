package com.example.nova.engine.guide

import com.example.nova.engine.events.EventBus
import com.example.nova.engine.events.NovaEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

data class GuideStep(
    val id: String,
    val targetNodeId: String,
    val instruction: String
)

class GuideEngine(private val scope: CoroutineScope) {
    private var currentFlow: List<GuideStep> = emptyList()
    private var currentStepIndex = 0

    init {
        scope.launch {
            EventBus.events
                .filterIsInstance<NovaEvent.ValidationSuccess>()
                .collect { event ->
                    if (currentFlow.isNotEmpty() && event.stepId == currentFlow[currentStepIndex].id) {
                        advanceStep()
                    }
                }
        }
    }

    fun startFlow(steps: List<GuideStep>) {
        if (steps.isEmpty()) return
        currentFlow = steps
        currentStepIndex = 0
        executeCurrentStep()
    }

    private fun executeCurrentStep() {
        val step = currentFlow[currentStepIndex]
        scope.launch {
            // Tell robot to highlight and point to the node
            EventBus.publish(NovaEvent.HighlightCommand(step.targetNodeId, step.instruction))
        }
    }

    private fun advanceStep() {
        currentStepIndex++
        if (currentStepIndex < currentFlow.size) {
            executeCurrentStep()
        } else {
            // Flow completed
            currentFlow = emptyList()
        }
    }
}
