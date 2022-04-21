package com.sunandsandsports.assignment.ui.random_user_ui.main_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.RequestManager
import com.sunandsandsports.assignment.R
import com.sunandsandsports.assignment.ui.adapter.LoadStateAdapter
import com.sunandsandsports.assignment.ui.adapter.RandomUserAdapter
import com.sunandsandsports.assignment.databinding.FragmentRandomUserListBinding
import com.sunandsandsports.assignment.model.RandomUserDetail
import com.sunandsandsports.assignment.ui.random_user_ui.RandomUserSharedViewModel
import com.sunandsandsports.assignment.ui.collectLatestLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class RandomUserListFragment : Fragment(R.layout.fragment_random_user_list) {
    private var _binding: FragmentRandomUserListBinding? = null
    private val sharedViewModel: RandomUserSharedViewModel by activityViewModels()

    @Inject
    lateinit var glideRequestManager: RequestManager
    private val adapter by lazy {
        RandomUserAdapter(glideRequestManager) { randomUserDetailData ->
            navigateToRandomUserDetailFragment(randomUserDetailData)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRandomUserListBinding.bind(view)
        setupRandomUserListUi()
        _binding?.includedLayout?.retryButton?.setOnClickListener { adapter.retry() }
    }

    private fun setupRandomUserListUi() {
        /**
         *  add dividers between RecyclerView's row items
         */
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        _binding?.recyclerView?.addItemDecoration(decoration)
        _binding?.recyclerView?.adapter =
            adapter.withLoadStateFooter(footer = LoadStateAdapter { adapter.retry() })
        collectLatestLifecycleFlow(sharedViewModel.randomUserPagingData) { _pagingData ->
            _pagingData?.let { it -> adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }
        collectLatestLifecycleFlow(sharedViewModel.coroutineExceptionMessage) {
            _binding?.apply {
                includedLayout.errorMsg.text = it
                includedLayout.errorMsg.isVisible = true
                recyclerView.isVisible = false
                includedLayout.retryButton.isVisible = true
            }
        }
        collectLatestLifecycleFlow(adapter.loadStateFlow) { loadState ->
            val isListEmpty =
                (loadState.refresh is LoadState.NotLoading) && adapter.itemCount == COUNT_ZERO
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as? LoadState.Error
            _binding?.apply {
                /**
                 *  show empty list
                 */
                includedLayout.errorMsg.text = when (errorState?.error) {
                    is IOException -> getString(R.string.server_error)
                    else -> getString(R.string.unknown_error)
                }
                includedLayout.errorMsg.isVisible =
                    isListEmpty || loadState.refresh is LoadState.Error

                /**
                 *  Only show the list if refresh succeeds
                 */
                recyclerView.isVisible =
                    loadState.refresh !is LoadState.Error && loadState.source.refresh !is LoadState.Loading
                /**
                 *  Show loading spinner during initial load or refresh.
                 */
                includedLayout.progressBar.isVisible =
                    loadState.source.refresh is LoadState.Loading
                /**
                 * Show the retry state if initial load or refresh fails.
                 */
                includedLayout
                    .retryButton.isVisible =
                    loadState.refresh is LoadState.Error || adapter.itemCount == COUNT_ZERO
            }
        }
    }

    private fun navigateToRandomUserDetailFragment(randomUserDetailData: RandomUserDetail) {
        sharedViewModel.setSelectedRandomUserDetails(randomUserDetailData)
        val navigateToRandomUserDetail =
            RandomUserListFragmentDirections.actionMainScreenFragmentToDetailScreenFragment()
        findNavController().navigate(navigateToRandomUserDetail)
    }

    companion object {
        private const val COUNT_ZERO = 0
    }
}