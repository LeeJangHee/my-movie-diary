package com.devlee.mymoviediary.presentation.fragment.main_bottom.create

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.database.MyDiaryDatabase
import com.devlee.mymoviediary.data.repository.MyDiaryRepository
import com.devlee.mymoviediary.databinding.BottomContentChoiceBinding
import com.devlee.mymoviediary.databinding.ItemSortPopupBinding
import com.devlee.mymoviediary.domain.repository.MediaPagingRepository
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.presentation.adapter.create.MediaPagingAdapter
import com.devlee.mymoviediary.presentation.fragment.BaseBottomSheetFragment
import com.devlee.mymoviediary.utils.*
import com.devlee.mymoviediary.utils.Constants.MEDIA_PAGE_SIZE
import com.devlee.mymoviediary.utils.dialog.CustomDialog
import com.devlee.mymoviediary.utils.recyclerview.CustomDecoration
import com.devlee.mymoviediary.viewmodels.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ContentChoiceFragment : BaseBottomSheetFragment<BottomContentChoiceBinding>(R.layout.bottom_content_choice) {

    companion object {
        private const val TAG = "ContentChoiceFragment"
    }

    private val contentChoiceViewModel: ContentCreateViewModel by navGraphViewModels(R.id.home_nav) {
        val repository = MyDiaryRepository(MyDiaryDatabase.getInstance(requireActivity()))
        ViewModelProviderFactory(repository)
    }
    private val mediaViewModel by viewModels<MediaViewModel> {
        val repository = MediaPagingRepository(requireContext(), args.contentType)
        MediaViewModelProviderFactory(repository)
    }
    private val args: ContentChoiceFragmentArgs by navArgs()

    private val mediaPagingAdapter by lazy { MediaPagingAdapter(args.contentType, contentChoiceViewModel, onFirstItemCallback) }
    private val audioDecoration by lazy { CustomDecoration(1.toDp(), 0f, 0f, getColorRes(requireContext(), R.color.color_efefef)) }

    private val exoPlayer: ExoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val updateSeekRunnable = Runnable { updateSeekBar() }

    private var selectedMediaItem: MediaItem? = null
    private var onFirstItemCallback: (ContentChoiceFileData?) -> Unit = { choiceFileData ->
        // 인스타 처럼 선택된 아이템이 없으면 처음 아이템으로 비디오 시작
        choiceFileData?.let {
            Log.d(TAG, "contentType: ${args.contentType.name}")
            if (args.contentType == ContentType.VIDEO) {
                if (it.video != null && selectedMediaItem == null) {
                    setMediaItem(MediaItem.fromUri(it.video))
                }
            } else {
                if (it.audio != null && selectedMediaItem == null) {
                    setMediaItem(MediaItem.fromUri(it.audio))
                }
            }
        }
    }

    override fun setView() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            type = args.contentType
            if (args.contentType == ContentType.VIDEO) {
                setVideo()
            } else {
                setAudio()
            }
            // fragment 제목
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
                Log.d(TAG, "확인 클릭")
                contentChoiceViewModel.setSelectMedia(listOf())
                selectedMediaItemCallback?.invoke()
                dismiss()
            }
            // 오디오 버튼 클릭
            audioPlayButton.setOnClickListener(::audioControlClick)

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

                // 변경되면 처음으로 이동
                binding.contentChoiceRecyclerView.post {
                    binding.contentChoiceRecyclerView.smoothScrollToPosition(0)
                }

                mediaViewModel.setSortItemFlow(sortItem)
            }
        }

        setBehavior()
        initExoPlayer()
        initSortItem()
        initMediaData()
        setSelectMediaItem()
        setTopCornerRadius(4, 4)
    }

    private fun initMediaData() = lifecycleScope.launchWhenResumed {
        mediaViewModel.mediaPagingData.observe(viewLifecycleOwner) { pagingData ->
            lifecycleScope.launch {
                Log.w(TAG, "initMediaData: $pagingData")

                mediaPagingAdapter.submitData(pagingData)
            }
        }

        mediaPagingAdapter.loadStateFlow.collectLatest { loadStates ->
            when (loadStates.refresh) {
                is LoadState.Error -> {
                    Log.d(TAG, "initMediaData: refresh error")
                    dismiss()
                    // 동영상 및 오디오가 없을 때, 다이얼로그 생성
                    CustomDialog.Builder(requireContext())
                        .setTitle(R.string.create_choice_item_empty_title)
                        .setMessage(R.string.create_choice_item_empty_message)
                        .setPositiveButton(R.string.ok_kr)
                        .show()
                }
                is LoadState.NotLoading -> Log.d(TAG, "initMediaData: refresh notLoading")
                else -> Log.d(TAG, "initMediaData: refresh Loading")
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

    private fun setSelectMediaItem() = lifecycleScope.launch {
        contentChoiceViewModel.selectedMediaList.collect { uriList ->
            uriList.lastOrNull()?.let {
                Log.d(TAG, "stateFlow start ${uriList.size}")
                selectedMediaItem = MediaItem.fromUri(it)
            } ?: run {
                Log.d(TAG, "stateFlow null")
                selectedMediaItem = null
                onFirstItemCallback.invoke(mediaPagingAdapter.getItemPosition())
                return@collect
            }
            contentChoiceViewModel.fileList.clear()
            uriList.forEachIndexed { i, uri ->
                Log.w(TAG, "uriList.forEachIndexed: $i $uri")
                if (binding.type == ContentType.VIDEO) {
                    contentChoiceViewModel.fileList.add(ContentChoiceFileData(video = uri))
                } else {
                    contentChoiceViewModel.fileList.add(ContentChoiceFileData(audio = uri))
                }
            }

            setMediaItem(selectedMediaItem)
        }
    }

    private fun PlayerView.currentIsPlaying() = run {
        val pvPlayer = player ?: return@run
        Log.d(TAG, "currentIsPlaying: ${pvPlayer.currentMediaItem}")
        binding.audioPlayButton.load(R.drawable.ic_play)
    }

    private fun setMediaItem(mediaItem: MediaItem? = null) {
        Log.w(TAG, "setMediaItem: ${mediaItem?.mediaId}")
        mediaItem?.let {
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
        contentChoiceViewModel.popupMenuSortItem.set(SortItem.ASC)
        contentChoiceViewModel.setSortItem(SortItem.DESC)
        binding.selectedSortItem = SortItem.DESC
        mediaViewModel.setSortItemFlow(SortItem.DESC)
    }

    private fun initExoPlayer() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                updateSeekBar()

                binding.audioPlayButton.load(R.drawable.ic_pause)
                when (playbackState) {
                    Player.STATE_IDLE, Player.STATE_ENDED -> {
                        // audio가 멈추면 다시 재생버튼으로 변경
                        binding.audioPlayButton.load(R.drawable.ic_play)
                        isPause = false
                    }
                }
            }
        })
    }

    /** Video setting */
    private fun BottomContentChoiceBinding.setVideo() = lifecycleScope.launch {
        contentChoiceRecyclerView.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(MEDIA_PAGE_SIZE)
            itemAnimator = null
            adapter = mediaPagingAdapter
            val spanCount = 3
            layoutManager = GridLayoutManager(requireContext(), spanCount)
        }

    }


    /** Audio setting */
    private fun BottomContentChoiceBinding.setAudio() {
        contentChoiceRecyclerView.apply {
            setHasFixedSize(true)
            itemAnimator = null
            adapter = mediaPagingAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(audioDecoration)
        }
    }

    private fun updateSeekBar() {
        val duration = if (exoPlayer.duration >= 0) exoPlayer.duration else 0
        val pos = exoPlayer.currentPosition

        updateSeekUi(duration, pos)

        val state = exoPlayer.playbackState
        binding.root.removeCallbacks(updateSeekRunnable)
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            binding.root.postDelayed(updateSeekRunnable, 300L)
        }
    }

    private fun updateSeekUi(duration: Long, pos: Long) = when (binding.type) {
        ContentType.VIDEO -> {
            with(binding.contentChoiceSeekBar) {
                max = (duration / 300).toInt()
                progress = (pos / 300).toInt()
            }
        }
        else -> {
            with(binding) {
                contentChoiceAudioSeekbar.apply {
                    max = (duration / 300).toInt()
                    progress = (pos / 300).toInt()
                }
                currentTimeline.text = DateFormatUtil.getAudioTimeLine(pos)
                totalTimeline.text = DateFormatUtil.getAudioTimeLine(duration)
            }
        }
    }

    private var isPause = false // 일시정지 했는지 알기 위해
    private fun audioControlClick(v: View) {
        if (exoPlayer.isPlaying) {
            // 일시정지 해야함
            exoPlayer.pause()
            isPause = true
            (v as ImageView).load(R.drawable.ic_play)
        } else {
            // 플레이
            if (isPause) {
                exoPlayer.play()
                isPause = false
            } else {
                exoPlayer.seekTo(0)
            }
            (v as ImageView).load(R.drawable.ic_pause)
        }
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