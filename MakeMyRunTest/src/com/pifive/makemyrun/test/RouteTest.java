/*     Copyright (c) 2012 Johannes Wikner, Anton Lindgren, Victor Lindhe,
 *         Niklas Andreasson, John Hult
 *
 *     Licensed to the Apache Software Foundation (ASF) under one
 *     or more contributor license agreements.  See the NOTICE file
 *     distributed with this work for additional information
 *     regarding copyright ownership.  The ASF licenses this file
 *     to you under the Apache License, Version 2.0 (the
 *     "License"); you may not use this file except in compliance
 *     with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing,
 *     software distributed under the License is distributed on an
 *     "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *     KIND, either express or implied.  See the License for the
 *     specific language governing permissions and limitations
 *     under the License.
 */

package com.pifive.makemyrun.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

import com.pifive.makemyrun.Route;

public class RouteTest extends android.test.InstrumentationTestCase{
	Route testObj;
	private JSONObject testCase1;
	private Route testRoute;
	@Override

	protected void setUp() throws Exception{
		super.setUp();
		if(testCase1==null){
			InputStream in = getInstrumentation().getContext().
					getResources().openRawResource(R.raw.routetestcase1);
			BufferedReader bufIn = new BufferedReader(new InputStreamReader(in));
			StringBuilder strBuild = new StringBuilder();
			
			String data = "";
			try {
				while ((data = bufIn.readLine()) != null) {
					strBuild.append(data);
				}
			} catch (IOException e) {
				// io errors.. shouldnt happen..
				e.printStackTrace();
			} finally {
				try {
					bufIn.close();
					in.close();
				} catch (IOException e) {}
			}
			try {
				testCase1 = new JSONObject(strBuild.toString());
			} catch (JSONException e) {
				Log.d("MMR",e.getMessage());
			}
			try {
				testRoute = new Route(testCase1);
			} catch (JSONException e) {
				fail();
			}
		}
	}
	
	public void testRoute(){
		boolean failed = false;
		try{
			new Route(new JSONObject());
		} catch (JSONException e){
			failed = true;
		}	
		assertTrue(failed);
	}
	
	public void testDistance(){
		assertTrue(testRoute != null && testRoute.getDistance() == 351);	
	}
	
	public void testWaypoints(){
		List<com.pifive.makemyrun.geo.Location> wps = testRoute.getWaypoints();
		assertTrue(wps.size() == 11);
	}
}
	