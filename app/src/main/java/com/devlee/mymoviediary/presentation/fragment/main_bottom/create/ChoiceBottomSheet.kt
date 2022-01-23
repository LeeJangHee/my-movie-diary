package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.BottomChoiceViewBinding
import com.devlee.mymoviediary.domain.use_case.ChoiceBottomSheetData
import com.devlee.mymoviediary.presentation.adapter.create.CreateBottomSheetAdapter
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.devlee.mymoviediary.viewmodels.MyDiaryViewModel
import com.devlee.mymoviediary.viewmodels.ViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChoiceBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomChoiceViewBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetViewModel: ContentCreateViewModel by viewModels()
    private val categoryViewModel: MyDiaryViewModel by viewModels {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }

    private val choiceBottomSheetAdapter by lazy { CreateBottomSheetAdapter(args.bottomChoiceType, bottomSheetViewModel) }

    private val args: ChoiceBottomSheetArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomChoiceViewBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setItemList()

    }

    private fun setItemList() {
        val itemList = mutableListOf<ChoiceBottomSheetData>()
        when (args.bottomChoiceType) {
            BottomChoiceType.CONTENT -> {
                itemList.add(ChoiceBottomSheetData(text = "영상 파일"))
                itemList.add(ChoiceBottomSheetData(text = "음성 파일"))
            }
            BottomChoiceType.CATEGORY -> {
                lifecycleScope.launch(Dispatchers.IO) {
                    categoryViewModel.categories.collect { categoryEntities ->
                        categoryEntities.forEach {
                            itemList.add(ChoiceBottomSheetData(category = it.category))
                        }
                    }
                }
            }
        }
        choiceBottomSheetAdapter.setBottomSheetItem(itemList)
    }

    private fun setRecyclerView() {
        binding.bottomChoiceRecycler.apply {
            adapter = choiceBottomSheetAdapter
        }
    }
}