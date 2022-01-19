package com.devlee.mymoviediary.viewmodels

import android.Manifest
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.domain.use_case.ContentChoiceData
import com.devlee.mymoviediary.presentation.adapter.create.CreateAdapter
import com.devlee.mymoviediary.presentation.adapter.create.CreateViewType
import com.devlee.mymoviediary.utils.dialog.CustomDialog
import com.gun0912.tedpermission.coroutine.TedPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentCreateViewModel : ViewModel() {

    private val TAG = "ContentCreateViewModel"

    var contentCreateAdapter = CreateAdapter(this)
    var contentChoiceDataList = arrayListOf<ContentChoiceData>(ContentChoiceData(CreateViewType.ADD.type))

    var deniedPermissionCallback : (() -> Unit)? = null


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
}