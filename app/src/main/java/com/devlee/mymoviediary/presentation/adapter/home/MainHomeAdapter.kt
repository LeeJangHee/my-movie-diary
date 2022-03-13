package com.devlee.mymoviediary.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.databinding.ItemMainBinding
import com.devlee.mymoviediary.utils.recyclerview.MyDiaryDiffUtil

class MainHomeAdapter : RecyclerView.Adapter<MainHomeAdapter.MyViewHolder>() {

    private var myDiaryList = emptyList<MyDiary>()

    inner class MyViewHolder(private val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myDiary: MyDiary) {
            binding.apply {
                this.myDiary = myDiary
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMainBinding.inflate(layoutInflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, i: Int) {
        holder.bind(myDiaryList[i])
    }

    override fun getItemCount(): Int = myDiaryList.size

    fun setData(newDiaryList: List<MyDiary>) {
        val mainDiffUtil = MyDiaryDiffUtil(myDiaryList, newDiaryList)
        val diffUtilResult = DiffUtil.calculateDiff(mainDiffUtil)
        myDiaryList = newDiaryList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}