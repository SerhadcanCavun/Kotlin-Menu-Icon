package com.example.project1

import IconAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class CellularStateReceiver(private val adapter: IconAdapter) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isCellularOn = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
            adapter.updateCellularState(isCellularOn)
        }
    }
}
