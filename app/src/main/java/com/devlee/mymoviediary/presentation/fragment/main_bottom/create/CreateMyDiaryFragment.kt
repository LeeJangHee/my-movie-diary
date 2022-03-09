package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.FragmentCreateMyDiaryBinding
import com.devlee.mymoviediary.domain.use_case.ChoiceBottomSheetData
import com.devlee.mymoviediary.domain.use_case.ContentChoiceData
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData.Companion.toContentChoiceData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.presentation.activity.main.MainActivity
import com.devlee.mymoviediary.presentation.adapter.create.CreateAdapter
import com.devlee.mymoviediary.presentation.adapter.create.CreateViewType
import com.devlee.mymoviediary.presentation.fragment.BaseFragment
import com.devlee.mymoviediary.presentation.layout.AppToolbarLayout
import com.devlee.mymoviediary.utils.*
import com.devlee.mymoviediary.utils.dialog.calendarDialogCallback
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
class CreateMyDiaryFragment : BaseFragment<FragmentCreateMyDiaryBinding>() {

    companion object {
        private const val TAG = "CreateMyDiaryFragment"
    }

    private val createViewModel: ContentCreateViewModel by navGraphViewModels(R.id.home_nav)
    private val contentCreateAdapter by lazy { CreateAdapter(createViewModel) }

    override fun setView() {
        setAppbar()
        setOnBackPressed()
        isMainBottomNavLayout.value = false

        binding.apply {
            initRecyclerView()
            viewModel = createViewModel.apply {
                // 권한 체크
                deniedPermissionCallback = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + requireActivity().packageName)
                    requireActivity().startActivity(intent)
                    requireActivity().finish()
                }

                // 선택 BottomSheet
                bottomChoiceViewCallback = { bottomChoiceType ->
                    Log.d(TAG, "setView: ${bottomChoiceType.name}")
                    val itemList = mutableListOf<ChoiceBottomSheetData>()
                    lifecycleScope.launch {
                        when (bottomChoiceType) {
                            BottomChoiceType.CONTENT -> {
                                itemList.add(ChoiceBottomSheetData(contentType = ContentType.VIDEO))
                                itemList.add(ChoiceBottomSheetData(contentType = ContentType.AUDIO))
                            }
                            BottomChoiceType.CATEGORY -> {
                                SharedPreferencesUtil.getCategoryListPref().forEach {
                                    itemList.add(ChoiceBottomSheetData(category = it))
                                }
                            }
                        }
                        val action = CreateMyDiaryFragmentDirections.actionCreateMyDiaryFragmentToBottomChoiceFragment(
                            bottomChoiceType,
                            itemList.toTypedArray()
                        )
                        findNavController().navigate(action)
                    }
                }
                contentChoiceDataList.observe(viewLifecycleOwner) {
                    contentCreateAdapter.setDiaryList(it)
                }
            }
            lifecycleOwner = viewLifecycleOwner
            createDateLayout.setOnClickListener(::setupSelectedDate)
        }
        // 날짜 선택
        calendarDialogCallback = { dateStr ->
            Log.d(TAG, "calendarCallback: $dateStr")
            createViewModel.dateStr.set(dateStr)
        }
        // Category 선택
        selectedCategoryCallback = { category ->
            Log.d(TAG, "categoryCallback: ${category.title}")
            createViewModel.selectedCategory.set(category)
        }
        // 파일 선택
        selectedContentCallback = { contentType ->
            delayUiThread(300) {
                val action = CreateMyDiaryFragmentDirections.actionCreateMyDiaryFragmentToContentChoiceFragment(
                    contentType = contentType
                )
                findNavController().navigate(action)
            }
        }
        // 아이템 선택
        selectedMediaItemCallback = ::updateChoiceMediaData
    }

    override fun onResume() {
        super.onResume()
        if (createViewModel.dateStr.get() == null) {
            createViewModel.dateStr.set(DateFormatUtil.getTodayDate())
        }
        Log.d(TAG, "onResume: dateStr = ${createViewModel.dateStr.get()}")
    }

    private fun FragmentCreateMyDiaryBinding.initRecyclerView() {
        createRecyclerView.adapter = contentCreateAdapter.apply {
            setDiaryList(arrayListOf(ContentChoiceData(CreateViewType.ADD.type)))
        }
    }

    private fun setupSelectedDate(view: View) {
        view.findNavController().navigate(R.id.action_createMyDiaryFragment_to_calendarSelectedFragment)
    }

    private fun updateChoiceMediaData() {
        Log.i(TAG, "updateChoiceMediaData-()")
        val convertList = arrayListOf(ContentChoiceData(CreateViewType.ADD.type))
        // Video or Audio 선택
        with(createViewModel) {
            fileList.forEach {
                convertList.add(it.toContentChoiceData())
            }
            contentChoiceDataList.postValue(convertList)
        }

    }

    private fun setOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                (requireActivity() as MainActivity).isBottomNav(true)
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.fragment_create_my_diary

    private fun setAppbar() {
        setTitleToolbar(title = getString(R.string.home_create_toolbar_title))
        setMenuToolbar(type = AppToolbarLayout.LEFT, strId = R.string.no_kr) {
            findNavController().popBackStack()
            (requireActivity() as MainActivity).isBottomNav(true)
        }
        setMenuToolbar(type = AppToolbarLayout.RIGHT, strId = R.string.next_kr) {
            findNavController().navigate(R.id.action_createMyDiaryFragment_to_moodFragment)
        }
    }
}

enum class BottomChoiceType {
    CONTENT, CATEGORY
}