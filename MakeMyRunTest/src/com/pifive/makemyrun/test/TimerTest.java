package com.pifive.makemyrun.test;

import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.TextView;

import com.pifive.makemyrun.Timer;

public class TimerTest extends AndroidTestCase {
	public void test() {
		Timer testTimer = new Timer(new TextView(getContext()));
		Thread teg = new Thread();
		teg.start();
		Log.d("MMR", "HALAAAAA: " + testTimer.getTime());
		assertTrue(testTimer.getTime() != 0);
	}

}
