package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.BottomChoiceViewBinding
import com.devlee.mymoviediary.presentation.adapter.create.CreateBottomSheetAdapter
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChoiceBottomSheet : BottomSheetDialogFragment() {

    private val TAG = "ChoiceBottomSheet"

    private var _binding: BottomChoiceViewBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetViewModel: ContentCreateViewModel by viewModels()

    private val choiceBottomSheetAdapter by lazy { CreateBottomSheetAdapter(args.bottomChoiceType, bottomSheetViewModel) }

    private val args: ChoiceBottomSheetArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomChoiceViewBinding.inflate(inflater, container, false).apply {
            viewModel = bottomSheetViewModel
            lifecycleOwner = viewLifecycleOwner
            cancelString = requireContext().getString(R.string.no_kr)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

        if (args.bottomChoiceType == BottomChoiceType.CATEGORY) {
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
                            BottomSheetBehavior.STATE_COLLAPSED -> Log.d(TAG, "onStateChanged: STATE_COLLAPSED")
                            BottomSheetBehavior.STATE_DRAGGING -> Log.d(TAG, "onStateChanged: STATE_DRAGGING")
                            BottomSheetBehavior.STATE_EXPANDED -> Log.d(TAG, "onStateChanged: STATE_EXPANDED")
                            BottomSheetBehavior.STATE_SETTLING -> Log.d(TAG, "onStateChanged: STATE_SETTLING")
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                })
                behavior.peekHeight = (requireContext().resources.getDimension(R.dimen.dp_56) * 5).toInt()
            }
        }

        choiceBottomSheetAdapter.setBottomSheetItem(args.choiceBottomSheetList.toList())
        setRecyclerView()
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
            adapter = choiceBottomSheetAdapter
        }
    }
}