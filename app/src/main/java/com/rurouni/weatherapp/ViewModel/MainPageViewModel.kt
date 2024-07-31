package com.rurouni.weatherapp.ViewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.gms.tasks.Task
import com.rurouni.weatherapp.model.WeatherModel
import com.rurouni.weatherapp.service.WeatherRepository
import kotlinx.coroutines.*
import kotlin.coroutines.resumeWithException

class MainPageViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    val weatherModel = MutableLiveData<WeatherModel>()
    val loading = MutableLiveData<Boolean>()
    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getWeather(fusedLocationClient: FusedLocationProviderClient, context: Context) {
        loading.value = true

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            try {
                val location = getLocation(fusedLocationClient, context)
                val response = weatherRepository.getWeather(location)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { weatherModel.value = it }
                    } else {
                        onError("Error : ${response.message()} ")
                    }
                    loading.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Error: ${e.localizedMessage}")
                    loading.value = false
                }
            }
        }
    }

    private suspend fun getLocation(fusedLocationClient: FusedLocationProviderClient, context: Context): String {
        return withContext(Dispatchers.IO) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val locationResult = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                        override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token
                        override fun isCancellationRequested() = false
                    }
                ).await()

                "${locationResult.latitude},${locationResult.longitude}"
            } else {
                throw Exception("Location permission not granted")
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
