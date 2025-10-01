package com.gogulf.passenger.app.utils.maputils;


import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;


import com.gogulf.passenger.app.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointsParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    TaskLoadedCallback taskLoadedCallback;
    String directionMode;

    public PointsParser(TaskLoadedCallback context, String directionMode) {
        this.taskLoadedCallback = context;
        this.directionMode = directionMode;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
//            Log.d("PointParserLog", "jObject -> " + jObject);
            DataParser parser = new DataParser();

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("PointParserLog", "routes -> " + routes.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points= new ArrayList<>();
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();
            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);
            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

//                Log.d("PointParserLog", "positions -> " + position);
                points.add(position);
            }
            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            if (directionMode.equalsIgnoreCase("walking")) {
                lineOptions.width(10);
                lineOptions.color(Color.MAGENTA);
            } else {
                lineOptions.width(10);
                lineOptions.color(Color.rgb(77, 204, 134));
//                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
//                lineOptions.addSpan(new StyleSpan(StrokeStyle.gradientBuilder(Color.RED, Color.YELLOW).build()));
            }
        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null) {
            //mMap.addPolyline(lineOptions);
            taskLoadedCallback.onTaskDone(lineOptions,points);

        } else {
            Log.d("PointsParser", "without Polylines drawn");
        }
    }
}
