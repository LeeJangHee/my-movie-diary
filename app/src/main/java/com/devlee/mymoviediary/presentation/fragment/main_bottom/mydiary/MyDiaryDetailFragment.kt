package com.devlee.mymoviediary.presentation.fragment.main_bottom.mydiary

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import coil.load
import coil.size.Scale
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentMyDiaryDetailBinding
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.dp

class MyDiaryDetailFragment : BaseFragment<FragmentMyDiaryDetailBinding>() {

    companion object {
        private const val TAG = "MyDiaryDetailFragment"
    }

    private val starImage: ImageView by lazy { ImageView(requireContext()) }

    override fun getLayoutId(): Int = R.layout.fragment_my_diary_detail

    override fun setView() {
        setAppbar()
    }

    private fun setAppbar() {
        setTitleToolbar(title = "1212")
        setMenuToolbar(type = AppToolbarLayout.LEFT, resId = R.drawable.back_icon) {
            findNavController().popBackStack()
        }
        val linearLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setPadding(0, 0, 11.dp(), 0)
        }

        starImage.apply {
            load(R.drawable.selector_star_icon) {
                scale(Scale.FILL)
            }
            setPadding(5.dp())
            setOnClickListener {

            }
        }

        val moreImage = ImageView(requireContext()).apply {
            load(R.drawable.more_icon) {
                scale(Scale.FILL)
            }
            setPadding(5.dp())
            setOnClickListener {
                Log.i(TAG, "more image click")
            }
        }

        linearLayout.addView(starImage)
        linearLayout.addView(moreImage)
        setMenuToolbar(type = AppToolbarLayout.RIGHT, view = linearLayout)

    }
}