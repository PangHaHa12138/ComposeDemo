package com.example.compose

import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService


class MusicPlaybackService : MediaSessionService() {

    // Kotlin的最新写法，支持多个session但通常只用一个
    private var mediaSession: MediaSession? = null
    // 可定义全局Player
    private var player: ExoPlayer? = null

    // 必须实现: 用于Controller等获取Session
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onCreate() {
        super.onCreate()

        // 1. 初始化ExoPlayer，准备资源
        player = ExoPlayer.Builder(this).build()
        val item1 = MediaItem.Builder()
            //.setUri("android.resource://$packageName/${R.raw.music_file1}") // 你的音频文件
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("歌曲名1")
                    .setArtist("歌手1")
                    //.setArtworkUri(android.net.Uri.parse("android.resource://$packageName/${R.drawable.cover1}"))
                    .build()
            )
            .build()

        val item2 = MediaItem.Builder()
            //.setUri("android.resource://$packageName/${R.raw.music_file2}")
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("歌曲名2")
                    .setArtist("歌手2")
                    //.setArtworkUri(android.net.Uri.parse("android.resource://$packageName/${R.drawable.cover2}"))
                    .build()
            )
            .build()

        player?.setMediaItems(listOf(item1, item2))
        player?.prepare()

        // 2. 建立MediaSession（强关联player与service）
        mediaSession = MediaSession.Builder(this, player!!).build()

        // 3. 创建前台通知
        createNotificationChannel()
        startForeground(33, createNotification())

        // 4. 启动自动播放（可省略）
        player?.playWhenReady = true
    }

    // 建议新建专用通知渠道
    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "music_channel",
                "音乐播放器",
                NotificationManager.IMPORTANCE_LOW
            )
            val mgr = getSystemService(NotificationManager::class.java)
            mgr.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 可根据当前播放项丰富标题和大图
        return NotificationCompat.Builder(this, "music_channel")
            .setContentTitle("音乐播放")
            .setContentText("正在后台播放...")
            //.setSmallIcon(R.drawable.ic_music_note) // 替换你自己的icon
            //.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.cover1))
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        mediaSession?.release()
        player?.release()
        mediaSession = null
        player = null
        super.onDestroy()
    }
}