package com.devlee.mymoviediary.presentation.adapter.create

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemDialogCalendarYearBinding
import com.devlee.mymoviediary.utils.DateFormatUtil
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil
import java.time.Year

@SuppressLint("NewApi")
class DialogYearAdapter : RecyclerView.Adapter<DialogYearAdapter.ViewHolder>() {

    private var yearList: List<Year> = listOf()

    inner class ViewHolder(val binding: ItemDialogCalendarYearBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Year) {
            binding.apply {
                yearString = DateFormatUtil.getYear(item.atDay(1))
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDialogCalendarYearBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(yearList[position])
    }

    override fun getItemCount(): Int = yearList.size


    fun setYearList(newItem: List<Year>) {
        val yearDiffUtil = MyDiaryDiffUtil(yearList, newItem)
        val diffUtilResult = DiffUtil.calculateDiff(yearDiffUtil)
        yearList = newItem
        diffUtilResult.dispatchUpdatesTo(this)
    }


}