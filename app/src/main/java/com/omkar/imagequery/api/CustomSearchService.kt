package com.omkar.imagequery.api

import com.omkar.imagequery.utils.API_KEY4
import com.omkar.imagequery.utils.CX4
import com.omkar.imagequery.utils.SEARCH_TYPE
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * REST API access points
 * searchType = image
 * start is startIndex of current search
 */

interface CustomSearchService {
    @GET("customsearch/v1/siterestrict/")
    fun getSearchResults(
        @Query("q") q: String,
        @Query("cx") cx: String = CX4,
        @Query("key") apiKey: String = API_KEY4,
        @Query("searchType") searchType: String = SEARCH_TYPE,
        @Query("start") startIndex: Int = 1
    ): Call<SearchResponse>
}