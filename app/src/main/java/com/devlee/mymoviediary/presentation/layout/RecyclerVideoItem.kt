package com.devlee.mymoviediary.presentation.layout

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.devlee.mymoviediary.databinding.ItemMydiaryDetailVideoBinding
import com.devlee.mymoviediary.utils.second
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

        if (mView == null) {
            mView = binding.root
            addView(mView)
        }
        exoPlayer = ExoPlayer.Builder(context).build()
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
        super.onAttachedToWindow()
        exoPlayer = ExoPlayer.Builder(context).build()
        mUri?.let {
            binding.diaryDetailPreviewVideo.init(MediaItem.fromUri(it))
        }
    }

    override fun onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow() called")
        super.onDetachedFromWindow()
        exoPlayer.release()
    }
}