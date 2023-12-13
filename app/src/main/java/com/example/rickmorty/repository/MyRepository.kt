package com.example.rickmorty.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.rickmorty.network.model.service.CharacterApi
import com.example.rickmorty.paging.PagingSource
import javax.inject.Inject

class MyRepository @Inject constructor( val api: CharacterApi) {

    fun getRickMorty()= Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200
        ), pagingSourceFactory = { PagingSource(api) }
    ).flow

    fun getFilteredRickMorty(name: String="", status: String="", gender :String="")= Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 200
        ), pagingSourceFactory = { PagingSource(api, name,status, gender ) }
    ).flow

}