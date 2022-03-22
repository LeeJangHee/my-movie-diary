package com.devlee.mymoviediary.presentation.adapter.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.databinding.ItemGridHomeBinding
import com.devlee.mymoviediary.databinding.ItemLinearHomeBinding
import com.devlee.mymoviediary.utils.recyclerview.MyDiaryDiffUtil

class MainHomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MainHomeAdapter"
    private var myDiaryList = emptyList<MyDiary>()
    private var layoutManager: GridLayoutManager? = null

    inner class MyLinearViewHolder(private val binding: ItemLinearHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myDiary: MyDiary) {
            binding.apply {
                this.myDiary = myDiary
                executePendingBindings()
            }
        }
    }

    inner class MyGirdViewHolder(private val binding: ItemGridHomeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(myDiary: MyDiary) {
            binding.apply {
                this.myDiary = myDiary
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HomeLayoutType.LINEAR.ordinal -> {
                val binding = ItemLinearHomeBinding.inflate(layoutInflater, parent, false)
                MyLinearViewHolder(binding)
            }
            else -> {
                val binding = ItemGridHomeBinding.inflate(layoutInflater, parent, false)
                MyGirdViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is MyLinearViewHolder -> holder.bind(myDiaryList[i])
            is MyGirdViewHolder -> holder.bind(myDiaryList[i])
        }
    }

    override fun getItemCount(): Int = myDiaryList.size

    override fun getItemViewType(position: Int): Int {
        val type = if (layoutManager?.spanCount == HomeLayoutType.GRID.spanCount) HomeLayoutType.GRID.ordinal
        else HomeLayoutType.LINEAR.ordinal
        Log.d(TAG, "getItemViewType: $type")
        return type
    }

    fun setData(newDiaryList: List<MyDiary>) {
        val mainDiffUtil = MyDiaryDiffUtil(myDiaryList, newDiaryList)
        val diffUtilResult = DiffUtil.calculateDiff(mainDiffUtil)
        myDiaryList = newDiaryList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun setLayoutManager(layoutManager: GridLayoutManager?) {
        this.layoutManager = layoutManager
    }
}

enum class HomeLayoutType(val spanCount: Int) {
    LINEAR(1), GRID(3)
}