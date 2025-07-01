package com.example.compose

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ModalDrawer
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Drafts
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon

import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TestRedBookActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RedBookApp()
        }
    }
}


@Composable
fun RedBookApp() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.White,
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(width = 50.dp, height = 50.dp),
                onClick = { navController.navigate("publish") },
                containerColor = Color.Red,
                elevation = FloatingActionButtonDefaults.loweredElevation()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "发布",
                    tint = Color.White
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true

    ) { innerPadding ->
        NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(navController)
        }
        composable("popular") {
            SetSystemBarIcons(darkIcons = false)
            VideoScreen()
        }
        composable("messages") {
            SetSystemBarIcons(darkIcons = true)
            MessagesScreen()
        }
        composable("profile") {
            ProfileScreen(navController)

        }
        composable("publish") {
            PlaceholderScreen("发布")
        }
        composable("search") {
            SearchPage()
        }
        composable("detail") {
            XiaohongshuDetailPage()
        }
    }
}

@Composable
fun PlaceholderScreen(label: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(label)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {

    val tabs = listOf(
        NavigationItem("首页", "home", Icons.Default.Home),
        NavigationItem("热门", "popular", Icons.Default.Whatshot),
        NavigationItem("消息", "messages", Icons.Default.Email),
        NavigationItem("我", "profile", Icons.Default.Person)
    )

    // 实时获取回退栈的当前目标route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        backgroundColor = Color.White
    ) {
        tabs.forEach { item ->
            val selected = currentRoute == item.route
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    // 使用 NavController 的 navigate 方法，避免历史堆叠
                    // 每次切换 Tab 时，清除其他 Tab 的历史
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) Color.Black else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (selected) Color.Black else Color.Gray
                    )
                },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray
            )
        }
    }

}

data class NavigationItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

@Composable
fun SetSystemBarIcons(darkIcons: Boolean) {
    val view = LocalView.current

    DisposableEffect(view, darkIcons) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.systemUiVisibility =
                if (darkIcons) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                else View.SYSTEM_UI_FLAG_VISIBLE
        }
        onDispose {}
    }
}


@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState = drawerState)
    val scope = rememberCoroutineScope() // 用于启动协程

    SetSystemBarIcons(darkIcons = true)

    ModalDrawer(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        drawerState = drawerState,
        drawerElevation = 0.dp,
        drawerBackgroundColor = Color.White,
        drawerContent = {
            DrawerContent(onCloseDrawer = {
                println("=====> onCloseDrawer ")
            })
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp),
            backgroundColor = Color.Transparent,
            scaffoldState = scaffoldState,
        ) { paddingValues ->
            HomeTabs(Modifier.padding(paddingValues), drawerState, navController)
        }
    }
}

@Composable
fun DrawerContent(
    onCloseDrawer: () -> Unit // 关闭抽屉回调
) {

// 上部功能项
    val menuItems = listOf(
        "发现好友" to Icons.Default.PersonAdd,
        "创作者中心" to Icons.Default.Create,
        "我的草稿" to Icons.Default.Drafts,
        "我的评论" to Icons.Default.Comment,
        "浏览记录" to Icons.Default.History,
        "订单" to Icons.Default.ShoppingCart,
        "购物车" to Icons.Default.AddShoppingCart,
        "钱包" to Icons.Default.AccountBalanceWallet,
        "社区公约" to Icons.Default.Policy
    )

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.White) // 设置抽屉背景色
            .padding(16.dp) // 设置页面内边距
    ) {
        // 滚动部分
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth() // 宽度填满可用空间
                .padding(bottom = 100.dp, top = 20.dp) // 为底部按钮预留空间
        ) {
            items(menuItems) { menuItem ->
                DrawerItem(icon = menuItem.second, text = menuItem.first)
                Spacer(modifier = Modifier.height(10.dp)) // 每项间隙
            }
        }

        // 固定部分：底部按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // 固定在 Box 底部
                .padding(vertical = 16.dp), // 给底部按钮适当的上下内边距
            horizontalArrangement = Arrangement.SpaceBetween // 平均分布按钮
        ) {
            BottomCircleMenu(icon = Icons.Default.QrCode, text = "扫一扫")
            BottomCircleMenu(icon = Icons.Default.Help, text = "帮助与客服")
            BottomCircleMenu(icon = Icons.Default.Settings, text = "设置")
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector, // 图标
    text: String       // 文字
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp)) // 圆角背景
            .background(Color(0xFFF5F5F5)) // 背景颜色
            .clickable {

            } // 点击操作
            .padding(horizontal = 8.dp, vertical = 8.dp), // 内容内边距
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp)) // 图标与文字间距
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = Color.Black
        )
    }
}

