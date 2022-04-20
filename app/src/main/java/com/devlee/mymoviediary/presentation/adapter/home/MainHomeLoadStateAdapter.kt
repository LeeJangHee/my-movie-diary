package com.devlee.mymoviediary.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlee.mymoviediary.databinding.ItemPagingStateBinding

/**
 * 홈 화면을 페이징으로 사용 후 로딩 이미지를 넣기 위한 Adapter
 */
class MainHomeLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<MainHomeLoadStateAdapter.StateHolder>() {

    inner class StateHolder(val binding: ItemPagingStateBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.pagingRetryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(isLoading: Boolean?) {
            binding.apply {
                this.isLoading = isLoading
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: StateHolder, loadState: LoadState) {
        val isLoading: Boolean? = when (loadState) {
            is LoadState.Loading -> true
            is LoadState.Error -> false
            else -> null
        }

        holder.bind(isLoading)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StateHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPagingStateBinding.inflate(layoutInflater, parent, false)
        return StateHolder(binding)
    }
}