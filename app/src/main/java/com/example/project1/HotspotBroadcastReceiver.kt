package com.example.project1

import IconAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class HotspotBroadcastReceiver(private val adapter: IconAdapter): BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent == null || context == null) return

        if(intent.action == "android.net.wifi.WIFI_AP_STATE_CHANGED") {
            val state = intent.getIntExtra("wifi_state", 0)
            val isHotspotActive = state == 13
            adapter.updateHotspotMode(isHotspotActive)
        }
    }
}