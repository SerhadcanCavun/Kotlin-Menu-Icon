package com.example.project1

import IconAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.graphics.Color
import android.media.AudioManager
import android.hardware.camera2.CameraManager

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var audioManager: AudioManager
    private lateinit var cameraManager: CameraManager
    private var isFlashOn: Boolean = true
    private lateinit var iconList: MutableList<IconItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.rv)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager


        val initialSoundMode = getInitialSoundMode()
        val initialLightMode = getInitialLightMode()

        iconList = mutableListOf(
            IconItem(R.drawable.icon_wifi_on, "Wifi", Color.WHITE),
            IconItem(getSoundIconResId(initialSoundMode), getSoundModeText(initialSoundMode), getSoundBackgroundColor(initialSoundMode)),
            IconItem(R.drawable.icon_oto_rotate, "Rotate", Color.WHITE),
            IconItem(R.drawable.icon_bluetooth_on, "Bluetooth", Color.WHITE),
            IconItem(R.drawable.icon_airplane_on, "Airplane Mod", Color.WHITE),
            IconItem(R.drawable.icon_light_on, "Light", getLightBackgroundColor()),
            IconItem(R.drawable.icon_power_mode, "Power Mode", Color.WHITE),
            IconItem(R.drawable.icon_cellular_on, "Cellular", Color.WHITE),
            IconItem(R.drawable.icon_blue_light_filter, "Night Light", Color.WHITE),
            IconItem(R.drawable.icon_wifi_tethering_on, "Hotspot", Color.WHITE),
            IconItem(R.drawable.icon_qr_code_scanner, "Qr Scanner", Color.WHITE),
            IconItem(R.drawable.icon_location_on, "Location", Color.WHITE)
        )

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = IconAdapter(iconList, this, initialSoundMode, IconAdapter.RotateMode.ROTATE)
    }

    private fun getInitialSoundMode(): IconAdapter.SoundMode {
        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> IconAdapter.SoundMode.NORMAL
            AudioManager.RINGER_MODE_VIBRATE -> IconAdapter.SoundMode.VIBRATION
            AudioManager.RINGER_MODE_SILENT -> IconAdapter.SoundMode.SILENT
            else -> IconAdapter.SoundMode.NORMAL
        }
    }

    private fun getSoundIconResId(soundMode: IconAdapter.SoundMode): Int {
        return when (soundMode) {
            IconAdapter.SoundMode.NORMAL -> R.drawable.icon_normal
            IconAdapter.SoundMode.VIBRATION -> R.drawable.icon_vibration
            IconAdapter.SoundMode.SILENT -> R.drawable.icon_silent
        }
    }

    private fun getSoundModeText(soundMode: IconAdapter.SoundMode): String {
        return when (soundMode) {
            IconAdapter.SoundMode.NORMAL -> "Normal"
            IconAdapter.SoundMode.VIBRATION -> "Vibration"
            IconAdapter.SoundMode.SILENT -> "Silent"
        }
    }

    private fun getSoundBackgroundColor(soundMode: IconAdapter.SoundMode): Int {
        return when(soundMode) {
            IconAdapter.SoundMode.NORMAL -> Color.CYAN
            IconAdapter.SoundMode.VIBRATION -> Color.CYAN
            IconAdapter.SoundMode.SILENT -> Color.WHITE
        }
    }

    private fun getInitialLightMode(): Boolean {
        return true
    }

    private fun getLightBackgroundColor(): Int {
        if(isFlashOn){
            return Color.WHITE
        }else{
            return Color.CYAN
        }
    }
}
