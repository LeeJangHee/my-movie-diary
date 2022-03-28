package com.devlee.mymoviediary.presentation.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.MyDiary
import com.devlee.mymoviediary.databinding.ItemCategoryBinding
import com.devlee.mymoviediary.databinding.ItemLinearHomeBinding
import com.devlee.mymoviediary.presentation.fragment.main_bottom.SearchType
import com.devlee.mymoviediary.utils.recyclerview.MyDiaryDiffUtil

class SearchAdapter(private val searchType: SearchType) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var searchCategoryItemList: List<Category> = emptyList()
    private var searchMainItemList: List<MyDiary> = emptyList()

    inner class MainSearchViewHolder(private val binding: ItemLinearHomeBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(myDiary: MyDiary) {
            binding.apply {
                this.myDiary = myDiary
                executePendingBindings()
            }
        }
    }

    inner class CategorySearchViewHolder(private val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                categoryModel = category
                editMode = false
                executePendingBindings()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when(viewType) {
            SearchType.Main.ordinal -> MainSearchViewHolder(ItemLinearHomeBinding.inflate(layoutInflater, parent, false))
            else -> CategorySearchViewHolder(ItemCategoryBinding.inflate(layoutInflater, parent, false))

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is MainSearchViewHolder -> holder.bind(searchMainItemList[i])
            is CategorySearchViewHolder -> holder.bind(searchCategoryItemList[i])
        }
    }

    override fun getItemCount(): Int {
        return when (searchType) {
            SearchType.Main -> searchMainItemList.size
            else -> searchCategoryItemList.size
        }
    }

    override fun getItemViewType(position: Int): Int = searchType.ordinal


    fun setSearchCategoryItemList(newItemList: List<Category>) {
        val searchDiffUtil = MyDiaryDiffUtil(searchCategoryItemList, newItemList)
        val diffUtilResult = DiffUtil.calculateDiff(searchDiffUtil)
        searchCategoryItemList = newItemList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun setSearchMainList(newItemList: List<MyDiary>) {
        val searchDiffUtil = MyDiaryDiffUtil(searchMainItemList, newItemList)
        val diffUtilResult = DiffUtil.calculateDiff(searchDiffUtil)
        searchMainItemList = newItemList
        diffUtilResult.dispatchUpdatesTo(this)
    }

}