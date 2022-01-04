package com.devlee.mymoviediary.presentation.adapter.category

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.databinding.ItemCategoryBinding
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil
import com.devlee.mymoviediary.utils.dialog.CustomDialog
import com.devlee.mymoviediary.utils.hideKeyboardIME
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainCategoryAdapter(
    val categoryViewModel: MyDiaryViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MainCategoryAdapter"

    private var categoryList = listOf<Category>()

    // 카테고리 리스트
    inner class CategoryHolder(private val categoryBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(categoryBinding.root) {
        val viewType = CategoryViewType.CATEGORY.type

        fun categoryBind(category: Category) {
            categoryBinding.apply {
                categoryModel = category
                executePendingBindings()
            }
        }
    }

    // 전체보기, 즐겨찾기
    inner class DefaultHolder(private val defaultBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(defaultBinding.root) {
        val viewType = CategoryViewType.DEFAULT.type

        fun defaultBind(category: Category) {
            defaultBinding.apply {
                categoryModel = category
                executePendingBindings()
            }
        }
    }

    // 카테고리 추가
    inner class AddHolder(private val addBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(addBinding.root) {
        val viewType = CategoryViewType.ADD.type
        var categoryColor: Int? = null
        var title: String = ""

        init {
            addBinding.categoryItem.setOnClickListener {
                categoryViewModel.editMode.set(true)
                addBinding.editMode = categoryViewModel.editMode.get()
            }

            addBinding.categoryEditColorPicker.setOnClickListener {
                val colorPickerView = LayoutInflater.from(it.context).inflate(R.layout.layout_color_picker, null, false)
                val colorView: ColorPickerView = colorPickerView.findViewById(R.id.colorPickerView)
                var hexColor: String? = null
                var colorEnvelope: ColorEnvelope? = null
                colorView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
                    Log.e(TAG, "#${envelope?.hexCode}")
                    hexColor = "#${envelope?.hexCode}"
                    colorEnvelope = envelope
                })

                CustomDialog.Builder(addBinding.root.context)
                    .setTitle("ColorPicker")
                    .setCustomView(colorPickerView)
                    .setPositiveButton("확인") {
                        Log.e(TAG, "setPositiveButton ${colorEnvelope?.color}")
                        categoryColor = colorEnvelope?.color
                    }
                    .setNegativeButton("취소") {

                    }
                    .show()
            }

            addBinding.categoryEdit.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    addBinding.categoryEditOk.isEnabled = false
                } else {
                    addBinding.categoryEditOk.isEnabled = true
                    title = it.toString()
                }
            }

            addBinding.categoryEditOk.setOnClickListener {
                if (categoryColor == null) return@setOnClickListener
                val categoryData = Category(
                    title = title,
                    type = CategoryViewType.CATEGORY.type,
                    color = categoryColor,
                    drawableRes = null
                )
                categoryViewModel.insertCategory(CategoryEntity(0, categoryData))
                categoryViewModel.editMode.set(false)
                addBinding.categoryEdit.text?.clear()
                it.hideKeyboardIME()
            }
        }

        fun addBinding(category: Category) {
            addBinding.apply {
                categoryModel = category
                editMode = categoryViewModel.editMode.get()
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


    override fun getItemViewType(position: Int): Int = categoryList[position].type


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