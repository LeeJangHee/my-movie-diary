package com.devlee.mymoviediary.presentation.adapter.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemContentChoiceVideoBinding
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import java.io.File

class ContentChoiceAdapter(
    private val type: ContentType,
    private val contentChoiceViewModel: ContentCreateViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "ContentChoiceAdapter"
    }

    private var fileList: List<File> = listOf()


    inner class ContentVideoViewHolder(val binding: ItemContentChoiceVideoBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                clickListener = View.OnClickListener {

                }
                executePendingBindings()
            }
        }
    }

    inner class ContentAudioViewHolder(val binding: ItemContentChoiceVideoBinding): RecyclerView.ViewHolder(binding.root) {

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContentVideoViewHolder -> {
                holder.bind()
            }
            is ContentAudioViewHolder -> {
                Log.d(TAG, "onBindViewHolder: ")
            }
        }
    }

    override fun getItemCount(): Int = fileList.size


    fun setFileList(newList: List<File>) {
        val contentChoiceDiffUtils = MyDiaryDiffUtil(fileList, newList)
        val diffUtilResult = DiffUtil.calculateDiff(contentChoiceDiffUtils)
        fileList = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }

}