package com.devlee.mymoviediary.presentation.adapter.home.mydiary

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemMydiaryDetailAudioBinding

class MyDiaryDetailAudioAdapter : ListAdapter<Uri?, MyDiaryDetailAudioAdapter.AudioHolder>(diffCallback) {

    inner class AudioHolder(val binding: ItemMydiaryDetailAudioBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AudioHolder(ItemMydiaryDetailAudioBinding.inflate(layoutInflater, parent, false))

    }

    override fun onBindViewHolder(holder: AudioHolder, i: Int) {
        holder.bind()
    }

    companion object {
        private const val TAG = "MyDiaryDetailAdapter"
        val diffCallback = object : DiffUtil.ItemCallback<Uri?>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = (oldItem == newItem)
        }
    }

}