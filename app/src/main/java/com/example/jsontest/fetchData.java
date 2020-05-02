package com.example.jsontest;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends AsyncTask<Void,Void,Void> {
    String data ="";
    String dataParsed = "";
    String singleParsed ="";
    JSONObject object;
    JSONArray features;
    JSONObject attributes;
    String[] names = new String[72]; // Holds Names of each county
    String namesList;
    int[] positives = new int[72]; // Holds number of Covid-19 cases in each county

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://services1.arcgis.com/ISZ89Z51ft1G16OK/ArcGIS/rest/services/COVID19_WI/FeatureServer/10/query?where=GEO+%3D+%27County%27&objectIds=&time=&resultType=none&outFields=NAME%2C+POSITIVE&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnDistinctValues=false&cacheHint=false&orderByFields=LoadDttm+desc&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=&sqlFormat=none&f=pjson&token=");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            object = new JSONObject(data);
            features = object.getJSONArray("features");

            for(int i = 0; i < names.length; i++){
                attributes = features.getJSONObject(i); // Object 0
                attributes = attributes.getJSONObject("attributes"); // Actual attributes object
                names[i] = attributes.getString("NAME");
                positives[i] = attributes.getInt("POSITIVE");

                dataParsed = dataParsed + names[i] + ": " + positives[i] +"\n";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.data.setText(dataParsed);

    }
}