package rtkaczyk.eris.sg.events

import android.widget.ArrayAdapter
import rtkaczyk.eris.api.Events.ErisEvent
import rtkaczyk.eris.api.Common
import android.content.Context
import java.util.ArrayList
import java.text.SimpleDateFormat
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import rtkaczyk.eris.sg._
import TypedResource._
import java.util.Date
import android.util.Log

class EventsAdapter private (val context: Context, val resourceId: Int, val events: ArrayList[(ErisEvent, Long)])
extends ArrayAdapter(context, resourceId, events) with Common {
  
  private val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")
  
  def this(context: Context) = this(context, R.layout.event_row, new ArrayList[(ErisEvent, Long)])

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
    val inflater = (context getSystemService Context.LAYOUT_INFLATER_SERVICE)
      .asInstanceOf[LayoutInflater]

    val (event, date) = events get position
    val row = inflater.inflate(R.layout.event_row, parent, false)
    
    row findView TR.event_name setText event.shortEvent
    row findView TR.event_date setText (sdf format new Date(date))
    if (event.description.isDefined)
      row findView TR.event_desc setText event.description.get
    else 
      row findView TR.event_desc setVisibility View.GONE

    row
  }

  def refresh(es: Seq[(ErisEvent, Long)]) {
    events.clear
    es foreach events.add
    
    notifyDataSetChanged
  }
}