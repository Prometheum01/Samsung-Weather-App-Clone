package com.rurouni.weatherapp.data.source.remote

import android.content.Context
import com.rurouni.weatherapp.utils.NetWorkResult
import com.rurouni.weatherapp.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

inline fun <reified T> toResultFlow(context: Context, crossinline call: suspend () -> Response<T>?): Flow<NetWorkResult<T>> {
    return flow {
        val isInternetConnected = Utils.hasInternetConnection(context)
        if (isInternetConnected) {
            emit(NetWorkResult.Loading( true))
            val c = call()
            c?.let { response ->
                try {
                    if (c.isSuccessful && c.body()!=null) {
                        c.body()?.let {
                            emit(NetWorkResult.Success(it))
                        }
                    } else {
                        emit(NetWorkResult.Error(null, response.message()))
                    }
                } catch (e: Exception) {
                    emit(NetWorkResult.Error(null, e.toString()))
                }
            }
        } else {
            emit(NetWorkResult.Error(null, "No Internet Connection"))
        }
    }.flowOn(Dispatchers.IO)
}