@Composable
fun BottomCircleMenu(
    icon: ImageVector,  // 图标
    text: String        // 文本
) {
    Column(
        modifier = Modifier
            .padding(bottom = 60.dp, start = 20.dp, end = 20.dp)
            .clickable { /* 点击事件 */ },
        horizontalAlignment = Alignment.CenterHorizontally // 图标和文字居中对齐
    ) {
        // 图标的圆形背景
        Box(
            modifier = Modifier
                .size(40.dp) // 圆形大小
                .clip(CircleShape) // 圆形
                .background(Color.LightGray), // 背景颜色
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp)) // 图标与文字间距
        Text(
            text = text,
            style = MaterialTheme.typography.caption,
            color = Color.Black
        )
    }
}

@Composable
fun HomeTabs(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    navController: NavController
) {
    // 数据源，包含 3 个 Tab 标签
    val tabs = listOf("关注", "发现", "附近")

    // 使用最新的 rememberPagerState API，其中 pageCount 是一个 lambda 表达式
    val pagerState = rememberPagerState(pageCount = { tabs.size })

    // 用于切换 Tab 和 PagerState 页面的协程
    val scope = rememberCoroutineScope()


    Column(modifier = modifier.fillMaxSize()) {
        // 顶部的 Tab Row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {

            ScrollableCenteredTabRow(
                tabs = tabs,
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = { tabIndex ->
                    // 点击 Tab 时，滚动到对应页面
                    scope.launch {
                        pagerState.animateScrollToPage(tabIndex)
                    }
                }
            )

            IconButton(
                modifier = Modifier.align(Alignment.TopStart),
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }) {
                Icon(Icons.Default.Menu, contentDescription = "菜单")
            }

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    navController.navigate("search")
                }) {
                Icon(Icons.Default.Search, contentDescription = "搜索")
            }

        }

        // 水平方向的 Pager，用于页面滑动
        HorizontalPager(
            state = pagerState,                 // Pager 的状态
            modifier = Modifier.fillMaxSize(),  // 填充整个页面
            verticalAlignment = Alignment.Top,  // 设置页面内内容的垂直对齐方式
            pageSpacing = 8.dp                  // 设置页面间距
        ) { page ->
            // 根据对应的页码加载不同的内容
            when (page) {
                0 -> WaterfallListWithPullToRefresh(navController) // 瀑布流第一页：推荐
                1 -> WaterfallListWithPullToRefresh(navController) // 瀑布流第二页：关注
                2 -> WaterfallListWithPullToRefresh(navController) // 瀑布流第三页：附近
            }
        }
    }
}

@Composable
fun ScrollableCenteredTabRow(
    tabs: List<String>,          // Tab 列表
    selectedTabIndex: Int,       // 当前选中的 Tab 索引
    onTabSelected: (Int) -> Unit // Tab 点击事件回调
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent), // 设置背景色，可以根据需求变更
        horizontalArrangement = Arrangement.Center // Tabs 居中对齐
    ) {
        items(tabs.size) { index ->
            Column(
                modifier = Modifier
                    .padding(10.dp) // 添加 padding 方便指示条与文字间距
                    .wrapContentWidth(), // 包裹内容宽度
                horizontalAlignment = Alignment.CenterHorizontally // 居中对齐 Tab 和指示条
            ) {
                // Tab 文本按钮
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) }
                ) {
                    Text(
                        text = tabs[index],
                        color = if (selectedTabIndex == index) Color.Black else Color.Gray,
                        fontSize = 14.sp
                    )
                }

                // 自定义指示条 (仅在选中时显示)
                if (selectedTabIndex == index) {
                    Box(
                        modifier = Modifier
                            .padding(top = 5.dp)
                            .height(2.dp) // 指示条高度
                            .width(25.dp) // 指示条宽度
                            .clip(RoundedCornerShape(2.dp)) // 圆角矩形
                            .background(Color.Red) // 可自定义指示条颜色
                    )
                } else {
                    Spacer(modifier = Modifier.height(2.dp)) // 创建占位空间，防止布局抖动
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WaterfallListWithPullToRefresh(navController: NavController) {
    // 用于存储数据
    val items = remember { mutableStateOf(generateFakeData(20)) }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    var itemCount by remember { mutableStateOf(15) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        itemCount += 5
        refreshing = false
    }

    val state = rememberPullRefreshState(refreshing, ::refresh)

    Box(
        Modifier
            .pullRefresh(state)
            .fillMaxWidth()
    ) {
        LazyVerticalGrid(
            modifier = Modifier.background(color = Color(0xfff6f6f6)),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(items.value) { item ->
                WaterfallItem(item, navController)
            }
        }


        PullRefreshIndicator(
            refreshing,
            state,
            Modifier.align(Alignment.TopCenter),
            contentColor = Color.Blue
        )
    }
}


@Composable
fun WaterfallItem(item: ItemData, navController: NavController) {

    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth().clickable { navController.navigate("detail") }
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.height(6.dp))
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = item.description,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2
            )
            Spacer(Modifier.height(2.dp))
            Row(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = item.avatarUrl,
                    contentDescription = "用户头像",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.username,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {}) {
                    Icon(Icons.Default.FavoriteBorder, null)
                }
                Text(
                    text = item.likes.toString(),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}

