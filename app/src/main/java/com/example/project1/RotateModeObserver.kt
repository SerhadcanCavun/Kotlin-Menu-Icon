package com.example.project1

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.provider.Settings

class RotateModeObserver(
    private val context: Context,
    private val onRotateModeChanged: (Boolean) -> Unit
) : ContentObserver(Handler()) {

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        val isAutoRotate = isAutoRotateEnabled()
        onRotateModeChanged(isAutoRotate)
    }

    private fun isAutoRotateEnabled(): Boolean {
        return try {
            Settings.System.getInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            ) == 1
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}
