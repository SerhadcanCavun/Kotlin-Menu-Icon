package com.example.project1

import AirplaneModeReceiver
import BluetoothStateReceiver
import IconAdapter
import WifiStateReceiver
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var audioManager: AudioManager
    private lateinit var iconList: MutableList<IconItem>
    private lateinit var soundModeReceiver: SoundModeReceiver
    private lateinit var iconAdapter: IconAdapter
    private lateinit var wifiStateReceiver: BroadcastReceiver
    private lateinit var bluetoothStateReceiver: BluetoothStateReceiver
    private lateinit var airplaneModeReceiver: AirplaneModeReceiver

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

        iconList = mutableListOf(
            IconItem(R.drawable.icon_wifi_on, "Wifi", Color.WHITE),
            IconItem(R.drawable.icon_normal, "Normal", Color.WHITE),
            IconItem(R.drawable.icon_oto_rotate, "Rotate", Color.CYAN),
            IconItem(R.drawable.icon_bluetooth_on, "Bluetooth", Color.WHITE),
            IconItem(R.drawable.icon_airplane_on, "Airplane Mod", Color.WHITE),
            IconItem(R.drawable.icon_light_on, "Light", Color.WHITE),
            IconItem(R.drawable.icon_power_mode, "Power Mode", Color.WHITE),
            IconItem(R.drawable.icon_cellular_on, "Cellular", Color.WHITE),
            IconItem(R.drawable.icon_blue_light_filter, "Night Light", Color.WHITE),
            IconItem(R.drawable.icon_wifi_tethering_on, "Hotspot", Color.WHITE),
            IconItem(R.drawable.icon_qr_code_scanner, "Qr Scanner", Color.WHITE),
            IconItem(R.drawable.icon_location_on, "Location", Color.WHITE)
        )

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        iconAdapter = IconAdapter(iconList, this)
        recyclerView.adapter = iconAdapter

        soundModeReceiver = SoundModeReceiver { newSoundMode ->
            runOnUiThread {
                iconAdapter.updateSoundMode(newSoundMode)
            }
        }
        wifiStateReceiver = WifiStateReceiver(iconAdapter)
        bluetoothStateReceiver = BluetoothStateReceiver(iconAdapter)
        airplaneModeReceiver = AirplaneModeReceiver(iconAdapter)
        registerReceivers()

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            iconAdapter.updateBluetoothState(mBluetoothAdapter.isEnabled())
        }
    }


    private fun registerReceivers() {
        val soundFilter = IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION)
        registerReceiver(soundModeReceiver, soundFilter)
        val wifiFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, wifiFilter)
        val bluetoothIntentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothStateReceiver, bluetoothIntentFilter)
        val airplaneIntentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, airplaneIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(soundModeReceiver)
        unregisterReceiver(wifiStateReceiver)
        unregisterReceiver(bluetoothStateReceiver)
        unregisterReceiver(airplaneModeReceiver)
    }
}