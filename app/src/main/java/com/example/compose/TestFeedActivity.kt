package com.example.compose

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Comment

import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter


import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter


class TestFeedActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigator()
        }
    }
}

data class FeedItem(
    val id: Int,
    val avatarUrl: String,
    val nickname: String,
    val text: String?,
    val images: List<String> = emptyList(),
    val video: String? = null
)

@Composable
fun FeedHeader(
    backgroundImage: String, // 头图 URL
    avatarImage: String,     // 用户头像 URL
    userName: String,        // 用户昵称
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp) // 设置头布局的高度
    ) {
        // 背景头图
        AsyncImage(
            model = backgroundImage,
            contentDescription = "Header Background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop // 避免图片拉伸
        )


        // 用户头像和昵称（右下角）
        UserInfo(
            avatarImage = avatarImage,
            userName = userName,
            modifier = Modifier
                .align(Alignment.BottomEnd) // 对齐到右下角
                .padding(16.dp) // 内边距
        )
    }
}

@Composable
fun UserInfo(
    avatarImage: String,
    userName: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ) {
        // 用户昵称
        Text(
            text = userName,
            color = Color.White, // 显示白色文本
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.width(8.dp)) // 添加头像与昵称的间距
        // 用户头像
        AsyncImage(
            model = avatarImage,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(60.dp) // 设置头像大小
                .clip(RoundedCornerShape(16)) // 圆形头像
                .border(2.dp, Color.White, RoundedCornerShape(16)) // 白色边框
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
fun FeedPage(navController: NavController) {
    val feedItems = remember { generateFeedData() }

    val context = LocalContext.current
    val isLoading = remember { mutableStateOf(false) } // 控制 Loading 显示
    val listState = rememberLazyListState()           // 列表状态

    val currentPlayIndex = rememberSaveable { mutableIntStateOf(-1) } // 保存当前播放视频的索引

    // 创建一个 ExoPlayer 实例，并监听其状态
    val exoPlayer = rememberExoPlayer(context) { isReady ->
        isLoading.value = !isReady // 根据播放器状态切换播放/缓冲状态
    }

    // 优化滚动监听逻辑
    LaunchedEffect(listState.firstVisibleItemIndex, listState.layoutInfo) {
        // 获取可见范围内的所有列表项
        val visibleItems = listState.layoutInfo.visibleItemsInfo

        // 定义 Feed 列表的起始位置（第 1 项开始）
        val feedItemsStartIndex = 1

        // 找到第一个可见的视频项在整个 LazyColumn 中的全局索引
        val firstVisibleVideoIndex = visibleItems.map { it.index }
            .firstOrNull { index ->
                // 排除头布局，确保索引 >= feedItemsStartIndex 且 feedItems 有视频内容
                index >= feedItemsStartIndex && feedItems[index - feedItemsStartIndex].video != null
            }

        // 更新播放器逻辑
        if (firstVisibleVideoIndex != null && firstVisibleVideoIndex != currentPlayIndex.value) {
            // 计算视频在 feedItems 中的实际索引
            val videoIndex = firstVisibleVideoIndex - feedItemsStartIndex
            val videoItem = feedItems[videoIndex] // 获取视频数据

            // 更新当前播放索引
            currentPlayIndex.value = firstVisibleVideoIndex

            // 显示加载指示器
            isLoading.value = true

            // 设置 ExoPlayer 视频源并准备播放
            exoPlayer.apply {
                setMediaItem(MediaItem.fromUri(videoItem.video!!))
                repeatMode = Player.REPEAT_MODE_ALL
                prepare()
                playWhenReady = true
            }
        }

        // 当前播放的视频滑出屏幕范围时，暂停并释放资源
        if (firstVisibleVideoIndex == null || !visibleItems.any { it.index == currentPlayIndex.value }) {
            exoPlayer.apply {
                pause()          // 暂停播放
                clearMediaItems() // 清空正在播放的视频资源
            }
            currentPlayIndex.value = -1 // 重置当前播放的索引
        }
    }



    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {

            // 添加头布局作为列表的第一个项
            item {
                FeedHeader(
                    backgroundImage = "https://www.veryol.com/uploads/rss_imgs/s_c404d548b3b644e690d7ace8e7d6d93f.jpg",
                    avatarImage = "https://tse3-mm.cn.bing.net/th/id/OIP-C.eQPvuWmUtxZRLNrUGJK6wQHaHa?rs=1&pid=ImgDetMain",
                    userName = "UserName",
                )
            }

            itemsIndexed(feedItems) { index, feedItem ->
                // 根据当前索引与 `currentPlayIndex.value` 判断是否是播放项
                val isPlaying = index == currentPlayIndex.value - 1
                FeedItemView(
                    feedItem = feedItem,
                    exoPlayer = exoPlayer,
                    isPlaying = isPlaying,
                    navController = navController,
                    isLoading = isLoading.value && isPlaying // Loading 仅在当前播放项生效
                )
            }
        }


        // 左上角返回按钮
        IconButton(
            onClick = {
                (context as? Activity)?.finish()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 28.dp, start = 10.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back Button",
                tint = Color.White // 设置白色按钮，适配深色背景
            )
        }

        // 右上角相机按钮
        IconButton(
            onClick = {
                println("=========> post feed")
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Camera Icon",
                tint = Color.White
            )
        }

    }


}


@Composable
fun rememberExoPlayer(
    context: Context,
    onPlaybackStateChanged: (Boolean) -> Unit // 用于通知缓冲和播放状态的变更
): ExoPlayer {
    val exoPlayer = remember { ExoPlayer.Builder(context).build() }

    DisposableEffect(Unit) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                // 播放器状态改变时通知 Compose
                when (state) {
                    Player.STATE_BUFFERING -> onPlaybackStateChanged(false) // 缓冲中
                    Player.STATE_READY -> onPlaybackStateChanged(true)      // 准备就绪，开始播放
                    else -> onPlaybackStateChanged(false)                   // 其他状态，例如暂停
                }
            }
        }

        exoPlayer.addListener(listener) // 添加状态监听器

        onDispose {
            exoPlayer.release()
            exoPlayer.removeListener(listener)
        }
    }

    return exoPlayer
}

