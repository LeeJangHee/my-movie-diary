package com.devlee.mymoviediary.presentation.adapter.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemColorFirstBinding
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil

class FirstColorPickAdapter: RecyclerView.Adapter<FirstColorPickAdapter.ViewHolder>() {

    var colorList = arrayListOf<String>()

    inner class ViewHolder(val binding: ItemColorFirstBinding): RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(color: String) {
            binding.apply {
                colorFirstItem.setBackgroundColor(color.toInt())
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemColorFirstBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colorList[position])
    }

    override fun getItemCount(): Int = colorList.size

    fun setColorIdList(list: ArrayList<String>) {
        val colorFirstDiffUtil = MyDiaryDiffUtil(colorList, list)
        val diffUtilResult = DiffUtil.calculateDiff(colorFirstDiffUtil)
        colorList = list
        diffUtilResult.dispatchUpdatesTo(this)
    }
}