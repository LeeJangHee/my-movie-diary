package com.devlee.mymoviediary.presentation.fragment.main_bottom.mydiary

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.FragmentMyDiaryDetailBinding
import com.devlee.mymoviediary.presentation.adapter.home.mydiary.MyDiaryDetailAudioAdapter
import com.devlee.mymoviediary.presentation.adapter.home.mydiary.MyDiaryDetailVideoAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.dp
import com.devlee.mymoviediary.utils.recyclerview.CustomDecoration
import com.devlee.mymoviediary.utils.toDp
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory
import com.google.android.material.tabs.TabLayoutMediator

class MyDiaryDetailFragment : BaseFragment<FragmentMyDiaryDetailBinding>() {

    companion object {
        private const val TAG = "MyDiaryDetailFragment"
    }

    private val starImage: ImageView by lazy { ImageView(requireContext()) }
    private val args: MyDiaryDetailFragmentArgs by navArgs()
    private val diaryDetailViewModel: MyDiaryViewModel by viewModels {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }
    private val detailAudioAdapter: MyDiaryDetailAudioAdapter by lazy { MyDiaryDetailAudioAdapter() }
    private val detailVideoAdapter: MyDiaryDetailVideoAdapter by lazy { MyDiaryDetailVideoAdapter() }
    private val audioItemDecoration by lazy {
        CustomDecoration(
            height = 1.toDp(),
            paddingLeft = 16.toDp(),
            paddingRight = 0f,
            color = requireContext().resources.getColor(R.color.color_efefef, null)
        )
    }

    override fun setView() {
        setAppbar()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            myDiary = args.myDiary
            category = args.category
            setRecyclerView()
        }
    }

    private fun FragmentMyDiaryDetailBinding.setRecyclerView() {
        diaryDetailViewPager.apply {
            adapter = detailVideoAdapter
            detailVideoAdapter.submitList(args.myDiary.video.map { it?.toUri() })
            diaryDetailIndicator3.setViewPager(this)
        }

        diaryDetailAudioRecycler.apply {
            adapter = detailAudioAdapter
            detailAudioAdapter.submitList(args.myDiary.recording.map { it?.toUri() })
        }
    }

    private fun setAppbar() {
        setTitleToolbar(title = args.myDiary.date)
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

    override fun getLayoutId(): Int = R.layout.fragment_my_diary_detail
}