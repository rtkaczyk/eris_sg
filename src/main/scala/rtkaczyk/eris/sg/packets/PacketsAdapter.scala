package rtkaczyk.eris.sg.packets

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import java.util.ArrayList
import java.util.{Collection => JCollection}
import java.text.SimpleDateFormat
import java.util.Date
import android.util.Log
import rtkaczyk.eris.api.Packet
import rtkaczyk.eris.api.Common
import rtkaczyk.eris.sg.TypedResource._
import java.util.{Collection => JCollection}
import rtkaczyk.eris.sg._
import android.widget.ArrayAdapter

class PacketsAdapter private (val context: Context, val resourceId: Int, val packets: ArrayList[Packet])
extends ArrayAdapter(context, resourceId, packets) with Common {
  
  private val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  
  def this(context: Context) = this(context, R.layout.packet_row, new ArrayList[Packet])

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
    val inflater = (context getSystemService Context.LAYOUT_INFLATER_SERVICE)
      .asInstanceOf[LayoutInflater]

    val packet = packets get position
    val row = inflater.inflate(R.layout.packet_row, parent, false)
    
    row findView TR.packet_device  setText packet.name
    row findView TR.packet_address setText ("(%s)" format packet.address)
    row findView TR.packet_size    setText ("Size: %d bytes" format packet.data.length)
    row findView TR.packet_date    setText (sdf format new Date(packet.time))

    row
  }

  def refresh(ps: JCollection[Packet]) {
    Log.d(TAG, "Displaying packets: [%d]" format ps.size)
    packets.clear
    packets addAll ps
    
    notifyDataSetChanged
  }
  
  def apply(position: Int) = packets get position
}