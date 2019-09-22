package com.omkar.imagequery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omkar.imagequery.ItemRepository
import com.omkar.imagequery.ui.ResultsViewModel

@Suppress("UNCHECKED_CAST")
class ResultsViewModelFactory(
    private val repository: ItemRepository,
    private val queryString: String
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResultsViewModel::class.java)) {
            return ResultsViewModel(repository, queryString) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}