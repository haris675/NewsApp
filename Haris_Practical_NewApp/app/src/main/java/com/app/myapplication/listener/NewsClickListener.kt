package com.app.myapplication.listener

import com.app.myapplication.model.ArticlesItem

interface NewsClickListener {

    fun onNewsClicked(item: ArticlesItem)

}