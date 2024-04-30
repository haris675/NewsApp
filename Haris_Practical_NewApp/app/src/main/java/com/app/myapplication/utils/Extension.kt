package com.app.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import com.google.android.material.snackbar.Snackbar

object Extension {

    fun String.getDataOrNA(): String {
        return if (this.isEmpty())
            "NA"
        else
            this
    }

    fun Context.isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    fun View.errorSnackBar(
        message: String
    ) {
        Snackbar
            .make(this, message, Snackbar.LENGTH_LONG).show();
    }
}