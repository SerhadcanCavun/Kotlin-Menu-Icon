import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.IconItem

class IconAdapter(
    private val iconList: List<IconItem>,
    private val context: Context,
    private var soundMode: SoundMode,
    private var rotateMode: RotateMode
) : RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    private var isFlashOn = false
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
            }
        }
    }

    override fun getItemCount() = iconList.size

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
            }
            RotateMode.PORTRAIT -> {
                enableAutoRotate()
                rotateMode = RotateMode.ROTATE
                iconItem.iconResId = R.drawable.icon_oto_rotate
                iconItem.text = "Rotate"
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
