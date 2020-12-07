package com.nemocorp.cv19.repository;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.nemocorp.cv19.model.data;

import org.json.JSONArray;
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

public class StateData {
    private static StateData instance;
    private final ArrayList<data> stateData =new ArrayList<>();
    private final MutableLiveData<List<data>> data= new MutableLiveData<>();

    public static StateData getInstance()
    {
        if(instance==null)
        {
            instance=new StateData();
        }
        return instance;
    }

    public MutableLiveData<List<data>> getState()
    {
        new getStateData(this).execute();
        return data;
    }
    private static class getStateData extends AsyncTask<String,String,String>{
        private final WeakReference<StateData> activityWeakReference;
        public getStateData(StateData stateData) {
            activityWeakReference=new WeakReference<>(stateData);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("https://api.covid19india.org/data.json");
                HttpURLConnection result= (HttpURLConnection) url.openConnection();
                result.connect();
                InputStream in =result.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(in));
                String line;
                StringBuilder buffer = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                br.close();
                in.close();
                result.disconnect();
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            String state;
            try {
                JSONObject js=new JSONObject(s);
                state = js.getString("statewise");
            JSONArray j2=new JSONArray(state);
            for(int i=0;i<j2.length();i++) {
                JSONObject js1=j2.getJSONObject(i);
                activityWeakReference.get().stateData.add(new data(js1.optString("state"),js1.optString("confirmed"),js1.optString("deaths"),
                        js1.optString("active"),js1.optString("recovered"),
                        js1.optString("deltaconfirmed"),js1.optString("deltadeaths"),js1.optString("deltarecovered")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            activityWeakReference.get().data.postValue(activityWeakReference.get().stateData);
            super.onPostExecute(s);
        }
    }
}
