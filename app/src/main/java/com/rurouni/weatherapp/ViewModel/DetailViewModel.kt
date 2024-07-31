package com.rurouni.weatherapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.rurouni.weatherapp.model.WeatherModel
import com.rurouni.weatherapp.service.WeatherRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException

class DetailViewModel(private val weatherRepository: WeatherRepository) : ViewModel()  {
    val errorMessage = MutableLiveData<String>()
    val weatherModel = MutableLiveData<WeatherModel>()
    val loading = MutableLiveData<Boolean>()
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getData(location : String, date : String) {
        loading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val response = weatherRepository.getWeatherWithDate(location, date)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { weatherModel.value = it }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                    loading.value = false
                }
            } catch (e : Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.localizedMessage}")
                    loading.value = false
                }
            }
        }
    }

    private suspend fun <T> Task<T>.await(): T {
        return suspendCancellableCoroutine { continuation ->
            addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result, null)
                } else {
                    continuation.resumeWithException(task.exception ?: Exception("Unknown Task Exception"))
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}