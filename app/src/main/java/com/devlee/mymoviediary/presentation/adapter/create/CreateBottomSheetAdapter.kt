package com.devlee.mymoviediary.presentation.adapter.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.databinding.ItemBottomChoiceBinding
import com.devlee.mymoviediary.domain.use_case.ChoiceBottomSheetData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.presentation.fragment.main_bottom.create.BottomChoiceType
import com.devlee.mymoviediary.utils.recyclerview.MyDiaryDiffUtil
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel

class CreateBottomSheetAdapter(
    private val type: BottomChoiceType,
    private val bottomViewModel: ContentCreateViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "CreateBottomSheetAdapter"
    private var bottomSheetItemList = listOf<ChoiceBottomSheetData>()

    // 동영상 또는 비디오 선택 View
    inner class BottomSheetContentViewHolder(val binding: ItemBottomChoiceBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(contentType: ContentType?) {
            binding.apply {
                textTitle = "${contentType?.text} 파일"
                bottomSheetItemClickListener = View.OnClickListener {
                    Log.d(TAG, "Content bind: ${contentType?.name}")
                    bottomViewModel.selectedContentItem(contentType)
                }
                executePendingBindings()
            }
        }
    }

    // 만들어둔 Category 선택 View
    inner class BottomSheetCategoryViewHolder(val binding: ItemBottomChoiceBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(category: Category?) {
            binding.apply {
                this.category = category
                bottomSheetItemClickListener = View.OnClickListener {
                    Log.d(TAG, "Category bind: ${category?.title}")
                    bottomViewModel.selectedCategoryItem(category)
                }
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBottomChoiceBinding.inflate(layoutInflater, parent, false)
        return when (type.ordinal) {
            0 -> BottomSheetContentViewHolder(binding)
            else -> BottomSheetCategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BottomSheetContentViewHolder -> {
                holder.bind(bottomSheetItemList[position].contentType)
            }
            is BottomSheetCategoryViewHolder -> {
                holder.bind(bottomSheetItemList[position].category)
            }
        }
    }

    override fun getItemCount(): Int = bottomSheetItemList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun setBottomSheetItem(newItem: List<ChoiceBottomSheetData>) {
        val choiceBottomSheetDiffUtil = MyDiaryDiffUtil(bottomSheetItemList, newItem)
        val diffUtilResult = DiffUtil.calculateDiff(choiceBottomSheetDiffUtil)
        bottomSheetItemList = newItem
        diffUtilResult.dispatchUpdatesTo(this)
    }
}