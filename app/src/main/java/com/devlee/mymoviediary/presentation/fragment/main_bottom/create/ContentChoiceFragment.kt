package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.content.ContentUris
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.BottomContentChoiceBinding
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.presentation.adapter.create.ContentChoiceAdapter
import com.devlee.mymoviediary.utils.*
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContentChoiceFragment : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "ContentChoiceFragment"
    }

    private var _binding: BottomContentChoiceBinding? = null
    private val binding get() = _binding!!

    private val contentChoiceViewModel: ContentCreateViewModel by viewModels()
    private val args: ContentChoiceFragmentArgs by navArgs()

    private val contentChoiceAdapter by lazy { ContentChoiceAdapter(args.contentType, contentChoiceViewModel) }

    private val exoPlayer: ExoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val updateSeekRunnable = Runnable { updateSeekBar() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheet_Base_Light)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = BottomContentChoiceBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            if (args.contentType == ContentType.VIDEO) {
                setVideo()
            } else {
                setAudio()
            }
            title = args.contentType.text
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
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            Log.d(TAG, "onStateChanged: STATE_HIDDEN")
                            dismissAllowingStateLoss()
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.maxChoiceLayout.y = behavior.peekHeight - binding.maxChoiceLayout.height.toFloat()
                            Log.d(TAG, "onStateChanged: STATE_COLLAPSED ${binding.maxChoiceLayout.y}")
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            binding.maxChoiceLayout.y = view.height - binding.maxChoiceLayout.height.toFloat()
                            Log.d(TAG, "onStateChanged: STATE_EXPANDED ${binding.maxChoiceLayout.y}")
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

            })

            view.post {
                behavior.peekHeight = (view.height / 10) * 8
                binding.maxChoiceLayout.y = behavior.peekHeight - binding.maxChoiceLayout.height.toFloat()
            }
        }

        initExoPlayer()

        binding.apply {
            lifecycleScope.launch {
                contentChoiceViewModel.selectedVideoList.collect { uriList ->

                    fun PlayerView.currentIsPlaying() = run {
                        val pvPlayer = player ?: return@run
                        Log.d(TAG, "currentIsPlaying: ${pvPlayer.currentMediaItem}")
                    }

                    uriList.lastOrNull()?.let {
                        Log.d(TAG, "stateFlow start ${uriList.size}")
                        val mediaItem = MediaItem.fromUri(it)

                        exoPlayer.apply {
                            setMediaItem(mediaItem)
                            prepare()
                            play()
                            playWhenReady = true
                        }

                    } ?: run {
                        exoPlayer.apply {
                            clearMediaItems()
                            stop()
                            playWhenReady = false
                        }
                    }
                    contentChoicePreviewVideo.apply {
                        this.player = exoPlayer
                        currentIsPlaying()
                    }
                }
            }
            contentChoiceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                }

                override fun onStartTrackingTouch(seekbar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekbar: SeekBar) {
                    exoPlayer.seekTo(seekbar.progress * 1000L)
                }

            })
            contentChoiceCancel.setOnClickListener { dismiss() }
            contentChoiceOk.setOnClickListener {
                // 확인
            }
            contentChoiceViewModel.maxChoiceItemCallback = {
                binding.maxChoiceLayout.show()
                delay(700) {
                    binding.maxChoiceLayout.hide()
                }
            }
        }
        setCornerRadius(view)
    }

    private fun initExoPlayer() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                updateSeekBar()
            }
        })
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

    private suspend fun loadVideo(): List<ContentChoiceFileData> {
        return withContext(Dispatchers.Main) {
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

            val videoList = mutableListOf<ContentChoiceFileData>()

            requireActivity().contentResolver.query(
                collection,
                projection,
                null,
                null,
                "${MediaStore.Video.Media.DATE_MODIFIED} ASC"
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
                        videoList.add(ContentChoiceFileData(video = contentUri))
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

    private fun updateSeekBar() {
        val duration = if (exoPlayer.duration >= 0) exoPlayer.duration else 0
        val pos = exoPlayer.currentPosition

        updateSeekUi(duration, pos)

        val state = exoPlayer.playbackState
        binding.root.removeCallbacks(updateSeekRunnable)
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            binding.root.postDelayed(updateSeekRunnable, 1000L)
        }
    }

    private fun updateSeekUi(duration: Long, pos: Long) = with(binding.contentChoiceSeekBar) {
        max = (duration / 1000).toInt()
        progress = (pos / 1000).toInt()
    }

    private fun releaseVideo() {
        exoPlayer.release()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.w(TAG, "onDestroyView: ")
        binding.root.removeCallbacks(updateSeekRunnable)
        releaseVideo()
    }
}