package com.devlee.mymoviediary.presentation.adapter.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.databinding.ItemGridHomeBinding
import com.devlee.mymoviediary.databinding.ItemLinearHomeBinding
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel

class MainHomeAdapter(
    private val homeViewModel: MyDiaryViewModel
) : ListAdapter<Pair<MyDiary, Category?>, RecyclerView.ViewHolder>(differCallback) {

    private var layoutManager: GridLayoutManager? = null

    inner class MyLinearViewHolder(private val binding: ItemLinearHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myDiary: Pair<MyDiary, Category?>) {
            binding.apply {
                this.myDiary = myDiary.first
                category = myDiary.second
                viewModel = homeViewModel
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyLinearViewHolder -> getItem(position)?.let { holder.bind(it) }
            is MyGirdViewHolder -> getItem(position)?.let { holder.bind(it.first) }
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

    override fun getItemViewType(position: Int): Int {
        val type = if (layoutManager?.spanCount == HomeLayoutType.GRID.spanCount) HomeLayoutType.GRID.ordinal
        else HomeLayoutType.LINEAR.ordinal
        Log.d(TAG, "getItemViewType: $type")
        return type
    }

    fun setLayoutManager(layoutManager: GridLayoutManager?) {
        this.layoutManager = layoutManager
    }


    companion object {
        private const val TAG = "MainHomePagingAdapter"
        private val differCallback = object : DiffUtil.ItemCallback<Pair<MyDiary, Category?>>() {
            override fun areItemsTheSame(oldItem: Pair<MyDiary, Category?>, newItem: Pair<MyDiary, Category?>): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Pair<MyDiary, Category?>, newItem: Pair<MyDiary, Category?>): Boolean = (oldItem == newItem)
        }
    }
}