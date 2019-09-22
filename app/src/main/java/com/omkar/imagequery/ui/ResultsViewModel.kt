package com.omkar.imagequery.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omkar.imagequery.ItemRepository
import com.omkar.imagequery.db.Item

class ResultsViewModel(
    private var repository: ItemRepository,
    private val queryString: String
) : ViewModel() {

    private val _query = MutableLiveData<String>(queryString)
    val query: LiveData<String> = _query

    val itemList: LiveData<List<Item>>? = repository.items
    val loadingStatus = repository.loadingStatus


    fun loadMoreItems(loadingFalse: () -> Unit) = repository.searchItems(queryString, loadingFalse)

    fun getNextStartIndex() = repository.nextStartIndex

    fun setStartIndex(nextStartIndex: Int) {
        repository.nextStartIndex = nextStartIndex
    }

    fun clearList() = repository.deleteAll()

}
