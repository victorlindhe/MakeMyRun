package com.pifive.makemyrun;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class MainActivity extends MapActivity {
	
	private MapView mapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapview);
        
        Button button = (Button) findViewById(R.id.generatebutton);
        button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				View overlay = findViewById(R.id.overlayMenu);
				overlay.setVisibility(View.GONE);
				mapView.requestFocus();
				mapView.requestFocusFromTouch();
				mapView.setClickable(true);
			}
        	
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override	
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Queries Google Directions with supplied query through a task.
	 * @param query The query to execute DirectionsTask with.
	 */
	private void startDirectionsTask(String query) {
        DirectionsTask directionsTask = new DirectionsTask(this, DirectionsTask.GOOGLE_URL);
        JSONObject googleRoute = directionsTask.simpleGet(query);
        
        try {
			Route route = new Route(googleRoute);
			RouteDrawer drawer = new RouteDrawer(mapView, route.getWaypoints());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Log.d("MMR", "ALL DONE");

	}
}
