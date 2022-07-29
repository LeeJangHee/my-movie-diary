package com.devlee.mymoviediary.presentation.activity.fullscreen

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ActivityFullScreenBinding
import com.devlee.mymoviediary.presentation.activity.BaseActivity
import com.devlee.mymoviediary.utils.Constants.FULLSCREEN_URI
import com.devlee.mymoviediary.utils.Constants.SAVE_PLAY_TIME
import com.devlee.mymoviediary.utils.gone
import com.devlee.mymoviediary.utils.show
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class FullScreenActivity : BaseActivity<ActivityFullScreenBinding>() {

    companion object {
        private const val TAG = "FullScreenActivity"
    }

    private var isFullScreen = true
    private var mUri: Uri? = null
    private lateinit var exoPlayer: ExoPlayer

    private var savePlayTimePosition: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.FullScreenActivityTheme)

        bottomNavLayout?.gone()
        appToolbarLayout?.gone()
        // 엑티비티 회전 설정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            WindowInsetsControllerCompat(window, binding.root).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        exoPlayer = ExoPlayer.Builder(this).build()
        mUri = intent.getParcelableExtra(FULLSCREEN_URI)
        savePlayTimePosition = intent.getLongExtra(SAVE_PLAY_TIME, 0L)

        if (savedInstanceState != null) {
            savePlayTimePosition = savedInstanceState.getLong(SAVE_PLAY_TIME, 0L)
        }

        binding.apply {
            lifecycleOwner = this@FullScreenActivity
        }

        binding.fullScreenPlayerView.apply {
            findViewById<ImageView>(R.id.exo_fullscreen).gone()
            findViewById<ImageView>(R.id.exo_minimal_fullscreen).show()
            findViewById<FrameLayout>(R.id.exo_fullScreen_layout).setOnClickListener {
                Log.d(TAG, "minimal_fullscreen click")
                finish()
            }

            mUri?.let {
                init(MediaItem.fromUri(it))
            }
        }
    }

    private fun PlayerView.init(mediaItem: MediaItem) {
        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true

            if (savePlayTimePosition > 0) {
                seekTo(savePlayTimePosition)
            }
        }

        player = exoPlayer
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(SAVE_PLAY_TIME, binding.fullScreenPlayerView.player?.currentPosition ?: 0L)
        Log.e(TAG, "onSaveInstanceState() called with: currentTimeline.windowCount --> ${binding.fullScreenPlayerView.player?.currentPosition}")
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    override fun getLayout(): Int = R.layout.activity_full_screen
}