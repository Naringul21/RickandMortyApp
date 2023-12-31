package com.example.rickmorty.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.network.model.Result
import com.example.rickmorty.network.model.service.CharacterApi

class PagingSource (private val characterApi: CharacterApi,
                    val name: String = "",
                    val status: String = "",
                    val gender: String = "") : PagingSource<Int, Result>() {
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {

        return try {
            val currentPage = params.key ?: 1
            val response = characterApi.getData(currentPage, name, status, gender)
            if (response.isSuccessful) {
                val responseData = mutableListOf<Result>()
                val data = response.body()
                data?.let { responseData.addAll(it.results)}
                Log.e("PagingSource", "Sucsess $data")

                LoadResult.Page(
                    data = responseData,
                    prevKey = if (currentPage == 1) null else -1 ,
                    nextKey = currentPage.plus(1)
                )

            } else{
                Log.e("PagingSource", "Error")
                LoadResult.Error(Throwable("nothing"))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)

        }

    }
}