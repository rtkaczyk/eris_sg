package rtkaczyk.eris.sg.packets

import android.app.Activity
import android.os.Bundle
import android.util.Log
import rtkaczyk.eris.api.Events._
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView
import android.view.View
import rtkaczyk.eris.sg._
import rtkaczyk.eris.sg.client.ApiClient
import android.support.v4.app.FragmentActivity


class PacketsActivity extends FragmentActivity with TypedActivity with ApiClient {
  
  lazy val adapter = new PacketsAdapter(this)
  private var queryId = -1
  
  val receiver = EventReceiver {
    case PacketsStored(_, n) => {
      Log.w(TAG, "Received PacketStored(%d)" format n)
      if (isBound && n > 0)
        requestRefresh
    }
    case QueryCompleted(id) => 
      if (id == queryId)
        refreshAdapter
  }
  
  override def onCreate(savedInstanceState: Bundle) {
    Log.d(TAG, "onCreate")
    super.onCreate(savedInstanceState)
    setContentView(R.layout.packets_layout)
    
    val view = findView(TR.list_packets)
    view setAdapter adapter
    view setOnItemClickListener new PacketClickListener(adapter, 
        getSupportFragmentManager)
    listenBind
  }
  
  override def onResume {
    Log.d(TAG, "onResume")
    super.onResume
    receiver registerWith this
    requestRefresh
  }
  
  override def onPause {
    Log.d(TAG, "onPause")
    receiver unregisterWith this
    super.onPause
  }
  
  override def onDestroy {
    Log.d(TAG, "onDestroy")
    unlistenBind
    super.onDestroy
  }
  
  override def onBindEris = requestRefresh
  
  private def requestRefresh {
    queryId = api.selectPackets(0, 0, "", 100)
  }
  
  private def refreshAdapter {
    if (isBound && queryId != -1) {
      val packets = api.getQuery(queryId)
      if (packets != null)
        adapter refresh packets
    }
  }
}