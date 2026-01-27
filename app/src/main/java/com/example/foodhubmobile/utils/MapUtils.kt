package com.example.foodhubmobile.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object MapUtils {

    fun openGoogleMaps(context: Context, address: String) {
        val gmmIntentUri = Uri.parse("google.navigation:q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        }
        context.startActivity(mapIntent)
    }
}
