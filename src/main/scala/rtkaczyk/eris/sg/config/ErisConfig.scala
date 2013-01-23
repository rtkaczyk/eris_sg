package rtkaczyk.eris.sg.config

import rtkaczyk.eris.api.Common
import android.content.SharedPreferences

object ErisConfig extends Common {
  private var fullDiscovery = false
  private var deviceName    = ".*"
  private var ignorePeriod  = "60"
  private var rfcommChannel = "2"
  private var storageCap    = "10000"
  private var fwdAuto       = false
  private var fwdAddress    = ""
  private var fwdInterval   = "60"
    
  def update(prefs: SharedPreferences) {
    fullDiscovery = prefs.getBoolean("full_discovery",   false)
    deviceName    = prefs.getString ("device_name",      ".*")
    ignorePeriod  = prefs.getString ("ignore_period",    "60")
    rfcommChannel = prefs.getString ("rfcomm_channel",   "2")
    storageCap    = prefs.getString ("storage_capacity", "10000")
    fwdAuto       = prefs.getBoolean("auto_forwarding",  false)
    fwdAddress    = prefs.getString ("forward_address",  "")
    fwdInterval   = prefs.getString ("forward_interval", "60")
  }
  
  def toXml: String = {
    <conf>
      <discovery
        auto="true"
        full={ fullDiscovery.toString }
        ignore-period={ ignorePeriod }
        restrict-name={ deviceName } >
        <!--accept>01:23:45:67:89:ab</accept-->
        <!--reject>00:00:00:00:00:00</reject-->
      </discovery>
      <receiver
        auto="true"
        channel={ rfcommChannel }
        timeout="5000"
        batch="0"
        full="true" />
      <storage
        capacity={ storageCap }
        vacuum-percent="20"
        vacuum-after-each-batch="false" />
      <forwarding
        auto={ fwdAuto.toString }
        batch="100"
        timeout="10000"
        success-interval={ fwdInterval }
        failure-interval={ fwdInterval }
        with-timestamp="true" >
        <address>{ fwdAddress }</address>
      </forwarding>
    </conf>.toString
  }
}