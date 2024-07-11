import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
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

class IconAdapter(private val iconList: List<IconItem>, private val context: Context) :
    RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

        private var isFlashOn = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_icon, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val iconItem = iconList[position]
        holder.bind(iconItem)

        holder.itemView.setOnClickListener{
            when(iconItem.text){
                "WIFI" ->openWifiSettings()
                "BLUETOOTH" ->openBluetoothSetting()
                "LIGHT" ->toggleLight()
            }
        }
    }

    override fun getItemCount() = iconList.size

    private fun openWifiSettings(){
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        context.startActivity(intent)
    }

    private fun openBluetoothSetting()
    {
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        context.startActivity(intent)
    }

    private fun toggleLight(){
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            val cameraId = cameraManager.cameraIdList[0]
            isFlashOn = !isFlashOn
            cameraManager.setTorchMode(cameraId, isFlashOn)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
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
