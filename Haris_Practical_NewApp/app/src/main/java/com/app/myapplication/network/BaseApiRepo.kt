package com.app.rescuemanagmentsystem.network

import android.content.Context
import com.app.myapplication.model.ModelNews
import com.app.myapplication.utils.AppState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseApiRepo @Inject constructor(
    @ApplicationContext val context: Context,
   private val baseApiService: ApiServices
) :
    BaseApiResponse() {

    fun newApiApi(query: String, apiKey: String): Flow<AppState<ModelNews>> =
        flow {
            emit(safeApiCall(context, { baseApiService.getNew(query, apiKey) }))
        }.flowOn(Dispatchers.IO)


}