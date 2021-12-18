package com.devlee.mymoviediary.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V : ViewBinding> : Fragment() {
    private var _binding: V? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (getLayoutId() > 0) {
            _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            setView()
            binding.root
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    open fun getLayoutId(): Int = 0

    abstract fun setView()

}