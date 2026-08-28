package com.example.nova.ui.demo

import android.graphics.Rect
import com.example.nova.engine.events.EventBus
import com.example.nova.engine.events.NovaEvent
import com.example.nova.engine.guide.GuideEngine
import com.example.nova.engine.guide.GuideStep
import com.example.nova.engine.scene.SceneGraph
import com.example.nova.engine.scene.UiNode
import com.example.nova.engine.scene.UiNodeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DemoMode(
    private val scope: CoroutineScope,
    private val sceneGraph: SceneGraph,
    private val guideEngine: GuideEngine
) {
    fun runDemo() {
        scope.launch {
            // 1. Mock the SceneGraph
            val uploadButton = UiNode(
                id = "btn_upload",
                bounds = Rect(200, 800, 600, 950),
                type = UiNodeType.BUTTON,
                isVisible = true,
                isEnabled = true
            )
            val saveButton = UiNode(
                id = "btn_save",
                bounds = Rect(800, 150, 1000, 250),
                type = UiNodeType.BUTTON,
                isVisible = true,
                isEnabled = true
            )
            sceneGraph.updateNodes(listOf(uploadButton, saveButton))

            // 2. Define the flow
            val steps = listOf(
                GuideStep("step1", "btn_upload", "Tap here to upload."),
                GuideStep("step2", "btn_save", "Now save your progress.")
            )

            // 3. Start Guidance
            guideEngine.startFlow(steps)

            // 4. Mock the user interaction (simulate tapping the button after 3 seconds)
            delay(3000)
            EventBus.publish(NovaEvent.ValidationSuccess("step1"))
            
            delay(3000)
            EventBus.publish(NovaEvent.ValidationSuccess("step2"))
        }
    }
}
