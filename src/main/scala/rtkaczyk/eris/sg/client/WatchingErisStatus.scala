package rtkaczyk.eris.sg.client

import rtkaczyk.eris.api.Events._
import android.content.Context
import android.util.Log
import rtkaczyk.eris.api.Common

trait WatchingErisStatus extends Context with Common {
  private val receiver = EventReceiver {
    case ErisStarted => {
      Log.d(TAG, "onErisStart")
      onErisStart
    }
    case ErisStopped => {
      Log.d(TAG, "onErisStop")
      onErisStop
    }
    case _ => 
  }
  
  protected def watchErisStatus {
    receiver registerWith this
  }
  
  protected def stopWatchingErisStatus {
    receiver unregisterWith this
  }
  
  protected def onErisStart {}
  protected def onErisStop {}
}