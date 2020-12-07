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

public class DistrictData {
    private static DistrictData instanceDis;
    private final ArrayList<data> DistrictData =new ArrayList<>();
    private final MutableLiveData<List<data>> data= new MutableLiveData<>();
    private final MutableLiveData<Boolean> progress = new MutableLiveData<>();
    private boolean inProgress = true;

    public static DistrictData getInstance(){
            instanceDis = new DistrictData();
        return instanceDis;
    }

    public MutableLiveData<List<data>> getDistrictData(String StateName)
    {
        new getDistrictData(this).execute(StateName);
        return data;
    }
    public MutableLiveData<Boolean> getInProgress()
    {
        progress.setValue(inProgress);
        return progress;
    }
    private static class getDistrictData extends AsyncTask<String,Void,String>{
        private final WeakReference<DistrictData> activityweakReference;
        private String StateName;
        public getDistrictData(DistrictData districtData) {
            activityweakReference = new WeakReference<>(districtData);
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StateName = strings[0];

            try {
                URL url = new URL("https://api.covid19india.org/v2/state_district_wise.json");
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
            try {
                JSONArray j2 = new JSONArray(s);
                JSONObject obj;
                StringBuilder j3= new StringBuilder();
                for (int i=0;i<j2.length();i++)
                {
                    obj=new JSONObject(j2.optString(i));
                    if(obj.getString("state").equals(StateName))
                        j3.append(obj.getString("districtData"));
                }
                j2=new JSONArray(j3.toString());
                for(int i=0;i<j2.length();i++)
                {
                    obj=new JSONObject(j2.optString(i));
                    JSONObject delta=new JSONObject(obj.optString("delta"));
                    activityweakReference.get().DistrictData.add(new data(obj.optString("district"),obj.optString("confirmed"),
                            obj.optString("deceased"),obj.optString("active"),obj.optString("recovered"),
                            delta.optString("confirmed"),delta.optString("deceased"),delta.optString("recovered")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            activityweakReference.get().data.postValue(activityweakReference.get().DistrictData);
            activityweakReference.get().inProgress=false;
            activityweakReference.get().progress.postValue(activityweakReference.get().inProgress);
            super.onPostExecute(s);
        }
    }
}
