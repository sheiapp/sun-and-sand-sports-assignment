package com.sunandsandsports.assignment.ui.random_user_ui

import app.cash.turbine.test
import com.sunandsandsports.assignment.data.data_source.RandomUserPagingDataSourceTest.Companion.randomUserResponse
import com.sunandsandsports.assignment.data.repository.FakeRandomUserRepositoryImplTest
import com.sunandsandsports.assignment.data.repository.RandomUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Created by Shaheer cs on 21/04/2022.
 */
@ExperimentalCoroutinesApi
class RandomUserSharedViewModelTest {

    lateinit var randomUserRepository: RandomUserRepository
    lateinit var viewmodel: RandomUserSharedViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)
        randomUserRepository = FakeRandomUserRepositoryImplTest()
        viewmodel = RandomUserSharedViewModel(randomUserRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check the selected data is actually setting to the stateflow`() = runBlocking {
        viewmodel.setSelectedRandomUserDetails(randomUserResponse.results[0])
        viewmodel.selectedRandomUserDetail.test {
            val emission = awaitItem()
            assertEquals(expected = randomUserResponse.results[0], actual = emission)
        }
    }
}