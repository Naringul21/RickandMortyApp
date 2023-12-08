package com.example.rickmorty.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickmorty.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(  val repo :MyRepository) :ViewModel() {

    fun getCharacters() = repo.getRickMorty().cachedIn(viewModelScope)


}