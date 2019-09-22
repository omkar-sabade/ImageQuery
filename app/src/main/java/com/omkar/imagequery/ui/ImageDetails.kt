package com.omkar.imagequery.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.omkar.imagequery.R
import com.omkar.imagequery.databinding.ImageDetailsFragmentBinding
import com.omkar.imagequery.utils.Injector


class ImageDetails : Fragment() {

    private val imageDetailsViewModel: ImageDetailsViewModel by viewModels {
        Injector.provideItemDetailViewModelFactory(requireContext(), arguments!!.getInt("itemId"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<ImageDetailsFragmentBinding>(
            inflater,
            R.layout.image_details_fragment, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = imageDetailsViewModel
        }

        imageDetailsViewModel.item.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "onCreateView: $it")
        })

        activity?.let {
            it.title = "Image Details"
        }
        return binding.root
    }

    companion object {
        const val TAG = "ImageDetails"
    }

}
