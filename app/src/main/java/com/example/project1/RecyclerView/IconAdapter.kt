import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.IconItem

class IconAdapter(
    private val iconList: List<IconItem>,
    private val context: Context
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    private var soundMode = SoundMode.NORMAL
    private var rotateMode = RotateMode.ROTATE
    private var isFlashOn: Boolean = false
    private var isWifiOn: Boolean = false
    private var isBluetoothOn: Boolean = false
    private var isAirplaneModeOn: Boolean = false
    private var isLocationOn: Boolean = false
    private var isCellularOn: Boolean = false
    private var isHotspotActive: Boolean = false

    private val audioManager: AudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    enum class SoundMode {
        NORMAL, VIBRATION, SILENT
    }

    enum class RotateMode {
        ROTATE, PORTRAIT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return IconViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val iconItem = iconList[position]
        holder.bind(iconItem)

        holder.itemView.setOnClickListener {
            when (iconItem.text) {
                "Wifi" -> openWifiSettings()
                "Bluetooth" -> openBluetoothSetting()
                "Light" -> toggleLight(holder, iconItem)
                "Normal", "Vibration", "Silent" -> toggleSoundMode(holder, iconItem)
                "Rotate", "Portrait" -> toggleRotateMode(holder, iconItem)
                "Airplane Mod" -> openAirplaneModeSettings()
                "Night Light" -> openNightLightSettings()
                "Location" -> openLocationSettings()
                "Cellular" -> openCellular()
                "Hotspot" -> openHotspotSettings()
                "Power Mode" -> togglePowerMode()
            }
        }
    }

    override fun getItemCount() = iconList.size

    private fun openHotspotSettings() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClassName(
            "com.android.settings",
            "com.android.settings.TetherSettings"
        )
        context.startActivity(intent)
    }

    private fun togglePowerMode() {
        val intent = Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS)
        context.startActivity(intent)
    }

    private fun openCellular() {
        val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
        context.startActivity(intent)
    }

    private fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun openWifiSettings() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        context.startActivity(intent)
    }

    private fun openBluetoothSetting() {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        context.startActivity(intent)
    }

    private fun toggleLight(holder: IconViewHolder, iconItem: IconItem) {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            cameraManager.setTorchMode(cameraId, isFlashOn)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        if(isFlashOn){
            iconItem.backgroundColor = Color.CYAN
            isFlashOn = false
        }
        else{
            iconItem.backgroundColor = Color.WHITE
            isFlashOn = true
        }
        holder.bind(iconItem)
    }

    private fun toggleSoundMode(holder: IconViewHolder, iconItem: IconItem) {
        if (!notificationManager.isNotificationPolicyAccessGranted) {
            requestNotificationPolicyAccess()
            return
        }
        when (soundMode) {
            SoundMode.NORMAL -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                soundMode = SoundMode.VIBRATION
                iconItem.iconResId = R.drawable.icon_vibration // Vibration icon
                iconItem.text = "Vibration"
                iconItem.backgroundColor = Color.CYAN
            }
            SoundMode.VIBRATION -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                soundMode = SoundMode.SILENT
                iconItem.iconResId = R.drawable.icon_silent // Mute icon
                iconItem.text = "Silent"
                iconItem.backgroundColor = Color.WHITE
            }
            SoundMode.SILENT -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                soundMode = SoundMode.NORMAL
                iconItem.iconResId = R.drawable.icon_normal // Normal icon
                iconItem.text = "Normal"
                iconItem.backgroundColor = Color.CYAN
            }
        }
        holder.bind(iconItem)
    }

    private fun toggleRotateMode(holder: IconViewHolder, iconItem: IconItem) {
        when (rotateMode) {
            RotateMode.ROTATE -> {
                disableAutoRotate()
                rotateMode = RotateMode.PORTRAIT
                iconItem.iconResId = R.drawable.icon_portrait
                iconItem.text = "Portrait"
                iconItem.backgroundColor = Color.WHITE
            }
            RotateMode.PORTRAIT -> {
                enableAutoRotate()
                rotateMode = RotateMode.ROTATE
                iconItem.iconResId = R.drawable.icon_oto_rotate
                iconItem.text = "Rotate"
                iconItem.backgroundColor = Color.CYAN
            }
        }
        holder.bind(iconItem)
    }

    private fun enableAutoRotate() {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                1
            )
        } else {
            requestWriteSettingsPermission()
        }
    }

    private fun disableAutoRotate() {
        if (Settings.System.canWrite(context)) {
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            )
        } else {
            requestWriteSettingsPermission()
        }
    }

    private fun openAirplaneModeSettings() {
        val intent = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)
        context.startActivity(intent)
    }

    private fun openNightLightSettings() {
        val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
        context.startActivity(intent)
    }

        fun updateSoundMode(newSoundMode: SoundMode) {
        val soundItemIndex = iconList.indexOfFirst { it.text in listOf("Normal", "Vibration", "Silent") }
        if (soundItemIndex != -1) {
            val soundItem = iconList[soundItemIndex]
            soundItem.iconResId = getSoundIconResId(newSoundMode)
            soundItem.text = getSoundModeText(newSoundMode)
            soundItem.backgroundColor = getSoundBackgroundColor(newSoundMode)
            notifyItemChanged(soundItemIndex)
        }
    }

    private fun getSoundIconResId(soundMode: SoundMode): Int {
        return when (soundMode) {
            SoundMode.NORMAL -> R.drawable.icon_normal
            SoundMode.VIBRATION -> R.drawable.icon_vibration
            SoundMode.SILENT -> R.drawable.icon_silent
        }
    }

    private fun getSoundModeText(soundMode: SoundMode): String {
        return when (soundMode) {
            SoundMode.NORMAL -> "Normal"
            SoundMode.VIBRATION -> "Vibration"
            SoundMode.SILENT -> "Silent"
        }
    }

    private fun getSoundBackgroundColor(soundMode: SoundMode): Int {
        return when (soundMode) {
            SoundMode.NORMAL -> Color.CYAN
            SoundMode.VIBRATION -> Color.CYAN
            SoundMode.SILENT -> Color.WHITE
        }
    }

    fun updateBluetoothState(isOn: Boolean) {
        isBluetoothOn = isOn
        val bluetoothItemIndex = iconList.indexOfFirst { it.text == "Bluetooth" }
        if(bluetoothItemIndex != -1){
            val bluetoothItem = iconList[bluetoothItemIndex]
            bluetoothItem.isBluetoothOn = isOn
            if(isOn) {
                bluetoothItem.backgroundColor = Color.CYAN
            } else {
                bluetoothItem.backgroundColor = Color.WHITE
            }
            notifyItemChanged(bluetoothItemIndex)
        }
    }

    fun updateWifiState(isOn: Boolean) {
        isWifiOn = isOn
        val wifiItemIndex = iconList.indexOfFirst { it.text == "Wifi" }
        if (wifiItemIndex != -1) {
            val wifiItem = iconList[wifiItemIndex]
            wifiItem.isWifiOn = isOn
            if (isOn) {
                wifiItem.backgroundColor = Color.CYAN
            } else {
                wifiItem.backgroundColor = Color.WHITE
            }
            notifyItemChanged(wifiItemIndex)
        }
    }

    fun updateAirplaneMode(isOn: Boolean) {
        this.isAirplaneModeOn = isOn
        val airplaneModItem = iconList.indexOfFirst { it.text == "Airplane Mod" }
        if(airplaneModItem != -1){
            val airplaneModeItem = iconList[airplaneModItem]
            airplaneModeItem.isAirplaneModeOn = isOn
            if(isOn){
                airplaneModeItem.backgroundColor = Color.CYAN
            } else {
                airplaneModeItem.backgroundColor = Color.WHITE
            }
            notifyItemChanged(airplaneModItem)
        }
    }

    fun updateLocationState(isOn: Boolean) {
        this.isLocationOn = isOn
        val locationItemIndex = iconList.indexOfFirst {  it.text == "Location" }
        if(locationItemIndex != -1) {
            val locationItem = iconList[locationItemIndex]
            locationItem.backgroundColor = if(isOn) Color.CYAN else Color.WHITE
            notifyItemChanged(locationItemIndex)
        }
    }

    fun updateCellularState(isOn: Boolean) {
        this.isCellularOn = isOn
        val cellularItemIndex = iconList.indexOfFirst { it.text == "Cellular" }
        if(cellularItemIndex != -1) {
            val cellularItem = iconList[cellularItemIndex]
            cellularItem.backgroundColor = if(isOn) Color.CYAN else Color.WHITE
            notifyItemChanged(cellularItemIndex)
        }
    }

    fun updateRotateMode(isAutoRotate: Boolean) {
        val rotateItemIndex = iconList.indexOfFirst { it.text == "Rotate" || it.text == "Portrait" }
        if (rotateItemIndex != -1) {
            val rotateItem = iconList[rotateItemIndex]
            if (isAutoRotate) {
                rotateItem.iconResId = R.drawable.icon_oto_rotate // Auto-Rotate ikonu
                rotateItem.text = "Rotate"
                rotateItem.backgroundColor = Color.CYAN
            } else {
                rotateItem.iconResId = R.drawable.icon_portrait // Portrait ikonu
                rotateItem.text = "Portrait"
                rotateItem.backgroundColor = Color.WHITE
            }
            notifyItemChanged(rotateItemIndex)
        }
    }

    fun updateHotspotMode(isHotspotActive: Boolean) {
        this.isHotspotActive = isHotspotActive
        val hotspotItemIndex = iconList.indexOfFirst { it.text == "Hotspot" }
        if (hotspotItemIndex != -1) {
            val hotspotItem = iconList[hotspotItemIndex]
            hotspotItem.backgroundColor = if (isHotspotActive) Color.CYAN else Color.WHITE
            notifyItemChanged(hotspotItemIndex)
        }
    }

    private fun requestNotificationPolicyAccess() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        context.startActivity(intent)
    }

    private fun requestWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }

    class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val tv_category: TextView = itemView.findViewById(R.id.icon_category)
        val icon_view: CardView = itemView.findViewById(R.id.iconView)
        fun bind(iconItem: IconItem) {
            icon.setImageResource(iconItem.iconResId)
            tv_category.text = iconItem.text
            icon_view.setCardBackgroundColor(iconItem.backgroundColor)
        }
    }
}
