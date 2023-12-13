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

    init {
        getCharacters()
    }

    fun getCharacters() = repo.getRickMorty().onEach {
        _characterPagingData.value = it
    }.launchIn(viewModelScope)


    fun updateFilterState(
        name: String = "",
        status: StatusType = StatusType.ALL,
        gender: GenderType = GenderType.ALL,
    ) {
        _filterState.update {
            it.copy(
                name = null, status = status, gender = gender
            )
        }
    }

    fun getFilteredCharacters(
        name: String = "",
        status: StatusType = StatusType.ALL,
        gender: GenderType = GenderType.ALL
    ) =
        repo.getFilteredRickMorty(name, status.statusName, gender.genderName).onEach {
            _characterPagingData.value = it
        }.launchIn(viewModelScope)


}

data class FilterState(
    val name: String? = null,
    val status: StatusType = StatusType.ALL,
    val gender: GenderType = GenderType.ALL
)

enum class StatusType(val statusName: String) {
    ALL(" "),
    ALIVE("alive"),
    DEAD("dead"),
    UNKNOWN("unknown");

    companion object {
        fun from(value: String) =
            values().find { it.statusName.lowercase() == value.lowercase() } ?: ALL
    }
}

enum class GenderType(val genderName: String) {
    ALL(" "),
    MALE("male"),
    FEMALE("female"),
    UNKNOWN("unknown"),
    GENDERLESS("genderless");

    companion object {
        fun from(value: String) =
            values().find { it.genderName.lowercase() == value.lowercase() } ?: ALL
    }
}