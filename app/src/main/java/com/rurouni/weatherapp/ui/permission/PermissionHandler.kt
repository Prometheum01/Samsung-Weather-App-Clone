package com.rurouni.weatherapp.ui.permission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionHandler(private val activity: AppCompatActivity) {

    fun checkPermission(
        permission: String,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        onPermissionGranted: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                // Kullanıcıya açıklama göster, Snackbar vs.
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
}