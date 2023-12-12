package com.example.rickmorty.network.model.service

import com.example.rickmorty.network.model.Result
import com.example.rickmorty.network.model.RickandMorty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {


    @GET("character")
    suspend fun getData(
        @Query("page") page: Int,
        @Query("name") name: String = "",
        @Query("status") status: String = "",
        @Query("gender") gender: String = ""
    ): Response<RickandMorty>



}