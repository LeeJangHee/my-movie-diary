package com.devlee.mymoviediary.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainBottomAdapter(
    fa: FragmentActivity,
    val fragments: ArrayList<Fragment>
): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}