package com.devlee.mymoviediary.presentation.adapter.create

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.fetch.VideoFrameUriFetcher
import coil.load
import coil.request.videoFrameMillis
import com.devlee.mymoviediary.databinding.ItemContentChoiceVideoBinding
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel

class MediaPagingAdapter(
    private val type: ContentType,
    private val contentCreateViewModel: ContentCreateViewModel,
    private val resultCallback: (ContentChoiceFileData?) -> Unit
) : PagingDataAdapter<ContentChoiceFileData, RecyclerView.ViewHolder>(differCallback) {

    private var selectedVideoList: MutableList<Uri> = mutableListOf()
    private var selectedAudioList: MutableList<Uri> = mutableListOf()

    inner class ContentVideoViewHolder(val binding: ItemContentChoiceVideoBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ShowToast")
        fun bind(fileData: ContentChoiceFileData) {
            binding.apply {
                // 선택된 비디오 리스트에 있을 경우에 check
                itemContentVideoCheck.isSelected = selectedVideoList.contains(fileData.video)

                clickListener = View.OnClickListener { v ->

                    fileData.video?.let { uri ->
                        if (!itemContentVideoCheck.isSelected) {
                            // MAX = 4, 추가 선택시 오류문구
                            if (selectedVideoList.size >= 4) {
                                contentCreateViewModel.maxChoiceItemCallback?.invoke()
                                return@OnClickListener
                            }
                            selectedVideoList.add(uri)
                        } else {
                            selectedVideoList.remove(uri)
                        }


                    }
                    itemContentVideoCheck.isSelected = !itemContentVideoCheck.isSelected

                    Log.d(TAG, "selectedVideoList: ${selectedVideoList.size}")
                    contentCreateViewModel.setSelectVideo(selectedVideoList)
                }

                itemContentVideoThumbnail.load(fileData.video) {
                    fetcher(VideoFrameUriFetcher(root.context))
                    videoFrameMillis(0)
                }
                executePendingBindings()
            }
        }
    }

    inner class ContentAudioViewHolder(val binding: ItemContentChoiceVideoBinding) : RecyclerView.ViewHolder(binding.root) {

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (type) {
            // 비디오
            ContentType.VIDEO -> {
                ContentVideoViewHolder(
                    ItemContentChoiceVideoBinding.inflate(layoutInflater, parent, false)
                )
            }
            // 오디오
            else -> {
                ContentVideoViewHolder(
                    ItemContentChoiceVideoBinding.inflate(layoutInflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is ContentVideoViewHolder -> {
                getItem(i)?.let {
                    holder.bind(it)
                }
                if (i == 0) {
                    resultCallback.invoke(getItem(i))
                }
                Log.d(TAG, "onBindViewHolder: ${getItem(i)}, $i")
            }
            is ContentAudioViewHolder -> {
                Log.d(TAG, "onBindViewHolder: ")
            }
        }
    }

    fun getItemPosition(pos: Int = 0): ContentChoiceFileData? {
        return if (itemCount > 0) getItem(pos) else null
    }


    companion object {
        private const val TAG = "MediaPagingAdapter"
        private val differCallback = object : DiffUtil.ItemCallback<ContentChoiceFileData>() {
            override fun areItemsTheSame(oldItem: ContentChoiceFileData, newItem: ContentChoiceFileData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ContentChoiceFileData, newItem: ContentChoiceFileData): Boolean {
                return oldItem == newItem
            }

        }
    }
}
