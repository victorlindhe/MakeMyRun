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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/**
 * Interface which should be implemented by all the classes that want data when
 * the MapView is tapped.
 */
public interface MapTapListener {
	/**
	 * When a MapView is tapped, this method will be called
	 * @param geoPoint - The GeoPoint corresponding to where you tapped
	 * @param mapView - The MapView that was tapped
	 */
	public void onTap(GeoPoint geoPoint, MapView mapView);
}
