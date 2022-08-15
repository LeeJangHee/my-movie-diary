package com.devlee.mymoviediary.presentation.adapter.home.mydiary

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.presentation.adapter.home.mydiary.item.RecyclerAudioItem

class MyDiaryDetailAudioAdapter : ListAdapter<Uri?, MyDiaryDetailAudioAdapter.AudioHolder>(diffCallback) {

    inner class AudioHolder(itemView: RecyclerAudioItem) : RecyclerView.ViewHolder(itemView) {

        fun bind(uri: Uri) {
            (itemView as RecyclerAudioItem).setMediaData(uri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioHolder {
        val itemView = RecyclerAudioItem(parent.context)
        return AudioHolder(itemView)

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