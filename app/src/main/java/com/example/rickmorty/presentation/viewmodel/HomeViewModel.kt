package com.example.rickmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickmorty.network.model.Result
import com.example.rickmorty.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: MyRepository) : ViewModel() {

    private val _characterPagingData = MutableStateFlow<PagingData<Result>>(PagingData.empty())
    val characterPagingData = _characterPagingData.cachedIn(viewModelScope)
    fun getCharacters() = repo.getRickMorty().onEach {
        _characterPagingData.value = it
    }.launchIn(viewModelScope)


    fun getFilteredCharacters(name: String="" , gender: String="" , status: String="" ) =
        repo.getFilteredRickMorty(name, gender, status).onEach {
            _characterPagingData.value = it
        }
            .launchIn(viewModelScope)


}