@Composable
fun FeedItemView(
    feedItem: FeedItem,
    navController: NavController,
    exoPlayer: ExoPlayer,
    isPlaying: Boolean,
    isLoading: Boolean // 用于控制当前视频的加载动画
) {
    Card(modifier = Modifier.padding(10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            // Header: Avatar and Nickname
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(feedItem.avatarUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = feedItem.nickname,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("下午 17:16")
                            withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 12.sp)) {
                                append("  来自 ")
                            }
                            withStyle(style = SpanStyle(color = Color.Blue, fontSize = 12.sp)) {
                                append("iphone16 pro max")
                            }
                        },
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                }
            }

            feedItem.text?.let {
                Text(
                    text = it, fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Content: Images or Video
            if (feedItem.images.isNotEmpty()) {
                ImageGrid(
                    images = feedItem.images,
                    onImageClick = { imageIndex ->
                        navController.navigate(
                            "image/${feedItem.id}/${imageIndex}"
                        )
                    }
                )
            } else if (feedItem.video != null) {
                // 视频内容
                VideoFeedItemView(
                    navController = navController,
                    feedItem = feedItem,
                    exoPlayer = exoPlayer,
                    isPlaying = isPlaying,
                    isLoading = isLoading,
                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            // 操作按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 点赞按钮
                ActionButton(
                    icon = Icons.Default.ThumbUp,  // 使用内置的点赞图标
                    text = "Like",
                    onClick = { println("=====> Liked +1") }
                )
                // 评论按钮
                ActionButton(
                    icon = Icons.Default.Comment,  // 评论图标
                    text = "Comment",
                    onClick = { println("=====> Commented +1") }
                )
                // 转发按钮
                ActionButton(
                    icon = Icons.Default.Share,  // 转发图标
                    text = "Share",
                    onClick = { println("=====> Shared +1") }
                )
            }
        }
    }
}


@Composable
fun VideoFeedItemView(
    exoPlayer: ExoPlayer,
    navController: NavController,
    feedItem: FeedItem,
    isPlaying: Boolean,
    isLoading: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.Black)
            .clickable {
                navController.navigate("video/${feedItem.id}")
            }
    ) {
        if (isPlaying) {
            // 如果是当前播放视频项，则绑定 ExoPlayer
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false // 隐藏播放器控制器
                    }
                },
                modifier = Modifier.matchParentSize()
            )

            // 缓冲状态时显示 Loading 动画
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        } else {
            // 如果不是当前播放的视频项，显示封面占位图
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Video Placeholder",
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun ActionButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 4.dp),
            color = Color.Gray
        )
    }
}


