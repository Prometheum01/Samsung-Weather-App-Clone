package com.rurouni.weatherapp.utils

import android.content.Context

class ApiResultHandler<T>(private val context: Context, private val onLoading: () -> Unit, private val onSuccess: (T?) -> Unit, private val onFailure: (T?) -> Unit) {

    fun handleApiResult(result: NetWorkResult<T?>) {
        when (result.status) {
            ApiStatus.LOADING -> {
                onLoading()
            }
            ApiStatus.SUCCESS -> {
                onSuccess(result.data)
            }

            ApiStatus.ERROR -> {
                onFailure(result.data)
                Utils.showAlertDialog(context, result.message.toString())
            }
        }
    }
}