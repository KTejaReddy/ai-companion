package com.example.nova.brain.core

import com.example.nova.engine.events.EventBus
import com.example.nova.engine.events.NovaEvent
import com.example.nova.engine.guide.GuideEngine
import com.example.nova.engine.guide.GuideStep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Brain(
    private val scope: CoroutineScope,
    private val intentEngine: IntentEngine,
    private val contextEngine: ContextEngine,
    private val planner: Planner,
    private val guideEngine: GuideEngine
) {
    fun processVoiceCommand(input: String) {
        scope.launch {
            // 1. Fetch Context
            val context = contextEngine.getCurrentContext()
            
            // 2. Parse Intent (Model Agnostic)
            val intent = intentEngine.parseIntent(input, context)
            
            if (intent.confidence < 0.5f) {
                // Trigger Conversation Manager for clarification
                return@launch
            }
            
            // 3. Generate Logical Plan
            val abstractTasks = planner.generatePlan(intent, context)
            
            // 4. Translate Abstract Tasks to GuideSteps (TaskEngine)
            val guideSteps = abstractTasks.map { task ->
                GuideStep(
                    id = java.util.UUID.randomUUID().toString(),
                    targetNodeId = task.targetElementId,
                    instruction = task.instruction
                )
            }
            
            // 5. Hand over to the Visual Guidance Engine
            guideEngine.startFlow(guideSteps)
        }
    }
}
