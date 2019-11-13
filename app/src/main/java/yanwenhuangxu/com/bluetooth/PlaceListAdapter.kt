package yanwenhuangxu.com.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView


class PlaceListAdapter(private val list:ArrayList<BluetoothDevice>,
                       private val context: Context) : RecyclerView.Adapter<PlaceListAdapter.ViewHolder>(),
    Parcelable {

    constructor(parcel: Parcel) : this(
        TODO("list"),
        TODO("context")
    ) {
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adpater, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        fun bindItem(bluetoothDevice: BluetoothDevice) {
            val name = itemView.findViewById(R.id.tvDeviceName) as TextView
            val address = itemView.findViewById(R.id.tvDeviceAddress) as TextView
            name.text = bluetoothDevice.name
            address.text = bluetoothDevice.address
        }

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaceListAdapter> {
        override fun createFromParcel(parcel: Parcel): PlaceListAdapter {
            return PlaceListAdapter(parcel)
        }

        override fun newArray(size: Int): Array<PlaceListAdapter?> {
            return arrayOfNulls(size)
        }
    }
}


