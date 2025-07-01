package com.example.compose

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter


class TestVideoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {

    val context = LocalContext.current

    MaterialTheme {
        Surface {

            Box(
                modifier = Modifier.fillMaxSize() // 使用 Box 实现分层布局
            ) {

                HeartLikeBox(
                    modifier = Modifier.fillMaxSize(),
                    imageRes = R.drawable.ic_heart
                ) {
                    // 第一层：视频内容（VideoScreen）
                    VideoScreen()
                }


                // 第二层：返回按钮
                IconButton(
                    onClick = {
                        // 点击返回按钮时执行 onBackPressed()
                        (context as? Activity)?.finish()
                    },
                    modifier = Modifier
                        .padding(top = 28.dp, start = 10.dp) // 设置按钮和屏幕边界的间距
                        .align(Alignment.TopStart) // 左上角对齐
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // 使用系统自带返回箭头图标
                        contentDescription = "Back",
                        tint = Color.White // 设置图标颜色为白色
                    )
                }
            }

        }
    }
}

@Composable
fun VideoScreen() {
    val context = LocalContext.current

    // 初始视频列表
    val initialVideoList = listOf(
        "https://jomin-web.web.app/resource/video/video_iu.mp4",
        "https://www.w3schools.com/html/movie.mp4",
        "https://media.w3.org/2010/05/sintel/trailer.mp4",
        "https://www.w3school.com.cn/example/html5/mov_bbb.mp4",
        "https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4",
        "https://stream7.iqilu.com/10339/upload_transcode/202002/09/20200209105011F0zPoYzHry.mp4",
        "https://stream7.iqilu.com/10339/upload_transcode/202002/09/20200209104902N3v5Vpxuvb.mp4",
        "https://static.ybhospital.net/test-video-2.mp4",
        "https://static.ybhospital.net/test-video-3.mp4",
        "https://static.ybhospital.net/test-video-4.mp4",
        "https://static.ybhospital.net/test-video-5.mp4",
        "https://static.ybhospital.net/test-video-6.mp4",

        "https://jomin-web.web.app/resource/video/video_iu.mp4",
        "https://www.w3schools.com/html/movie.mp4",
        "https://media.w3.org/2010/05/sintel/trailer.mp4",
        "https://www.w3school.com.cn/example/html5/mov_bbb.mp4",
        "https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4",
        "https://stream7.iqilu.com/10339/upload_transcode/202002/09/20200209105011F0zPoYzHry.mp4",
        "https://stream7.iqilu.com/10339/upload_transcode/202002/09/20200209104902N3v5Vpxuvb.mp4",
        "https://static.ybhospital.net/test-video-2.mp4",
        "https://static.ybhospital.net/test-video-3.mp4",
        "https://static.ybhospital.net/test-video-4.mp4",
        "https://static.ybhospital.net/test-video-5.mp4",
        "https://static.ybhospital.net/test-video-6.mp4",

    )
    val videoList = remember { mutableStateListOf(*initialVideoList.toTypedArray()) }

    // ExoPlayer 实例
    val exoPlayer = remember(context) { ExoPlayer.Builder(context).build() }

    // 屏幕高度
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // 滚动状态管理
    val listState = rememberLazyListState()

    // 当前播放的视频索引
    var currentPlayingIndex by remember { mutableIntStateOf(0) }

    // 动态加载：当用户滚动到最后一个视频时，添加新的视频
//    LaunchedEffect(remember { derivedStateOf { listState.firstVisibleItemIndex } }) {
//        if (listState.firstVisibleItemIndex == videoList.size - 1) {
//            videoList.addAll(initialVideoList) // 加载更多视频
//        }
//    }

    // 自动切换视频：当滑动到新页面时，播放对应视频
    LaunchedEffect(currentPlayingIndex) {
        if (currentPlayingIndex in videoList.indices) {
            val videoUri = videoList[currentPlayingIndex]
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    // 实现阻尼效果：监听滚动状态，切换当前播放的视频
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            // 获取第一个可见项的索引和偏移量
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val firstVisibleOffset = listState.firstVisibleItemScrollOffset

            // 判断如果超过全屏高度的一半，切换到下一页；否则回正当前页
            val targetIndex = if (firstVisibleOffset > screenHeight.value / 2) {
                firstVisibleIndex + 1
            } else {
                firstVisibleIndex
            }

            // 如果目标页与当前正在播放的页不同，则滑动过去
            if (currentPlayingIndex != targetIndex) {
                currentPlayingIndex = targetIndex // 更新当前播放页索引
                listState.animateScrollToItem(
                    index = targetIndex,
                    scrollOffset = 0 // 对齐到目标项顶部
                )
            }
        }
    }

    // 使用自动对齐行为
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = listState) // 自动对齐逻辑
    ) {
        itemsIndexed(videoList) { index, videoUri ->
            VideoItem(
                uri = videoUri,
                isSelected = index == currentPlayingIndex,
                exoPlayer = exoPlayer,
                screenHeight = screenHeight
            )
        }
    }
}

