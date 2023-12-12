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
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val repo: MyRepository) : ViewModel() {

    private val _characterPagingData = MutableStateFlow<PagingData<Result>>(PagingData.empty())
    val characterPagingData = _characterPagingData.cachedIn(viewModelScope)

    private val _filterState = MutableStateFlow<FilterState>(FilterState())
    val filterState = _filterState.asStateFlow()
    fun getCharacters() = repo.getRickMorty().onEach {
        _characterPagingData.value = it
    }.launchIn(viewModelScope)


   fun updateFilterState(gender: GenderType = GenderType.ALL, status: StatusType = StatusType.ALL ){
       _filterState.update {
           it.copy(
               gender = gender, status = status
           )
       }
   }

    fun getFilteredCharacters(name: String="", gender: GenderType = GenderType.ALL, status: StatusType = StatusType.ALL ) =
        repo.getFilteredRickMorty(name, status.statusName, gender.genderName).onEach {
            _characterPagingData.value = it
        }
            .launchIn(viewModelScope)


}

data class FilterState(
    val name: String?=null,
    val gender: GenderType=GenderType.ALL,
    val status: StatusType = StatusType.ALL
)

enum class StatusType(val statusName: String){
    ALL("all"),
    ALIVE("alive"),
    DEAD("dead"),
    UNKNOWN("unknown")
}

enum class GenderType(val genderName: String){
    ALL("all"),
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("unknown"),
    GENDERLESS("genderless");

    companion object{
        fun from(value: String) = values().find { it.genderName.lowercase() == value.lowercase() } ?: ALL
    }
}