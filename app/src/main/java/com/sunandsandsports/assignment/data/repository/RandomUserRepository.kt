package com.sunandsandsports.assignment.data.repository

import androidx.paging.PagingData
import com.sunandsandsports.assignment.model.RandomUserDetail
import kotlinx.coroutines.flow.Flow

/**
 * Created by Shaheer cs on 20/04/2022.
 */
interface RandomUserRepository {
    fun getRandomUserList(): Flow<PagingData<RandomUserDetail>>
}