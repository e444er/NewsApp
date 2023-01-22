package com.example.newsapp.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentFavoriteBinding
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.details.DetailViewModel
import com.example.newsapp.ui.search.SearchFragmentDirections
import com.example.newsapp.ui.search.SearchViewModel
import com.example.newsapp.utils.Resource
import com.example.newsapp.utils.SwipeToDeleteCallback
import com.example.newsapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding: FragmentFavoriteBinding by viewBinding()
    private val viewModel by viewModels<DetailViewModel>()
    private lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        viewModel.getArticles.observe(viewLifecycleOwner){
            newsAdapter.differ.submitList(it)
        }
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter()
        binding.rvFav.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            setHasFixedSize(true)


            val swipe = object  : SwipeToDeleteCallback(){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val item = newsAdapter.differ.currentList[viewHolder.adapterPosition]
                    viewModel.deleteArticles(item)
                }
            }
            val touchHelper = ItemTouchHelper(swipe)
            touchHelper.attachToRecyclerView(binding.rvFav)
        }

        newsAdapter.onItemClickListener = {
            val nav = FavoriteFragmentDirections.actionFavoriteFragmentToDetailsFragment(
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