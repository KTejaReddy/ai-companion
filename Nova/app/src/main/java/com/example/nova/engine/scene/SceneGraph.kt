package com.example.nova.engine.scene

import android.graphics.Rect

enum class UiNodeType {
    BUTTON, IMAGE, TEXT, INPUT, MENU, UNKNOWN
}

data class UiNode(
    val id: String,
    val bounds: Rect,
    val type: UiNodeType,
    val isVisible: Boolean,
    val isEnabled: Boolean,
    val confidence: Float = 1.0f
) {
    val centerX: Float get() = bounds.exactCenterX()
    val centerY: Float get() = bounds.exactCenterY()
}

class SceneGraph {
    private val nodes = mutableMapOf<String, UiNode>()

    fun updateNodes(newNodes: List<UiNode>) {
        nodes.clear()
        newNodes.forEach { nodes[it.id] = it }
    }

    fun findNodeByType(type: UiNodeType): UiNode? {
        return nodes.values.firstOrNull { it.type == type && it.isVisible }
    }
    
    fun findNodeById(id: String): UiNode? {
        return nodes[id]
    }
}
