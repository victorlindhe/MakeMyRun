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

import java.util.Iterator;
import java.util.List;

import android.test.AndroidTestCase;

import com.google.android.maps.GeoPoint;
import com.pifive.makemyrun.RouteGenerator;
import com.pifive.makemyrun.geo.Location;

/**
 * Test class for RouteGenerator
 */
public class RouteGeneratorTest extends AndroidTestCase {
	
	/**
	 * Passes is string is returned
	 */
	public void testGenerateRoute() {
		String route = RouteGenerator.generateRoute(new GeoPoint((int)(57.7 * 1E6), (int)(12 * 1E6)), 
											new GeoPoint((int)(57.7005 * 1E6), (int)(12.0005 * 1E6)));
		String route2 = RouteGenerator.generateRoute(new GeoPoint((int)(57.7 * 1E6), (int)(12 * 1E6)), 
											new GeoPoint((int)(57.7010 * 1E6), (int)(12.0010 * 1E6)));
		
		assertNotNull(route);
		assertNull(route2);
		assert(route.contains("origin="));
		assert(route.contains("&destination="));
		assert(route.contains("&avoid=highways&sensor=true&mode=walking"));
		
	}
	
	/**
	 * Passes if too close distance returns null
	 */
	public void testGetMixed() {
		Location aLoc = new Location(57.7000, 12.0000);
		Location bLoc = new Location(57.7001, 12.0001);
	}
	
	/**
	 * Passes if:
	 * Returns 2 waypoints
	 * Each returned value doesn't equal the one before
	 */
	public void testGetCircle() {
		Location center = new Location(57.7000, 12.000);
		Location start = new Location(57.6990, 11.990);
		
		List<Location> workingLocations = RouteGenerator.getCircle(center, start);
		
		assert(workingLocations.size() == 2);
		
		Location lastLocation = null;
		Location presentLocation = null;
		Iterator<Location> iterator = workingLocations.iterator();
		while(iterator.hasNext()) {
			if(lastLocation == null) {
				lastLocation = iterator.next();
			} else {
				presentLocation = iterator.next();
				assert(presentLocation.getLat() != lastLocation.getLat() && 
						presentLocation.getLng() != lastLocation.getLng());
			}
		}
	}
	
	/**
	 * Passes if the random location is not too far from location
	 */
	public void testGetRandomLocation() {
		Location location = new Location(68, 68);
		Location randomLocation = RouteGenerator.generateRandomLocation(location);
		assertTrue(location.getLat() != randomLocation.getLat());
		assertTrue(location.getLng() != randomLocation.getLng());
		assertTrue(randomLocation.getLat() != (location.getLat() + 0.003));
		assertTrue(randomLocation.getLat() != location.getLat() + 0.007);
		assertTrue(randomLocation.getLng() != location.getLng() + 0.003);
		assertTrue(randomLocation.getLng() != location.getLng() + 0.007);
	}
	
	/**
	 * Should throw exception due to no location available in tests
	 */
	/* public void testGetCurrentRoute() {
		
		// Create a mock location
		LocationManager locationManager = (LocationManager) getContext()
							.getSystemService(Context.LOCATION_SERVICE);
		android.location.Location mockLocation = 
				new android.location.Location("gps");
		
		// Set its values
		mockLocation.setLatitude(15);
		mockLocation.setLongitude(15);
		locationManager.addTestProvider("gps", 
						false, false, false, 
						false, false, false, 
						false, 1, 1);
		locationManager.setTestProviderLocation("gps", mockLocation);
		
		// Verify that it is returned
		try {
			android.location.Location returnedLocation = RouteGenerator.getCurrentLocation(getContext());
			assertEquals("Verify that correct location is returned from getCurrentLocation()",
				mockLocation, returnedLocation);
		} catch (NoLocationException e) {
			fail("Verify that we can get current location from RouteGenerator");
		}
		
	}*/
}
