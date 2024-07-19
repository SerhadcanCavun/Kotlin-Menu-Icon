import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings

class AirplaneModeReceiver(private val adapter: IconAdapter) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED == intent.action) {
            val isAirplaneModeOn = Settings.System.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON, 0
            ) != 0
            adapter.updateAirplaneMode(isAirplaneModeOn)
        }
    }
}
