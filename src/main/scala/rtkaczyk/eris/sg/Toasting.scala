package rtkaczyk.eris.sg

import android.widget.Toast
import android.content.Context

trait Toasting extends Context {
  protected def toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show
  }
}