@Composable
fun ImageGrid(images: List<String>, onImageClick: (Int) -> Unit) {
    val chunkedImages = images.chunked(3) // 每行 3 张图片
    Column(modifier = Modifier.fillMaxWidth()) {
        chunkedImages.forEachIndexed { rowIndex, rowImages ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowImages.forEachIndexed { columnIndex, imageUrl ->
                    // 全局索引计算
                    val globalIndex = rowIndex * 3 + columnIndex
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clickable { onImageClick(globalIndex) },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun FullScreenImageView(feedId: Int, imageIndex: Int, navController: NavController) {
    val feedItem = generateFeedData().first { it.id == feedId }
    val imageUrl = feedItem.images[imageIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { navController.popBackStack() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        IconButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier
                .padding(top = 28.dp, start = 10.dp) // 设置按钮和屏幕边界的间距
                .align(Alignment.TopStart) // 左上角对齐
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, // 使用系统自带返回箭头图标
                contentDescription = "Back",
                tint = Color.Black // 设置图标颜色为白色
            )
        }
    }
}

@Composable
fun FullScreenVideoView(feedId: Int, navController: NavController) {
    val feedItem = generateFeedData().first { it.id == feedId }
    val videoUrl = feedItem.video!!

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { navController.popBackStack() },
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = {
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = {
                navController.popBackStack()
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

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "feed") {
        composable("feed") {
            FeedPage(navController = navController)
        }
        composable(
            "image/{feedId}/{imageIndex}",
            arguments = listOf(
                navArgument("feedId") { type = NavType.IntType },
                navArgument("imageIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val feedId = backStackEntry.arguments!!.getInt("feedId")
            val imageIndex = backStackEntry.arguments!!.getInt("imageIndex")
            FullScreenImageView(
                feedId = feedId,
                imageIndex = imageIndex,
                navController = navController
            )
        }
        composable(
            "video/{feedId}",
            arguments = listOf(navArgument("feedId") { type = NavType.IntType })
        ) { backStackEntry ->
            val feedId = backStackEntry.arguments!!.getInt("feedId")
            FullScreenVideoView(feedId = feedId, navController = navController)
        }
    }
}

fun generateFeedData(): List<FeedItem> {
    val avatarUrl = "https://c-ssl.duitang.com/uploads/item/201703/09/20170309211351_3eKNs.jpeg"
    val exampleImages = listOf(
        "https://ts1.cn.mm.bing.net/th/id/R-C.f53e729846f226805d024614f131a35a?rik=UNhpy6HlpeTm1w&riu=http%3a%2f%2fnews.cjn.cn%2fcsqpd%2fwh_20004%2f202312%2fW020231228698551919727.png&ehk=g4pM%2fJts6jDISOt6wqWgtBXFQlrX3F8DF4BiS2hPPGU%3d&risl=&pid=ImgRaw&r=0",
        "https://doc-fd.zol-img.com.cn/t_s2000x2000/g7/M00/0F/0F/ChMkK2aiqk6IDBbNAAMnLO0CCIoAAhApQB15M0AAydE825.jpg",
        "https://img.pcauto.com.cn/images/upload/upc/tx/auto5/2407/19/c10/434290660_1721398493214.jpg",
        "https://tse1-mm.cn.bing.net/th/id/OIP-C.0YlNEcHIQtMBgcRDqmLHQwHaHa?rs=1&pid=ImgDetMain",
        "https://img.pcauto.com.cn/images/upload/upc/tx/auto5/2410/31/c6/460149189_1730358940439_800x600.jpg",
        "https://www.dingdanghao.com/wp-content/uploads/2024/07/s_067ab6713d974189aa932b93282fda84.jpg",
        "https://www.veryol.com/uploads/rss_imgs/s_c404d548b3b644e690d7ace8e7d6d93f.jpg",
        "https://file.moyublog.com/free_wallpapers_titlepic/anodwq.jpg",
        "https://img.pcauto.com.cn/images/upload/upc/tx/auto5/2407/19/c10/434290659_1721398491560.jpg",
    )
    val txt =
        "这是一段示例文字\n 在 Jetpack Compose 中，可以使用 AnnotatedString 和 Text 组合来实现富文本样式，即使单个文字内容具有不同颜色、大小或其他样式。"

    return listOf(
        FeedItem(
            id = 0,
            avatarUrl = avatarUrl,
            nickname = "User $0",
            text = txt,
            images = emptyList(),
            video = null
        ),
        FeedItem(
            id = 1,
            avatarUrl = avatarUrl,
            nickname = "User $1",
            text = txt,
            images = exampleImages.take(1),
            video = null
        ),
        FeedItem(
            id = 2,
            avatarUrl = avatarUrl,
            nickname = "User $2",
            text = txt,
            images = emptyList(),
            video = "https://www.w3school.com.cn/example/html5/mov_bbb.mp4"
        ),
        FeedItem(
            id = 3,
            avatarUrl = avatarUrl,
            nickname = "User $3",
            text = txt,
            images = exampleImages.take(2),
            video = null
        ),
        FeedItem(
            id = 4,
            avatarUrl = avatarUrl,
            nickname = "User $4",
            text = txt,
            images = exampleImages.take(3),
            video = null
        ),
        FeedItem(
            id = 5,
            avatarUrl = avatarUrl,
            nickname = "User $5",
            text = txt,
            images = exampleImages.take(4),
            video = null
        ),
        FeedItem(
            id = 6,
            avatarUrl = avatarUrl,
            nickname = "User $6",
            text = txt,
            images = exampleImages.take(5),
            video = null
        ),
        FeedItem(
            id = 7,
            avatarUrl = avatarUrl,
            nickname = "User $7",
            text = txt,
            images = emptyList(),
            video = "https://jomin-web.web.app/resource/video/video_iu.mp4"
        ),
        FeedItem(
            id = 8,
            avatarUrl = avatarUrl,
            nickname = "User $8",
            text = txt,
            images = exampleImages.take(6),
            video = null
        ),
        FeedItem(
            id = 9,
            avatarUrl = avatarUrl,
            nickname = "User $9",
            text = txt,
            images = exampleImages.take(7),
            video = null
        ),
        FeedItem(
            id = 10,
            avatarUrl = avatarUrl,
            nickname = "User $10",
            text = txt,
            images = exampleImages.take(8),
            video = null
        ),
        FeedItem(
            id = 11,
            avatarUrl = avatarUrl,
            nickname = "User $11",
            text = txt,
            images = exampleImages.take(9),
            video = null
        ),

        )


}

