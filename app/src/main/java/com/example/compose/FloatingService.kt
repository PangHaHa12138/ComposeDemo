package com.example.compose

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.BehindLiveWindowException
import androidx.media3.ui.PlayerView

class FloatingService : Service() {
    private lateinit var windowManager: WindowManager
    private var floatView: View? = null
    private var exoPlayer: ExoPlayer? = null

    private val windowWidthDp = 300f
    private val windowHeightDp = 170f
    private val initialMarginTopDp = 1f
    private val statusBarHeightDp = 1f     // 状态栏预估高度
    private val navigationBarHeightDp = 48f // 导航栏预估高度
    private val edgeMarginDp = 5f

    // 转换dp为px
    private fun dp2px(dp: Float): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        ).toInt()

    override fun onBind(intent: Intent?): IBinder? = null

    @SuppressLint("InflateParams")
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // 布局
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_floating_widget, null)

        // 悬浮窗参数
        val params = WindowManager.LayoutParams(
            dp2px(windowWidthDp),
            dp2px(windowHeightDp),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START
        params.x = dp2px(edgeMarginDp)
        params.y = dp2px(initialMarginTopDp)

        // 播放器区域
        val playerView = view.findViewById<PlayerView>(R.id.player_view)
        playerView.useController = false
        playerView.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        )
        exoPlayer = ExoPlayer.Builder(this).build().apply {
            addListener(object : Player.Listener {
                @OptIn(UnstableApi::class)
                override fun onPlayerError(error: PlaybackException) {
                    if (error.cause is BehindLiveWindowException) {
                        // 直播流已经“追不上”，seek到最新并重播
                        seekToDefaultPosition()
                        prepare()
                        playWhenReady = true
                    }
                }
            })
        }
        playerView.player = exoPlayer

        val videoUrl = "http://220.161.87.62:8800/hls/0/index.m3u8"
        val mediaItem = MediaItem.fromUri(videoUrl)
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady = true


        val ivPlayPause = view.findViewById<ImageView>(R.id.iv_play_pause)
        val ivClose = view.findViewById<ImageView>(R.id.iv_close)
        val ivBackHome = view.findViewById<ImageView>(R.id.iv_back_home)

        // ---- 1. 中间按钮，关闭按钮的显隐 ----
        var isButtonVisible = true
        fun setButtonVisible(visible: Boolean) {
            val newVis = if (visible) View.VISIBLE else View.GONE
            if (ivPlayPause.visibility != newVis) ivPlayPause.visibility = newVis
            if (ivClose.visibility != newVis) ivClose.visibility = newVis
            if (ivBackHome.visibility != newVis) ivBackHome.visibility = newVis
            isButtonVisible = visible
        }
        setButtonVisible(true)

        // 父布局点击显示/隐藏按钮（排除按钮自身事件）
        view.setOnClickListener {
            setButtonVisible(!isButtonVisible)
        }
        ivPlayPause.setOnClickListener {
            // 不传递到父布局
            it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            // 切换播放
            val isPlaying = exoPlayer?.isPlaying == true
            exoPlayer?.playWhenReady = !isPlaying
            ivPlayPause.setImageResource(
                if (isPlaying)
                    android.R.drawable.ic_media_play
                else
                    android.R.drawable.ic_media_pause
            )
            // 顺便让按钮显示一段时间后自动隐藏（可选）
            setButtonVisible(true)
        }
        ivClose.setOnClickListener {
            it.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
            stopSelf()
        }


        ivBackHome.setOnClickListener {
            val intent = Intent(this, TestLiveActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            // 关闭悬浮窗
            stopSelf()
        }

        // ---- 2. 悬浮窗拖动，有边界限制 ----
        view.setOnTouchListener(object : View.OnTouchListener {
            var startX = 0
            var startY = 0
            var touchX = 0
            var touchY = 0
            var isMoving = false
            var downRawY = 0f

            // 获取屏幕大小
            fun getScreenArea(): Pair<Int, Int> {
                val displayMetrics = resources.displayMetrics
                return displayMetrics.widthPixels to displayMetrics.heightPixels
            }

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                val action = event.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = params.x
                        startY = params.y
                        touchX = event.rawX.toInt()
                        touchY = event.rawY.toInt()
                        downRawY = event.rawY   // 记录按下的y
                        isMoving = false
                        return false
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val dX = event.rawX.toInt() - touchX
                        val dY = event.rawY.toInt() - touchY
                        params.x = startX + dX
                        params.y = startY + dY
                        val (_, _) = getScreenArea()

                        windowManager.updateViewLayout(view, params)
                        isMoving = true
                        return true
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        if (isMoving) {
                            val (screenW, screenH) = getScreenArea()
                            val minX = dp2px(edgeMarginDp)
                            val minY = dp2px(statusBarHeightDp + edgeMarginDp)
                            val maxX = screenW - dp2px(windowWidthDp) - dp2px(edgeMarginDp)
                            val maxY =
                                screenH - dp2px(windowHeightDp) - dp2px(navigationBarHeightDp + edgeMarginDp)

                            // 只在拖动并越界且方向吻合时吸顶/吸底
                            when {
                                params.y < minY && event.rawY < downRawY -> { // 向上拖超越，吸顶
                                    params.y = minY
                                }

                                params.y > maxY && event.rawY > downRawY -> { // 向下拖超越，吸底
                                    params.y = maxY
                                }
                                // 横向吸边逻辑如有需求也可依此加
                            }
                            // X方向仍可照旧做限制
                            params.x = params.x.coerceIn(minX, maxX)
                            // 更新Window位置
                            windowManager.updateViewLayout(view, params)
                            isMoving = false
                        }
                        return false
                    }
                }
                return false
            }
        })

        floatView = view
        windowManager.addView(view, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        floatView?.let { windowManager.removeView(it) }
        exoPlayer?.release()
        exoPlayer = null
    }
}