package com.sunandsandsports.assignment.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.sunandsandsports.assignment.databinding.RandomUserItemBinding
import com.sunandsandsports.assignment.model.RandomUserDetail

/**
 * Created by Shaheer cs on 20/04/2022.
 */
class RandomUserAdapter(
    private val requestManager: RequestManager,
    private val clickEventData: (RandomUserDetail) -> Unit
) : PagingDataAdapter<RandomUserDetail, ItemViewHolder>(moviesDiffUtil) {


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { user ->
            holder.onBindView(user)
            holder.itemView.setOnClickListener {
                 clickEventData(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            RandomUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(itemView, requestManager)
    }


    companion object {
        private val moviesDiffUtil = object : DiffUtil.ItemCallback<RandomUserDetail>() {
            override fun areItemsTheSame(
                oldItem: RandomUserDetail,
                newItem: RandomUserDetail
            ) =
                newItem.email == oldItem.email

            override fun areContentsTheSame(
                oldItem: RandomUserDetail,
                newItem: RandomUserDetail
            ) =
                newItem == oldItem
        }
    }
}


class ItemViewHolder(
    private val viewItem: RandomUserItemBinding,
    private val requestManager: RequestManager
) : RecyclerView.ViewHolder(viewItem.root) {
    fun onBindView(randomUserDetail: RandomUserDetail) {
        viewItem.userName.text = randomUserDetail.name.getFullName()
        viewItem.gender.text = randomUserDetail.gender
        setupMovieThumbnail(randomUserDetail.picture.thumbnail)
    }

    private fun setupMovieThumbnail(posterPath: String) {
        requestManager
            .load(posterPath)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(viewItem.userPicture)
    }
}