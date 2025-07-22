package com.nsergio.dev.myinstagramcompose.features.photo_preview.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChanged
import kotlin.math.abs

/**
 * Detector que solo consume los movimientos multi-touch (pinch).
 * Los swipes con un dedo pasan al pager.
 */
suspend fun PointerInputScope.detectConditionalTransformGestures(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit
) {
    val viewConfig = viewConfiguration
    awaitEachGesture {
        // Espera primer down
        awaitFirstDown(requireUnconsumed = false)
        var pastSlop = false
        var accumulatedZoom = 1f
        var accumulatedPan = Offset.Zero

        do {
            val event = awaitPointerEvent(PointerEventPass.Main)
            val zoomChange = event.calculateZoom()
            val panChange = event.calculatePan()
            accumulatedZoom *= zoomChange
            accumulatedPan += panChange

            // detecta si pasa el slop
            if (!pastSlop) {
                val zoomMotion = abs(1 - accumulatedZoom) * event.calculateCentroidSize()
                val panMotion = accumulatedPan.getDistance()
                if (zoomMotion > viewConfig.touchSlop || panMotion > viewConfig.touchSlop) {
                    pastSlop = true
                }
            }

            if (pastSlop) {
                onGesture(
                    event.calculateCentroid(),
                    panChange,
                    zoomChange,
                    event.calculateRotation()
                )
                // consume solo si es pinch (más de un dedo)
                if (event.changes.size > 1) {
                    event.changes.forEach { if (it.positionChanged()) it.consume() }
                }
            }
        } while (event.changes.any { it.pressed })
    }
}