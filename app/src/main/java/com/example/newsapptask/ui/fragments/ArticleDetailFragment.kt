package com.example.newsapptask.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapptask.R
import com.example.newsapptask.models.Article
import com.example.newsapptask.ui.activities.MainActivity
import com.example.newsapptask.view_model.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article_detail.*

class ArticleDetailFragment : Fragment(R.layout.fragment_article_detail) {

    lateinit var newsViewModel: NewsViewModel
    private val args: ArticleDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        newsViewModel = (activity as MainActivity).newsViewModel
        val article = args.article
        populateData(article)
    }

    private fun populateData(article: Article) {
        Glide.with(this).load(article.urlToImage).placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(imageViewDetail)
        tvSourceDetail.text = article.source?.name
        tvTitleDetail.text = article.title
        tvDescriptionDetail.text = article.description
    }
}