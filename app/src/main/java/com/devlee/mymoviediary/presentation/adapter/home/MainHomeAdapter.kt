package com.devlee.mymoviediary.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemMainListBinding
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil

class MainHomeAdapter: RecyclerView.Adapter<MainHomeAdapter.MyViewHolder>() {

    private var myDiaryList = emptyList<Int>()

    inner class MyViewHolder(private val binding: ItemMainListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {

                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMainListBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = myDiaryList.size

    fun setData(newDiaryList: List<Int>) {
        val mainDiffUtil = MyDiaryDiffUtil(myDiaryList, newDiaryList)
        val diffUtilResult = DiffUtil.calculateDiff(mainDiffUtil)
        myDiaryList = newDiaryList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}