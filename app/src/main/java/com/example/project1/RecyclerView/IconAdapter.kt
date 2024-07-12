import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.Image.Plane
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.IconItem

class IconAdapter(private val iconList: List<IconItem>, private val context: Context) :
    RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    private var isFlashOn = false
    private var soundMode: SoundMode = SoundMode.NORMAL
    private var rotateMode: RotateMode = RotateMode.ROTATE
    private val audioManager: AudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    enum class SoundMode {
        NORMAL, VIBRATION, SILENT
    }

    enum class RotateMode{
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
                "Light On" -> toggleLight(holder, iconItem)
                "Light Off" -> toggleLight(holder, iconItem)
                "Normal" -> toggleSoundMode(holder, iconItem)
                "Vibration" ->toggleSoundMode(holder, iconItem)
                "Silent" -> toggleSoundMode(holder, iconItem)
                "Rotate" -> toggleRotateMode(holder, iconItem)
                "Portrait" -> toggleRotateMode(holder, iconItem)
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
        if(isFlashOn){
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    isFlashOn = !isFlashOn
                    cameraManager.setTorchMode(cameraId, isFlashOn)
                    holder.icon.setBackgroundColor(Color.parseColor("#ABCF37"))
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
                iconItem.iconResId = R.drawable.icon_light_off
                iconItem.text = "Light Off"
            }
            else{
                val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                try {
                    val cameraId = cameraManager.cameraIdList[0]
                    isFlashOn = !isFlashOn
                    cameraManager.setTorchMode(cameraId, isFlashOn)
                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
                iconItem.iconResId = R.drawable.icon_light_on
                iconItem.text = "Light On"

            }
            holder.bind(iconItem)
        }



    private fun toggleSoundMode(holder: IconViewHolder, iconItem: IconItem) {
        when (soundMode) {
            SoundMode.NORMAL -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                soundMode = SoundMode.VIBRATION
                iconItem.iconResId = R.drawable.icon_vibration // Vibration icon
                iconItem.text = "Vibration"
            }
            SoundMode.VIBRATION-> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                soundMode = SoundMode.SILENT
                iconItem.iconResId = R.drawable.icon_silent // Mute icon
                iconItem.text = "Silent"
            }
            SoundMode.SILENT -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                soundMode = SoundMode.NORMAL
                iconItem.iconResId = R.drawable.icon_normal // Normal icon
                iconItem.text = "Normal"
            }
        }
        // Notify the adapter that the item has changed to trigger a UI update
        holder.bind(iconItem)
    }

    private fun toggleRotateMode(holder: IconViewHolder, iconItem: IconItem){
        when(rotateMode){
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


    private fun requestWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = android.net.Uri.parse("package:${context.packageName}")
        context.startActivity(intent)
    }

    class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val tv_category: TextView = itemView.findViewById(R.id.icon_category)
        fun bind(iconItem: IconItem) {
            icon.setImageResource(iconItem.iconResId)
            tv_category.text = iconItem.text
        }
    }
}
