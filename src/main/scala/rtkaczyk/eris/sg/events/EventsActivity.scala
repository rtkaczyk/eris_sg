package rtkaczyk.eris.sg.events

import android.app.Activity
import android.os.Bundle
import rtkaczyk.eris.sg._
import rtkaczyk.eris.api.Events.EventReceiver
import android.util.Log
import rtkaczyk.eris.api.Common

class EventsActivity extends Activity with TypedActivity with Common {
  
  lazy val adapter = new EventsAdapter(this)
  
  val receiver = EventReceiver {
    case _ => refreshAdapter
  }
  
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.events_layout)
    
    val view = findView(TR.list_events)
    view setAdapter adapter
  }
  
  override def onResume {
    super.onResume
    receiver registerWith this
    refreshAdapter
  }
  
  override def onPause {
    receiver unregisterWith this
    super.onPause
  }
  
  def refreshAdapter {
    if (WatchdogService.isRunning)
      adapter refresh WatchdogService().events
  }
}