package com.devlee.mymoviediary.presentation.adapter.home.mydiary

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.presentation.adapter.home.mydiary.item.RecyclerVideoItem

class MyDiaryDetailVideoAdapter : ListAdapter<Uri?, MyDiaryDetailVideoAdapter.VideoHolder>(diffCallback) {

    inner class VideoHolder(itemView: RecyclerVideoItem) : RecyclerView.ViewHolder(itemView) {

        fun bind(uri: Uri?) {
            (itemView as RecyclerVideoItem).setMediaData(uri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val itemView = RecyclerVideoItem(parent.context)
        return VideoHolder(itemView)
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