@Composable
fun MessagesScreen() {
    // 一个上下一体可滚动的页面
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp)
            .background(Color(0xfff6f6f6)), // 背景色
        verticalArrangement = Arrangement.spacedBy(1.dp) // 每个小区域之间的间距
    ) {
        // 添加顶部按钮
        item {
            MessageTopButtons()
        }

        // 聊天记录的假数据块
        items(chatRecords) { chat ->
            ChatRecordCell(chat)
        }

        // 标题“你可能感兴趣的人”
        item {
            SectionTitle("你可能感兴趣的人")
        }

        // 推荐关注区域的假数据块
        items(recommendations) { recommendation ->
            RecommendationCell(recommendation)
        }
    }
}

@Composable
fun MessageTopButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 5.dp, horizontal = 12.dp), // 设置内边距
        horizontalArrangement = Arrangement.SpaceEvenly, // 按钮水平均匀分布
        verticalAlignment = Alignment.CenterVertically // 垂直居中对齐
    ) {
        // Horizontal 3 Buttons (赞和收藏、新增关注、评论和@)
        TopButton(
            icon = Icons.Default.Favorite,
            label = "赞和收藏",
            backgroundColor = Color(0xFFFFE6E6), // 浅粉色背景
            iconColor = Color(0xFFFF4081)        // 粉色图标
        )
        TopButton(
            icon = Icons.Default.PersonAdd,
            label = "新增关注",
            backgroundColor = Color(0xFFE6F3FF), // 浅蓝色背景
            iconColor = Color(0xFF2196F3)        // 蓝色图标
        )
        TopButton(
            icon = Icons.Default.Comment,
            label = "评论和@",
            backgroundColor = Color(0xFFE6FFE6), // 浅绿色背景
            iconColor = Color(0xFF4CAF50)        // 绿色图标
        )
    }
}

@Composable
fun TopButton(
    icon: ImageVector,
    label: String,
    backgroundColor: Color,
    iconColor: Color
) {
    // 每个按钮的整体布局
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // 图标和文字居中
        modifier = Modifier
            .clickable { /* 点击事件处理 */ }
    ) {
        // 图标圆角背景
        Box(
            modifier = Modifier
                .size(50.dp) // 圆角背景的大小
                .clip(RoundedCornerShape(30)) // 圆形
                .background(backgroundColor), // 设置背景颜色
            contentAlignment = Alignment.Center // 图标居中
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor, // 图标颜色
                modifier = Modifier.size(30.dp) // 图标大小
            )
        }
        Spacer(modifier = Modifier.height(8.dp)) // 图标和文本之间的间距
        // 按钮文字
        Text(
            text = label,
            style = MaterialTheme.typography.body2,
            color = Color.Black
        )
    }
}

@Composable
fun ChatRecordCell(chat: ChatRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp), // 外边距
        verticalAlignment = Alignment.CenterVertically // 内容垂直居中
    ) {
        AsyncImage(
            model = chat.avatarRes,
            contentDescription = "用户头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp)) // 左侧头像与中间内容的间距

        // 中间：昵称和文字
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = chat.name,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = chat.content,
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }

        // 右边：时间
        Text(
            text = chat.time,
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h6,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp)
    )
}

