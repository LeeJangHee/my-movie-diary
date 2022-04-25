package com.devlee.mymoviediary.presentation.adapter.home.mydiary

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemMydiaryDetailVideoBinding

class MyDiaryDetailVideoAdapter: ListAdapter<Uri?, MyDiaryDetailVideoAdapter.VideoHolder>(diffCallback) {

    inner class VideoHolder(val binding: ItemMydiaryDetailVideoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {

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
        holder.bind()
    }

    companion object {
        private const val TAG = "MyDiaryDetailVideoAdapter"
        val diffCallback = object : DiffUtil.ItemCallback<Uri?>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
        }
    }
}