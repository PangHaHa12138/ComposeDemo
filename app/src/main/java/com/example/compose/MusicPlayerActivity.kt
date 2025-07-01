package com.example.compose

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

import kotlinx.coroutines.delay

class MusicPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerScreen()
        }
    }
}

//=======================

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MusicPlayerScreen() {

    val context = LocalContext.current

    // 歌曲列表（只需填写raw与对应信息，多首均可）
    data class Song(
        val name: String,
        val artist: String,
        val coverRes: Int,
        @androidx.annotation.RawRes val rawRes: Int
    )

    val songList = listOf(
        Song("最后一页", "江语晨", R.drawable.cover_demo, R.raw.music_demo1),
        Song("跳楼机", "LBI利比", R.drawable.cover_demo2, R.raw.music_demo2),
        Song("忘不掉的你", "h3R3", R.drawable.cover_demo3, R.raw.music_demo3),
        // 如需添加更多，继续添加
    )
    var songIndex by remember { mutableIntStateOf(0) }
    val songCount = songList.size
    val curSong = songList[songIndex]
    // 1.播放器
    val exoPlayer = rememberExoPlayerMutable(context, curSong.rawRes)
    val isPlaying = remember { mutableStateOf(false) }
    val totalDuration = remember { mutableLongStateOf(0L) }
    val currentProgress = remember { mutableFloatStateOf(0f) }
    val currentTime = remember { mutableLongStateOf(0L) }

    // 黑胶
    val discRes = R.drawable.ic_disc
    // 唱针
    val needleRes = R.drawable.ic_needle3
    // 黑胶背景
    val discBackground = R.drawable.ic_disc_blackground

    //列表循环
    DisposableEffect(exoPlayer, songIndex) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    // 列表循环
                    songIndex = if (songIndex < songCount - 1) songIndex + 1 else 0
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose { exoPlayer.removeListener(listener) }
    }

    // 播放监听：进度等
    LaunchedEffect(exoPlayer, isPlaying.value, songIndex) {
        while (true) {
            totalDuration.longValue = exoPlayer.duration.coerceAtLeast(1L)
            currentTime.longValue = exoPlayer.currentPosition
            currentProgress.floatValue = if (totalDuration.longValue > 0)
                exoPlayer.currentPosition.toFloat() / totalDuration.longValue else 0f
            delay(100)
        }
    }

    // 自动播放、记得关闭单曲循环
    LaunchedEffect(exoPlayer, songIndex) {
        exoPlayer.repeatMode = Player.REPEAT_MODE_OFF
        exoPlayer.playWhenReady = true
        isPlaying.value = true
    }


    var diskRotation by remember { mutableFloatStateOf(0f) }
    val rotationSpeed = 0.042f // 每帧递增度数，可调（速度）

    // 动画：黑胶旋转，唱针旋转
    LaunchedEffect(isPlaying.value) {
        while (isPlaying.value) {
            diskRotation += rotationSpeed * 16    // 16是大致每帧毫秒
            if (diskRotation > 360f) diskRotation -= 360f
            delay(16)
        }
        // 这里不处理归零，保持在当前角度，暂停状态
    }

    val needleAngle by animateFloatAsState(
        targetValue = if (isPlaying.value) 0f else -25f, // 靠近时0，离开-25
        animationSpec = tween(500)
    )

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val reservedBottomSpace = 300.dp // 为底部控制区/信息区预留区域空间

    // 1. 唱盘外框最大直径 = 比例（如68%）* 屏幕宽，不超过 屏幕高-底部栏
    val maxDiscSize = screenWidth * 0.88f
    val maxDiscHeight = screenHeight - reservedBottomSpace
    val discSize = if (maxDiscSize < maxDiscHeight) maxDiscSize else maxDiscHeight

    // 2. 细分
    val backgroundSize = discSize * 0.98f           // 背景直径=外框约98%
    val blackDiscSize = discSize * 0.93f            // 黑胶盘直径=外框约93%
    val coverSize = discSize * 0.6f                 // 封面直径=黑胶盘约60%
    val needleWidth = discSize * 0.32f              // 唱针宽
    val needleHeight = needleWidth * 1.8f           // 唱针高
    val needleOffsetX = discSize * 0.14f            // 唱针X方向偏移
    val needleOffsetY = -needleHeight * 0.55f       // 唱针Y方向偏移

    // ========= UI ============

    Box(
        Modifier
            .fillMaxSize()
    ) {
        // 背景封面高斯 + 遮罩
        Image(
            painter = painterResource(id = curSong.coverRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(45.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x66000000))
        )
        // 顶部Bar
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                /*返回*/
                (context as? Activity)?.finish()
                (context as? Activity)?.overridePendingTransition(0, R.anim.slide_out_bottom)

            }) {
                Icon(Icons.Default.ArrowBackIosNew, null, tint = Color.White)
            }
            IconButton(onClick = {
                /*分享*/
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "音乐分享")
                intent.putExtra(Intent.EXTRA_TEXT, "我正在听好听的歌曲“DEMO歌曲名”，推荐给你！")
                context.startActivity(Intent.createChooser(intent, "分享音乐到..."))

            }) {
                Icon(Icons.Default.Share, null, tint = Color.White)
            }
        }

        // =========== 优化黑胶盘与唱针合成区（自适应屏幕） ===========

        Box(
            Modifier
                .align(Alignment.Center)
                .size(discSize)
                .offset(y = -discSize * 0.15f)
        ) {
            // 黑胶盘合成体
            Box(
                Modifier
                    .align(Alignment.Center)
                    .size(backgroundSize)
                    .graphicsLayer { rotationZ = diskRotation }
            ) {
                // 黑胶盘背景
                Image(
                    painter = painterResource(id = discBackground),
                    contentDescription = null,
                    modifier = Modifier.matchParentSize()
                )
                // 黑胶盘
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .size(blackDiscSize)

                ) {
                    Image(
                        painter = painterResource(id = discRes),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )
                }
                // 封面
                Box(
                    modifier = Modifier
                        .size(coverSize)
                        .align(Alignment.Center)
                        .clip(CircleShape)
                        .background(Color.White, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = curSong.coverRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                }
            }
            // 唱针
            Image(
                painter = painterResource(id = needleRes),
                contentDescription = null,
                modifier = Modifier
                    .size(width = needleWidth, height = needleHeight)
                    .align(Alignment.TopCenter)
                    .offset(x = needleOffsetX, y = needleOffsetY)
                    .graphicsLayer {
                        rotationZ = needleAngle
                        transformOrigin = TransformOrigin(0.12f, 0.13f)
                    }
                    .zIndex(2f)
            )
        }

        var isLiked by remember { mutableStateOf(false) }
        var likeCount by remember { mutableIntStateOf(520) }
        var commentCount by remember { mutableIntStateOf(999) }


        // 下部信息
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Row(
                Modifier
                    .fillMaxWidth(0.88f)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 左边歌曲名+艺术家
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        curSong.name,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        curSong.artist,
                        color = Color.White.copy(alpha = 0.78f),
                        fontSize = 15.sp,
                        maxLines = 1
                    )
                }
                // 右侧点赞/评论按钮+数量
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            isLiked = !isLiked
                            likeCount += if (isLiked) 1 else -1
                        }
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (isLiked) Color(0xFFD83B67) else Color.White
                        )
                    }
                    Text(
                        likeCount.toString(),
                        color = Color.White,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    IconButton(onClick = { /* 评论点击 */ }) {
                        Icon(
                            Icons.Default.Comment,
                            contentDescription = "Comment",
                            tint = Color.White
                        )
                    }
                    Text(
                        commentCount.toString(),
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            // 播放进度
            Row(
                Modifier.fillMaxWidth(0.85f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    formatTime(currentTime.longValue),
                    color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp
                )
                Slider(
                    value = currentProgress.floatValue,
                    onValueChange = {
                        currentProgress.floatValue = it
                    },
                    onValueChangeFinished = {
                        exoPlayer.seekTo(
                            (currentProgress.floatValue * totalDuration.longValue).toLong()
                                .coerceAtLeast(0)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    )
                )
                Text(
                    formatTime(totalDuration.longValue),
                    color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp
                )
            }
            Spacer(Modifier.height(8.dp))
            // 播放控制
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    // 上一曲
                    songIndex = if (songIndex > 0) songIndex - 1 else songCount - 1
                }) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = {
                    // 播放/暂停
                    if (exoPlayer.isPlaying) exoPlayer.pause()
                    else exoPlayer.play()
                    isPlaying.value = exoPlayer.isPlaying
                }) {
                    Icon(
                        if (isPlaying.value) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        null, tint = Color.White, modifier = Modifier.size(66.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = {
                    // 下一曲
                    songIndex = if (songIndex < songCount - 1) songIndex + 1 else 0
                }) {
                    Icon(
                        Icons.Default.SkipNext,
                        null,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Spacer(Modifier.height(42.dp))
        }
    }
}

// ============= 工具代码 ================

// 快速格式化时间
private fun formatTime(ms: Long): String {
    val totalSec = (ms / 1000).toInt()
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%02d:%02d".format(min, sec)
}

// 拿到可自动释放的ExoPlayer
@Composable
fun rememberExoPlayer(context: Context, @androidx.annotation.RawRes rawRes: Int): ExoPlayer {
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }
    DisposableEffect(Unit) {
        // 资源
        val mediaItem = MediaItem.fromUri("rawresource://${context.packageName}/$rawRes")
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        onDispose {
            exoPlayer.release()
        }
    }
    return exoPlayer
}

// ExoPlayer记住每一首资源
@Composable
fun rememberExoPlayerMutable(context: Context, @androidx.annotation.RawRes rawRes: Int): ExoPlayer {
    val exoPlayer = remember(rawRes) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("rawresource://${context.packageName}/$rawRes")
            setMediaItem(mediaItem)
            prepare()
        }
    }
    DisposableEffect(exoPlayer) { onDispose { exoPlayer.release() } }
    return exoPlayer
}