@Composable
fun VideoItem(uri: String, isSelected: Boolean, exoPlayer: ExoPlayer, screenHeight: Dp) {
    val avatarUrl = "https://c-ssl.duitang.com/uploads/item/201703/09/20170309211351_3eKNs.jpeg"

    // 状态：管理 loading 动画是否显示
    val isBuffering = remember { mutableStateOf(false) }

    // 监听视频播放状态
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        // 视频正在缓冲，显示加载动画
                        isBuffering.value = true
                    }

                    Player.STATE_READY -> {
                        // 视频已准备好（正在播放或可播放），隐藏加载动画
                        isBuffering.value = false
                    }

                    else -> isBuffering.value = false

                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    Box(
        modifier = Modifier
            .height(screenHeight)
            .fillMaxSize()
            .background(if (isSelected) Color.Black else Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {

        if (isSelected) {
            // 播放当前选中的视频
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false // 播放器控制器ui

                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // 非选中状态的视频项
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Video Preview",
                    color = Color.White
                )
            }
        }


        // 右边的功能区
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxHeight()
                .padding(end = 16.dp, bottom = 50.dp), // 距离右边缘的间距
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. 头像 + 添加关注按钮
            Box(
                modifier = Modifier
                    .height(68.dp) // 显式设置比头像更高的高度（56.dp头像 + 一半按钮 12.dp）
                    .wrapContentWidth(Alignment.CenterHorizontally) // 水平方向以内容宽度为准
            ) {
                // 圆形头像
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(56.dp) // 头像大小
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape) // 白色描边
                        .align(Alignment.TopCenter) // 头像在 Box 的顶部
                )

                // 关注按钮
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(24.dp) // 按钮大小
                        .clip(CircleShape)
                        .background(Color.Red) // 红色背景
                        .border(1.dp, Color.White, CircleShape) // 白色描边
                        .align(Alignment.BottomCenter)
                        .clickable {
                            println("=======> click 关注 ")
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add, // 替换为自定义加号图标
                        contentDescription = null,
                        tint = Color.White, // 图标颜色
                        modifier = Modifier.size(20.dp) // 图标大小
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. 点赞图标和数值
            ActionIconWithText(
                icon = Icons.Default.Favorite, // 你可以换成自定义的图标
                text = "123",
                iconColor = Color.Red,
                padding = 10.dp,
                0
            )
            // 3. 评论图标和数值
            ActionIconWithText(
                icon = Icons.Default.Comment,
                text = "456",
                iconColor = Color.White,
                padding = 10.dp,
                1
            )
            // 4. 收藏图标和数值
            ActionIconWithText(
                icon = Icons.Default.Star,
                text = "789",
                iconColor = Color.White,
                padding = 10.dp,
                2
            )
            // 5. 转发图标和数值
            ActionIconWithText(
                icon = Icons.Default.Share,
                text = "852",
                iconColor = Color.White,
                padding = 10.dp,
                3
            )

            // 6. 音乐封面旋转动画
            RotatingMusicCover(avatarUrl)
        }

        // 底部信息区域
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, end = 80.dp), // 距离左下角的间距
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "@一只猪",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "请注意#lazyListState是在#MessagesList方法中定义的默认值为rememberLazyListState()这是Compose中的一种常见模式。",
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "2025-3-1 下午15:10  IP属地:惠州",
                color = Color.LightGray,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(35.dp))

        }

        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            // 加载动画（显示/隐藏根据 `isBuffering` 状态）
            if (isBuffering.value) {
                TiktokLoadingAnimation() // 自定义加载动画
            }
        }


    }
}

@Composable
fun TiktokLoadingAnimation() {
    // 定义宽度变化的动画状态+
    val lineWidth = remember { Animatable(initialValue = 0f) } // 最初线宽为 0f
    val opacity = remember { Animatable(initialValue = 0f) }  // 最初不透明度为 0f

    // 获取屏幕宽度
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val initialWidth = screenWidth / 4 // 起始宽度: 屏幕的 1/5
    val expandedWidth = screenWidth * 4 / 5 // 展开后的宽度: 屏幕的 3/4

    // 使用 LaunchedEffect 触发动画循环
    LaunchedEffect(Unit) {
        while (true) {
            // 短线从无到有的显示
            lineWidth.animateTo(
                targetValue = initialWidth.value,
                animationSpec = tween(
                    durationMillis = 100 // 线条初始时渐显
                )
            )
            opacity.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 100 // 透明度同步渐显
                )
            )

            // 宽度向左右两端扩展（100ms，模拟弹性）
            lineWidth.animateTo(
                targetValue = expandedWidth.value,
                animationSpec = tween(
                    durationMillis = 400, // 明确设置为 100ms
                    easing = CubicBezierEasing(0.3f, 0.0f, 0.3f, 1.0f)
                )
            )

            // 快速淡出消失（100ms）
            opacity.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 100 // 渐隐动画
                )
            )

            // 重置动画
            lineWidth.snapTo(0f)
            opacity.snapTo(0f)
        }
    }

    // 加载动画 UI：底部中间动画的线条
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp), // 距离底部的间距
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(lineWidth.value.dp) // 动态宽度值
                .height(1.dp) // 白色线条高度
                .graphicsLayer(alpha = opacity.value) // 动态透明度
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f), // 渐变开始：透明
                            Color.White.copy(alpha = 0.8f), // 渐变中间：半透明
                            Color.White.copy(alpha = 0f) // 渐变结束：透明
                        )
                    ),
                    shape = RoundedCornerShape(2.dp) // 圆角边框形状
                )
        )
    }
}

@Composable
fun ActionIconWithText(icon: ImageVector, text: String, iconColor: Color, padding: Dp, index: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = padding)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    when (index) {
                        0 -> println("========> click 点赞")
                        1 -> println("========> click 评论")
                        2 -> println("========> click 收藏")
                        3 -> println("========> click 转发")
                    }
                }
        )
        Spacer(modifier = Modifier.height(4.dp)) // 图标和文字之间的间隔
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RotatingMusicCover(avatarUrl: String) {

    // 定义旋转动画
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing), // 6秒完成一圈
            repeatMode = RepeatMode.Restart
        )
    )

    // 显示圆形音乐封面
    Image(
        painter = rememberAsyncImagePainter(avatarUrl),
        contentDescription = "Music Cover",
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(2.dp, Color.White, CircleShape)
            .graphicsLayer(rotationZ = rotation)
    )
}