/*
 * Copyright (c) Kyle McBurnett 2024.
 */

import RowItem.NoBorder
import RowItem.RightBorder
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
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

@OptIn(ExperimentalFoundationApi::class)
fun main() = singleWindowApplication {
//    var topBoxOffset by remember { mutableStateOf(Offset(0f, 0f)) }
//
//    Box(modifier = Modifier.offset {
//        IntOffset(topBoxOffset.x.toInt(), topBoxOffset.y.toInt())
//    }.size(100.dp)
//        .background(Color.Green)
//        .pointerInput(Unit) {
//            detectDragGestures(
//                matcher = PointerMatcher.Primary
//            ) {
//                topBoxOffset += it
//            }
//        }
//    ) {
//        Text(text = "Drag with LMB", modifier = Modifier.align(Alignment.Center))
//    }
    ResizableBorder(listOf(RightBorder { Text("First panel") }, NoBorder { Text("middle") }))
}

sealed class RowItem {
    abstract val item: @Composable () -> Unit

    data class RightBorder(val initialWidth: Dp = 200.dp, override val item: @Composable () -> Unit) : RowItem()
    data class NoBorder(override val item: @Composable () -> Unit) : RowItem()
}


@Composable
fun ResizableBorder(items: List<RowItem>) {
    require(items.count { it is NoBorder } <= 1)
//    val resizePointerIcon = remember { MouseCursor { PointerIcon(Cursor(E_RESIZE_CURSOR)) } }
    val resizeBorderThickness = 5.dp
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color.LightGray, Color.DarkGray, Color.LightGray),
        startX = 0f,
        endX = resizeBorderThickness.value
    )
    Row(modifier = Modifier.fillMaxSize()) {
        items.forEach { rowItem ->
            when (rowItem) {
                is RightBorder -> {
                    var width by remember { mutableStateOf(rowItem.initialWidth) }

                    Box() {
                        Box(
                            modifier = Modifier.width(width).fillMaxHeight().background(Color.Gray)
                        ) {
                            rowItem.item()
                        }


                    }
                    Box(modifier = Modifier.fillMaxHeight().width(resizeBorderThickness).background(brush = gradient)
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(onHorizontalDrag = { change: PointerInputChange, dragAmount: Float ->
                                width += (dragAmount / 2).dp
                            })
                        }
                        .pointerHoverIcon(PointerIcon(Cursor(E_RESIZE_CURSOR)))
                    )
                }

                is NoBorder -> rowItem.item()
            }
        }
    }
}


