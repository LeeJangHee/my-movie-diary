package com.devlee.mymoviediary.viewmodels

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.data.model.Category
import com.devlee.mymoviediary.data.model.Mood
import com.devlee.mymoviediary.domain.use_case.ChoiceBottomSheetData
import com.devlee.mymoviediary.domain.use_case.ContentChoiceData
import com.devlee.mymoviediary.domain.use_case.ContentChoiceFileData
import com.devlee.mymoviediary.domain.use_case.ContentType
import com.devlee.mymoviediary.presentation.adapter.create.CreateAdapter
import com.devlee.mymoviediary.presentation.adapter.create.CreateViewType
import com.devlee.mymoviediary.presentation.fragment.main_bottom.create.BottomChoiceType
import com.devlee.mymoviediary.utils.dialog.CustomDialog
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class ContentCreateViewModel : ViewModel() {

    private val TAG = "ContentCreateViewModel"

    var contentCreateAdapter = CreateAdapter(this)
    var contentChoiceDataList = arrayListOf<ContentChoiceData>(ContentChoiceData(CreateViewType.ADD.type))

    var choiceBottomSheetList: MutableSharedFlow<List<ChoiceBottomSheetData>> = MutableSharedFlow()
    private var _selectedVideoList: MutableSharedFlow<List<Uri>> = MutableSharedFlow()
    val selectedVideoList = _selectedVideoList.asSharedFlow()

    var test: MutableStateFlow<String> = MutableStateFlow("")

    var deniedPermissionCallback: (() -> Unit)? = null
    var bottomChoiceViewCallback: ((BottomChoiceType) -> Unit)? = null

    var selectedCategoryItem: ((Category) -> Unit)? = null
    var selectedContentItem: ((ContentType) -> Unit)? = null

    var maxChoiceItemCallback: (() -> Unit)? = null

    @SuppressLint("NewApi")
    var dateStr: ObservableField<String?> = ObservableField()
    var fileList: ObservableArrayList<ContentChoiceFileData> = ObservableArrayList()
    var selectedCategory: ObservableField<Category?> = ObservableField()
    var start: ObservableBoolean = ObservableBoolean(false)
    var mood: ObservableField<Mood> = ObservableField(Mood.NONE)

    private var _selectedSortItem: MutableSharedFlow<SortItem> = MutableSharedFlow()
    val selectedSortItem get() = _selectedSortItem.asSharedFlow()
    var popupMenuSortItem: ObservableField<SortItem> = ObservableField(SortItem.DESC)

    init {
        contentCreateAdapter.setDiaryList(contentChoiceDataList)
    }


    fun contentAddItemClick(view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            val permissionResult =
                TedPermission.create()
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .check()

            launch(Dispatchers.Main) {
                if (permissionResult.isGranted) {
                    Log.d(TAG, "거부된 권한 없음 ${permissionResult.isGranted}")
                    bottomChoiceViewCallback?.invoke(BottomChoiceType.CONTENT)
                } else {
                    if (permissionResult.deniedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        permissionResult.deniedPermissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)
                    ) {
                        CustomDialog.Builder(view.context)
                            .setTitle(R.string.permission_dialog_title)
                            .setMessage(R.string.permission_dialog_content_choice_message)
                            .setPositiveButton(R.string.ok_kr) {
                                deniedPermissionCallback?.invoke()
                            }
                            .show()
                    }
                }
            }
            Log.d(TAG, "contentAddItemClick: $permissionResult")
        }
    }

    fun setChoiceBottomSheetData(newData: List<ChoiceBottomSheetData>) =
        viewModelScope.launch(Dispatchers.IO) {
            choiceBottomSheetList.emit(newData)
        }

    fun onClickCategory() {
        bottomChoiceViewCallback?.invoke(BottomChoiceType.CATEGORY)
    }

    fun selectedCategoryItem(category: Category?) {
        Log.w(TAG, "selectedCategoryItem: ${category?.title}/${category?.color}")
        if (category != null) {
            selectedCategoryItem?.invoke(category)
        } else {
            Log.d(TAG, "Category is Null")
        }

    }

    fun selectedContentItem(contentType: ContentType?) {
        Log.w(TAG, "selectedContentItem: ${contentType?.name}")
        if (contentType != null) {
            selectedContentItem?.invoke(contentType)
        } else {
            Log.d(TAG, "ContentType is Null")
        }
    }

    fun uri2Bitmap(context: Context, uri: Uri?): Bitmap? {
        var bitmap: Bitmap? = null
        if (uri != null) {
            try {
                val mmr = MediaMetadataRetriever().apply {
                    setDataSource(context, uri)
                }
                bitmap = mmr.frameAtTime
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    fun setSelectVideo(videoList: List<Uri>) = viewModelScope.launch {
        _selectedVideoList.emit(videoList)
    }

    fun setSortItem(item: SortItem?) = viewModelScope.launch {
        item?.let {
            _selectedSortItem.emit(item)
        }
    }

}

enum class SortItem(val title: String, val order: String) {
    ASC("최신 순", "ASC"),
    DESC("오래된 순", "DESC")
}