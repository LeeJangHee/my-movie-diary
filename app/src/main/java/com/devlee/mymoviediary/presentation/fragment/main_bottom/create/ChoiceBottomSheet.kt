package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.BottomChoiceViewBinding
import com.devlee.mymoviediary.presentation.adapter.create.CreateBottomSheetAdapter
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ChoiceBottomSheet : BottomSheetDialogFragment() {

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
        choiceBottomSheetAdapter.setBottomSheetItem(args.choiceBottomSheetList.toList())
        setRecyclerView()
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