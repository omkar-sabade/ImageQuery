package com.omkar.imagequery.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omkar.imagequery.ItemRepository
import com.omkar.imagequery.ui.ImageDetailsViewModel

@Suppress("UNCHECKED_CAST")
class ImageDetailViewModelFactory(
    private val repository: ItemRepository,
    private val itemID: Int
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageDetailsViewModel::class.java)) {
            return ImageDetailsViewModel(repository, itemID) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
