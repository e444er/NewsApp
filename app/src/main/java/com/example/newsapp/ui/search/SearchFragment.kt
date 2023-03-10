package com.example.newsapp.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentSearchBinding
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.main.MainFragmentDirections
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding: FragmentSearchBinding by viewBinding()

    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        searchText()
        setClick()

        viewModel.searchLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBarSearch.visibility = View.GONE
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    binding.progressBarSearch.visibility = View.GONE
                    response.message?.let {
                        Log.d("checkData", "MainFragment: error: $it")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBarSearch.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun searchText() {
        var job: Job? = null
        binding.editSearch.addTextChangedListener { text: Editable? ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                text?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.getSearchNews(query = it.toString())
                    }
                }
            }
        }
    }


    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun setClick() {
        newsAdapter.onItemClickListener = {
            val nav = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(
                article = it
            )
            findNavController().navigate(nav)
        }
        newsAdapter.onShareClickListener = {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, it.url)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share using..."))
        }
    }
}