package com.devlee.mymoviediary.presentation.adapter.category

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemColorFirstBinding
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil
import com.devlee.mymoviediary.utils.categoryFirstItemClick

class FirstColorPickAdapter : RecyclerView.Adapter<FirstColorPickAdapter.ViewHolder>() {

    private val TAG = "FirstColorPickAdapter"

    var colorList = arrayListOf<Int>()

    inner class ViewHolder(val binding: ItemColorFirstBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.colorFirstItem.setOnClickListener {
                val backgroundColor = binding.colorFirstItem.background
                if (backgroundColor is ColorDrawable) {
                    Log.d(TAG, "color = ${backgroundColor.color}")
                    categoryFirstItemClick?.invoke(backgroundColor.color)
                }
            }
        }

        fun bind(color: Int) {
            binding.apply {
                colorFirstItem.setBackgroundColor(color)
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

    fun setColorIdList(list: ArrayList<Int>) {
        val colorFirstDiffUtil = MyDiaryDiffUtil(colorList, list)
        val diffUtilResult = DiffUtil.calculateDiff(colorFirstDiffUtil)
        colorList = list
        diffUtilResult.dispatchUpdatesTo(this)
    }
}