package com.example.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LiveRoomViewModel : ViewModel() {

    val chatMessages = mutableStateListOf<ChatMessage>()

    private var keyId = 0   // 全局自增动画key
    var hearts = mutableStateListOf<HeartAnimInfo>()
        private set

    private var started = false


    fun startTicker(
        avatar: String,
        rainbowColors: List<Color>,
        demoLists: List<String>
    ) {
        // 防止重复开启
        if (started) return
        started = true

        var index = 0
        var index2 = 0

        // 启动飘心动画的协程
        viewModelScope.launch {
            try {
                while (true) {
                    addHeart(rainbowColors)

                    delay(500) // 飘心动画间隔 500ms

                    //println("===> Loop hearts i = $index size = ${hearts.size}")

                    index++
                }
            } catch (e: Exception) {
                println("===> 飘心动画协程终止: ${e.message}")
            }
        }

        // 启动插入消息的协程
        viewModelScope.launch {
            try {
                var msgId = 1
                while (true) {
                    chatMessages.add(
                        ChatMessage(
                            id = msgId++,
                            avatar = avatar,
                            nickname = "用户$msgId",
                            content = demoLists.random()
                        )
                    )
                    // 限制 chatMessages 的最大数量
                    if (chatMessages.size > 100) chatMessages.removeAt(0)
                    delay(1000) // 插入消息间隔 1s

                    //println("===> Loop chatMessages i = $index2  size = ${chatMessages.size}")

                    index2++

                }
            } catch (e: Exception) {
                println("===> 插入消息协程终止: ${e.message}")
            }
        }
    }


    fun addHeart(rainbowColors: List<Color>) {
        val color = rainbowColors.random()
        hearts.add(HeartAnimInfo(keyId++, color))

    }

    // 完全删除指定 key
    fun removeHeart(key: Int) {
        if (hearts.size > 100) {
            hearts.removeAll { it.key == key }
        }
    }
}

data class HeartAnimInfo(
    val key: Int,   // 此为只作用于 Compose 状态系统唯一标识
    val color: Color
)