@Composable
fun RecommendationCell(recommendation: Recommendation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp), // 外边距
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左边：圆形头像
        AsyncImage(
            model = recommendation.avatarRes,
            contentDescription = "用户头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp)) // 圆形头像和中间内容间距

        // 中间：昵称和粉丝数
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recommendation.name,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "粉丝 ${recommendation.followers}",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )
        }

        // 右边：关注按钮和删除按钮
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .clickable {
                        println("===========> follow")
                    }
                    .size(width = 50.dp, height = 25.dp) // 圆角背景的大小
                    .clip(RoundedCornerShape(60)) // 圆形
                    .border(width = 1.dp, color = Color(0xFFFF4081), shape = RoundedCornerShape(60))
                    .background(Color.White), // 设置背景颜色
                contentAlignment = Alignment.Center // 图标居中
            ) {
                Text("关注", color = Color(0xFFFF4081), fontSize = 13.sp)
            }

            IconButton(onClick = {
                println("==========> delete")
            }) {
                Icon(Icons.Default.Close, contentDescription = "删除")
            }
        }
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    val items = remember { mutableStateOf(generateFakeData(20)) }

    SetSystemBarIcons(darkIcons = false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xfff6f6f6)),
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            // 添加头布局

            item(span = { GridItemSpan(2) }) {
                ProfileHeader()
            }

//            items(items.value) { item ->
//                Row {
//                    WaterfallItem(item)
//                }
//            }

            itemsIndexed(items.value) { index, item ->
                Row(
                    modifier = Modifier.padding(
                        start = if (index % 2 == 0) 4.dp else 0.dp,
                        end = if (index % 2 == 1) 4.dp else 0.dp
                    )
                ) {
                    WaterfallItem(item,navController) // 显示每一项的内容
                }
            }
        }

        ProfileTopBar() // 标题栏
    }
}

// 标题栏
@Composable
fun ProfileTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(top = 40.dp)
            .background(Color.Transparent)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 左侧菜单按钮
            IconButton(onClick = { /* 点击菜单 */ }) {
                Icon(Icons.Default.Menu, tint = Color.White, contentDescription = "菜单")
            }

            // 右侧按钮组：设置背景、扫一扫、分享
            Row {
                IconButton(onClick = { /* 设置背景 */ }) {
                    Icon(Icons.Default.Image, tint = Color.White, contentDescription = "设置背景")
                }
                IconButton(onClick = { /* 扫一扫 */ }) {
                    Icon(Icons.Default.CameraAlt, tint = Color.White, contentDescription = "扫一扫")
                }
                IconButton(onClick = { /* 分享 */ }) {
                    Icon(Icons.Default.Share, tint = Color.White, contentDescription = "分享")
                }
            }
        }
    }
}

