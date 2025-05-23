package com.example.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun HeartLikeBox(
    modifier: Modifier = Modifier,
    imageRes: Int, // 必须传入心形图片资源ID, e.g. R.mipmap.ic_heart
    background: @Composable (() -> Unit)? = null // 可选, 你的底层内容（如视频等）
) {
    // 飘心数据（位置/唯一key）
    val hearts = remember { mutableStateListOf<HeartState>() }
    // 连点时间数组
    val hitTimes = remember { LongArray(3) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset: Offset ->
                    // 拷贝时间戳到左边，最后一位赋最新
                    for (i in 0 until hitTimes.size - 1) {
                        hitTimes[i] = hitTimes[i + 1]
                    }
                    hitTimes[hitTimes.size - 1] = System.currentTimeMillis()
                    // 连续3次击
                    if (hitTimes[0] >= (System.currentTimeMillis() - 500)) {
                        hearts.add(
                            HeartState(offset.x, offset.y, Random.nextInt())
                        )
                    }
                }
            }
    ) {
        // 背景内容
        background?.invoke()
        // 飘心层（动画）
        hearts.forEach { heart ->
            key(heart.key) {
                HeartLikeAnimation(
                    imageRes = imageRes,
                    x = heart.x,
                    y = heart.y,
                    onAnimationEnd = { hearts.remove(heart) }
                )
            }
        }
    }
}

@Composable
fun HeartLikeAnimation(
    imageRes: Int,
    x: Float,
    y: Float,
    onAnimationEnd: () -> Unit
) {
    // 随机旋转角度
    val angleList = listOf(-35f, -25f, 0f, 25f, 35f)
    val rotation = remember { angleList.random() }

    val animPercent = remember { Animatable(0f) }
    // 启动主控动画
    LaunchedEffect(Unit) {
        animPercent.animateTo(
            1f,
            animationSpec = tween(durationMillis = 1200, easing = LinearEasing)
        )
        onAnimationEnd()
    }
    // 动画关键路径（前后段略有调整以还原原效果）
    val alpha = lerp(1f, 0f, ((animPercent.value - 0.4f) / 0.6f).coerceIn(0f, 1f))
    val scale = when {
        animPercent.value < 0.08f -> lerp(2f, 0.9f, animPercent.value / 0.08f)
        animPercent.value < 0.2f -> lerp(0.9f, 1f, (animPercent.value - 0.08f) / 0.12f)
        animPercent.value < 0.7f -> 1f
        else -> lerp(1f, 3f, (animPercent.value - 0.7f) / 0.3f)
    }
    val offsetY = when {
        animPercent.value < 0.33f -> lerp(0f, -60f, animPercent.value / 0.33f)
        else -> lerp(-60f, -240f, (animPercent.value - 0.33f) / 0.67f)
    }
    Box(
        modifier = Modifier
            .offset {
                // 150与300是心形图片中心偏移(以原300x300尺寸居中)，如用vector自行调整
                IntOffset((x - 150).roundToInt(), (y - 300 + offsetY).roundToInt())
            }
            .size((60 * scale).dp)
            .rotate(rotation)
            .alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
    start + (stop - start) * fraction

private data class HeartState(val x: Float, val y: Float, val key: Int)