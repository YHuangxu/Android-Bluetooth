package yanwenhuangxu.com.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle

import kotlinx.android.synthetic.main.scan_device_list.*
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FoundBTDevices : AppCompatActivity() {
    private var mBluetoothAdapter: BluetoothAdapter? = null
    private var arrayOfFoundBTDevices: ArrayList<BluetoothDevice>? = null

    private var listAdapter: PlaceListAdapter?=null
    private var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_device_list)
        layoutManager= LinearLayoutManager(this) as RecyclerView.LayoutManager?
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        lvNewDevices.layoutManager = layoutManager

        displayListOfFoundDevices()
    }

    private fun displayListOfFoundDevices() {
        arrayOfFoundBTDevices = ArrayList<BluetoothDevice>()

        // start looking for bluetooth devices
        mBluetoothAdapter!!.startDiscovery()

        // Discover new devices
        // Create a BroadcastReceiver for ACTION_FOUND
        val mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val action = intent.action
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND == action) {
                    // Get the bluetoothDevice object from the Intent
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    arrayOfFoundBTDevices!!.add(device)
//                    var mDeviceListAdapter = FoundBTDevicesAdaptor(applicationContext, R.layout.adpater,
//                        arrayOfFoundBTDevices!!
//                    )
                    listAdapter = PlaceListAdapter(arrayOfFoundBTDevices!!, applicationContext)
                    lvNewDevices.adapter = listAdapter
                    listAdapter!!.notifyDataSetChanged()
                }
            }
        }


        if (mBluetoothAdapter?.isDiscovering!!) {
            mBluetoothAdapter!!.cancelDiscovery()


            //check BT permissions in manifest
            checkBTPermissions()

            mBluetoothAdapter!!.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mReceiver, discoverDevicesIntent)
        }
        if (!mBluetoothAdapter!!.isDiscovering()) {

            //check BT permissions in manifest
            checkBTPermissions()

            mBluetoothAdapter!!.startDiscovery()
            val discoverDevicesIntent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            registerReceiver(mReceiver, discoverDevicesIntent)
        }
        // Register the BroadcastReceiver
//        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
//        registerReceiver(mReceiver, filter)


    }

    override fun onPause() {
        super.onPause()

        mBluetoothAdapter!!.cancelDiscovery()
    }


    private fun checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            var permissionCheck =
                this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")
            if (permissionCheck != 0) {

                this.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1001
                ) //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.")
        }
    }
}