// 头部布局
@Composable
fun ProfileHeader() {
    Box {

        AsyncImage(
            model = "https://www.veryol.com/uploads/rss_imgs/s_c404d548b3b644e690d7ace8e7d6d93f.jpg",
            contentDescription = "用户头像",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 70.dp, end = 16.dp, bottom = 46.dp)
        ) {
            // 第一行：头像 + 昵称和 UID
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 左边：圆形头像
                AsyncImage(
                    model = avatar,
                    contentDescription = "用户头像",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("昵称", style = MaterialTheme.typography.h6, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "UID: 1234567890",
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 第二行：个人描述
            Text(
                "这是个人描述，喜欢创作和分享内容~",
                style = MaterialTheme.typography.body2,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 第三行：性别
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("性别: 男", style = MaterialTheme.typography.body2, color = Color.White)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 第四行：关注、粉丝、获赞与收藏 + 编辑资料与设置按钮
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 关注、粉丝、获赞与收藏
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem("关注", 123)
                    StatItem("粉丝", 456)
                    StatItem("获赞与收藏", 789)
                }
                Spacer(modifier = Modifier.width(20.dp))
                // 编辑资料和设置按钮
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .clickable {
                                println("===========> follow")
                            }
                            .size(width = 70.dp, height = 25.dp) // 圆角背景的大小
                            .clip(RoundedCornerShape(60)) // 圆形
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(60)
                            )
                            .background(Color.Transparent), // 设置背景颜色
                        contentAlignment = Alignment.Center // 图标居中
                    ) {
                        Text("编辑资料", color = Color.White, fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { /* 设置逻辑 */ }) {
                        Icon(
                            Icons.Default.Settings,
                            tint = Color.White,
                            contentDescription = "设置"
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 横向滑动卡片
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(cardData) { card ->
                    ProfileCard(card)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(30.dp)
                .clip(RoundedCornerShape(topStartPercent = 30, topEndPercent = 30))
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Text("笔记", fontSize = 11.sp, color = Color.Black)

            Spacer(modifier = Modifier.width(16.dp))

            Text("收藏", fontSize = 11.sp, color = Color.Black)

            Spacer(modifier = Modifier.width(16.dp))

            Text("赞过", fontSize = 11.sp, color = Color.Black)
        }

    }
}

// 关注、粉丝、获赞与收藏 Item
@Composable
fun StatItem(label: String, number: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$number", fontSize = 15.sp, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 14.sp, color = Color.White)
    }
}

// 横向滑动卡片
@Composable
fun ProfileCard(card: CardData) {
    Box(
        modifier = Modifier
            .size(105.dp, 50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x4DFFFFFF))
            .padding(8.dp)
    ) {
        Column {
            Text(card.title, fontSize = 12.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(card.subtitle, fontSize = 10.sp, color = Color.LightGray)
        }
    }
}

// 双列网格布局
@Composable
fun <T> LazyListScope.gridItems(
    items: List<T>,
    columns: Int,
    itemContent: @Composable (T) -> Unit
) {
    val rows = if (items.size % columns == 0) items.size / columns else items.size / columns + 1
    items(rows) { rowIndex ->
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (columnIndex in 0 until columns) {
                val itemIndex = rowIndex * columns + columnIndex
                if (itemIndex < items.size) {
                    Box(
                        Modifier
                            .weight(1f)
                            .padding(8.dp)
                    ) {
                        itemContent(items[itemIndex])
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchPage() {
    // 模拟数据
    val historySearches = listOf("Compose", "Android", "Jetpack", "Kotlin", "Hot")
    val youMayLike = listOf("Compose", "Jetpack", "RecyclerView", "Navigation", "Flow", "Canvas")
    val hotSearches = List(20) { index -> "Hot Search $index" }
    val hotViews = List(20) { (5000..100000).random() } // 模拟随机阅览量

    // 搜索框的输入文本
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // 标题栏
        TopAppBar(
            title = {
                SearchBarSection(
                    searchText = searchText,
                    onTextChange = { searchText = it },
                    onBackClick = { /* Do back */ },
                    onCameraClick = { /* Open camera */ },
                    onSearchClick = { /* Log searchText */ }
                )
            },
            modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
            backgroundColor = Color.White,
            elevation = 0.dp
        )

        // 滑动内容部分
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // 历史搜索部分
            item {
                Text(
                    text = "历史搜索",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    historySearches.forEach { text ->
                        RoundedSearchItem(text)
                    }
                }
            }

            // 猜你想搜
            item {
                Text(
                    text = "猜你想搜",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            items(3) { rowIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val leftItem = youMayLike.getOrNull(rowIndex * 2)
                    val rightItem = youMayLike.getOrNull(rowIndex * 2 + 1)

                    if (leftItem != null) {
                        RoundedSearchItem(text = leftItem, modifier = Modifier.weight(1f))
                    }
                    if (rightItem != null) {
                        RoundedSearchItem(text = rightItem, modifier = Modifier.weight(1f))
                    }
                }
            }

            // 小红书热点
            item {
                Text(
                    text = "小红书热点",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
            items(hotSearches.size) { index ->
                HotSearchItem(
                    rank = index + 1,
                    content = hotSearches[index],
                    views = hotViews[index]
                )
            }
        }
    }
}

@Composable
fun SearchBarSection(
    searchText: String,
    onTextChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCameraClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color.White)
            .height(56.dp) // 标题栏统一高度
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // 确保所有内容在垂直方向居中
    ) {
        // 返回按钮
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        // 搜索框 (修复高度问题)
        Box(
            modifier = Modifier
                .weight(1f) // 占据剩余空间
                .fillMaxHeight()
                .clip(RoundedCornerShape(30.dp)) // 圆角背景
                .background(Color(0xFFF2F2F2)), // 灰色背景
            contentAlignment = Alignment.Center
        ) {

            // 搜索输入框本体
            TextField(
                value = searchText,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        text = "搜索内容",
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.body1,
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxSize(),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black, // 输入文字颜色
                    backgroundColor = Color.Transparent, // 无背景色（由父容器控制）
                    focusedIndicatorColor = Color.Transparent, // 无焦点状态下的下划线
                    unfocusedIndicatorColor = Color.Transparent // 非焦点状态下的下划线
                ),
                textStyle = TextStyle(fontSize = 14.sp),
                trailingIcon = {
                    IconButton(
                        onClick = onCameraClick,
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(24.dp) // 设置相机图标的大小
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Camera",
                            tint = Color.Gray
                        )
                    }
                },
                singleLine = true, // 仅支持单行输入，避免高度变化
            )

        }



        Spacer(modifier = Modifier.width(4.dp))

        // 搜索按钮
        TextButton(onClick = onSearchClick) {
            Text(text = "搜索", color = Color.Black)
        }
    }
}

@Composable
fun RoundedSearchItem(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .background(Color.Transparent, RoundedCornerShape(20.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(20.dp)) // 添加描边
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = Color.Black
        )
    }
}

@Composable
fun HotSearchItem(rank: Int, content: String, views: Int) {
    val rankColor = when (rank) {
        1 -> Color(0xFFD32F2F) // 深红
        2 -> Color(0xFFF44336) // 红色
        3 -> Color(0xFFFFCDD2) // 浅红
        else -> Color.Gray      // 灰色
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 排行数字
        Text(
            text = rank.toString(),
            color = rankColor,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.Center
        )
        // 热搜内容
        Text(
            text = content,
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        // 浏览量
        Text(
            text = "$views 浏览",
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}

@Composable
fun XiaohongshuDetailPage() {
    // 整体布局，LazyColumn 包含所有内容
    Scaffold(
        modifier = Modifier.padding(top = 30.dp),
        topBar = { TopBar() }, // 顶部标题栏
        bottomBar = { CommentInputBar() }, // 底部输入栏
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // 适配 Scaffold 的内边距
        ) {
//            // 顶部用户信息
//            item {
//                UserInfoSection()
//            }

            // 图片展示区域
            item {
                ImageCarousel()
            }

            // 文本描述内容
            item {
                TextDescription(description = "这是图片的详细描述内容。支持换行和多段文字。")
            }

            // 带话题的部分
            item {
                TopicsSection(topics = listOf("#旅游", "#美食", "#风景"))
            }

            // 相关搜索
            item {
                RelatedSearch(query = "寻找绝美风景地")
            }

            // 发布时间和 IP 地址
            item {
                PublishInfo(publishTime = "5小时以前", ipAddress = "来自北京")
            }

            // 显示评论标题
            item {
                Text(
                    text = "共50条评论",
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 评论输入框
            item {
                CommentInputArea()
            }

            // 评论列表内容
            items(10) { index ->
                // 每条评论
                CommentItem(
                    nickname = "用户$index",
                    comment = "这是第 $index 条评论内容。",
                    time = "1小时以前",
                    ip = "北京",
                    likes = index * 2
                )
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
        Spacer(modifier = Modifier.width(8.dp))
        AsyncImage(
            model = avatar,
            contentDescription = "用户头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "昵称", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { /* 关注操作 */ },
            modifier = Modifier.height(36.dp).clip(RoundedCornerShape(18.dp))
        ) {
            Text(text = "关注")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.Share, contentDescription = "分享")
    }
}


@Composable
fun UserInfoSection() {
    // 示例用户信息部分
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = avatar,
            contentDescription = "用户头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "用户名", style = MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { /* 关注逻辑 */ }) {
            Text("关注")
        }
    }
}

@Composable
fun ImageCarousel() {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Column {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height((screenWidthDp * 1.25f).dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(5) { // 假设有 5 张图片
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = exampleImages.random(),
                    contentDescription = "图片展示",
                    contentScale = ContentScale.Crop
                )
            }
        }
        // 图片指示点
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { index -> // 假设有 5 个点
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (index == 0) Color.Red else Color.Gray)
                        .padding(4.dp)
                )
            }
        }
    }
}


@Composable
fun TextDescription(description: String) {
    Text(
        text = description,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun TopicsSection(topics: List<String>) {
    LazyRow(modifier = Modifier.padding(8.dp)) {
        items(topics) { topic ->
            Text(
                text = topic,
                modifier = Modifier
                    .background(Color.LightGray, RoundedCornerShape(16.dp))
                    .padding(8.dp)
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun CommentInputArea() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.LightGray, RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("写点什么吧...") },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.EmojiEmotions, contentDescription = "表情")
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.Default.AddPhotoAlternate, contentDescription = "图片")
    }
}

@Composable
fun RelatedSearch(query: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = "搜索")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "相关搜索：$query")
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ArrowForward, contentDescription = "箭头")
    }
}

