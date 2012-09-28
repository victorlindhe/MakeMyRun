package com.pifive.makemyrun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Automatically requests directions by a query, default through Google
 * Directions. Response is converted to JSON and returned by user calling .get()
 * 
 * @author atamon
 * 
 */
public class DirectionsTask extends AsyncTask<String, Integer, JSONObject> {

	public final static String GOOGLE_URL = "http://maps.googleapis.com/maps/api/directions/json?";
	public final static String TEST_QUERY = "origin=Stockholm&destination=Gothenburg&sensor=false";
	public final static String GOOGLE_QUERY_ERROR = "REQUEST_DENIED";
	public final static String GOOGLE_QUERY_SUCCESS = "OK";

	private Context context;
	private int cancelCause;
	private final String restAPI;
	
	/**
	 * A constructor which enables us to create task with custom host API.
	 * WARNING: This class is mainly implemented to match Google Directions API,
	 * using it with other restful APIs will probably fail.
	 * @param restAPI The API to contact for each request.
	 */
	public DirectionsTask(Context context, String restAPI) {
		this.context = context;
		this.restAPI = restAPI;
	}
	
	/**
	 * Adds an abstractionlayer to the super.get() method
	 * and returns its result or an empty JSONObject if we're interrupted.
	 * @return
	 */
	public JSONObject simpleGet(String query) {
		execute(query);
		JSONObject obj = new JSONObject();
        try {
        	obj = get();
        	
    	// We have already handled printing of user-errors. Flood the log!
        } catch (Exception e) {
        	Log.w("MMR", e.getStackTrace().toString());
        }
        return obj;
	}

	@Override
	/**
	 * Performs a google REST request with provided query
	 * @param query Optional query to provide to the google request
	 * @return Returns a google reponse parsed as JSON
	 * or an empty JSONObjectif we fail to parse JSON from REST response.
	 */
	protected JSONObject doInBackground(String... query) {
		String myQuery = TEST_QUERY;
		if (query.length == 1) {
			myQuery = query[0];
		}

		JSONObject json = new JSONObject();
		try {
			URL url = new URL(restAPI + myQuery);

			String googleString = requestData(url);

			json = parseJSONString(googleString);
		} catch (MalformedURLException e) {
			cancelCause = R.string.url_format_failed;
			cancel(true);
		} catch (DirectionsException e) {
			if (cancelCause == 0) {
				cancelCause = R.string.google_rest_failed;				
			}
			cancel(true);
		} 
		
		return json;
	}

	/**
	 * Performs a http request to a REST API for a JSON string.
	 * 
	 * @param url
	 *            The url to perform the request to.
	 * @return Returns a string for parsing to JSON.
	 */
	private String requestData(URL url) {
		HttpURLConnection connection = null;
		BufferedReader in = null;
		StringBuilder stringBuilder = new StringBuilder();
		try {
			// Send request to REST API
			connection = (HttpURLConnection) url.openConnection();
			Log.i("MMR",
					"Google response HTTP status: "
							+ connection.getResponseMessage());
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			// Parse response to string
			String data;
			Log.d("MMR", "Parsing Google response stream");
			while ((data = in.readLine()) != null) {
				stringBuilder.append(data);
			}
			// Catch IOException and close connection and stream.
		} catch (IOException e) {
			cancel(true);
			cancelCause = R.string.google_rest_failed;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * Parses a JSON string to a JSONObject and converts JSON Exceptions to
	 * our abstraction of an Exception.
	 * @param string The string which should contain valid JSON formatted text.
	 * @return Returns a JSONObject parsed from the string sent in.
	 */
	public JSONObject parseJSONString(String string) throws DirectionsException {
		try {
			JSONObject json = new JSONObject(string);

			Log.i("MMR", "Google response stream parsed successfully to JSON");

			String status = json.getString("status");
			Log.d("MMR", "Google response status: " + status);

			// If google can't handle it we never want to send it forward
			if (status.equals(GOOGLE_QUERY_ERROR)) {
				throw new DirectionsException(
						"Google returned error REQUEST_INVALID");
			}
			
			return json;
		} catch (JSONException e) {
			cancelCause = R.string.json_parse_failed;
			cancel(true);
			throw new DirectionsException("Response from Google was invalid JSON");
		}
	}
	
	/**
	 * Displays a toast with an error message to the end-user.
	 */
	@Override
	protected void onCancelled() {
		
		if (cancelCause != 0) {
    		Toast.makeText(context, cancelCause, Toast.LENGTH_LONG).show();
    	}
	}

	/**
	 * Returns the resource ID for the cancel message
	 * 
	 * @return Returns an integer ID for the string resource describing cause of
	 *         cancellation.
	 */
	public int getCancelCause() {
		return cancelCause;
	}
}
