package com.example.project1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log

class SoundModeReceiver(private val onSoundModeChanged: (IconAdapter.SoundMode) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (AudioManager.RINGER_MODE_CHANGED_ACTION == intent.action) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val soundMode = when (audioManager.ringerMode) {
                AudioManager.RINGER_MODE_NORMAL -> IconAdapter.SoundMode.NORMAL
                AudioManager.RINGER_MODE_VIBRATE -> IconAdapter.SoundMode.VIBRATION
                AudioManager.RINGER_MODE_SILENT -> IconAdapter.SoundMode.SILENT
                else -> IconAdapter.SoundMode.NORMAL
            }
            Log.d("SoundModeReceiver", "Sound mode changed: $soundMode")
            onSoundModeChanged(soundMode)
        }
    }
}
