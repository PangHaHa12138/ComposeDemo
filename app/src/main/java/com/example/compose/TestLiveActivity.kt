package com.example.compose

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.math.roundToInt

class TestLiveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveRoomScreen()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startService(Intent(this, FloatingService::class.java))
    }
}


// ================= 主体入口 =================

@Composable
fun LiveRoomScreen() {

    val avatar =
        "https://c-ssl.duitang.com/uploads/item/201703/09/20170309211351_3eKNs.jpeg"
    val avatar2 =
        "https://tse3-mm.cn.bing.net/th/id/OIP-C.eQPvuWmUtxZRLNrUGJK6wQHaHa?rs=1&pid=ImgDetMain"

    val context = LocalContext.current

    val rainbowColors = listOf(
        Color(0xFFFF0000), // 红
        Color(0xFFFF7F00), // 橙
        Color(0xFFFFFF00), // 黄
        Color(0xFF00FF00), // 绿
        Color(0xFF00BFFF), // 青
        Color(0xFF0000FF), // 蓝
        Color(0xFF8B00FF)  // 紫
    )

    val videoUrl0 = "http://220.161.87.62:8800/hls/0/index.m3u8"
    val videoUrl1 = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"
    val videoUrl2 = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8"
    val videoUrl3 = "http://39.164.160.249:9901/tsfile/live/0125_1.m3u8"
    val videoUrl4 = "http://39.164.160.249:9901/tsfile/live/0122_1.m3u8"
    val videoUrl5 = "http://fn.tmde.top:35455/nptv/dongnan.m3u8"
    val videoUrl6 = "http://39.164.160.249:9901/tsfile/live/0128_1.m3u8"
    val videoUrl7 = "http://185.189.225.150:85/8madrid/index.m3u8"



    val anchorInfo = AnchorInfo(
        avatar = avatar2,
        nickname = "软糯小香批",
        level = "Lv.12"
    )

    val topRank = listOf(
        GiftUser(avatar),
        GiftUser(avatar),
        GiftUser(avatar)
    )

    val viewModel: LiveRoomViewModel = viewModel()

    val demoLists =
        listOf("牛逼666", "点赞!主播真棒", "厉害了，我的哥", "来啦，老弟", "送你一朵小红花")


    var overlayVisible by remember { mutableStateOf(true) }

    // 为浮层定义动画偏移（可选）
    val offsetX by animateDpAsState(if (overlayVisible) 0.dp else 300.dp)


    LaunchedEffect(Unit) {
        viewModel.startTicker(avatar, rainbowColors, demoLists)
    }

    HeartLikeBox(
        modifier = Modifier.fillMaxSize(),
        imageRes = R.drawable.ic_heart
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            AsyncImage(
                model = avatar2,
                contentDescription = "背景",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 18.dp)      // 高斯模糊
            )

            // 视频全屏
            ExoPlayerVideo(videoUrl3)

            // ---------- 包裹所有浮层的"叠加容器"及左右滑动 ----------
            OverlayWithSlide {

                // top 区域: 主播信息、排行榜
                TopArea(
                    anchorInfo = anchorInfo,
                    rankList = topRank,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 12.dp, end = 30.dp)
                )

                // 聊天内容
                Box(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(bottom = 50.dp, start = 10.dp) // 预留底部给输入区
                        .heightIn(max = 240.dp)
                ) {
                    ChatMessagesList(viewModel.chatMessages)
                }

                // 底部输入框和功能按钮
                BottomArea(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 25.dp)
                        .align(Alignment.BottomStart),
                    onSend = { text ->
                        viewModel.chatMessages.add(
                            ChatMessage(
                                id = viewModel.chatMessages.size + 1000,
                                avatar = avatar,
                                nickname = "我",
                                content = text
                            )
                        )
                    },
                    onLike = {
                        viewModel.addHeart(rainbowColors)
                    },
                )

                // 右下飘心动画
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    viewModel.hearts.forEach { heart ->
                        FloatingHeart(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(end = 56.dp, bottom = 80.dp),
                            animationKey = heart.key,   // 永远唯一且不复用
                            color = heart.color,
                            onFinished = { viewModel.removeHeart(heart.key) }
                        )
                    }
                }
            }

            IconButton(
                onClick = {

                    context.startService(Intent(context, FloatingService::class.java))

                    (context as? Activity)?.finish()

                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 40.dp, end = 5.dp)
                    .size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "关闭",
                    tint = Color.White,
                )
            }
        }
    }


}

