package com.nemocorp.cv19.repository;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.nemocorp.cv19.model.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WorldData {
    private static WorldData instance;
    private final MutableLiveData<List<data>> data= new MutableLiveData<>();
    private final MutableLiveData<Boolean> progress = new MutableLiveData<>();
    private boolean inProgress = true;

    public static WorldData getInstance(){
        if(instance == null)
            instance = new WorldData();
        return instance;
    }
    public void startAsyncTask(){
        new getWorldData(this).execute();
    }
    public MutableLiveData<List<data>> getWorldData()
    {
        return data;
    }
    public MutableLiveData<Boolean> getInProgress()
    {
        progress.setValue(inProgress);
        return progress;
    }
    private static class getWorldData extends AsyncTask<String,Void,String>
    {
        private final WeakReference<WorldData> activityweakReference;

        public getWorldData(WorldData worldData) {
            activityweakReference = new WeakReference<WorldData>(worldData);
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://corona.lmao.ninja/v2/countries");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            int total_recovered=0;
            int total_active=0;
            int total_death=0;
            int total_confirmed=0;
            int total_recovered_inc=0;
            int total_death_inc=0;
            int total_confirmed_inc=0;
            ArrayList<data> worldData = new ArrayList<>();
            try {
                JSONArray js = new JSONArray(s);
                for (int i = 0; i < js.length(); i++) {
                    JSONObject obj = new JSONObject(js.optString(i));
                    worldData.add(new data(obj.optString("country"),obj.optString("cases"),obj.optString("deaths"),obj.optString("active")
                            ,obj.optString("recovered"),obj.optString("todayCases"),obj.optString("todayDeaths"),obj.optString("todayRecovered")));

                    total_recovered+=Integer.parseInt(obj.optString("recovered"));
                    total_active+=Integer.parseInt(obj.optString("active"));
                    total_confirmed+=Integer.parseInt(obj.optString("cases"));
                    total_death+=Integer.parseInt(obj.optString("deaths"));
                    total_recovered_inc+=Integer.parseInt(obj.optString("todayRecovered"));
                    total_confirmed_inc+=Integer.parseInt(obj.optString("todayCases"));
                    total_death_inc+=Integer.parseInt(obj.optString("todayDeaths"));
                }
                worldData.add(0,new data("Total",String.valueOf(total_confirmed),String.valueOf(total_death),String.valueOf(total_active)
                        ,String.valueOf(total_recovered),String.valueOf(total_confirmed_inc),String.valueOf(total_death_inc),String.valueOf(total_recovered_inc)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            activityweakReference.get().inProgress=false;
            activityweakReference.get().progress.postValue(activityweakReference.get().inProgress);
            activityweakReference.get().data.postValue(worldData);
            super.onPostExecute(s);
        }
    }
}
