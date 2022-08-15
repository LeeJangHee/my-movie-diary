package com.devlee.mymoviediary.presentation.adapter.home.mydiary.item

import android.animation.ValueAnimator
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import com.devlee.mymoviediary.databinding.ItemMydiaryDetailAudioBinding
import com.devlee.mymoviediary.utils.startDownToUpAnimation
import com.devlee.mymoviediary.utils.startUpToDownAnimation
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlin.properties.Delegates

class RecyclerAudioItem : ConstraintLayout {

    companion object {
        private const val TAG = "RecyclerAudioItem"
    }

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var binding: ItemMydiaryDetailAudioBinding
    private var defaultHeight by Delegates.notNull<Int>()

    private var mView: View? = null
    private var mUri: Uri? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private fun initView() {
        layoutParams = LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = ItemMydiaryDetailAudioBinding.inflate(layoutInflater, this, false)

        if (mView == null) {
            mView = binding.root
            addView(mView)
        }

        binding.root.doOnPreDraw {
            binding.diaryDetailAudioExpandView.updateLayoutParams { height = 0 }
            Log.v(TAG, "doOnPreDraw: rootHeight ${it.height}, ${it.measuredHeight}, $defaultHeight")
        }

        binding.root.doOnLayout {
            defaultHeight = binding.diaryDetailAudioExpandView.height
            Log.v(TAG, "doOnLayout: rootHeight ${it.height}, ${it.measuredHeight}, $defaultHeight")
        }
    }

    private fun initExpandState() {
        binding.diaryDetailExpandButton.setOnClickListener {
            it.isSelected = !it.isSelected
            if (it.isSelected) {
                it.startDownToUpAnimation()
            } else {
                it.startUpToDownAnimation()
            }
            binding.diaryDetailAudioExpandView.setOnExpandable(it.isSelected, defaultHeight)
        }
    }

    fun setMediaData(uri: Uri?) {
        this.mUri = uri
    }

    fun ExoPlayer.init(mediaItem: MediaItem) {
        Log.d(TAG, "init() called with: mediaItem = $mediaItem")

        setMediaItem(mediaItem)
        prepare()
        playWhenReady = false
    }

    private fun getFileName(): String? {
        Log.d(TAG, "getFileName() called with: context = $context, uri = $mUri")
        return mUri?.let {
            context.contentResolver.query(
                it,
                null,
                null,
                null,
                null
            )?.use { cursor ->
                cursor.moveToLast()
                val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val title = cursor.getString(titleColumn)
                Log.d(TAG, "getFileName: $title")
                title
            }
        }
    }

    fun playAudio() {

    }

    fun stopAudio() {

    }

    fun pauseAudio() {

    }


    override fun onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow() called")
        exoPlayer = ExoPlayer.Builder(context).build()
        mUri?.let {
            exoPlayer.init(MediaItem.fromUri(it))
        }
        binding.diaryDetailAudioText.text = getFileName()
        initExpandState()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow() called")
        super.onDetachedFromWindow()
        exoPlayer.release()
    }

    private fun View.setOnExpandable(isExpand: Boolean, defaultHeight: Int) {
        Log.d(TAG, "setOnExpandable() called with: isExpand = $isExpand, defaultHeight = $defaultHeight")
        this@setOnExpandable.post {
            if (isExpand) {
                ValueAnimator.ofFloat(0f, 1f).apply {
                    duration = 300
                    interpolator = LinearInterpolator()
                    addUpdateListener {
                        val value = it.animatedValue as Float
                        this@setOnExpandable.updateLayoutParams {
                            height = (defaultHeight * value).toInt()
                            Log.d(TAG, "setOnExpandable() expand: $height")
                        }
                    }
                }.start()
            } else {
                ValueAnimator.ofFloat(1f, 0f).apply {
                    duration = 300
                    interpolator = LinearInterpolator()
                    addUpdateListener {
                        val value = it.animatedValue as Float
                        this@setOnExpandable.updateLayoutParams {
                            height = (defaultHeight * value).toInt()
                            Log.d(TAG, "setOnExpandable() collapse: $height")
                        }
                    }
                }.start()
            }
        }
    }

}