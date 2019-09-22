package com.omkar.imagequery.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omkar.imagequery.R
import com.omkar.imagequery.databinding.ResultsFragmentBinding
import com.omkar.imagequery.ui.adapter.ImageItemAdapter
import com.omkar.imagequery.utils.Injector
import com.omkar.imagequery.utils.Status
import kotlinx.android.synthetic.main.results_fragment.*
import java.util.*

class Results : Fragment() {

    private val viewModel: ResultsViewModel by viewModels {
        Injector.provideResultsViewModelFactory(requireContext(), arguments!!.getString("query")!!)
    }
    private var isLoading = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var onScrollListener: RecyclerView.OnScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<ResultsFragmentBinding>(
            inflater,
            R.layout.results_fragment, container, false
        ).apply {
            this.viewmodel = viewmodel
        }

        val imageItemAdapter = ImageItemAdapter()
        binding.itemList.adapter = imageItemAdapter
        observeItemList(imageItemAdapter)


        activity?.let {
            val searchString = arguments!!.getString("query")!!
            it.title = when {
                searchString.length < 10 -> searchString
                else -> searchString.substring(0, 10)
            }
            sharedPreferences = it.getPreferences(Context.MODE_PRIVATE)
            if (sharedPreferences.contains("query")
                && sharedPreferences.contains("nextStartIndex")
                && sharedPreferences.getString(
                    "query",
                    "NA"
                )?.toLowerCase(Locale.getDefault()) == searchString.toLowerCase(
                    Locale.getDefault()
                )
                && sharedPreferences.getInt("nextStartIndex", 1) != 1
            ) {
                Log.d(TAG, "Loading saved results")
                viewModel.setStartIndex(sharedPreferences.getInt("nextStartIndex", -1))
            } else {
                viewModel.clearList()
                viewModel.setStartIndex(1)
                isLoading = true
                viewModel.loadMoreItems(::setLoadingFalse)
                sharedPreferences.edit().putString("query", searchString).apply()
            }
        }

        binding.moreContent.hide()
        attachLoadOnScrollListener(binding, imageItemAdapter)
        return binding.root

    }

    fun setLoadingFalse() {
        isLoading = false
    }

    private fun attachLoadOnScrollListener(
        binding: ResultsFragmentBinding,
        adapter: ImageItemAdapter
    ) {
        onScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (more_content.isOrWillBeShown) more_content.hide()
                if (isLoading)
                    return
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    isLoading = true
                    viewModel.loadMoreItems(::setLoadingFalse)
                    layoutManager.scrollToPosition(lastPosition + 1)
                }
            }
        }
        binding.itemList.addOnScrollListener(onScrollListener)
    }


    @SuppressLint("SetTextI18n")
    private fun observeItemList(imageItemAdapter: ImageItemAdapter) {
        viewModel.itemList?.observe(viewLifecycleOwner, Observer { items ->
            when {
                items.isNotEmpty() -> {
                    sharedPreferences.edit().putInt("nextStartIndex", viewModel.getNextStartIndex())
                        .apply()
                    Log.d(TAG, "next start index is ${viewModel.getNextStartIndex()}")
                    shimmer_view_container.stopShimmerAnimation()
                    shimmer_view_container.visibility = View.GONE
                }
                else -> shimmer_view_container.visibility = View.VISIBLE
            }
            imageItemAdapter.submitList(items)
            num_results.text = """Showing ${items.size} results"""
        })

        viewModel.loadingStatus.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> startShimmerAnimation()
                Status.SUCCESS -> {
                    when {
                        it.data == -1 -> {
                            Toast.makeText(
                                requireContext(),
                                "That's all for now!", //Nothing more to show
                                Toast.LENGTH_SHORT
                            ).apply {
                                this.view.setBackgroundResource(R.color.dunzoGreen)
                                this.show()
                            }
                            item_list.clearOnScrollListeners()
                        }
                        else -> {
                            if (it.data != 0) {
                                Toast.makeText(
                                    requireContext(),
                                    "Added ${it.data} items!",
                                    Toast.LENGTH_SHORT
                                ).apply {
                                    this.view.setBackgroundResource(R.color.dunzoGreen)
                                    this.show()
                                }
                                more_content.show()
                            }
                        }
                    }
                    stopShimmerAnimation()
                }
                Status.CONSUMED -> {
                    //This means the data is loaded from db
                    load_more_bar.visibility = View.GONE
                }
                else -> {
                    if (it.data == 0) showError(it.message!!)
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load more data. " + it.message,
                            Toast.LENGTH_LONG
                        ).also { toast ->
                            toast.view.setBackgroundResource(R.color.dunzoGreen)
                            toast.show()
                        }
                    }
                    stopShimmerAnimation()
                }
            }
        })
        viewModel.loadingStatus.value?.status = Status.CONSUMED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startShimmerAnimation()
    }

    private fun startShimmerAnimation() {
        if (item_list.adapter?.itemCount == 0) {
            shimmer_view_container.visibility = View.VISIBLE
            shimmer_view_container.startShimmerAnimation()
        }
        load_more_bar.visibility = View.VISIBLE
    }

    private fun stopShimmerAnimation() {
        if (item_list.adapter?.itemCount == 0) {
            shimmer_view_container.visibility = View.GONE
            shimmer_view_container.stopShimmerAnimation()
        }
        load_more_bar.visibility = View.GONE
    }

    private fun showError(errorMessage: String) {
        item_list.visibility = View.GONE
        error_message.visibility = View.VISIBLE
        error_info.text = errorMessage
    }


    override fun onDestroy() {
        activity?.title = "Image Query"
        error_message?.visibility = View.GONE
        super.onDestroy()
    }

    companion object {
        const val TAG = "Results"
    }
}
