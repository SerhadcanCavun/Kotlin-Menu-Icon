package com.example.project1

data class IconItem(
    var iconResId: Int,
    var text: String,
    var backgroundColor: Int,
    var isWifiOn: Boolean = false,
    var isBluetoothOn: Boolean = false,
    var isAirplaneModeOn: Boolean = false,
    var isLocationOn: Boolean = false,
    var isCellularOn: Boolean = false,
    var isPowerModeOn: Boolean = false
)
