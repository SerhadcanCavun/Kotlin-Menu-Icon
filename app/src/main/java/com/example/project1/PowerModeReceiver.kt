package com.example.project1

import IconAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager

class PowerModeReceiver(private val iconAdapter: IconAdapter) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check if the action corresponds to battery saver mode changes
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            val batteryStatus = intent.getIntExtra("status", -1)
            val isPowerSaveMode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
                powerManager.isPowerSaveMode
            } else {
                false
            }
            // Update the adapter based on power save mode status
            iconAdapter.updatePowerMode(isPowerSaveMode)
        }
    }
}
