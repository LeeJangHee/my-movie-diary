package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.content.ContentUris
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.BottomContentChoiceBinding
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.presentation.adapter.create.ContentChoiceAdapter
import com.devlee.mymoviediary.utils.dp
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class ContentChoiceFragment : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "ContentChoiceFragment"
    }

    private var _binding: BottomContentChoiceBinding? = null
    private val binding get() = _binding!!

    private val contentChoiceViewModel: ContentCreateViewModel by viewModels()
    private val args: ContentChoiceFragmentArgs by navArgs()

    private val contentChoiceAdapter by lazy { ContentChoiceAdapter(args.contentType, contentChoiceViewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomContentChoiceBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            if (args.contentType == ContentType.VIDEO) {
                setVideo()
            } else {
                setAudio()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutParams = (binding.root.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = layoutParams.behavior
        if (behavior is BottomSheetBehavior) {
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

            })
        }
        binding.apply {

        }
    }

    /** Video setting */
    private fun BottomContentChoiceBinding.setVideo() = lifecycleScope.launch {
        val videos = loadVideo()
        contentChoiceRecyclerView.apply {
            adapter = contentChoiceAdapter.apply {
                setFileList(videos)
            }
            val spanCount = 3
            layoutManager = GridLayoutManager(requireContext(), spanCount)
        }
    }


    /** Audio setting */
    private fun BottomContentChoiceBinding.setAudio() {

    }

    private suspend fun loadVideo(): List<File> {
        return withContext(Dispatchers.IO) {
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }

            val projection = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE
            )

            val videoList = mutableListOf<File>()

            requireActivity().contentResolver.query(
                collection,
                projection,
                null,
                null,
                "${MediaStore.Video.Media.DATE_MODIFIED} DESC"
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
                val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    Log.d(TAG, "loadVideo: $contentUri, $id, $displayName, $mimeType")
                    try {
                        videoList.add(File(contentUri.toString()))
                    } catch (e: Exception) {
                        Log.e(TAG, "loadVideo-(): ", e)
                        e.printStackTrace()
                    }
                }
                Log.d(TAG, "loadVideo: size ${videoList.size}")
                videoList
            } ?: listOf()
        }
    }
}