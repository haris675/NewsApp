package com.app.myapplication.fragment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.myapplication.App
import com.app.myapplication.model.ArticlesItem
import kotlinx.coroutines.launch

class FavouriteViewModel : ViewModel() {

    private var _stateListData = MutableLiveData<ArrayList<ArticlesItem>>()
    val stateListData: MutableLiveData<ArrayList<ArticlesItem>> get() = _stateListData

    fun getAllFavouriteNews() = viewModelScope.launch {
        _stateListData.value = ArrayList(App.database.favouriteDao().getAllFavouriteNews())
    }

}