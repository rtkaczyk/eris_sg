package rtkaczyk.eris.sg.packets

import android.support.v4.app.DialogFragment
import rtkaczyk.eris.api.Packet
import android.os.Bundle
import android.app.AlertDialog
import rtkaczyk.eris.api.Common
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView
import android.view.View
import android.support.v4.app.FragmentManager

object PacketDialog {
  private var manager: FragmentManager = null
  private var packet: Packet = null
  
  def apply(pkt: Packet, mgr: FragmentManager): PacketDialog = {
    packet = pkt
    manager = mgr
    new PacketDialog
  }
}

class PacketDialog extends DialogFragment with Common {
  import PacketDialog._
  
  override def onCreateDialog(savedInstanceState: Bundle) = {
    val builder = new AlertDialog.Builder(getActivity)
    builder setTitle "Packet content"
    builder setMessage decode(packet.data)
    val dialog = builder.create
    dialog.setCanceledOnTouchOutside(true)
    dialog
  }
  
  def decode(bytes: Array[Byte]): String =
    new String(bytes map { 
      b => if ((0x20 to 0x7E contains b.toInt) || (b.toChar == '\n')) b else '?'.toByte
    })
    
  def show: Unit = show(manager, TAG)
}

class PacketClickListener(adapter: PacketsAdapter, manager: FragmentManager) 
extends OnItemClickListener {
  
  override def onItemClick(parent: AdapterView[_], view: View, 
      position: Int, id: Long) {
    PacketDialog(adapter(position), manager).show
  }
}