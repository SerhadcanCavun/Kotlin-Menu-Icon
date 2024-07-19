package com.example.project1

import IconAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

class LocationStateReceiver (private val adapter: IconAdapter): BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(LocationManager.PROVIDERS_CHANGED_ACTION == intent.action) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isLocationOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            adapter.updateLocationState(isLocationOn)
        }
    }
}