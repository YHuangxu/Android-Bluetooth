package yanwenhuangxu.com.bluetooth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

class MainActivity : AppCompatActivity() {
    private val deviceList = ArrayList<BluetoothDevice>()
    private val mReceiver1 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            if (action == BluetoothAdapter.ACTION_STATE_CHANGED) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_STATE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.STATE_TURNING_ON -> statusBluetoothTv.text = application.getString(R.string.turning_bt_on)
                    BluetoothAdapter.STATE_TURNING_OFF -> statusBluetoothTv.text = application.getString(R.string.turning_bt_off)
                    BluetoothAdapter.STATE_ON -> {
                        statusBluetoothTv.text=application.getString(R.string.bt_on)
                        bluetoothIv.setImageResource(R.drawable.ic_action_on)
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        statusBluetoothTv.text=application.getString(R.string.bt_off)
                        bluetoothIv.setImageResource(R.drawable.ic_action_off)
                    }
                }
            }
        }
    }

    private val mReceiver2 = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
                val state = intent.getIntExtra(
                    BluetoothAdapter.EXTRA_SCAN_MODE,
                    BluetoothAdapter.ERROR
                )
                when (state) {
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> discoverableTv.text =
                        application.getString(R.string.dis_on)
                    BluetoothAdapter.SCAN_MODE_NONE -> discoverableTv.text =
                        application.getString(R.string.dis_off)
                }
            }
        }
    }

    //adapter
    private var btAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            statusBluetoothTv.text = application.getString(R.string.not_supported)
        }
        //set image/status  according to bluetooth status(on/off)
        bluetoothIv.setImageResource(R.drawable.ic_action_off)
        statusBluetoothTv.text = application.getString(R.string.bt_off)
        discoverableTv.text = application.getString(R.string.dis_off)
        if (btAdapter.isEnabled) {
            bluetoothIv.setImageResource(R.drawable.ic_action_on)
            statusBluetoothTv.text = application.getString(R.string.bt_on)
        }
        if (btAdapter.scanMode != BluetoothAdapter.SCAN_MODE_NONE) {
            discoverableTv.text = application.getString(R.string.dis_on)
        }

        // Register for broadcasts on BluetoothAdapter state change
        val filter1 = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(mReceiver1, filter1)

        // Register for broadcasts on BluetoothAdapter scan mode change
        val filter2 = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        registerReceiver(mReceiver2, filter2)

        //on btn click
        onBtn.setOnClickListener {
            if (!btAdapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    application.getString(R.string.bt_already_on),
                    Toast.LENGTH_SHORT).show()
            }
        }

        //off btn click
        offBtn.setOnClickListener {
            clearDeviceList()
            if (btAdapter.isEnabled) {
                btAdapter.disable()
            } else {
                Toast.makeText(this,
                    application.getString(R.string.bt_already_off),
                    Toast.LENGTH_SHORT).show()
            }
        }

        //discover bluetooth btn click
        discoverableBtn.setOnClickListener {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
            startActivity(intent)
        }

        //get paired devices btn click
        pairedBtn.setOnClickListener {
            val pairedDevices = btAdapter.bondedDevices
            if (btAdapter.isEnabled) {
                clearDeviceList()
                deviceList.addAll(pairedDevices)
                for (device in deviceList) {
                    pairedTv.append(device.name + " : " + device.address + "\n")
                }
            } else {
                Toast.makeText(
                    this,
                    application.getString(R.string.turn_bt_on_before_get_paired),
                    Toast.LENGTH_SHORT).show()
            }
        }

        scanBtn.setOnClickListener {
            val intent = Intent(this, FoundBTDevices::class.java)
            startActivity(intent)
        }

    }

    private fun clearDeviceList() {
        deviceList.clear()
        pairedTv.text = null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver1)
        unregisterReceiver(mReceiver2)
    }

}