package com.omkar.imagequery.ui

import androidx.lifecycle.ViewModel
import com.omkar.imagequery.ItemRepository

class ImageDetailsViewModel(
    repository: ItemRepository,
    itemID: Int
) : ViewModel() {
    val item = repository.getItemById(itemID)
}
