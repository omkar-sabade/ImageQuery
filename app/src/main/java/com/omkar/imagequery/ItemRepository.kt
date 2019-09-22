package com.omkar.imagequery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.omkar.imagequery.api.CustomSearchService
import com.omkar.imagequery.api.SearchResponse
import com.omkar.imagequery.db.Item
import com.omkar.imagequery.db.ItemDAO
import com.omkar.imagequery.utils.DoAsync
import com.omkar.imagequery.utils.Injector
import com.omkar.imagequery.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemRepository(private val itemDAO: ItemDAO) {

    private val customSearchService: CustomSearchService =
        Injector.getRetrofitInstance().create(CustomSearchService::class.java)

    private val _loadingStatus = MutableLiveData<Resource<Int>>()
    val loadingStatus: LiveData<Resource<Int>> = _loadingStatus

    var items = itemDAO.selectAll()
    var nextStartIndex = 1

    fun searchItems(queryString: String, loadingFalse: () -> Unit) {
        DoAsync {
            if (nextStartIndex == -1) {
                _loadingStatus.postValue(Resource.success(-1))
                loadingFalse()
                return@DoAsync
            }
            _loadingStatus.postValue(Resource.loading(null))
            customSearchService.getSearchResults(queryString, startIndex = nextStartIndex)
                .enqueue(object :
                    Callback<SearchResponse> {
                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        val message = t.message ?: "Unknown Error"
                        _loadingStatus.postValue(Resource.error(message, items.value?.size))
                        loadingFalse()
                    }

                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.code() == 403) {
                            _loadingStatus.postValue(
                                Resource.error(
                                    "API request limit exhausted",
                                    items.value?.size
                                )
                            )
                            return
                        }
                        when (val itemList = response.body()?.items) {
                            null -> _loadingStatus.postValue(Resource.success(0))
                            else -> {
                                insertItems(itemList.filter {
                                    it.isValid()
                                })
                                _loadingStatus.postValue(Resource.success(itemList.size))

                                val nextPage = response.body()?.queries?.nextPage
                                nextStartIndex = if (nextPage.isNullOrEmpty().not()) {
                                    val a = nextPage?.get(0)
                                    when (a != null) {
                                        true -> a.startIndex!!
                                        else -> -1
                                    }
                                } else {
                                    -1
                                }
                            }
                        }
                        loadingFalse()
                    }
                })
        }
    }


    fun insertItems(items: List<Item>) {
        DoAsync {
            itemDAO.insertAll(items)
        }
    }

    fun getItemById(itemId: Int): LiveData<Item> {
        return itemDAO.selectById(itemId)
    }

    fun deleteAll() {
        DoAsync {
            itemDAO.deleteAll()
        }
    }

    companion object {
        @Volatile
        private var instance: ItemRepository? = null

        fun getInstance(itemDAO: ItemDAO): ItemRepository {
            return instance ?: synchronized(this) {
                instance ?: ItemRepository(itemDAO).also { instance = it }
            }
        }
    }


}