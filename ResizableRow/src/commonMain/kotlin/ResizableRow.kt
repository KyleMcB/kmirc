/*
 * Copyright (c) Kyle McBurnett 2024.
 */
package com.xingpeds.resizerow

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import java.awt.Cursor
import java.awt.Cursor.E_RESIZE_CURSOR

private fun main() = singleWindowApplication {
    ResizableBorder(
        RightBorder(containerModifier = Modifier.background(color = Color.Red)) { Text("First panel") },
        NoBorder { Text("middle") },
        LeftBorder(containerModifier = Modifier.background(Color.Blue)) { Text("last panel") }
    )
}

/**
 * top level representation for content in the resizable row
 */
sealed interface RowItem {
    val content: @Composable BoxScope.() -> Unit
}

/**
 * for items that have a left or right border
 */
sealed interface Bordered : RowItem {
    val containerModifier: Modifier
    val borderModifier: Modifier
    val borderWidth: Dp
}

/**
 * simple data class for an item with a right border
 */
data class RightBorder(
    val initialWidth: Dp = 200.dp,
    override val borderWidth: Dp = 6.dp,
    override val containerModifier: Modifier = Modifier,
    override val borderModifier: Modifier = Modifier
        .fillMaxHeight()
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.LightGray, Color.Black, Color.LightGray)
            )
        ),
    override val content: @Composable BoxScope.() -> Unit
) : Bordered

/**
 * simple data class for an item with a left border
 */
data class LeftBorder(
    val initialWidth: Dp = 200.dp,
    override val borderWidth: Dp = 6.dp,
    override val containerModifier: Modifier = Modifier,
    override val borderModifier: Modifier = Modifier
        .fillMaxHeight()
        .background(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.LightGray, Color.Black, Color.LightGray)
            )
        ),
    override val content: @Composable BoxScope.() -> Unit,
) : Bordered

/**
 * simple data class for a row section that has no border.
 * This will expand to fit any left over area in the resizable row.
 */
data class NoBorder(override val content: @Composable BoxScope.() -> Unit) : RowItem


/**
 * A composable function that creates a resizable border for a row layout.
 *
 * @param items Vararg of [RowItem] representing the content and properties for each section of the resizable row.
 */
@Composable
fun ResizableBorder(vararg items: RowItem) {
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
        items.forEach { rowItem ->
            when (rowItem) {
                is LeftBorder -> {
                    val item: LeftBorder = rowItem
                    var width by remember { mutableStateOf(rowItem.initialWidth) }
                    Box(modifier = Modifier
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                                width -= (dragAmount / 2).dp
                            })
                        }
                        .pointerHoverIcon(PointerIcon(Cursor(E_RESIZE_CURSOR)))
                        .width(rowItem.borderWidth)
                        .then(rowItem.borderModifier)
                    )
                    Box(
                        modifier = Modifier.width(width).then(rowItem.containerModifier)
                    ) {
                        item.content(this)
                    }
                }

                is RightBorder -> {
                    var width by remember { mutableStateOf(rowItem.initialWidth) }

                    Box(
                        modifier = Modifier.width(width).then(rowItem.containerModifier)
                    ) {
                        rowItem.content(this)
                    }
                    Box(modifier = Modifier
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                                width += (dragAmount / 2).dp
                            })
                        }
                        .pointerHoverIcon(PointerIcon(Cursor(E_RESIZE_CURSOR)))
                        .width(rowItem.borderWidth)
                        .then(rowItem.borderModifier)
                    )
                }

                is NoBorder -> Box(modifier = Modifier.weight(1f)) { rowItem.content(this) }
            }
        }
    }
}