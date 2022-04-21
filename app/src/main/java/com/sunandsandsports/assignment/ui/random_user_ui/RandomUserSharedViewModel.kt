package com.sunandsandsports.assignment.ui.random_user_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sunandsandsports.assignment.data.repository.RandomUserRepository
import com.sunandsandsports.assignment.model.RandomUserDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shaheer cs on 20/04/2022.
 */
@HiltViewModel
class RandomUserSharedViewModel @Inject constructor(private val randomUserRepository: RandomUserRepository) :
    ViewModel() {


    private val _randomUserPagingData = MutableStateFlow<PagingData<RandomUserDetail>?>(null)
    val randomUserPagingData = _randomUserPagingData.asStateFlow()
    private val _selectedRandomUserDetail = MutableStateFlow<RandomUserDetail?>(null)
    val selectedRandomUserDetail = _selectedRandomUserDetail.asStateFlow()
    private val _coroutineExceptionMessage = MutableSharedFlow<String?>()
    val coroutineExceptionMessage = _coroutineExceptionMessage.asSharedFlow()
    private val handler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _coroutineExceptionMessage.emit(throwable.message)
        }
    }

    init {
        getRandomUserDataList()
    }

    private fun getRandomUserDataList() = viewModelScope.launch(handler) {
        randomUserRepository.getRandomUserList().cachedIn(viewModelScope).collectLatest {
            _randomUserPagingData.value = it
        }
    }

    fun setSelectedRandomUserDetails(randomUserDetailData: RandomUserDetail) {
        _selectedRandomUserDetail.value = randomUserDetailData
    }
}