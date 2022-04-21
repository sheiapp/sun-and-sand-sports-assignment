package com.sunandsandsports.assignment.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sunandsandsports.assignment.data.data_source.RandomUserPagingDataSource
import com.sunandsandsports.assignment.model.RandomUserDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Shaheer cs on 20/04/2022.
 */
class RandomUserRepositoryImpl @Inject constructor(private val randomUserPagingDataSource: RandomUserPagingDataSource) :
    RandomUserRepository {
    override fun getRandomUserList(): Flow<PagingData<RandomUserDetail>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_MAX_PAGE_SIZE
            ),
            pagingSourceFactory = { randomUserPagingDataSource }
        ).flow

    companion object {
        const val NETWORK_PAGE_SIZE = 10
        const val NETWORK_MAX_PAGE_SIZE = 50
    }
}