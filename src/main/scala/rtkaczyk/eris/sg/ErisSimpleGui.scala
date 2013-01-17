package rtkaczyk.eris.sg

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.app.TabActivity
import android.content.Intent
import android.view.Window
import android.content.Context
import android.util.Log
import rtkaczyk.eris.sg.packets.PacketsActivity
import rtkaczyk.eris.sg.events.EventsActivity
import rtkaczyk.eris.sg.client.ApiClient
import rtkaczyk.eris.sg.client.WatchingErisStatus
import rtkaczyk.eris.sg.events.WatchdogService
import rtkaczyk.eris.sg.config.ConfigActivity
import android.content.SharedPreferences
import rtkaczyk.eris.sg.config.ErisConfig

class ErisSimpleGui extends TabActivity with TypedActivity with ApiClient 
with Toasting with WatchingErisStatus {
  
  private var stopRequested = false
  private var preferences: SharedPreferences = null
  
  val prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
    override def onSharedPreferenceChanged(prefs: SharedPreferences, key: String) {
      ErisConfig update prefs
      onConfigChanged
    }
  }
  
  override def onCreate(bundle: Bundle) {
    Log.d(TAG, "onCreate")
    super.onCreate(bundle)
    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
    setContentView(R.layout.main)
    getWindow setFeatureInt (Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar)

    val eventsSpec = getTabHost newTabSpec "Events"
    eventsSpec setIndicator ("Events", getResources getDrawable R.drawable.icon_events)
    eventsSpec setContent (new Intent(this, classOf[EventsActivity]))
    
    val packetsSpec = getTabHost newTabSpec "Packets"
    packetsSpec setIndicator ("Packets", getResources getDrawable R.drawable.icon_packets)
    packetsSpec setContent (new Intent(this, classOf[PacketsActivity]))
    
    getTabHost addTab eventsSpec
    getTabHost addTab packetsSpec
    
    
    preferences = getSharedPreferences("ErisConfig", Context.MODE_PRIVATE)
    ErisConfig update preferences
    preferences registerOnSharedPreferenceChangeListener prefListener
    
    
    startWatchdog
    
    listenBind
    if (isErisRunning)
      bindEris
    watchErisStatus
  }
  
  override def onResume() {
    Log.d(TAG, "onResume")
    super.onResume
    signalBind
  }
  
  override def onDestroy() {
    Log.d(TAG, "onDestroy")
    
    preferences unregisterOnSharedPreferenceChangeListener prefListener
    
    unlistenBind
    stopWatchingErisStatus
    unbindEris
    
    if(stopRequested)
      stopWatchdog
    
    super.onDestroy
  }
  
  override def onCreateOptionsMenu(menu: Menu) = {
    getMenuInflater.inflate(R.layout.menu, menu)
    true
  }

  override def onOptionsItemSelected(item: MenuItem) = {
    item.getItemId match {
      case R.id.menu_start => {
        startEris
        true
      }
      case R.id.menu_stop => {
        stopEris
        true
      }
      case R.id.menu_forward => {
        toast("Forwarding packets")
        true
      }
      case R.id.menu_preferences => {
        showConfig
        true
      }
      case _ => super.onOptionsItemSelected(item)
    }
  }
  
  def startEris {
    stopRequested = false
    if (!isBound || !isErisRunning) {
      Log.i(TAG, "Connecting to Eris service")
      startService(
          new Intent(ErisServiceClass)
          putExtra("xmlConfig", ErisConfig.toXml)
      )
      bindEris
      signalBind
    } else toast("Already connected")
  }

  def stopEris {
    stopRequested = true
    if (!isUnbound) {
      Log.i(TAG, "Disconnecting from Eris service")
      unbindEris
      stopService(new Intent(ErisServiceClass))
      if (!isUnbound)
        onUnbindEris
    } else toast("Already disconnected")
  }
  
  def showConfig {
    startActivity(new Intent(this, classOf[ConfigActivity]))
  }
  
  def onConfigChanged {
    if (isBound)
      api xmlConfigure ErisConfig.toXml
  }
  
  override def onErisStart = bindEris
  override def onErisStop = { unbindEris; signalBind }
  
  override def onBindEris {
    Log.d(TAG, "onBindEris")
    signalBind
    toast("Connected to Eris")
  }
  
  override def onUnbindEris {
    Log.d(TAG, "onUnbindEris")
    signalBind
    toast("Disconnected from Eris")
  }
  
  def signalBind {
    val image = if (isBound) R.drawable.icon_greenled else R.drawable.icon_redled
    findView(TR.status) setImageResource image
  }
  
  def startWatchdog: Unit = startService(new Intent(getBaseContext, classOf[WatchdogService]))
  def stopWatchdog: Unit = stopService(new Intent(getBaseContext, classOf[WatchdogService]))
}
