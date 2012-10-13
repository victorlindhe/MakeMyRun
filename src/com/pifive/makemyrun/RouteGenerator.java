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

/**
 * RouteGenerator.java
 */
package com.pifive.makemyrun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 *	RouteGenerator
 *	Class with static methods for route generation.
 */
public abstract class RouteGenerator {

	/**
	 * Private constructor to prevent from being instantiated
	 */
	private RouteGenerator() {
		
	}
	
	/**
	 * Returns a string containing a google query with generated waypoints 
	 * @param startEndLoc - Current location that the generated route will start and end at
	 * @return a google query with which you can query google for more steps
	 */
	private static String generateRoute(final com.pifive.makemyrun.geo.Location startLoc, final com.pifive.makemyrun.geo.Location endLoc) {		
		// build the beginning of the google query
		StringBuilder stringBuilder = new StringBuilder("origin=");
		stringBuilder.append(startLoc.getLat());
		stringBuilder.append(",");
		stringBuilder.append(startLoc.getLng());
		stringBuilder.append("&destination=");
		stringBuilder.append(endLoc.getLat());
		stringBuilder.append(",");
		stringBuilder.append(endLoc.getLng());
		stringBuilder.append("&waypoints=optimize:true|");

		// get the centerpoint of the 'circle'
		com.pifive.makemyrun.geo.Location centerLocation = generateRandomLocation(startLoc);
		List<com.pifive.makemyrun.geo.Location> waypoints = getCircle(centerLocation, startLoc);
		for (com.pifive.makemyrun.geo.Location waypoint : waypoints) {
			stringBuilder.append(waypoint.getLat());
			stringBuilder.append(",");
			stringBuilder.append(waypoint.getLng());
			stringBuilder.append("|");
		}
		// remove the last |
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append("&avoid=highways&sensor=true&mode=walking");
		System.out.println(stringBuilder.toString());
		return stringBuilder.toString();
	}
	
	/**
	 * Generates a route.
	 * If distance between points < 100 meters, a circle route will be returned.
	 * @param startLoc
	 * @param endLoc
	 * @return
	 */
	public static String generateRoute(final GeoPoint startLoc, final GeoPoint endLoc) {
		Location sLoc = new Location("start location");
		sLoc.setLatitude(startLoc.getLatitudeE6() / 1E6);
		sLoc.setLongitude(startLoc.getLongitudeE6() / 1E6);
		
		Location eLoc = new Location("end location");
		eLoc.setLatitude(endLoc.getLatitudeE6() / 1E6);
		eLoc.setLongitude(endLoc.getLongitudeE6() / 1E6);
		
		System.out.println("distance " + sLoc.distanceTo(eLoc));
		
		if(sLoc.distanceTo(eLoc) < 100.0) {
			return generateRoute(new com.pifive.makemyrun.geo.Location
					(sLoc.getLatitude(), sLoc.getLongitude()),
					new com.pifive.makemyrun.geo.Location
					(sLoc.getLatitude(), sLoc.getLongitude()));
			
		} else {
			return null;
		}
	}
	
	
	/**
	 * Generates a location with coordinates 0.005 - 0.010 latitude and 
	 * longitude from the passed location
	 * @param location
	 * @return
	 */
	public static com.pifive.makemyrun.geo.Location generateRandomLocation(com.pifive.makemyrun.geo.Location location) {
		// create another location approx 0.005 - 0.010 from the current
		Random random = new Random();
		double randomNumber = 0.005 + random.nextDouble() * 0.010;
		double latSign = random.nextBoolean() ? 1.0 : -1.0;
		double longSign = random.nextBoolean() ? 1.0 : -1.0;
		
		double centerLatitude = location.getLat() + latSign*randomNumber;
		double centerLongitude = location.getLng() + longSign*randomNumber;
		
		return new com.pifive.makemyrun.geo.Location(centerLatitude, centerLongitude);
	}
	
	/**
	 * Returns a circle-shaped run
	 * @param center
	 * @param start
	 * @return
	 */
	public static List<com.pifive.makemyrun.geo.Location> getCircle(com.pifive.makemyrun.geo.Location center, 
			com.pifive.makemyrun.geo.Location start) {
		
		List<com.pifive.makemyrun.geo.Location> locations = new ArrayList<com.pifive.makemyrun.geo.Location>();
		double angle = (Math.PI * 2) / 3;
		double longDiff = start.getLng() - center.getLng();
		double latDiff = start.getLat() - center.getLat();
		double radius = Math.sqrt(Math.pow(longDiff, 2) + Math.pow(latDiff, 2));
		
		locations.add(new com.pifive.makemyrun.geo.Location(center.getLat() + Math.cos(angle)*radius, 
									   center.getLng() + Math.sin(angle)*radius));
		locations.add(new com.pifive.makemyrun.geo.Location(center.getLat() + Math.cos(angle*2)*radius, 
				   center.getLng() + Math.sin(angle*2)*radius));
		
		return locations;
	}
}
