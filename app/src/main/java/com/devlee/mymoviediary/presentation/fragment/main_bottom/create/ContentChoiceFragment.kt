package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.BottomContentChoiceBinding
import com.devlee.mymoviediary.databinding.ItemSortPopupBinding
import com.devlee.mymoviediary.domain.repository.MediaPagingRepository
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.presentation.adapter.create.MediaPagingAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseBottomSheetFragment
import com.devlee.mymoviediary.utils.Constants.MEDIA_PAGE_SIZE
import com.devlee.mymoviediary.utils.delayUiThread
import com.devlee.mymoviediary.utils.hide
import com.devlee.mymoviediary.utils.show
import com.devlee.mymoviediary.viewmodels.ContentCreateViewModel
import com.devlee.mymoviediary.viewmodels.MediaViewModel
import com.devlee.mymoviediary.viewmodels.MediaViewModelProviderFactory
import com.devlee.mymoviediary.viewmodels.SortItem
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContentChoiceFragment : BaseBottomSheetFragment<BottomContentChoiceBinding>(R.layout.bottom_content_choice) {

    companion object {
        private const val TAG = "ContentChoiceFragment"
    }

    private val contentChoiceViewModel: ContentCreateViewModel by viewModels()
    private val mediaViewModel by viewModels<MediaViewModel> {
        val repository = MediaPagingRepository(requireContext(), args.contentType)
        MediaViewModelProviderFactory(repository)
    }
    private val args: ContentChoiceFragmentArgs by navArgs()

    private val mediaPagingAdapter by lazy { MediaPagingAdapter(args.contentType, contentChoiceViewModel) }

    private val exoPlayer: ExoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val updateSeekRunnable = Runnable { updateSeekBar() }

    override fun setView() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            if (args.contentType == ContentType.VIDEO) {
                setVideo()
            } else {
                setAudio()
            }
            title = args.contentType.text

            // 정렬 item 클릭
            sortItemView.setOnClickListener(::setSortItemViewClick)

            // 타임라인 change callback
            contentChoiceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                }

                override fun onStartTrackingTouch(seekbar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekbar: SeekBar) {
                    exoPlayer.seekTo(seekbar.progress * 1000L)
                }

            })
            // 취소 클릭
            contentChoiceCancel.setOnClickListener { dismiss() }
            // 확인 클릭
            contentChoiceOk.setOnClickListener {
                // 확인
            }

            // 아이템 선택 max 일 경우 경고 문구
            contentChoiceViewModel.maxChoiceItemCallback = {
                binding.maxChoiceLayout.show()
                delayUiThread(700) {
                    binding.maxChoiceLayout.hide()
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            contentChoiceViewModel.selectedSortItem.collect { sortItem ->
                Log.d(TAG, "selectedSortItem: $sortItem")
                binding.selectedSortItem = sortItem
                val scrollPos = if (sortItem == SortItem.ASC) 1
                else mediaPagingAdapter.itemCount - 1

                Log.w(TAG, "selectedSortItem: $scrollPos", )

//                binding.contentChoiceRecyclerView.smoothScrollToPosition(scrollPos)

                mediaViewModel.setSortItemFlow(sortItem)
            }
        }

        setBehavior()
        initExoPlayer()
        initSortItem()
        initMediaData()
        setSelectVideoItem()
        setTopCornerRadius(4, 4)
    }

    private fun initMediaData() {
        mediaViewModel.mediaPagingData.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                Log.w(TAG, "initMediaData: $it")
                mediaPagingAdapter.submitData(it)
            }
        }
    }

    private fun setSortItemViewClick(v: View) {
        Log.w(TAG, "sortItemView: click")
        val itemBinding = ItemSortPopupBinding.inflate(LayoutInflater.from(activity))
        val popupWindow = PopupWindow(itemBinding.root, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)
        popupWindow.showAsDropDown(v)

        with(itemBinding) {
            Log.w(TAG, "itemBinding: ${itemBinding.root}")
            popupItem = contentChoiceViewModel.popupMenuSortItem.get()

            sortPopupMenu.width = v.width
            root.setOnClickListener {
                Log.v(TAG, "itemBinding click $popupItem, ${binding.selectedSortItem}")
                contentChoiceViewModel.popupMenuSortItem.set(binding.selectedSortItem)
                contentChoiceViewModel.setSortItem(popupItem)

                popupWindow.dismiss()
            }
        }
    }

    private fun setSelectVideoItem() = lifecycleScope.launch {
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
            binding.contentChoicePreviewVideo.apply {
                this.player = exoPlayer
                currentIsPlaying()
            }
        }
    }

    private fun setBehavior() {
        val view = binding.root
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
    }

    private fun initSortItem() {
        contentChoiceViewModel.popupMenuSortItem.set(SortItem.DESC)
        contentChoiceViewModel.setSortItem(SortItem.ASC)
        binding.selectedSortItem = SortItem.ASC
        mediaViewModel.setSortItemFlow(SortItem.ASC)
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
        contentChoiceRecyclerView.apply {
//            setHasFixedSize(true)
//            setItemViewCacheSize(MEDIA_PAGE_SIZE)
            itemAnimator = null
            adapter = mediaPagingAdapter
            val spanCount = 3
            layoutManager = GridLayoutManager(requireContext(), spanCount)
        }

    }


    /** Audio setting */
    private fun BottomContentChoiceBinding.setAudio() {

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