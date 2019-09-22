package com.omkar.imagequery.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.omkar.imagequery.R
import com.omkar.imagequery.databinding.ImageItemBinding
import com.omkar.imagequery.db.Item

class ImageItemAdapter : ListAdapter<Item, RecyclerView.ViewHolder>(
    object :
        DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem.image.thumbnailLink == newItem.image.thumbnailLink
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ImageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ItemViewHolder).bind(item)

    }

    class ItemViewHolder(
        private val binding: ImageItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.item?.let { item ->
                    navigateToItem(item, it)
                }
            }
            binding.expand.setOnClickListener {
                binding.extras.visibility = when (binding.extras.visibility) {
                    View.VISIBLE -> {
                        binding.expand.setBackgroundResource(R.drawable.ic_arrow_drop_down_black_24dp)
                        View.GONE
                    }
                    else -> {
                        binding.expand.setBackgroundResource(R.drawable.ic_arrow_drop_up_black_24dp)
                        View.VISIBLE
                    }
                }
            }
        }

        private fun navigateToItem(
            item: Item,
            it: View
        ) {
            it.findNavController().navigate(R.id.action_results_to_imageDetails, Bundle().apply {
                this.putInt("itemId", item.id)
            })
        }

        fun bind(imageItem: Item) {
            binding.apply {
                item = imageItem
                executePendingBindings()
            }
        }
    }

}