package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.BottomChoiceViewBinding
import com.devlee.mymoviediary.presentation.adapter.create.CreateBottomSheetAdapter
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.recyclerview.CustomLinearLayoutManager
import com.devlee.mymoviediary.utils.selectedCategoryCallback
import com.devlee.mymoviediary.utils.toDp
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class ChoiceBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "ChoiceBottomSheet"
    }

    private var _binding: BottomChoiceViewBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetViewModel: ContentCreateViewModel by viewModels()

    private val choiceBottomSheetAdapter by lazy { CreateBottomSheetAdapter(args.bottomChoiceType, bottomSheetViewModel) }

    private val isScroll: Boolean by lazy { args.choiceBottomSheetList.size >= 4 }

    private val args: ChoiceBottomSheetFragmentArgs by navArgs()

    /** Animation */
    private val fadeIn by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in) }
    private val fadeOut by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheet_Base_Light)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomChoiceViewBinding.inflate(inflater, container, false).apply {
            viewModel = bottomSheetViewModel.apply {
                // 선택 categoryItem
                selectedCategoryItem = { category ->
                    Log.d(TAG, "selectedCategory: ${category.title}")
                    selectedCategoryCallback?.invoke(category)
                    dismiss()
                }
            }
            lifecycleOwner = viewLifecycleOwner
            cancelString = requireContext().getString(R.string.no_kr)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

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
                                    startAnimation(fadeIn)
                                    y = behavior.peekHeight - requireContext().resources.getDimension(R.dimen.dp_56)
                                    visibility = View.VISIBLE
                                }
                            }
                            BottomSheetBehavior.STATE_DRAGGING -> {
                                Log.d(TAG, "onStateChanged: STATE_DRAGGING")
                                binding.bottomChoiceCancelView.apply {
//                                    startAnimation(fadeOut)
                                    visibility = View.GONE
                                }
                            }
                            BottomSheetBehavior.STATE_EXPANDED -> {
                                Log.d(TAG, "onStateChanged: STATE_EXPANDED")
                                binding.bottomChoiceCancelView.apply {
                                    startAnimation(fadeIn)
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

        choiceBottomSheetAdapter.setBottomSheetItem(args.choiceBottomSheetList.toList())
        setRecyclerView()
        setCornerRadius(view)
    }

    private fun setCornerRadius(view: View) {
        val model = ShapeAppearanceModel().toBuilder().apply {
            setTopRightCorner(CornerFamily.ROUNDED, 4.toDp())
            setTopLeftCorner(CornerFamily.ROUNDED, 4.toDp())
        }.build()

        val shape = MaterialShapeDrawable(model).apply {
            val backgroundColor = getColorRes(requireContext(), R.color.white)
            fillColor = ColorStateList.valueOf(backgroundColor)
        }

        view.background = shape
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        Log.d(TAG, "setupDialog: ")
    }

    override fun onResume() {
        super.onResume()
        binding.bottomChoiceCancel.root.setOnClickListener { this.dismiss() }
    }

    private fun setRecyclerView() {
        binding.bottomChoiceRecycler.apply {
            layoutManager = CustomLinearLayoutManager(requireContext(), isScroll)
            adapter = choiceBottomSheetAdapter
        }
    }
}