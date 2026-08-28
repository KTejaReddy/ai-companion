package com.example.nova.engine.behavior

import com.example.nova.engine.guide.GuideStep

class TeachingEngine {
    
    enum class TeachingStage {
        QUESTION, HINT_1, HINT_2, FULL_ANSWER
    }

    private val stepStages = mutableMapOf<String, TeachingStage>()

    fun getInstructionForStep(step: GuideStep, isRetry: Boolean = false): String {
        val currentStage = stepStages.getOrDefault(step.id, TeachingStage.QUESTION)
        
        if (isRetry) {
            val nextStage = advanceStage(currentStage)
            stepStages[step.id] = nextStage
            return generateTextForStage(step, nextStage)
        }

        stepStages[step.id] = TeachingStage.QUESTION
        return generateTextForStage(step, TeachingStage.QUESTION)
    }

    private fun advanceStage(current: TeachingStage): TeachingStage {
        return when (current) {
            TeachingStage.QUESTION -> TeachingStage.HINT_1
            TeachingStage.HINT_1 -> TeachingStage.HINT_2
            TeachingStage.HINT_2 -> TeachingStage.FULL_ANSWER
            TeachingStage.FULL_ANSWER -> TeachingStage.FULL_ANSWER
        }
    }

    private fun generateTextForStage(step: GuideStep, stage: TeachingStage): String {
        return when (stage) {
            TeachingStage.QUESTION -> "Can you find the button to ${step.instruction}?"
            TeachingStage.HINT_1 -> "Hint: It looks like a ${step.targetNodeId}."
            TeachingStage.HINT_2 -> "Almost! It's right around here."
            TeachingStage.FULL_ANSWER -> "Here is the ${step.instruction} button. Tap it."
        }
    }
}
