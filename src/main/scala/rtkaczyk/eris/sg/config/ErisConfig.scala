package rtkaczyk.eris.sg.config

import rtkaczyk.eris.api.Common
import android.content.SharedPreferences

object ErisConfig extends Common {
  private var fullDiscovery = false
  private var deviceName    = ".*"
  private var ignorePeriod  = "300"
  private var rfcommChannel = "2"
  private var storageCap    = "10000"
  private var fwdAuto       = false
  private var fwdAddress    = ""
    
  def update(prefs: SharedPreferences) {
    fullDiscovery = prefs.getBoolean("full_discovery",   false)
    deviceName    = prefs.getString ("device_name",      ".*")
    ignorePeriod  = prefs.getString ("ignore_period",    "300")
    rfcommChannel = prefs.getString ("rfcomm_channel",   "2")
    storageCap    = prefs.getString ("storage_capacity", "10000")
    fwdAuto       = prefs.getBoolean("auto_forwarding",  false)
    fwdAddress    = prefs.getString ("forward_address",  "")
  }
  
  def toXml: String = {
    <conf>
      <discovery>
        <auto>true</auto>
        <full>{ fullDiscovery }</full>
        <ignore-period>{ ignorePeriod }</ignore-period>
        <restrict-name>{ deviceName }</restrict-name>
      </discovery>
      <receiver>
        <auto>true</auto>
        <channel>{ rfcommChannel }</channel>
        <batch-size>0</batch-size>
      </receiver>
      <storage>
        <capacity>{ storageCap }</capacity>
        <vacuum-percent>20</vacuum-percent>
      </storage>
      <forwarder>
        <auto>{ fwdAuto }</auto>
        <address>{ fwdAddress }</address>
        <batch-size> 10 </batch-size>
      </forwarder>
    </conf>.toString
  }
}