import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BluetoothStateReceiver(private val adapter: IconAdapter) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (BluetoothAdapter.ACTION_STATE_CHANGED == intent.action) {
            val bluetoothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            when (bluetoothState) {
                BluetoothAdapter.STATE_ON -> adapter.updateBluetoothState(true)
                BluetoothAdapter.STATE_OFF -> adapter.updateBluetoothState(false)
            }
        }
    }
}
