package com.example.newsapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentMainBinding
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.details.DetailViewModel
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding: FragmentMainBinding by viewBinding()
    private val viewModel by viewModels<MainViewModel>()
    private val viewModeld by viewModels<DetailViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        setClick()

        viewModel.newsLiveData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    response.message?.let {
                        Log.d("checkData", "MainFragment: error: $it")
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvMain.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)
        }
    }

    private fun setClick() {
        newsAdapter.onItemClickListener = {
            val nav = MainFragmentDirections.actionMainFragmentToDetailsFragment(
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