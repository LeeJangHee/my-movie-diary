package com.devlee.mymoviediary.presentation.fragment.main_bottom.mydiary

import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.core.view.setPadding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.devlee.mymoviediary.viewmodels.MyDiaryDetailViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyDiaryDetailFragment : BaseFragment<FragmentMyDiaryDetailBinding>() {

    companion object {
        private const val TAG = "MyDiaryDetailFragment"
    }

    private val starImage: ImageView by lazy { ImageView(requireContext()) }
    private val args: MyDiaryDetailFragmentArgs by navArgs()
    private val diaryDetailViewModel: MyDiaryDetailViewModel by viewModels {
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
        diaryDetailViewModel.setMyDiaryDetail(args.myDiary)
        diaryDetailViewModel.setMyDiaryDetailCategory(args.category)
        initData()
    }

    private fun initData() {
        lifecycleScope.launchWhenResumed {
            diaryDetailViewModel.apply {
                myDiaryDetail.collectLatest { myDiary ->
                    Log.d(TAG, "myDiaryDetail : $myDiary")
                    binding.myDiary = myDiary
                    detailVideoAdapter.submitList(myDiary.video.map { it?.toUri() })
                    detailAudioAdapter.submitList(myDiary.recording.map { it?.toUri() })
                }

                myDiaryDetailCategory.collectLatest { category ->
                    Log.d(TAG, "myDiaryDetailCategory : $category")
                    binding.category = category
                }
            }
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
            val myDiary = args.myDiary
            isSelected = myDiary.star
            load(R.drawable.selector_star_icon) {
                scale(Scale.FILL)
            }
            setPadding(5.dp())
            setOnClickListener {
                isSelected = !isSelected
                lifecycleScope.launch(Dispatchers.IO) {
                    val myDiaryId = diaryDetailViewModel.getMyDiaryId(myDiary) ?: return@launch
                    val categoryId = diaryDetailViewModel.getCategoryId(binding.category)
                    Log.d(TAG, "star click : $myDiary")

                    myDiary.star = isSelected

                    diaryDetailViewModel.updateMyDiary(
                        myDiaryId = myDiaryId,
                        categoryId = categoryId,
                        myDiary = myDiary
                    )
                }
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