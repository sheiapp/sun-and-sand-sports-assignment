package com.sunandsandsports.assignment.data.data_source


import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams.Refresh
import androidx.paging.PagingSource.LoadResult.Page
import com.sunandsandsports.assignment.api.RandomUserApi
import com.sunandsandsports.assignment.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

/**
 * Created by Shaheer cs on 20/04/2022.
 */
@ExperimentalCoroutinesApi
class RandomUserPagingDataSourceTest {

    @Mock
    lateinit var api: RandomUserApi

    private lateinit var randomUserPagingDataSource: RandomUserPagingDataSource


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        randomUserPagingDataSource = RandomUserPagingDataSource(api)
    }

    @Test
    fun `check paging source load- failure - due to exception`() = runTest {
        val error = RuntimeException("404", Throwable())
        given(api.getRandomUserList(Mockito.anyInt(), Mockito.anyInt())).willThrow(error)
        val expectedResult = PagingSource.LoadResult.Error<Int, RandomUserDetail>(error)
        assertEquals(
            expected = expectedResult, actual = randomUserPagingDataSource.load(
                Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `check paging source load - failure - due to received null`() = runTest {
        given(api.getRandomUserList(Mockito.anyInt(), Mockito.anyInt())).willReturn(null)
        val expectedResult =
            PagingSource.LoadResult.Error<Int, RandomUserDetail>(NullPointerException())
        assertEquals(
            expected = expectedResult.toString(), actual = randomUserPagingDataSource.load(
                Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ).toString()
        )
    }

    @Test
    fun `check paging source refresh - success`() = runTest {
        given(api.getRandomUserList(Mockito.anyInt(), Mockito.anyInt())).willReturn(
            randomUserResponse
        )
        /**
         * prevKey value is null as we are loading first page
         */
        val expectedResult = Page(
            data = randomUserResponse.results,
            prevKey = null,
            nextKey = 1
        )
        assertEquals(
            expectedResult, randomUserPagingDataSource.load(
                Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `check paging source append - success`() = runTest {
        given(api.getRandomUserList(Mockito.anyInt(), Mockito.anyInt())).willReturn(
            randomUserResponse
        )
        /**
         * prevKey value is null as we are loading second page
         */
        val expectedResult = Page(
            data = randomUserResponse.results,
            prevKey = 0,
            nextKey = 2
        )
        assertEquals(
            expectedResult, randomUserPagingDataSource.load(
                PagingSource.LoadParams.Append(
                    key = 1,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    @Test
    fun `check paging source prepend - success`() = runTest {
        given(api.getRandomUserList(Mockito.anyInt(), Mockito.anyInt())).willReturn(
            randomUserResponse
        )
        /**
         * prevKey value is null as we are loading first page again
         */
        val expectedResult = Page(
            data = randomUserResponse.results,
            prevKey = null,
            nextKey = 1
        )
        assertEquals(
            expectedResult, randomUserPagingDataSource.load(
                PagingSource.LoadParams.Append(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            )
        )
    }

    companion object {

        val randomUserResponse = RandomUserResponse(
            results = listOf(
                createRandomResults(), createRandomResults(), createRandomResults()
            ), info = Info(results = 10, page = 0)
        )
        val pagingData = PagingData.from(randomUserResponse.results)
        private fun createRandomResults(): RandomUserDetail {
            return RandomUserDetail(
                email = "email",
                gender = "male",
                name = Name(title = "mr", first = "shaheer", last = "cs"),
                dob = Dob(age = "28", date = "121212"),
                location = Location("duabi", "UAE", "000000", "Dubai"),
                phone = "9746797970",
                picture = Picture("large", "medium", "thumbnail")
            )
        }
    }
}