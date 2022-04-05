package com.devlee.mymoviediary.presentation.adapter.create

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemCreateAddBinding
import com.devlee.mymoviediary.databinding.ItemCreateContentBinding
import com.devlee.mymoviediary.domain.use_case.ContentChoiceData
import com.devlee.mymoviediary.utils.recyclerview.MyDiaryDiffUtil
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel

class CreateAdapter(
    private val contentCreateViewModel: ContentCreateViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var myDiaryList = arrayListOf<ContentChoiceData>()

    inner class ContentAddViewHolder(val binding: ItemCreateAddBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                clickListener = View.OnClickListener {
                    contentCreateViewModel.contentAddItemClick(it)
                }
                executePendingBindings()
            }
        }
    }

    inner class ContentDiaryViewHolder(val binding: ItemCreateContentBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            // 아이템 삭제
            binding.itemCreateDelBtn.setOnClickListener {
                myDiaryList.removeAt(bindingAdapterPosition)
                contentCreateViewModel.setContentChoice(myDiaryList)
                notifyItemRemoved(bindingAdapterPosition)
            }
        }

        fun bind(contentChoiceData: ContentChoiceData) {
            binding.apply {
                contentData = contentChoiceData
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            CreateViewType.ADD.type -> {
                ContentAddViewHolder(ItemCreateAddBinding.inflate(layoutInflater, parent, false))
            }
            else -> {
                ContentDiaryViewHolder(ItemCreateContentBinding.inflate(layoutInflater, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is ContentAddViewHolder -> holder.bind()
            is ContentDiaryViewHolder -> holder.bind(myDiaryList[i])
        }
    }

    override fun getItemCount(): Int = myDiaryList.size

    override fun getItemViewType(position: Int): Int = myDiaryList[position].itemType

    fun setDiaryList(newMyDiaryList: ArrayList<ContentChoiceData>) {
        val createDiffUtil = MyDiaryDiffUtil(myDiaryList, newMyDiaryList)
        val diffUtilResult = DiffUtil.calculateDiff(createDiffUtil)
        myDiaryList = newMyDiaryList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}

enum class CreateViewType(val type: Int) {
    ADD(0),
    DIARY(1)
}