@Composable
fun PublishInfo(publishTime: String, ipAddress: String) {
    Text(
        text = "$publishTime · $ipAddress",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Gray
    )
}

@Composable
fun CommentSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "共50条评论",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(16.dp)
        )

        // 评论输入框
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.LightGray, RoundedCornerShape(16.dp))
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("写点什么吧...") },
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.Face, contentDescription = "表情")
            Icon(Icons.Default.Image, contentDescription = "图片")
        }

        // 评论列表
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(10) { commentIndex ->
                CommentItem(
                    nickname = "用户$commentIndex",
                    comment = "这是第 $commentIndex 条评论内容。",
                    time = "1小时前",
                    ip = "北京",
                    likes = commentIndex * 2
                )
            }
        }
    }
}

@Composable
fun CommentItem(
    nickname: String,
    comment: String,
    time: String,
    ip: String,
    likes: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        AsyncImage(
            model = avatar,
            contentDescription = "头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = nickname, style = MaterialTheme.typography.subtitle2)
            Text(
                text = comment,
                style = MaterialTheme.typography.body2
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = time, color = Color.Gray, fontSize = 12.sp)
                Text(text = "·", color = Color.Gray)
                Text(text = ip, color = Color.Gray, fontSize = 12.sp)
                Text(text = "回复", color = Color.Gray, fontSize = 12.sp)
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.ThumbUp, contentDescription = "点赞")
            Text(text = likes.toString(), fontSize = 12.sp)
        }
    }
}

