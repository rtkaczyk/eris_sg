package rtkaczyk.eris.sg.client

import android.content.Context
import scala.util.control.Exception.ignoring
import android.content.Intent
import rtkaczyk.eris.api.Common
import android.util.Log
import android.app.ActivityManager
import scala.collection.JavaConversions._


trait ApiClient extends Context with Common {
  
  private def bound = ErisConnection.bound
  private def bound_=(b: Boolean) { ErisConnection.bound = b} 

  def isErisRunning() = {
    val manager = getSystemService(Context.ACTIVITY_SERVICE).asInstanceOf[ActivityManager]
    manager getRunningServices Int.MaxValue exists { _.service.getClassName == ErisServiceClass }
  }
  
  def api = ErisConnection.api
  def isBound = api != null && bound
  def isUnbound = api == null && !bound
  
  protected def bindEris() {
    if (!bound) {
      Log.d(TAG, "Binding Eris")
      bound = bindService(new Intent(ErisServiceClass), ErisConnection, Context.BIND_AUTO_CREATE)
    }
  }
  
  protected def unbindEris() {
    if (bound) {
      Log.d(TAG, "Unbinding Eris")
      ignoring(classOf[IllegalArgumentException]) {
        unbindService(ErisConnection)
      }
      bound = false
    }
  }
  
  def onBindEris {}
  def onUnbindEris {}
  
  protected def listenBind {
    ErisConnection addListener ApiClient.this
  }
  
  protected def unlistenBind {
    ErisConnection removeListener ApiClient.this
  }
}
