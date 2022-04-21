package com.sunandsandsports.assignment.data.repository

import androidx.paging.PagingData
import com.sunandsandsports.assignment.data.data_source.RandomUserPagingDataSourceTest.Companion.pagingData
import com.sunandsandsports.assignment.model.RandomUserDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Shaheer cs on 21/04/2022.
 */
@ExperimentalCoroutinesApi
class FakeRandomUserRepositoryImplTest : RandomUserRepository {
    override fun getRandomUserList(): Flow<PagingData<RandomUserDetail>> = flow {
        emit(pagingData)
    }
}