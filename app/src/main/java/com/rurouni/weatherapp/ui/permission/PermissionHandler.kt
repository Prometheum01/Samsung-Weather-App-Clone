package com.rurouni.weatherapp.ui.permission

import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rurouni.weatherapp.ui.view.components.Messages

class PermissionHandler(private val activity: AppCompatActivity) {

    fun checkPermission(
        permission: String,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        onRationale: () -> Unit,
        onPermissionGranted: () -> Unit
    ) {
        when {
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED -> {
                onPermissionGranted()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
                onRationale()
            }
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
}