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
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.PhoneStateListener
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var iconList: MutableList<IconItem>
    private lateinit var iconAdapter: IconAdapter
    private lateinit var soundModeReceiver: SoundModeReceiver
    private lateinit var wifiStateReceiver: BroadcastReceiver
    private lateinit var bluetoothStateReceiver: BluetoothStateReceiver
    private lateinit var airplaneModeReceiver: AirplaneModeReceiver
    private lateinit var locationStateReceiver: LocationStateReceiver
    private lateinit var cellularStateReceiver: CellularStateReceiver
    private lateinit var rotateModeObserver: RotateModeObserver
    private lateinit var hotspotBroadcastReceiver: HotspotBroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.O)
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
        rotateModeObserver = RotateModeObserver(this) { isAutoRotate ->
            iconAdapter.updateRotateMode(isAutoRotate)
        }
        contentResolver.registerContentObserver(
            Settings.System.getUriFor(Settings.System.ACCELEROMETER_ROTATION),
            false,
            rotateModeObserver
        )

        wifiStateReceiver = WifiStateReceiver(iconAdapter)
        bluetoothStateReceiver = BluetoothStateReceiver(iconAdapter)
        airplaneModeReceiver = AirplaneModeReceiver(iconAdapter)
        locationStateReceiver = LocationStateReceiver(iconAdapter)
        cellularStateReceiver = CellularStateReceiver(iconAdapter)
        hotspotBroadcastReceiver = HotspotBroadcastReceiver(iconAdapter)
        registerReceivers()

        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
        } else {
            iconAdapter.updateBluetoothState(mBluetoothAdapter.isEnabled())
        }

        val isAirplaneModeOn = isAirplaneModeOn(this)
        iconAdapter.updateAirplaneMode(isAirplaneModeOn)

        val isLocationEnabled = isLocationEnabled(this)
        iconAdapter.updateLocationState(isLocationEnabled)

        val isCellularDataEnabled = isCellularDataEnabled(this)
        iconAdapter.updateCellularState(isCellularDataEnabled)


    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun isCellularDataEnabled(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerReceivers() {
        val soundFilter = IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION)
        registerReceiver(soundModeReceiver, soundFilter)
        val wifiFilter = IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION)
        registerReceiver(wifiStateReceiver, wifiFilter)
        val bluetoothIntentFilter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(bluetoothStateReceiver, bluetoothIntentFilter)
        val airplaneIntentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, airplaneIntentFilter)
        val locationIntentFilter = IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION)
        registerReceiver(locationStateReceiver, locationIntentFilter)
        val cellularIntentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(cellularStateReceiver, cellularIntentFilter)
        val hotspotIntentFilter = IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED")
        registerReceiver(hotspotBroadcastReceiver, hotspotIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(soundModeReceiver)
        unregisterReceiver(wifiStateReceiver)
        unregisterReceiver(bluetoothStateReceiver)
        unregisterReceiver(airplaneModeReceiver)
        unregisterReceiver(locationStateReceiver)
        unregisterReceiver(cellularStateReceiver)
        unregisterReceiver(hotspotBroadcastReceiver)
        contentResolver.unregisterContentObserver(rotateModeObserver)
    }
}