@Composable
fun CommentInputBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.LightGray, RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Edit, contentDescription = "编辑")
        Spacer(modifier = Modifier.width(8.dp))
        Text("说点什么...")
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ThumbUp, contentDescription = "点赞")
        Spacer(Modifier.width(4.dp))
        Icon(Icons.Default.Bookmark, contentDescription = "收藏")
        Spacer(Modifier.width(4.dp))
        Icon(Icons.Default.Comment, contentDescription = "评论")
    }
}


data class CardData(val title: String, val subtitle: String)

val cardData = listOf(
    CardData("购物", "提升幸福感"),
    CardData("订单", "查看订单"),
    CardData("购物车", "随便看看"),
    CardData("创作灵感", "创作新思路"),
    CardData("浏览记录", "查看历史记录"),
    CardData("兴趣贴纸", "个性贴纸秀")
)


// 聊天记录数据模型
data class ChatRecord(
    val avatarRes: String,
    val name: String,
    val content: String,
    val time: String
)

// 推荐关注数据模型
data class Recommendation(
    val avatarRes: String,
    val name: String,
    val followers: Int
)

var avatar = "https://c-ssl.duitang.com/uploads/item/201703/09/20170309211351_3eKNs.jpeg"

// 假数据：聊天记录
val chatRecords = listOf(
    ChatRecord(avatar, "张三", "你好！", "下午 3:45"),
    ChatRecord(avatar, "李四", "在吗？", "上午 11:21"),
    ChatRecord(avatar, "王五", "请问有空来聊下吗？", "昨天"),
    ChatRecord(avatar, "赵六", "不错的回答！", "周一"),
    ChatRecord(avatar, "孙七", "推荐的内容看过了", "周日")
)

// 假数据：推荐关注
val recommendations = listOf(
    Recommendation(avatar, "Alice", 1234),
    Recommendation(avatar, "Bob", 4567),
    Recommendation(avatar, "Charlie", 2468),
    Recommendation(avatar, "David", 1357),
    Recommendation(avatar, "Eve", 8745)
)

data class ItemData(
    val imageUrl: String,
    val description: String,
    val avatarUrl: String,
    val username: String,
    val likes: Int
)

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

fun generateFakeData(count: Int): List<ItemData> {
    return List(count) { index ->
        ItemData(
            imageUrl = exampleImages.random(),
            description = "这是一段示例文字在JetpackCompose中，可以使用AnnotatedString和Text组合来实现富文本样式，即使单个文字内容具有不同颜色、大小或其他样式。",
            avatarUrl = "https://c-ssl.duitang.com/uploads/item/201703/09/20170309211351_3eKNs.jpeg",
            username = "用户 $index",
            likes = (0..999).random()
        )
    }
}





