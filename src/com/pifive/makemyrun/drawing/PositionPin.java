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

package com.pifive.makemyrun.drawing;

import android.graphics.Bitmap;

import com.google.android.maps.GeoPoint;

/**
 * 
 * @author niklas
 *
 */
public class PositionPin {	
	private GeoPoint location;
	private Bitmap image;
	
	/**
	 * 
	 * @param startLocation
	 * @param image
	 */
	public PositionPin(GeoPoint startLocation, Bitmap image) {
		this.location = startLocation;
		this.image = image;
	}
	
	/**
	 * 
	 * @return
	 */
	public GeoPoint getGeoPoint() {
		return location;
	}
	
	/**
	 * 
	 * @param location
	 */
	public void setGeoPoint(GeoPoint location) {
		this.location = location;
	}
	
	/**
	 * 
	 * @return
	 */
	public Bitmap getImage() {
		return image;
	}
}
