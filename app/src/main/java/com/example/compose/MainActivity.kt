package com.example.compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.compose.ui.theme.TestComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TestComposeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }
    Surface(modifier) {

        CheckPermission()

        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClick = { shouldShowOnboarding = false })
        } else {
            Greetings()
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        AsyncImage(
            model = "https://tse1-mm.cn.bing.net/th/id/OIP-C.0YlNEcHIQtMBgcRDqmLHQwHaHa?rs=1&pid=ImgDetMain",
            contentDescription = "Background",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop // 避免图片拉伸
        )

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to 惠州", color = Color.White, fontWeight = FontWeight.Bold)
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onContinueClick
            ) {
                Text("Continue")
            }

            ElevatedButton(
                modifier = Modifier.padding(vertical = 10.dp),
                onClick = {
                    context.startActivity(Intent(context, TestVideoActivity::class.java))
                }) {
                Text("testTikTok")
            }

            ElevatedButton(
                modifier = Modifier.padding(vertical = 10.dp),
                onClick = {
                    context.startActivity(Intent(context, TestFeedActivity::class.java))
                }) {
                Text("testFeed")
            }

            ElevatedButton(
                modifier = Modifier.padding(vertical = 10.dp),
                onClick = {
                    context.startActivity(Intent(context, TestRedBookActivity::class.java))
                }) {
                Text("testRedBook")
            }

            ElevatedButton(
                modifier = Modifier.padding(vertical = 10.dp),
                onClick = {
                    context.startActivity(Intent(context, TestLiveActivity::class.java))
                }) {
                Text("testLive")
            }

            ElevatedButton(
                modifier = Modifier.padding(vertical = 10.dp),
                onClick = {

                    val intent = Intent(context, MusicPlayerActivity::class.java)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.slide_in_bottom, 0)
                    }

                }) {
                Text("testMusic")
            }
        }
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )

    )


    Surface(

        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {

        Card(
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(modifier = Modifier.padding(24.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = extraPadding.coerceAtLeast(0.dp))
                ) {
                    Text(text = "Hello")
                    Text(
                        text = name, style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                }
                ElevatedButton(
                    onClick = { expanded = !expanded }
                ) {

                    Text(if (expanded) "show less" else "show more")
                }
            }
        }

    }
}

@Composable
fun CheckPermission() {

    val context = LocalContext.current

    val openDialog = remember { mutableStateOf(!Settings.canDrawOverlays(context)) }


    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(
                    text = "开启悬浮窗权限",
                    fontWeight = FontWeight.W700,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "这将意味着，我们会在直播退出后继续在悬浮窗显示直播",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        goSetting(context)
                        openDialog.value = false
                    },
                ) {
                    Text(
                        "确认",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        goSetting(context)
                        openDialog.value = false
                    }
                ) {
                    Text(
                        "取消",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        )
    }
}


fun goSetting(context: Context) {
    //val intent1 = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.packageName))
    //context.startActivity(intent1)

    val intent2 = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent2.data = Uri.parse("package:" + context.packageName)
    intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent2)
}

@Composable
fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {
//    Column(modifier = modifier.padding(vertical = 4.dp)) {
//        for (name in names) {
//            Greeting(name)
//        }
//    }
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "PreviewDark")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestComposeTheme {
        MyApp()
    }
}