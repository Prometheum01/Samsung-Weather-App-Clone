package com.rurouni.weatherapp.ui.view.components

import android.view.View
import com.google.android.material.snackbar.Snackbar

object CustomSnackbar {

    fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(view, message, duration).show()
    }

    fun showActionSnackbar(
        view: View,
        message: String,
        actionMessage: String,
        action: () -> Unit,
        duration: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(view, message, duration).setAction(actionMessage) {
            action()
        }.show()
    }
}
