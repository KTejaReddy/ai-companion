package com.example.nova.engine.physics

import android.view.WindowManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Modifier.draggableBubble(
    windowManager: WindowManager,
    params: WindowManager.LayoutParams,
    view: android.view.View,
    coroutineScope: CoroutineScope
): Modifier = this.pointerInput(Unit) {
    detectDragGestures(
        onDragStart = { },
        onDragEnd = {
            // Fling logic could go here (e.g. animate to nearest edge)
        },
        onDragCancel = { },
        onDrag = { change, dragAmount ->
            change.consume()
            // Update physical window position
            params.x += dragAmount.x.toInt()
            params.y += dragAmount.y.toInt()
            windowManager.updateViewLayout(view, params)
        }
    )
}
