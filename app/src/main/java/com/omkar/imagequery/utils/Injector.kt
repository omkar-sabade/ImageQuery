package com.omkar.imagequery.utils

import android.content.Context
import com.omkar.imagequery.ItemRepository
import com.omkar.imagequery.db.SearchDB
import com.omkar.imagequery.viewmodels.ImageDetailViewModelFactory
import com.omkar.imagequery.viewmodels.ResultsViewModelFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Injector {
    private fun getResultsRepository(context: Context): ItemRepository {
        return ItemRepository.getInstance(SearchDB.getInstance(context.applicationContext).itemDAO())
    }

    fun provideResultsViewModelFactory(
        context: Context,
        queryString: String
    ): ResultsViewModelFactory {
        return ResultsViewModelFactory(getResultsRepository(context), queryString)
    }

    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(5L, TimeUnit.SECONDS).build()
    }

    fun getBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
    }

    fun getRetrofitInstance() = getBuilder().build()


    fun provideItemDetailViewModelFactory(
        context: Context,
        itemID: Int
    ): ImageDetailViewModelFactory {
        return ImageDetailViewModelFactory(getResultsRepository(context), itemID)
    }

}