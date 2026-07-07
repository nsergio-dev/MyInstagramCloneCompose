package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawWithContent
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.hypot
import kotlin.math.pow
import kotlin.math.sqrt

private fun Offset.magnitude(): Float = sqrt(x * x + y * y)

@Composable
fun DragToDismissContainer(
    zoomScale: Float,
    zoomOffset: Offset,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    minCircleDuringDrag: Float = 0.75f, // 0.9 = suave, 0.65 = más marcado
    dismissThresholdFraction: Float = 0.18f, // de la altura para cerrar
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    var containerWidthPx by remember { mutableFloatStateOf(1f) }
    var containerHeightPx by remember { mutableFloatStateOf(1f) }

    var dragY by remember { mutableFloatStateOf(0f) }
    var isClosing by remember { mutableStateOf(false) }
    val circleAnim = remember { Animatable(1f) }

    val dragFraction by remember {
        derivedStateOf { (dragY / containerHeightPx).coerceIn(0f, 1f) }
    }

    fun easeOutCubic(t: Float) = 1f - (1f - t).pow(3)

    val circleDuringDrag by remember {
        derivedStateOf { 1f - (1f - minCircleDuringDrag) * easeOutCubic(dragFraction) }
    }

    val contentScale by remember { derivedStateOf { 1f - 0.15f * dragFraction } }
    val scrimAlpha by remember { derivedStateOf { 1f - 0.9f * dragFraction } }

    val canDismiss by remember {
        derivedStateOf {
            val nearIdentity = zoomScale <= 1.01f
            val nearCenter = zoomOffset.magnitude() <= with(density) { 24.dp.toPx() }
            nearIdentity && nearCenter
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {
                containerWidthPx = it.width.toFloat()
                containerHeightPx = it.height.toFloat()
            }
            .pointerInput(canDismiss) {
                if (!canDismiss) return@pointerInput
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dy ->
                        dragY = (dragY + dy).coerceIn(0f, containerHeightPx)
                        change.consume()
                    },
                    onDragEnd = {
                        val shouldDismiss = dragY > containerHeightPx * dismissThresholdFraction
                        val settleSpec: AnimationSpec<Float> = if (shouldDismiss) {
                            tween(durationMillis = 180, easing = LinearOutSlowInEasing)
                        } else {
                            spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        }
                        scope.launch {
                            if (shouldDismiss) {
                                isClosing = true
                                circleAnim.snapTo(circleDuringDrag)
                                val jobCircle = launch {
                                    circleAnim.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(
                                            durationMillis = 180,
                                            easing = LinearOutSlowInEasing
                                        )
                                    )
                                }
                                val jobDrag = launch {
                                    val animY = Animatable(dragY)
                                    animY.animateTo(
                                        targetValue = containerHeightPx,
                                        animationSpec = tween(
                                            durationMillis = 180,
                                            easing = LinearOutSlowInEasing
                                        )
                                    ) { dragY = value }
                                }
                                joinAll(jobCircle, jobDrag)
                                onDismiss()
                            } else {
                                val animY = Animatable(dragY)
                                animY.animateTo(0f, settleSpec) { dragY = value }
                                isClosing = false
                                circleAnim.snapTo(1f)
                            }
                        }
                    },
                    onDragCancel = {
                        scope.launch {
                            val animY = Animatable(dragY)
                            animY.animateTo(
                                0f,
                                spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            ) { dragY = value }
                            isClosing = false
                            circleAnim.snapTo(1f)
                        }
                    }
                )
            }
            .drawWithContent {
                val cx = containerWidthPx / 2f
                val cy = containerHeightPx / 2f
                val maxR = hypot(containerWidthPx / 2f, containerHeightPx / 2f)
                val currentCircle = if (isClosing) circleAnim.value else circleDuringDrag
                val r = maxR * currentCircle

                val circlePath = Path().apply {
                    addOval(Rect(cx - r, cy - r, cx + r, cy + r))
                }

                clipPath(circlePath) {
                    this@drawWithContent.drawContent()
                }

                val full = Path().apply { addRect(Rect(0f, 0f, size.width, size.height)) }
                val outside = Path.combine(PathOperation.Difference, full, circlePath)
                drawPath(outside, Color.Black.copy(alpha = scrimAlpha))
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = contentScale
                    scaleY = contentScale
                }
        ) {
            content()
        }
    }
}