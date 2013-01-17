package rtkaczyk.eris.sg.client

import rtkaczyk.eris.api.IErisApi
import rtkaczyk.eris.api.Common
import android.content.ServiceConnection
import android.content.ComponentName
import android.os.IBinder
import android.util.Log
import scala.collection.mutable.HashSet


private[client] object ErisConnection extends ServiceConnection with Common {
  private var _api: IErisApi = null
  var bound = false
  def api = _api
  
  private val listeners = HashSet[ApiClient]()
  
  override def onServiceConnected(name: ComponentName, binder: IBinder) {
    Log.d(TAG, "Connected to ErisService")
    _api = IErisApi.Stub.asInterface(binder)
    listeners foreach { _.onBindEris }
  }

  override def onServiceDisconnected(name: ComponentName) {
    Log.d(TAG, "Disconnected from ErisService")
    _api = null
    listeners foreach { _.onUnbindEris }
  }
  
  def addListener(listener: ApiClient) {
    listeners += listener
  }
  
  def removeListener(listener: ApiClient) {
    listeners -= listener
  }
}