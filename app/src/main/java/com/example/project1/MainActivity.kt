package com.example.project1

import IconAdapter
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

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

        val iconList = listOf(
            IconItem(R.drawable.icon_wifi_on, "Wifi"),
            IconItem(R.drawable.icon_normal, "Normal"),
            IconItem(R.drawable.icon_oto_rotate, "Rotate"),
            IconItem(R.drawable.icon_bluetooth_on, "Bluetooth"),
            IconItem(R.drawable.icon_airplane_on, "Airplane Mod"),
            IconItem(R.drawable.icon_light_on, "Light Off"),
            IconItem(R.drawable.icon_power_mode, "Power Mode"),
            IconItem(R.drawable.icon_cellular_on, "Cellular"),
            IconItem(R.drawable.icon_blue_light_filter, "Night Light"),
            IconItem(R.drawable.icon_wifi_tethering_on, "Hotspot"),
            IconItem(R.drawable.icon_qr_code_scanner, "Qr Scanner"),
            IconItem(R.drawable.icon_location_on, "Location"),
        )

        recyclerView.layoutManager = GridLayoutManager(this, 4)
        recyclerView.adapter = IconAdapter(iconList, this)
    }
}