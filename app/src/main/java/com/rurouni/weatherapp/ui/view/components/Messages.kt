package com.rurouni.weatherapp.ui.view.components

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object Messages {
    fun noInternetMessage(context: Context) {
        Toast.makeText(context, "You do not have internet connection", Toast.LENGTH_LONG).show()
    }

    fun noLocationPermissionMessage(context: Context) {
        Toast.makeText(context, "You do not have location permission", Toast.LENGTH_LONG).show()
    }

    fun deniedLocationPermissionMessage(view: View,  action: () -> Unit,) {
        CustomSnackbar.showActionSnackbar(view, "We have to know your location for showing weather data", "Give Permission", action, Snackbar.LENGTH_INDEFINITE)
    }

    fun openLocationMessage(view: View,  action: () -> Unit) {
        CustomSnackbar.showActionSnackbar(view, "We have to know your location for showing weather data", "Open Location", action, Snackbar.LENGTH_INDEFINITE)
    }
}