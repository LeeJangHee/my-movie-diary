package com.devlee.mymoviediary.presentation.adapter.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.databinding.ItemCategoryBinding
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil

class MainCategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var categoryList = listOf<Category>()

    // 카테고리 리스트
    class CategoryHolder(private val categoryBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(categoryBinding.root) {
        fun categoryBind(category: Category) {
            categoryBinding.apply {
                categoryModel = category
                executePendingBindings()
            }
        }
    }

    // 전체보기, 즐겨찾기
    class DefaultHolder(private val defaultBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(defaultBinding.root) {
        fun defaultBind(category: Category) {
            defaultBinding.apply {
                categoryModel = category
                executePendingBindings()
            }
        }
    }

    // 카테고리 추가
    class AddHolder(private val addBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(addBinding.root) {
        fun addBinding(category: Category) {
            addBinding.apply {
                categoryModel = category
                executePendingBindings()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)
        return when (viewType) {
            CategoryViewType.DEFAULT.type -> DefaultHolder(binding)
            CategoryViewType.CATEGORY.type -> CategoryHolder(binding)
            CategoryViewType.ADD.type -> AddHolder(binding)
            else -> CategoryHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DefaultHolder -> {
                holder.defaultBind(categoryList[position])
            }
            is CategoryHolder -> {
                holder.categoryBind(categoryList[position])
            }
            is AddHolder -> {
                holder.addBinding(categoryList[position])
            }
        }
    }

    override fun getItemCount(): Int = categoryList.size


    override fun getItemViewType(position: Int): Int {
        return categoryList[position].type
    }

    fun setCategoryList(newCategoryList: List<Category>) {
        val categoryDiffUtil = MyDiaryDiffUtil(categoryList, newCategoryList)
        val diffUtilResult = DiffUtil.calculateDiff(categoryDiffUtil)
        categoryList = newCategoryList
        diffUtilResult.dispatchUpdatesTo(this)
    }

}

enum class CategoryViewType(val type: Int) {
    DEFAULT(0),
    CATEGORY(1),
    ADD(2)
}