package rtkaczyk.eris.sg.events

import rtkaczyk.eris.api.Common
import android.content.Context
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.content.Intent


//FIXME: czy to potrzebne??
trait BindingWatchdog extends Context with Common {
  
  private var bound = false
  
  val watchdogConn = new ServiceConnection {
    override def onServiceConnected(name: ComponentName, binder: IBinder) {
      //Log.d(TAG, "Connected to WatchdogService")
    }
  
    override def onServiceDisconnected(name: ComponentName) {
      //Log.d(TAG, "Disconnected from WatchdogService")
    }
  }
  
  def watchdog = WatchdogService()
  def isBound = bound && WatchdogService.isRunning
  
  def bindWatchdog {
    if (!bound && WatchdogService.isRunning) {
      bound = bindService(new Intent(this, classOf[WatchdogService]), watchdogConn, 0)
    }
  }
  
  def unbindWatchdog {
    if (bound) {
      unbindService(watchdogConn)
      bound = false
    }
  }
}