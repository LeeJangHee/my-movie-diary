package com.devlee.mymoviediary.presentation.adapter.create

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.ItemDialogCalendarYearBinding
import com.devlee.mymoviediary.utils.DateFormatUtil
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.toDp
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import java.time.Year

@SuppressLint("NewApi")
class DialogYearAdapter(
    val requireActivity: FragmentActivity,
    val yearList: MutableList<Year>,
    val listener: (Year) -> Unit
) : RecyclerView.Adapter<DialogYearAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "DialogYearAdapter"
    }

    private var selectedYear: Year = Year.now()

    inner class ViewHolder(val binding: ItemDialogCalendarYearBinding) : RecyclerView.ViewHolder(binding.root) {

        private fun ItemDialogCalendarYearBinding.selectedYearStyle(year: Year) = with(yearItemTextView) {
            background = null
            setTextColor(getColorRes(requireActivity, R.color.color_1c1c1c))

            if (selectedYear == year) {
                val shapeModelRound = ShapeAppearanceModel().toBuilder().apply {
                    setAllCorners(CornerFamily.ROUNDED, 17.toDp())
                }.build()

                val shape = MaterialShapeDrawable(shapeModelRound).apply {
                    val backgroundColor = getColorRes(requireActivity, R.color.color_1c1c1c)
                    fillColor = ColorStateList.valueOf(backgroundColor)
                }

                background = shape
                setTextColor(getColorRes(requireActivity, R.color.white))
            }
        }


        fun bind(item: Year) {
            binding.apply {
                yearString = DateFormatUtil.getYear(item.atDay(1))
                selectedYearStyle(item)
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
        val year = yearList[position]
        holder.bind(year)
        holder.binding.root.setOnClickListener {
            listener.invoke(year)
            selectedItem(year)
        }
    }

    override fun getItemCount(): Int = yearList.size

    fun selectedItem(newYear: Year) {
        selectedYear = newYear
        notifyDataSetChanged()
    }


}