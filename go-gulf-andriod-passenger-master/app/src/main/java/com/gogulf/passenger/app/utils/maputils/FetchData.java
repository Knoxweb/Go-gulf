package com.gogulf.passenger.app.utils.maputils;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchData extends AsyncTask<String,Void,String> {
    TaskLoadedCallback context;
    String directionMode = "driving";

    public FetchData(TaskLoadedCallback context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        directionMode = strings[1];
        try{

            data = downloadUrl(strings[0]);
        }catch (Exception e){
            Log.d("FetchData",e.getMessage());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        PointsParser parserTask = new PointsParser(context, directionMode);
        // Invokes the thread for parsing the JSON data
        parserTask.execute(s);
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