// ========== 视频播放 ==========
@OptIn(UnstableApi::class)
@Composable
fun ExoPlayerVideo(url: String) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(url))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }
    // 监听生命周期，暂停/恢复播放
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    exoPlayer.playWhenReady = true
                    exoPlayer.play()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    exoPlayer.playWhenReady = false
                    exoPlayer.pause()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // 页面销毁时释放资源
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                useController = false
                player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxSize(),
    )


//        Box(
//            modifier = Modifier
//                .fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            PlayerSurface(
//                exoPlayer,
//                surfaceType = SURFACE_TYPE_SURFACE_VIEW,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    // 画面保持16:9
//                    .aspectRatio(16 / 9f),
//            )
//        }
}

// ================== 顶部主播&榜单 =================
@Composable
fun TopArea(
    anchorInfo: AnchorInfo,
    rankList: List<GiftUser>,
    modifier: Modifier = Modifier
) {

    val follow = remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // 主播
        Row(
            Modifier
                .background(Color.White.copy(alpha = 0.22f), shape = RoundedCornerShape(25.dp))
                .padding(vertical = 2.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserImage(
                url = anchorInfo.avatar,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    anchorInfo.nickname,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    anchorInfo.level,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .background(
                            Color.Black.copy(alpha = 0.32f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Button(
                onClick = {
                    follow.value = !follow.value
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFF8577C),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
            ) {
                val text = if (follow.value) "已关注" else "关注"
                Text(text, fontSize = 13.sp)
            }
        }
        // 榜单
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.22f), shape = RoundedCornerShape(25.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row {
                rankList.forEach { user ->
                    UserImage(
                        url = user.avatar,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                            .padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}

// =============== 聊天列表 ===============
@Composable
fun ChatMessagesList(messages: List<ChatMessage>) {

    val listState = rememberLazyListState()
    // 每当有新消息时自动滑到顶部（reverseLayout下视觉“底部”）
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 240.dp)
            .padding(bottom = 8.dp),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        itemsIndexed(messages) { index, msg ->
            Row(
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserImage(
                    url = msg.avatar,
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    msg.nickname,
                    fontSize = 13.sp,
                    color = Color(0xFFD82750),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    msg.content,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }
}

// ============= 底部输入区与功能按钮 =============
@Composable
fun BottomArea(
    onSend: (String) -> Unit,
    onLike: () -> Unit,
    modifier: Modifier = Modifier
) {
    var msgInput by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.align(Alignment.BottomStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 输入框
            Box(
                Modifier
                    .weight(1f)
                    .background(Color.White.copy(alpha = 0.90f), RoundedCornerShape(24.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                BasicTextField(
                    value = msgInput,
                    onValueChange = { msgInput = it },
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 15.sp, color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    decorationBox = { innerTextField ->
                        if (msgInput.isEmpty()) {
                            Text(
                                text = "说点什么…",
                                color = Color.Gray,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (msgInput.isNotBlank()) {
                                onSend(msgInput)
                                msgInput = ""
                                keyboardController?.hide()
                            }
                        }
                    ),
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // 发送
            CircleIconButton(
                icon = Icons.AutoMirrored.Default.Send,
                onClick = {
                    if (msgInput.isNotBlank()) {
                        onSend(msgInput)
                        msgInput = ""
                        keyboardController?.hide()
                    }
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            // 送礼
            CircleIconButton(
                icon = Icons.Default.CardGiftcard,
                onClick = {

                    onLike()
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            // 菜单
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(Color.White.copy(alpha = 0.90f), CircleShape)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.MoreHoriz, "", tint = Color.Black)
            }
        }
        // 最右有飘心按钮区域(与其他按钮分开以免被遮)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-20).dp),
            contentAlignment = Alignment.Center
        ) {
            // 飘心可附加在此
        }
    }
}

// 圆形ICON按钮
@Composable
fun CircleIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f), CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, "", tint = Color(0xFFDA3261))
    }
}

// ============== 飘心动画 ==============
@Composable
fun FloatingHeart(
    modifier: Modifier = Modifier,
    animationKey: Int,
    color: Color,
    onFinished: (() -> Unit)? = null
) {
    // 最大上升距离，Y 越大飞得越高
    val travelY = 500f
    // X 方向最大偏移，可以让曲线更宽弯
    val travelX = 140f
    //曲线的高度控制
    val curveHeight = 220f
    // 用于让左右曲线交错，偶数右曲线，奇数左曲线
    val curveDir = if (animationKey % 2 == 0) 1 else -1
    // 动画总时长（毫秒）
    val animDuration = 3200
    // 进度动画，范围 0~1
    val progress = remember { Animatable(0f) }
    LaunchedEffect(animationKey) {
        // 启动动画
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animDuration, easing = LinearEasing)
        )
        // 动画结束后回调移除
        onFinished?.invoke()
    }
    val t = progress.value
    // 大小渐变，由0.6x到1.2x，lerp 是线性插值
    val scale = lerp(0.6f, 1.2f, t)
    // 透明渐变，到最后一点点时淡出
    val alpha = if (t < 0.8f) 1f else 1f - (t - 0.8f) * 20

    val offset = calculateBezier(
        t,
        // P0：起点，原点 (x=0, y=0)，即心形动画的起始位置，通常在底部中央
        Offset(0f, 0f),
        // P1：第一个控制点，决定心形初始向左或向右偏移（取决于curveDir），高度为-curveHeight，控制曲线的第一段拐弯
        Offset(curveDir * travelX / 2, -curveHeight),
        // P2：第二个控制点，方向与第一个相反，同样生成弯曲，Y更接近终点，制造反向回拉的弯
        Offset(-curveDir * travelX / 2, -travelY + curveHeight),
        // P3：曲线终点，决定最后停在哪里（左右、高度）
        Offset(curveDir * travelX, -travelY)
    )
    Box(
        modifier = modifier
            .offset { IntOffset(offset.x.dp.roundToPx(), offset.y.dp.roundToPx()) }
            .size((28 * scale).dp)
            .alpha(alpha)
    ) {
        Icon(
            Icons.Default.Favorite,
            contentDescription = "",
            tint = color.copy(alpha = alpha),
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun calculateBezier(
    t: Float,
    start: Offset, control1: Offset, control2: Offset, end: Offset
): Offset {
    val u = 1 - t
    val x = u.pow(3) * start.x +
            3 * u.pow(2) * t * control1.x +
            3 * u * t.pow(2) * control2.x +
            t.pow(3) * end.x
    val y = u.pow(3) * start.y +
            3 * u.pow(2) * t * control1.y +
            3 * u * t.pow(2) * control2.y +
            t.pow(3) * end.y
    return Offset(x, y)
}


// =========== 通用圆形网络图片 =============
@Composable
fun UserImage(
    url: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
    )
}


@Composable
fun OverlayWithSlide(
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    var parentWidth by remember { mutableFloatStateOf(0f) }

    Box(
        Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                parentWidth = it.size.width.toFloat()
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        // 吸附动画，小于一半回弹，大于一半滑出
                        scope.launch {

                            if (offsetX.value > parentWidth / 2) {
                                // 只要超过一半就彻底隐藏
                                offsetX.animateTo(parentWidth, animationSpec = tween(300))
                            } else {
                                // 其他情况完全回弹
                                offsetX.animateTo(0f, animationSpec = tween(300))
                            }

                        }
                    },
                    onDrag = { change, dragAmount ->
                        val newX = (offsetX.value + dragAmount.x).coerceIn(0f, parentWidth)
                        scope.launch { offsetX.snapTo(newX) }
                    }
                )
            }
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
    ) {
        content()
    }
}

@Composable
fun FadeEdge(
    modifier: Modifier = Modifier,
    top: Boolean = true,
    height: Dp = 32.dp // 渐变高度可调
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = if (top)
                        listOf(Color.White.copy(alpha = 0.9f), Color.Transparent)
                    else
                        listOf(Color.Transparent, Color.White.copy(alpha = 0.9f))
                )
            )
    )
}

fun tickerFlow(period: Long): Flow<Unit> = flow {
    while (true) {
        emit(Unit)
        delay(period)
    }
}


