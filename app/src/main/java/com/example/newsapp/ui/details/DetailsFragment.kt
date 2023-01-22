package com.example.newsapp.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.FragmentDetailsBinding
import com.example.newsapp.model.Article
import com.example.newsapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val binding: FragmentDetailsBinding by viewBinding()
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        like(listOf(args.article))

        Glide.with(binding.root)
            .load(args.article.urlToImage)
            .into(binding.imageView)
        binding.textTitle.text = args.article.title
        binding.textQ.text = args.article.description

        binding.button.setOnClickListener {
            val url = Intent()
                .setAction(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(Uri.parse(args.article.url))
            startActivity(url)

        }

        binding.imageShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, args.article.url)
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share using..."))
        }

        binding.imageFavorite.setOnClickListener {
            viewModel.saveArticles(args.article)
        }
    }

    ///????
    private fun like(article: List<Article>) {
        if (article.isEmpty()) {
            binding.imageFavorite.isVisible = false
            binding.imageFavdis.isVisible = true
        } else {
            binding.imageFavorite.isVisible = true
            binding.imageFavdis.isVisible = false
        }
    }
}