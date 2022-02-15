package com.devlee.mymoviediary.presentation.adapter.create

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devlee.mymoviediary.databinding.ItemContentChoiceVideoBinding
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel

class ContentChoiceAdapter(
    private val type: ContentType,
    private val contentChoiceViewModel: ContentCreateViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "ContentChoiceAdapter"
    }

    private var fileList: List<ContentChoiceFileData> = listOf()
    private var selectedVideoList: MutableList<Uri> = mutableListOf()
    private var selectedAudioList: MutableList<Uri> = mutableListOf()

    inner class ContentVideoViewHolder(val binding: ItemContentChoiceVideoBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ShowToast")
        fun bind(fileData: ContentChoiceFileData) {
            binding.apply {
                clickListener = View.OnClickListener { v ->

                    fileData.video?.let { uri ->
                        if (!itemContentVideoCheck.isSelected) {
                            // MAX = 4, 추가 선택시 오류문구
                            if (selectedVideoList.size >= 4) {
                                contentChoiceViewModel.maxChoiceItemCallback?.invoke()
                                return@OnClickListener
                            }
                            selectedVideoList.add(uri)
                        } else {
                            selectedVideoList.remove(uri)
                        }
                    }
                    itemContentVideoCheck.isSelected = !itemContentVideoCheck.isSelected

                    Log.d(TAG, "selectedVideoList: ${selectedVideoList.size}")
                    contentChoiceViewModel.setSelectVideo(selectedVideoList)
                }

                itemContentVideoThumbnail.load(contentChoiceViewModel.uri2Bitmap(root.context, fileData.video))
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
                holder.bind(fileList[i])
            }
            is ContentAudioViewHolder -> {
                Log.d(TAG, "onBindViewHolder: ")
            }
        }
    }

    override fun getItemCount(): Int = fileList.size


    fun setFileList(newList: List<ContentChoiceFileData>) {
        val contentChoiceDiffUtils = MyDiaryDiffUtil(fileList, newList)
        val diffUtilResult = DiffUtil.calculateDiff(contentChoiceDiffUtils)
        fileList = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }

}