package com.devlee.mymoviediary.presentation.adapter.category

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.entity.CategoryEntity
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.databinding.ItemCategoryBinding
import com.devlee.mymoviediary.databinding.LayoutColorFirstPickerBinding
import com.devlee.mymoviediary.utils.categoryErrorView
import com.devlee.mymoviediary.utils.categoryFirstItemClick
import com.devlee.mymoviediary.utils.categoryUserPickItemClick
import com.devlee.mymoviediary.utils.dialog.CustomDialog
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.recyclerview.CategoryTouchCallback
import com.devlee.mymoviediary.utils.recyclerview.MyDiaryDiffUtil
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainCategoryAdapter(
    val requireActivity: FragmentActivity,
    val categoryViewModel: MyDiaryViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "MainCategoryAdapter"
        private const val MAX_CATEGORY_NAME_BYTE = 20
    }

    private var categoryList = listOf<Pair<Category, Int>>()

    private var itemTouchCallback: CategoryTouchCallback? = null

    // Edit mode 변수
    private var categoryColor: Int? = null
    private var title: String = ""

    // 카테고리 리스트
    inner class CategoryHolder(private val categoryBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(categoryBinding.root) {
        val viewType = CategoryViewType.CATEGORY.type

        init {
            // 삭제버튼
            categoryBinding.categoryMenuDel.setOnClickListener {
                // 삭제하기 전에 아이템을 원래 위치로 돌리고 삭제한다.
                itemTouchCallback?.removePreviousClamp(this)

                CustomDialog.Builder(it.context)
                    .setTitle(R.string.category_delete_title)
                    .setMessage(R.string.category_delete_message)
                    .setPositiveButton(R.string.ok_kr, R.color.color_ff3939) {
                        // 삭제
                        categoryViewModel.deleteCategory(categoryList[bindingAdapterPosition].first)
                    }
                    .setNegativeButton(R.string.no_kr)
                    .show()
            }

            // 편집 버튼
            categoryBinding.categoryMenuChange.setOnClickListener {
                itemTouchCallback?.removePreviousClamp(this)
                setEditMode(categoryBinding, true, categoryList[bindingAdapterPosition].first)
            }

            categoryBinding.categoryEdit.addTextWatch(categoryBinding)

            // 취소
            categoryBinding.categoryEditCancel.setOnClickListener { reset(categoryBinding) }

            // 색상 선택
            categoryBinding.categoryEditColorPicker.setOnClickListener { showColorPickerDialog(categoryBinding) }

            // 확인
            categoryBinding.categoryEditOk.setOnClickListener {
                if (!isVerifyText(it)) return@setOnClickListener

                val preCategory = categoryList[bindingAdapterPosition].first
                val category = Category(
                    title = title,
                    type = CategoryViewType.CATEGORY.type,
                    color = categoryColor,
                    drawableRes = null
                )
                categoryViewModel.updateCategory(category, preCategory, category.title)
                reset(categoryBinding)
            }
        }

        fun categoryBind(category: Category, categoryCount: Int?) {
            categoryBinding.apply {
                categoryModel = category
                this.categoryCount = categoryCount
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

        init {
            // 추가 버튼 클릭
            addBinding.categoryItem.setOnClickListener {
                setMode(addBinding, true)
            }

            // 색상 선택
            addBinding.categoryEditColorPicker.setOnClickListener { showColorPickerDialog(addBinding) }

            // 에딧 리스너
            addBinding.categoryEdit.addTextWatch(addBinding)

            // 확인
            addBinding.categoryEditOk.setOnClickListener {

                if (!isVerifyText(it)) return@setOnClickListener

                if (categoryColor == null) {
                    categoryColor = getColorRes(addBinding.root.context, R.color.color_c3c3c3)
                }
                val categoryData = Category(
                    title = title,
                    type = CategoryViewType.CATEGORY.type,
                    color = categoryColor,
                    drawableRes = null
                )
                categoryViewModel.insertCategory(
                    CategoryEntity(
                        categoryId = 0,
                        title = title,
                        category = categoryData
                    )
                )
                reset(addBinding)
            }

            // 취소
            addBinding.categoryEditCancel.setOnClickListener { reset(addBinding) }
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
                holder.defaultBind(categoryList[position].first)
            }
            is CategoryHolder -> {
                holder.categoryBind(categoryList[position].first, categoryList[position].second)
            }
            is AddHolder -> {
                holder.addBinding(categoryList[position].first)
            }
        }
    }

    override fun getItemCount(): Int = categoryList.size


    override fun getItemViewType(position: Int): Int = categoryList[position].first.type


    fun setCategoryList(newCategoryList: List<Pair<Category, Int>>) {
        val categoryDiffUtil = MyDiaryDiffUtil(categoryList, newCategoryList)
        val diffUtilResult = DiffUtil.calculateDiff(categoryDiffUtil)
        categoryList = newCategoryList
        diffUtilResult.dispatchUpdatesTo(this)
    }

    fun setItemTouchCallback(itemTouchCallback: CategoryTouchCallback) {
        this.itemTouchCallback = itemTouchCallback
    }

    private fun reset(binding: ItemCategoryBinding) {
        setMode(binding, false)
        binding.categoryEdit.text?.clear()
        setColorPick(binding, getColorRes(requireActivity, R.color.color_c3c3c3))
    }

    private fun setColorPick(binding: ItemCategoryBinding, color: Int?) {
        categoryColor = color
        categoryColor?.let {
            binding.categoryEditColorPicker.setBackgroundColor(it)
        }
    }

    private fun setMode(binding: ItemCategoryBinding, set: Boolean) {
        categoryViewModel.editMode.set(set)
        binding.editMode = categoryViewModel.editMode.get()
    }

    private fun setEditMode(binding: ItemCategoryBinding, set: Boolean, category: Category) {
        setMode(binding, set)
        binding.apply {
            categoryEdit.setText(category.title)
            setColorPick(this, category.color)
        }
    }

    private fun EditText.addTextWatch(binding: ItemCategoryBinding) = apply {
        addTextChangedListener {
            if (it.isNullOrEmpty()) {
                binding.categoryEditOk.isEnabled = false
            } else {
                binding.categoryEditOk.isEnabled = true
                title = it.toString()
            }
        }
    }

    private fun showColorPickerDialog(binding: ItemCategoryBinding) {
        fun allColorPickDialog() {
            val colorPickerView = LayoutInflater.from(binding.root.context).inflate(R.layout.layout_color_picker, null, false)
            val colorView: ColorPickerView = colorPickerView.findViewById(R.id.colorPickerView)
            var colorEnvelope: ColorEnvelope? = null
            colorView.attachBrightnessSlider(colorPickerView.findViewById(R.id.colorPickerBrightnessView))
            colorView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
                Log.d(TAG, "#${envelope?.hexCode}")
                colorEnvelope = envelope
            })

            CustomDialog.Builder(binding.root.context)
                .setTitle(R.string.dialog_title_color_pick)
                .setCustomView(colorPickerView)
                .setPositiveButton(R.string.ok_kr) {
                    Log.d(TAG, "setPositiveButton ${colorEnvelope?.color}")
                    setColorPick(binding, colorEnvelope?.color)
                }
                .setNegativeButton(R.string.no_kr)
                .show()
        }

        fun firstColorPickDialog() {
            val firstColorPickBinding = LayoutColorFirstPickerBinding.inflate(LayoutInflater.from(binding.root.context))
            firstColorPickBinding.viewModel = categoryViewModel
            Log.d(TAG, "tablelayout width ${firstColorPickBinding.root.width}")
            val customDialog = CustomDialog.Builder(binding.root.context)
                .setTitle(R.string.dialog_title_color_pick)
                .setCustomView(firstColorPickBinding.root)
                .setPositiveButton(R.string.ok_kr) {
                    allColorPickDialog()
                }
                .setNegativeButton(R.string.no_kr)
                .show()

            // 선택 아이탬 클릭
            categoryFirstItemClick = { color ->
                setColorPick(binding, color)
                customDialog.dismiss()
            }

            categoryUserPickItemClick = {
                allColorPickDialog()
                customDialog.dismiss()
            }
        }

        firstColorPickDialog()
    }

    private fun isVerifyText(view: View): Boolean {
        val categoryNameBytes = title.toByteArray(charset("euc-kr"))
        // 최대, 최소 길이
        if (categoryNameBytes.size < 1 || categoryNameBytes.size > MAX_CATEGORY_NAME_BYTE) {
            categoryErrorView?.invoke(view)
            return false
        }
        return true
    }

}

enum class CategoryViewType(val type: Int) {
    DEFAULT(0),
    CATEGORY(1),
    ADD(2)
}