package rtkaczyk.eris.sg.config

import android.preference.PreferenceActivity
import rtkaczyk.eris.api.Common
import android.os.Bundle
import rtkaczyk.eris.sg._

class ConfigActivity extends PreferenceActivity with Common {
  
  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    
    getPreferenceManager setSharedPreferencesName "ErisConfig"
    addPreferencesFromResource(R.layout.preferences)
  }
}