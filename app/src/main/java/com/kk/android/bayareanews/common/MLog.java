package com.kk.android.bayareanews.common;

import android.util.Log;
import android.util.TimingLogger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public final class MLog {

	private static boolean isEnabled = true;
	private final static Map<String, TimingLogger> timingLoggers = new HashMap<String, TimingLogger>();


	private MLog() {
	}

   public static boolean isEnabled() {
      return isEnabled;
   }

	public static void setEnabled(final boolean enable) {
		isEnabled = enable;
		if (!enable) {
			disableSystemOut();
		}
	}



	public static void v(final String tag, final String msg) {
		if (isEnabled) {
			Log.v(tag,msg);
		}
	}


	public static void d(final String tag, final String msg) {
		if (isEnabled) {
			Log.d(tag,msg);
		}
	}


	public static void i(final String tag, final String msg) {
		if (isEnabled) {
			Log.i(tag, msg);
		}
	}


	public static void w(final String tag, final String msg) {
		if (isEnabled) {
			Log.w(tag,msg);
		}
	}


	public static void e(final String tag, final String msg) {
		if (isEnabled) {
			Log.e(tag, msg);
		}
	}


	public static void e(final String tag, final String msg, final Throwable t) {
		if (isEnabled) {
			Log.e(tag, msg, t);
		}
	}

	private static void disableSystemOut() {

		/*
		 * disable System.out.print.....
		 */
		System.setOut(new PrintStream(new OutputStream() {

			public void write(int b) {

				// do nothing
			}
		}));

		System.setErr(new PrintStream(new OutputStream() {

			public void write(int b) {

				// do nothing
			}
		}));
	}

}
