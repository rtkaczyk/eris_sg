package rtkaczyk.eris.sg.events

import android.app.Service
import android.content.Intent
import android.util.Log
import rtkaczyk.eris.api.Events._
import scala.annotation.tailrec
import rtkaczyk.eris.api.Common
import android.os.Binder


object WatchdogService extends Binder {
  private var _service: WatchdogService = null
  private def service_=(s: WatchdogService) { _service = s }
  def service = _service
  def apply() = _service
  def isRunning = _service != null
}


class WatchdogService extends Service with Common {
  
  object EventQueue {
    var capacity = 100
    private var list = List[(ErisEvent, Long)]()
    
    def add(event: ErisEvent) {
      list ::= (event -> now)
      if (list.size > 2 * capacity)
        list = get
    }
    
    def get = list take capacity
  }
  
  var eventList = EventList
  
  val receiver = EventReceiver {
    e => if (eventList contains e.event)
      EventQueue add e
  }
  
  override def onBind(intent: Intent) = {
    Log.d(TAG, "Binding service: [%s]" format intent.getAction)
    WatchdogService
  }

  override def onCreate() {
    super.onCreate
    Log.d(TAG, "onCreate")
    WatchdogService.service = this
    receiver registerWith this
  }
  
  override def onStartCommand(intent: Intent, flags: Int, startId: Int) = {
    Log.d(TAG, "onStartCommand")
    Service.START_STICKY
  }

  override def onDestroy() {
    Log.d(TAG, "onDestroy")
    WatchdogService.service = null
    receiver unregisterWith this
    super.onDestroy
  }
  
  def events = EventQueue.get
} 