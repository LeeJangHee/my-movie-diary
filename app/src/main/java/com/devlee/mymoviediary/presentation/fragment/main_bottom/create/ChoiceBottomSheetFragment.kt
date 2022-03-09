package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.BottomChoiceViewBinding
import com.devlee.mymoviediary.presentation.adapter.create.CreateBottomSheetAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseBottomSheetFragment
import com.devlee.mymoviediary.utils.recyclerview.CustomLinearLayoutManager
import com.devlee.mymoviediary.utils.selectedCategoryCallback
import com.devlee.mymoviediary.utils.selectedContentCallback
import com.devlee.mymoviediary.utils.startFadeInAnimation
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ChoiceBottomSheetFragment : BaseBottomSheetFragment<BottomChoiceViewBinding>(R.layout.bottom_choice_view) {

    companion object {
        private const val TAG = "ChoiceBottomSheet"
    }

    private val bottomSheetViewModel: ContentCreateViewModel by navGraphViewModels(R.id.home_nav)

    private val choiceBottomSheetAdapter by lazy { CreateBottomSheetAdapter(args.bottomChoiceType, bottomSheetViewModel) }

    private val isScroll: Boolean by lazy { args.choiceBottomSheetList.size >= 4 }

    private val args: ChoiceBottomSheetFragmentArgs by navArgs()

    override fun setView() {
        Log.d(TAG, "setView: ")
        binding.apply {
            viewModel = bottomSheetViewModel.apply {
                // 선택 categoryItem
                selectedCategoryItem = { category ->
                    Log.d(TAG, "selectedCategory: ${category.title}")
                    selectedCategoryCallback?.invoke(category)
                    dismiss()
                }
                selectedContentItem = { contentType ->
                    Log.d(TAG, "selectedContentType: ${contentType.name}")
                    selectedContentCallback?.invoke(contentType)
                    dismiss()
                }
            }
            lifecycleOwner = viewLifecycleOwner
            cancelString = requireContext().getString(R.string.no_kr)
            bottomChoiceCancel.bottomSheetItemClickListener = View.OnClickListener { dismiss() }
        }

        setBottomChoiceType()
        choiceBottomSheetAdapter.setBottomSheetItem(args.choiceBottomSheetList.toList())
        setRecyclerView()
        setTopCornerRadius(4, 4)
    }

    private fun setBottomChoiceType() {
        if (args.bottomChoiceType == BottomChoiceType.CATEGORY) {
            // Category 추가할 때, 취소버튼 y좌표

            val dy = if (args.choiceBottomSheetList.size < 4) {
                requireContext().resources.getDimension(R.dimen.dp_56) * args.choiceBottomSheetList.size
            } else {
                requireContext().resources.getDimension(R.dimen.dp_56) * 4
            }
            binding.bottomChoiceCancelView.y = dy
            val layoutParams = (binding.root.parent as View).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = layoutParams.behavior
            if (behavior is BottomSheetBehavior) {
                behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                Log.d(TAG, "onStateChanged: STATE_HIDDEN")
                                dismissAllowingStateLoss()
                            }
                            BottomSheetBehavior.STATE_COLLAPSED -> {
                                Log.d(TAG, "onStateChanged: STATE_COLLAPSED")
                                binding.bottomChoiceCancelView.apply {
                                    startFadeInAnimation()
                                    y = behavior.peekHeight - requireContext().resources.getDimension(R.dimen.dp_56)
                                    visibility = View.VISIBLE
                                }
                            }
                            BottomSheetBehavior.STATE_DRAGGING -> {
                                Log.d(TAG, "onStateChanged: STATE_DRAGGING")
                                binding.bottomChoiceCancelView.apply {
                                    visibility = View.GONE
                                }
                            }
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                Log.d(TAG, "onStateChanged: STATE_EXPANDED")
                                binding.bottomChoiceCancelView.apply {
                                    startFadeInAnimation()
                                    y = binding.root.height - requireContext().resources.getDimension(R.dimen.dp_56)
                                    visibility = View.VISIBLE
                                }
                            }
                            BottomSheetBehavior.STATE_SETTLING -> Log.d(TAG, "onStateChanged: STATE_SETTLING")
                            BottomSheetBehavior.STATE_HALF_EXPANDED -> Log.d(TAG, "onStateChanged: STATE_HALF_EXPANDED")
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                })
                behavior.peekHeight = (requireContext().resources.getDimension(R.dimen.dp_56) * 5).toInt()
            }
        } else {
            // Content 추가할 때, 취소버튼 y좌표
            binding.bottomChoiceCancelView.y = requireContext().resources.getDimension(R.dimen.dp_56) * 2
        }
    }

    private fun setRecyclerView() {
        binding.bottomChoiceRecycler.apply {
            layoutManager = CustomLinearLayoutManager(requireContext(), isScroll)
            adapter = choiceBottomSheetAdapter
        }
    }
}