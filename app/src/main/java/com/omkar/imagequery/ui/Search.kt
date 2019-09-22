package com.omkar.imagequery.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.omkar.imagequery.R
import kotlinx.android.synthetic.main.search_fragment.*


class Search : Fragment() {

    companion object {
        const val TAG = "Search"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearch()
    }

    private fun setUpSearch() {
        input.setOnEditorActionListener { _: View, action_id: Int, _: KeyEvent? ->
            val query = input.text.toString().trim()
            if (action_id == KeyEvent.KEYCODE_ENTER && action_id == KeyEvent.ACTION_DOWN && query.isEmpty().not()) {
                navigate(query)
                true
            } else {
                if (input.text.isEmpty()) {
                    input.error = "This can't be empty"
                }
                false
            }
        }

        input.setOnKeyListener { _: View, keyCode: Int, event: KeyEvent ->
            val query = input.text.toString().trim()
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER && query.isEmpty().not()) {
                navigate(query)
                true
            } else {
                false
            }
        }
    }

    private fun navigate(query: String) {
        findNavController().navigate(R.id.action_search_to_results, Bundle().also {
            it.putString("query", query)
        })
        Log.d(TAG, "navigate: $query")
    }

}
