package com.devlee.mymoviediary.presentation.adapter.home.mydiary

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ItemMydiaryDetailVideoBinding
import com.devlee.mymoviediary.viewmodels.MyDiaryDetailViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class MyDiaryDetailVideoAdapter(
    private val viewModel: MyDiaryDetailViewModel,
) : ListAdapter<Uri?, MyDiaryDetailVideoAdapter.VideoHolder>(diffCallback) {

    inner class VideoHolder(val binding: ItemMydiaryDetailVideoBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.diaryDetailPlayButton.setOnClickListener {

            }
        }

        fun bind(uri: Uri?) {
            binding.apply {
                uri?.let {
                    val mediaItem = MediaItem.fromUri(it)
                    viewModel.changeVideoPlayer(diaryDetailPreviewVideo, mediaItem)
                }
                viewModel.videoPlayButtonCallback = {
                    val playButtonImage = if (it) {
                        R.drawable.detail_video_play_icon
                    } else {
                        R.drawable.detail_video_pause_icon
                    }

                    diaryDetailPlayButton.load(playButtonImage)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMydiaryDetailVideoBinding.inflate(layoutInflater, parent, false)
        return VideoHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private const val TAG = "MyDiaryDetailVideoAdapter"
        val diffCallback = object : DiffUtil.ItemCallback<Uri?>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
        }
    }
}