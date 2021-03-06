package com.devlee.mymoviediary.utils.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.devlee.mymoviediary.R
import com.devlee.mymoviediary.databinding.DialogViewBinding
import com.devlee.mymoviediary.utils.getColorRes
import com.devlee.mymoviediary.utils.show

class CustomDialog {

    private var binding: DialogViewBinding? = null
    private var dialog: AlertDialog? = null

    fun dismiss() {
        dialog?.dismiss()
    }

    class Builder(val context: Context) {
        private var title: String? = null
        private var customView: View? = null
        private var message: String? = null
        private var positiveText: String? = null
        private var negativeText: String? = null
        private var positiveButtonListener: (() -> Unit)? = null
        private var negativeButtonListener: (() -> Unit)? = null
        private var positiveTextColor: Int? = null
        private var negativeTextColor: Int? = null
        private var dismissListener: (() -> Unit)? = null
        private var cancelable: Boolean = true


        /** Title */
        fun setTitle(@StringRes titleId: Int): Builder {
            return setTitle(context.getString(titleId))
        }

        fun setTitle(title: String?): Builder {
            this.title = title
            return this
        }

        /** CustomView */
        fun setCustomView(view: View): Builder {
            customView = view
            return this
        }

        /** Message */
        fun setMessage(@StringRes messageId: Int): Builder {
            return setMessage(context.getString(messageId))
        }

        fun setMessage(message: String?): Builder {
            this.message = message
            return this
        }

        /** Positive Button */
        fun setPositiveButton(@StringRes textId: Int, @ColorRes colorRes: Int = R.color.color_1c1c1c, listener: (() -> Unit)? = null): Builder {
            return setPositiveButton(context.getString(textId), colorRes, listener)
        }

        fun setPositiveButton(textStr: String?, color: Int = R.color.color_1c1c1c, listener: (() -> Unit)? = null): Builder {
            positiveText = textStr
            positiveTextColor = getColorRes(context, color)
            positiveButtonListener = listener
            return this
        }

        /** Negative Button */
        fun setNegativeButton(@StringRes textId: Int, @ColorRes colorRes: Int = R.color.color_1c1c1c, listener: (() -> Unit)? = null): Builder {
            return setNegativeButton(context.getString(textId), colorRes, listener)
        }

        fun setNegativeButton(textStr: String?, color: Int = R.color.color_1c1c1c, listener: (() -> Unit)? = null): Builder {
            negativeText = textStr
            negativeTextColor = getColorRes(context, color)
            negativeButtonListener = listener
            return this
        }

        /** Dismiss */
        fun setOnDismissListener(listener: (() -> Unit)?): Builder {
            dismissListener = listener
            return this
        }

        /** Cancelable */
        fun setCancelable(set: Boolean): Builder {
            cancelable = set
            return this
        }

        fun show(): CustomDialog {
            val customPopupHelper = CustomDialog()

            try {
                val view = LayoutInflater.from(context).inflate(R.layout.dialog_view, null, false)
                customPopupHelper.binding = DialogViewBinding.bind(view)

                customPopupHelper.dialog = AlertDialog.Builder(context)
                    .setView(view)
                    .setCancelable(cancelable)
                    .setOnDismissListener {
                        dismissListener?.invoke() ?: it.dismiss()
                    }
                    .show()

            } catch (e: Exception) {
                e.printStackTrace()
            }

            customPopupHelper.dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            customPopupHelper.binding?.run {
                title?.let {
                    this.customDialogTitle.apply {
                        text = it
                        this.show()
                    }
                }


                message?.let {
                    this.customDialogMessage.apply {
                        text = it
                        this.show()
                    }
                }

                customView?.let {
                    this.customDialogView.apply {
                        removeAllViews()
                        addView(it)
                        this.show()
                    }
                }

                positiveText?.let {
                    this.customDialogPositive.also { positiveTextView ->
                        positiveTextView.text = it
                        positiveTextView.setTextColor(positiveTextColor ?: getColorRes(context, R.color.color_1c1c1c))
                        positiveTextView.setOnClickListener {
                            positiveButtonListener?.invoke()
                            customPopupHelper.dialog?.dismiss()
                        }
                        positiveTextView.show()
                    }
                }

                negativeText?.let {
                    this.customDialogNegative.also { negativeTextView ->
                        negativeTextView.text = it
                        negativeTextView.setTextColor(negativeTextColor ?: getColorRes(context, R.color.color_1c1c1c))
                        negativeTextView.setOnClickListener {
                            negativeButtonListener?.invoke()
                            customPopupHelper.dialog?.dismiss()
                        }
                        negativeTextView.show()
                    }
                }
            }

            return customPopupHelper
        }
    }
}