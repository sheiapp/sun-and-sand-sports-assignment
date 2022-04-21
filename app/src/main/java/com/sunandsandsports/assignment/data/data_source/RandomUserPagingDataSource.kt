package com.sunandsandsports.assignment.data.data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sunandsandsports.assignment.api.RandomUserApi
import com.sunandsandsports.assignment.model.RandomUserDetail
import java.io.IOException

/**
 * Created by Shaheer cs on 20/04/2022.
 */
class RandomUserPagingDataSource(private val randomUserApi: RandomUserApi) :
    PagingSource<Int, RandomUserDetail>() {
    override fun getRefreshKey(state: PagingState<Int, RandomUserDetail>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(ONE)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(ONE)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RandomUserDetail> {
        val pageNumber = params.key ?: STARTING_PAGE_INDEX
        val pageSize = params.loadSize
        return try {
            val response =
                randomUserApi.getRandomUserList(pageNo = pageNumber, noOfResults = pageSize)
            LoadResult.Page(
                data = response.results,
                prevKey = if (pageNumber == STARTING_PAGE_INDEX) null else pageNumber.minus(ONE),
                nextKey = if (response.results.isEmpty()) null else pageNumber.plus(ONE)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 0
        private const val ONE = 1
    }
}

