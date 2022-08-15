package com.devlee.mymoviediary.presentation.adapter.home.mydiary.item

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ItemMydiaryDetailVideoBinding
import com.devlee.mymoviediary.presentation.activity.fullscreen.FullScreenActivity
import com.devlee.mymoviediary.utils.Constants.FULLSCREEN_URI
import com.devlee.mymoviediary.utils.Constants.SAVE_PLAY_TIME
import com.devlee.mymoviediary.utils.gone
import com.devlee.mymoviediary.utils.second
import com.devlee.mymoviediary.utils.show
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView


class RecyclerVideoItem : ConstraintLayout {

    companion object {
        private const val TAG = "RecyclerVideoItem"
    }

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var binding: ItemMydiaryDetailVideoBinding
    private var mView: View? = null

    private var mUri: Uri? = null
    private var isFullScreen = false

    private val controllerView by lazy { binding.diaryDetailPreviewVideo }

    constructor(context: Context) : super(context) {
        Log.d(TAG, "null() called with: context = $context")
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        Log.d(TAG, "null() called with: context = $context, attrs = $attrs")
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView()
    }


    private fun initView() {
        layoutParams = LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)

        Log.d(TAG, "initView() called")

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemMydiaryDetailVideoBinding.inflate(layoutInflater, this, false)

        // FullScreen Button
        validateFullScreen()

        controllerView.findViewById<FrameLayout>(R.id.exo_fullScreen_layout).setOnClickListener {
            binding.diaryDetailPreviewVideo.player?.pause()
            val intent = Intent(this.context, FullScreenActivity::class.java).apply {
                putExtra(FULLSCREEN_URI, mUri)
                putExtra(SAVE_PLAY_TIME, binding.diaryDetailPreviewVideo.player?.currentPosition)
            }
            this.context.startActivity(intent)

        }

        if (mView == null) {
            mView = binding.root
            addView(mView)
        }
    }

    fun setMediaData(uri: Uri?) {
        this.mUri = uri
    }

    fun PlayerView.init(mediaItem: MediaItem) {
        Log.d(TAG, "init() called with: mediaItem = $mediaItem")
        controllerShowTimeoutMs = 1.second

        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = false
        }

        player = exoPlayer
    }

    override fun onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow() called")
        exoPlayer = ExoPlayer.Builder(context).build()
        mUri?.let {
            binding.diaryDetailPreviewVideo.init(MediaItem.fromUri(it))
        }
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow() called")
        exoPlayer.release()
        super.onDetachedFromWindow()
    }

    private fun validateFullScreen() {
        if (isFullScreen) {
            controllerView.apply {
                findViewById<ImageView>(R.id.exo_minimal_fullscreen).show()
                findViewById<ImageView>(R.id.exo_fullscreen).gone()
            }
        } else {
            controllerView.apply {
                findViewById<ImageView>(R.id.exo_minimal_fullscreen).gone()
                findViewById<ImageView>(R.id.exo_fullscreen).show()
            }
        }
        requestLayout()
    }
}