package com.sunandsandsports.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sunandsandsports.assignment.R
import com.sunandsandsports.assignment.databinding.LoadStateViewBinding
import java.io.IOException

/**
 * Created by Shaheer cs on 20/04/2022.
 */
class LoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoadStateViewHolder>() {
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.load_state_view, parent, false)
        val binding = LoadStateViewBinding.bind(view)
        return LoadStateViewHolder(binding, retry)
    }
}

class LoadStateViewHolder(
    private val binding: LoadStateViewBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = when (loadState.error) {
                is IOException -> binding.root.resources.getString(R.string.server_error)
                else -> binding.root.resources.getString(R.string.unknown_error)
            }
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }
}
