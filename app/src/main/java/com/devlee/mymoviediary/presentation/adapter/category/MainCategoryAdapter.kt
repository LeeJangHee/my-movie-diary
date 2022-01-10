package com.devlee.mymoviediary.presentation.adapter.category

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.databinding.ItemCategoryBinding
import com.devlee.mymoviediary.databinding.LayoutColorFirstPickerBinding
import com.devlee.mymoviediary.utils.MyDiaryDiffUtil
import com.devlee.mymoviediary.utils.categoryErrorView
import com.devlee.mymoviediary.utils.categoryFirstItemClick
import com.devlee.mymoviediary.utils.dialog.CustomDialog
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainCategoryAdapter(
    val requireActivity: FragmentActivity,
    val categoryViewModel: MyDiaryViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "MainCategoryAdapter"
    private val MAX_CATEGORY_NAME_BYTE = 20

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
    @SuppressLint("ShowToast")
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
                        Log.d(TAG, "#${envelope?.hexCode}")
                        colorEnvelope = envelope
                    })

                    CustomDialog.Builder(it.context)
                        .setTitle(R.string.dialog_title_color_pick)
                        .setCustomView(colorPickerView)
                        .setPositiveButton(R.string.ok_kr) {
                            Log.d(TAG, "setPositiveButton ${colorEnvelope?.color}")
                            setColorPick(colorEnvelope?.color)
                        }
                        .setNegativeButton(R.string.no_kr)
                        .show()
                }

                fun firstColorPickDialog() {
                    val firstColorPickBinding = LayoutColorFirstPickerBinding.inflate(LayoutInflater.from(it.context))
                    firstColorPickBinding.adapter = FirstColorPickAdapter()

                    val customDialog = CustomDialog.Builder(it.context)
                        .setTitle(R.string.dialog_title_color_pick)
                        .setCustomView(firstColorPickBinding.root)
                        .setPositiveButton(R.string.ok_kr) {
                            allColorPickDialog()
                        }
                        .setNegativeButton(R.string.no_kr)
                        .show()

                    categoryFirstItemClick = { color ->
                        setColorPick(color)
                        customDialog.dismiss()
                    }
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

                val categoryNameBytes = title.toByteArray(charset("euc-kr"))
                // 최대, 최소 길이
                if (categoryNameBytes.size < 1 || categoryNameBytes.size > MAX_CATEGORY_NAME_BYTE) {
                    categoryErrorView?.invoke(addBinding.root)
                    return@setOnClickListener
                }
                if (categoryColor == null) {
                    categoryColor = getColorRes(addBinding.root.context, R.color.color_c3c3c3)
                }
                val categoryData = Category(
                    title = title,
                    type = CategoryViewType.CATEGORY.type,
                    color = categoryColor,
                    drawableRes = null
                )
                categoryViewModel.insertCategory(CategoryEntity(0, categoryData))
                reset()
            }

            // 취소
            addBinding.categoryEditCancel.setOnClickListener {
                reset()
            }
        }

        private fun reset() {
            setEditMode(false)
            addBinding.categoryEdit.text?.clear()
            setColorPick(getColorRes(requireActivity, R.color.color_c3c3c3))
        }

        private fun setColorPick(color: Int?) {
            categoryColor = color
            categoryColor?.let {
                addBinding.categoryEditColorPicker.setBackgroundColor(it)
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