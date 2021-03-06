/* © 2010 Stephan Reichholf <stephan at reichholf dot net>
 * 
 * Licensed under the Create-Commons Attribution-Noncommercial-Share Alike 3.0 Unported
 * http://creativecommons.org/licenses/by-nc-sa/3.0/
 */

package net.reichholf.dreamdroid.helpers.enigma2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.reichholf.dreamdroid.R;
import net.reichholf.dreamdroid.helpers.ExtendedHashMap;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;

/**
 * @author sreichholf
 * 
 */
public class Timer {
	public static final String KEY_REFERENCE = "reference";
	public static final String KEY_SERVICE_NAME = "servicename";
	public static final String KEY_EIT = "eit";
	public static final String KEY_NAME = "name";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_DESCRIPTION_EXTENDED = "descriptionex";
	public static final String KEY_DISABLED = "disabled";
	public static final String KEY_BEGIN = "begin";
	public static final String KEY_BEGIN_READEABLE = "begin_readable";
	public static final String KEY_END = "end";
	public static final String KEY_END_READABLE = "end_readable";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_DURATION_READABLE = "duration_readable";
	public static final String KEY_START_PREPARE = "startprepare";
	public static final String KEY_JUST_PLAY = "justplay";
	public static final String KEY_AFTER_EVENT = "afterevent";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_TAGS = "tags";
	public static final String KEY_LOG_ENTRIES = "logentries";
	public static final String KEY_FILE_NAME = "filename";
	public static final String KEY_BACK_OFF = "backoff";
	public static final String KEY_NEXT_ACTIVATION = "nextactivation";
	public static final String KEY_FIRST_TRY_PREPARE = "firsttryprepare";
	public static final String KEY_STATE = "state";
	public static final String KEY_REPEATED = "repeated";
	public static final String KEY_DONT_SAVE = "dontsave";
	public static final String KEY_CANCELED = "canceled";
	public static final String KEY_TOGGLE_DISABLED = "toggledisabled";
	
	public static final int STATE_WAITING = 0;
	public static final int STATE_PREPARED = 1;
	public static final int STATE_RUNNING = 2;
	public static final int STATE_ENDED = 3;	
	
	public static enum Afterevents {
		NOTHING(0), STANDBY(1), DEEP_STANDBY(2), AUTO(3);

		private int value;

		Afterevents(int val) {
			value = val;
		}

		@Override
		public String toString() {
			return new Integer(value).toString();
		}

		public int intValue() {
			return new Integer(value).intValue();
		}

		public String getText(Activity ac) {
			return (String) ac.getResources().getTextArray(R.array.afterevents)[value];
		}
	}

	/**
	 * @return
	 */
	public static ExtendedHashMap getInitialTimer() {
		ExtendedHashMap timer = new ExtendedHashMap();
		timer.put(KEY_DESCRIPTION, "");
		timer.put(KEY_LOCATION, "/hdd/movie/");
		timer.put(KEY_DISABLED, "0"); // enabled
		timer.put(KEY_JUST_PLAY, "0"); // record
		timer.put(KEY_AFTER_EVENT, Afterevents.AUTO.toString()); // auto
		timer.put(KEY_REPEATED, "0"); // One-Time-Event

		// Calculate values for begin and end
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(new Date());

		long s = (cal.getTimeInMillis() / 1000);
		long e = s + 3600;

		String begin = Long.toString(s);
		String end = Long.toString(e);

		timer.put(KEY_BEGIN, begin);
		timer.put(KEY_END, end);

		return timer;
	}

	/**
	 * @param event
	 * @return
	 */
	public static ExtendedHashMap createByEvent(ExtendedHashMap event) {
		ExtendedHashMap timer = getInitialTimer();

		String start = event.getString(Event.KEY_EVENT_START);
		int duration = new Integer(event.getString(Event.KEY_EVENT_DURATION));
		int end = duration + new Integer(start);

		timer.put(Timer.KEY_BEGIN, start);
		timer.put(Timer.KEY_END, String.valueOf(end));
		timer.put(Timer.KEY_NAME, event.getString(Event.KEY_EVENT_TITLE));
		timer.put(Timer.KEY_DESCRIPTION, event.getString(Event.KEY_EVENT_DESCRIPTION));
		timer.put(Timer.KEY_DESCRIPTION_EXTENDED, event.getString(Event.KEY_EVENT_DESCRIPTION_EXTENDED));
		timer.put(Timer.KEY_SERVICE_NAME, event.getString(Event.KEY_SERVICE_NAME));
		timer.put(Timer.KEY_REFERENCE, event.getString(Event.KEY_SERVICE_REFERENCE));

		return timer;
	}
	
	public static ArrayList<NameValuePair> getSaveParams(ExtendedHashMap timer, ExtendedHashMap timerOld){
		/*
		 * URL to create /web/timerchange? sRef= &begin= &end= &name=
		 * &description= &dirname= &tags= &eit= &disabled= &justplay=
		 * &afterevent= &repeated= &channelOld= &beginOld= &endOld=
		 * &deleteOldOnSave=
		 */
		// Build Parameters using timer HashMap
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sRef", timer.getString(KEY_REFERENCE)));
		params.add(new BasicNameValuePair("begin", timer.getString(KEY_BEGIN)));
		params.add(new BasicNameValuePair("end", timer.getString(KEY_END)));
		params.add(new BasicNameValuePair("name", timer.getString(KEY_NAME)));
		params.add(new BasicNameValuePair("description", timer.getString(KEY_DESCRIPTION)));
		params.add(new BasicNameValuePair("dirname", timer.getString(KEY_LOCATION)));
		params.add(new BasicNameValuePair("tags", timer.getString(KEY_TAGS)));
		params.add(new BasicNameValuePair("eit", timer.getString(KEY_EIT)));
		params.add(new BasicNameValuePair("disabled", timer.getString(KEY_DISABLED)));
		params.add(new BasicNameValuePair("justplay", timer.getString(KEY_JUST_PLAY)));
		params.add(new BasicNameValuePair("afterevent", timer.getString(KEY_AFTER_EVENT)));
		params.add(new BasicNameValuePair("repeated", timer.getString(KEY_REPEATED)));

		if (timerOld != null) {
			params.add(new BasicNameValuePair("channelOld", timerOld.getString(KEY_REFERENCE)));
			params.add(new BasicNameValuePair("beginOld", timerOld.getString(KEY_BEGIN)));
			params.add(new BasicNameValuePair("endOld", timerOld.getString(KEY_END)));
			params.add(new BasicNameValuePair("deleteOldOnSave", "1"));
		} else {
			params.add(new BasicNameValuePair("deleteOldOnSave", "0"));
		}
		
		return params;
	}
	
	public static ArrayList<NameValuePair> getEventIdParams(ExtendedHashMap event){
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sRef", event.getString(Event.KEY_SERVICE_REFERENCE)));
		params.add(new BasicNameValuePair("eventid", event.getString(Event.KEY_EVENT_ID)));
		
		return params;
	}
	
	public static ArrayList<NameValuePair> getDeleteParams(ExtendedHashMap timer){
		// URL - web/timerdelete?sRef=&begin=&end=
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("sRef", timer.getString(KEY_REFERENCE)));
		params.add(new BasicNameValuePair("begin", timer.getString(KEY_BEGIN)));
		params.add(new BasicNameValuePair("end", timer.getString(KEY_END)));
		
		return params;
	}

}
