package com.example.nova.brain.simulation

import com.example.nova.brain.core.*
import com.example.nova.engine.guide.GuideEngine
import kotlinx.coroutines.CoroutineScope

class MockIntentEngine : IntentEngine {
    override suspend fun parseIntent(input: String, context: AppContext): Intent {
        // Regex mock for simulation
        if (input.contains("remove background", ignoreCase = true)) {
            return Intent("REMOVE_BACKGROUND", "com.canva.editor", 0.95f)
        }
        return Intent("UNKNOWN", null, 0.1f)
    }
}

class MockContextEngine : ContextEngine {
    override fun getCurrentContext(): AppContext {
        return AppContext("com.canva.editor", "MainEditorScreen")
    }
}

class MockPlanner : Planner {
    override suspend fun generatePlan(intent: Intent, context: AppContext): List<AbstractTask> {
        if (intent.action == "REMOVE_BACKGROUND") {
            return listOf(
                AbstractTask("CLICK", "btn_upload", "Tap Upload"),
                AbstractTask("CLICK", "btn_edit", "Select Edit"),
                AbstractTask("CLICK", "btn_bg_remover", "Tap Background Remover")
            )
        }
        return emptyList()
    }
}

class BrainSimulation {
    fun run(scope: CoroutineScope, guideEngine: GuideEngine) {
        val brain = Brain(
            scope = scope,
            intentEngine = MockIntentEngine(),
            contextEngine = MockContextEngine(),
            planner = MockPlanner(),
            guideEngine = guideEngine
        )

        // Simulate voice input
        brain.processVoiceCommand("Hey Nova, help me remove background.")
    }
}
