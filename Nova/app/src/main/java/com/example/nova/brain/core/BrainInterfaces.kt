package com.example.nova.brain.core

data class Intent(
    val action: String,
    val targetApp: String?,
    val confidence: Float
)

data class AppContext(
    val currentAppPackage: String,
    val currentScreenName: String
)

interface IntentEngine {
    suspend fun parseIntent(input: String, context: AppContext): Intent
}

interface ContextEngine {
    fun getCurrentContext(): AppContext
}

data class AbstractTask(
    val type: String,
    val targetElementId: String,
    val instruction: String
)

interface Planner {
    suspend fun generatePlan(intent: Intent, context: AppContext): List<AbstractTask>
}
