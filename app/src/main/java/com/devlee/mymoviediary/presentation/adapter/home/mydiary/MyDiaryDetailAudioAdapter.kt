package com.devlee.mymoviediary.presentation.adapter.home.mydiary

import android.animation.ValueAnimator
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.view.doOnLayout
import androidx.core.view.doOnPreDraw
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemMydiaryDetailAudioBinding
import com.devlee.mymoviediary.utils.startDownToUpAnimation
import com.devlee.mymoviediary.utils.startUpToDownAnimation

class MyDiaryDetailAudioAdapter : ListAdapter<Uri?, MyDiaryDetailAudioAdapter.AudioHolder>(diffCallback) {

    private var defaultHeight: Int = 0

    inner class AudioHolder(val binding: ItemMydiaryDetailAudioBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // TODO: 레이아웃 깜빡이는 현상 수정 필요
            with(binding.root) {
                doOnLayout {
                    defaultHeight = binding.diaryDetailAudioExpandView.height
                    Log.v(TAG, "doOnLayout: rootHeight ${it.height}, ${it.measuredHeight}, $defaultHeight")
                }
                doOnPreDraw {
                    binding.diaryDetailAudioExpandView.updateLayoutParams { height = 0 }
                    Log.v(TAG, "doOnPreDraw: rootHeight ${it.height}, ${it.measuredHeight}, $defaultHeight")
                }
                post {
                    Log.v(TAG, "post: rootHeight ${this.height}, ${this.measuredHeight}, $defaultHeight")
                }
            }
        }

        fun bind(uri: Uri) {
            binding.apply {
                diaryDetailAudioText.text = getFileName(root.context, uri)
                diaryDetailExpandButton.setOnClickListener {
                    it.isSelected = !it.isSelected
                    Log.v(TAG,
                        "bind.root click expandLayoutHeight = ${diaryDetailAudioExpandView.height}, ${diaryDetailAudioExpandView.measuredHeight}")
                    Log.i(TAG, "bind: rootHeight ${root.height}, ${root.measuredHeight}")
                    if (it.isSelected) {
                        it.startDownToUpAnimation()
                    } else {
                        it.startUpToDownAnimation()
                    }
                    diaryDetailAudioExpandView.setOnExpandable(it.isSelected, defaultHeight)
                }
                executePendingBindings()
            }
        }

        private fun getFileName(context: Context, uri: Uri): String? {
            Log.d(TAG, "getFileName() called with: context = $context, uri = $uri")
            return context.contentResolver.query(
                uri,
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

        private fun View.setOnExpandable(isExpand: Boolean, defaultHeight: Int) {
            Log.d(TAG, "setOnExpandable() called with: isExpand = $isExpand, defaultHeight = $defaultHeight")
            post {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AudioHolder(ItemMydiaryDetailAudioBinding.inflate(layoutInflater, parent, false))

    }

    override fun onBindViewHolder(holder: AudioHolder, i: Int) {
        val uri = getItem(i) ?: Uri.EMPTY
        holder.bind(uri)
    }

    companion object {
        private const val TAG = "MyDiaryDetailAudioAdapter"
        val diffCallback = object : DiffUtil.ItemCallback<Uri?>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
        }
    }

}