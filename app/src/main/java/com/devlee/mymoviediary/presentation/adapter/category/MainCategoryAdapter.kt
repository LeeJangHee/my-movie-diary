package com.devlee.mymoviediary.presentation.adapter.category

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.databinding.ItemCategoryBinding
import com.devlee.mymoviediary.databinding.LayoutColorFirstPickerBinding
import com.devlee.mymoviediary.databinding.LayoutColorPickerBinding
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil
import com.devlee.mymoviediary.utils.dialog.CustomDialog
import com.devlee.mymoviediary.utils.getColorRes
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
            // 추가 버튼 클릭
            addBinding.categoryItem.setOnClickListener {
                setEditMode(true)
            }

            // 색상 선택
            addBinding.categoryEditColorPicker.setOnClickListener {
                fun allColorPickDialog() {
                    val colorPickerView = LayoutInflater.from(it.context).inflate(R.layout.layout_color_picker, null, false)
                    val colorView: ColorPickerView = colorPickerView.findViewById(R.id.colorPickerView)
                    var colorEnvelope: ColorEnvelope? = null
                    colorView.attachBrightnessSlider(colorPickerView.findViewById(R.id.colorPickerBrightnessView))
                    colorView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
                        Log.e(TAG, "#${envelope?.hexCode}")
                        colorEnvelope = envelope
                    })

                    CustomDialog.Builder(it.context)
                        .setTitle(R.string.dialog_title_color_pick)
                        .setCustomView(colorPickerView)
                        .setPositiveButton(R.string.ok_kr) {
                            Log.e(TAG, "setPositiveButton ${colorEnvelope?.color}")
                            categoryColor = colorEnvelope?.color
                            // 칼라 선택 후 백그라운드 색상을 바꿔 준다.
                            categoryColor?.let { colorInt ->
                                addBinding.categoryEditColorPicker.setBackgroundColor(colorInt)
                            }
                        }
                        .setNegativeButton(R.string.no_kr) {

                        }
                        .show()
                }

                fun firstColorPickDialog() {
                    val firstColorPickBinding = LayoutColorFirstPickerBinding.inflate(LayoutInflater.from(it.context))
                    firstColorPickBinding.adapter = FirstColorPickAdapter()

                    CustomDialog.Builder(it.context)
                        .setTitle(R.string.dialog_title_color_pick)
                        .setCustomView(firstColorPickBinding.root)
                        .setPositiveButton(R.string.ok_kr) {
                            allColorPickDialog()
                        }
                        .setNegativeButton(R.string.no_kr)
                        .show()
                }

                firstColorPickDialog()
            }

            // 에딧 리스너
            addBinding.categoryEdit.addTextChangedListener {
                if (it.isNullOrEmpty()) {
                    addBinding.categoryEditOk.isEnabled = false
                } else {
                    addBinding.categoryEditOk.isEnabled = true
                    title = it.toString()
                }
            }

            // 확인
            addBinding.categoryEditOk.setOnClickListener {
                if (categoryColor == null) return@setOnClickListener
                val categoryData = Category(
                    title = title,
                    type = CategoryViewType.CATEGORY.type,
                    color = categoryColor,
                    drawableRes = null
                )
                categoryViewModel.insertCategory(CategoryEntity(0, categoryData))
                setEditMode(false)
                addBinding.categoryEdit.text?.clear()
            }

            // 취소
            addBinding.categoryEditCancel.setOnClickListener {
                addBinding.categoryEditColorPicker.setBackgroundColor(getColorRes(it.context, R.color.color_c3c3c3))
                setEditMode(false)
            }
        }

        private fun setEditMode(set: Boolean) {
            categoryViewModel.editMode.set(set)
            addBinding.editMode = categoryViewModel.editMode.get()
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