package com.app.myapplication.fragment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.myapplication.model.ModelNews
import com.app.myapplication.utils.AppState
import com.app.myapplication.utils.Constants
import com.app.rescuemanagmentsystem.network.BaseApiRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val apiRepo: BaseApiRepo) : ViewModel() {

    private var _stateListData = MutableLiveData<AppState<ModelNews>>()
    val stateListData: MutableLiveData<AppState<ModelNews>> get() = _stateListData

    fun getNewsApi() = viewModelScope.launch {
        _stateListData.value = AppState.Loading()
        apiRepo.newApiApi(Constants.QUERY, Constants.API_KEY).collect {
            _stateListData.value = it
        }
    }

}