package com.example.nova.ui.robot

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import com.example.nova.engine.state.RobotState
import kotlinx.coroutines.delay

@Composable
fun NovaRobot(
    state: RobotState,
    modifier: Modifier = Modifier
) {
    // Breathing animation (scale)
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Blinking logic
    var isBlinking by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) {
            delay((2000..5000).random().toLong())
            isBlinking = true
            delay(150)
            isBlinking = false
        }
    }

    val eyeHeightScale by animateFloatAsState(
        targetValue = if (isBlinking || state == RobotState.SLEEPING) 0.1f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessHigh)
    )

    Canvas(modifier = modifier.size(80.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.width / 3

        scale(scale) {
            // Draw main body (soft glow)
            drawCircle(
                color = Color.DarkGray.copy(alpha = 0.8f),
                radius = radius,
                center = center
            )

            // Draw glowing eye
            val eyeColor = when (state) {
                RobotState.LISTENING -> Color.Cyan
                RobotState.THINKING -> Color.Magenta
                RobotState.SLEEPING -> Color.Gray
                else -> Color.White
            }
            drawOval(
                color = eyeColor,
                topLeft = Offset(center.x - radius / 3, center.y - (radius / 4) * eyeHeightScale),
                size = androidx.compose.ui.geometry.Size(radius / 1.5f, (radius / 2) * eyeHeightScale)
            )
        }
    }
}
