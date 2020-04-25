package com.nemocorp.cv19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    static Context ctx;
    static boolean run=false;
    ImageView toggle;
    BottomNavigationView bv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sh=getSharedPreferences("MODE",MODE_PRIVATE);//for getting the last mode set
        int k=Integer.parseInt(sh.getString("mode","1"));
        AppCompatDelegate.setDefaultNightMode(k);
        startActivity(new Intent(MainActivity.this,Splash.class));//start splash screen
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggle=findViewById(R.id.mode);
        if(k==1)//set dark mode button image based on current mode
            toggle.setBackground(getResources().getDrawable(R.drawable.night));
        else
            toggle.setBackground(getResources().getDrawable(R.drawable.day));
        Toolbar yourToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(yourToolbar);
        isInternetAvailable();
        variables.activity=this;
        ctx=getApplicationContext();
        variables.main_list=findViewById(R.id.list);
        bv=findViewById(R.id.bottom_navigation);
        stuff_to_do();
        onclick();
        bv.setSelectedItemId(R.id.india);
        bv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.india:
                        break;
                    case R.id.world:
                        Intent i=new Intent(MainActivity.this,world.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
    }
    @Override
    public void onResume()
    {
        bv.setSelectedItemId(R.id.india);
        if(variables.backpressed) {//if dont place this then after closing app through back press and then open wont show list
            create_list();
            variables.backpressed=false;
        }
        variables.main_list_clicked=false;
        super.onResume();
    }
    public void mode(View view){//for dark mode
        int k=1;
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
        {
            case Configuration.UI_MODE_NIGHT_YES:
                k=AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(k);
                toggle.setBackground(getResources().getDrawable(R.drawable.night));
                recreate();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                k=AppCompatDelegate.MODE_NIGHT_YES;
                AppCompatDelegate.setDefaultNightMode(k);
                toggle.setBackground(getResources().getDrawable(R.drawable.day));
                recreate();
                break;
        }
        SharedPreferences sh=getSharedPreferences("MODE",MODE_PRIVATE);
        SharedPreferences.Editor edit=sh.edit();
        edit.putString("mode", String.valueOf(k));
        edit.commit();
    }
    @Override
    public void onBackPressed()
    {
        variables.backpressed=true;//when app opened after back press from main need to recreate list
        super.onBackPressed();
    }
    @Override
    public void onStop()
    {
        variables.backpressed=true;//when app opened after onStop need to recreate list
        super.onStop();
    }
    public void stuff_to_do() //running async task for state data and world data
    {
        if(!run) {
            run=true;
            new Get_Data_World().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new Get_Data_State().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
    public void internet_check_dialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("No Internet");
        alertDialog.setMessage("Turn on Internet");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isInternetAvailable();
                    }
                });

        alertDialog.show();

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
        layoutParams.weight = 10;
        btnPositive.setLayoutParams(layoutParams);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //creating search
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem search=menu.findItem(R.id.action_search);
        SearchView sv= (SearchView) MenuItemCompat.getActionView(search);
        EditText txtSearch=(sv.findViewById(R.id.search_src_text));
        ImageView searchclose=sv.findViewById(R.id.search_close_btn);
        searchclose.setImageResource(R.drawable.close);
        txtSearch.setHint("Search by State");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                List<String> temp=new ArrayList<>();
                List<String>temp2=new ArrayList<>();
                List<String> temp3=new ArrayList<>();
                List<String>temp4=new ArrayList<>();
                List<String>temp5=new ArrayList<>();
                variables.temp4.clear();
                for(int i=0;i<variables.states.size();i++) {
                    String temp1=variables.states.get(i);
                    if (temp1.toLowerCase().contains(s.toLowerCase())) {
                        temp.add(variables.states.get(i));
                        temp2.add(variables.active.get(i));
                        temp3.add(variables.confirmed.get(i));
                        temp4.add(variables.recovered.get(i));
                        temp5.add(variables.death.get(i));
                        variables.temp4.add(i);
                    }
                }
                if(variables.temp4.size()<0 || variables.temp4.size()==variables.states.size())
                { create_list();}
                else
                {myAdapter madapter=new myAdapter(MainActivity.this,temp, temp5,temp4,temp3,temp2);
                    madapter.notifyDataSetChanged();
                    variables.main_list.setAdapter(madapter);}
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void isInternetAvailable() {
        try {
                final String command = "ping -c 1 google.com";
                boolean k= Runtime.getRuntime().exec(command).waitFor() == 0;
                if(!k)
                    internet_check_dialog();
                else if(!run)
                    stuff_to_do();
        } catch (Exception e) {
            internet_check_dialog();
        }
    }
    public void onclick() //list element click
    {
        variables.main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!variables.main_list_clicked) { //list only clicked once so only one activity starts otherwise duplicates are created on quick clicks
                    variables.main_list_clicked=true;
                    try {
                        if (variables.temp4.size() > 0) {
                            position = variables.temp4.get(position);
                            new Get_Data_District().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variables.states.get(position));
                            variables.main_list.setClickable(false);
                        } else if (position != 0)//this is the position of total
                        {
                            new Get_Data_District().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, variables.states.get(position));
                            variables.main_list.setClickable(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    static public void create_list()
    {
        myAdapter adapter=new myAdapter(variables.activity, variables.states,variables.death,variables.recovered,
                variables.confirmed,variables.active);
        adapter.notifyDataSetChanged();
        variables.main_list.setAdapter(adapter);
    }
    static public void clear_district()
    {
        variables.dis.clear();
        variables.dis_recovered.clear();
        variables.dis_death.clear();
        variables.dis_confirmed.clear();
        variables.dis_active.clear();
    }
    static public void clear_world()
    {
        variables.world.clear();
        variables.world_recovered.clear();
        variables.world_death.clear();
        variables.world_confirmed.clear();
        variables.world_active.clear();
    }
    static public void clear_state()
    {
        variables.states.clear();
        variables.recovered.clear();
        variables.death.clear();
        variables.confirmed.clear();
        variables.active.clear();
    }
    static public void start_district(){
        Intent i=new Intent(ctx,district.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }
}
class Get_Data_State extends AsyncTask<String,Void,String>{

    @Override
    protected String doInBackground(String... strings) {
        variables.connection = null;
        variables.reader = null;
        try {
            URL url = new URL("https://api.covid19india.org/data.json");
            variables.connection = (HttpURLConnection) url.openConnection();
            variables.connection.connect();
            InputStream stream = variables.connection.getInputStream();
            variables.reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = variables.reader.readLine()) != null) {
                buffer.append(line+"\n");
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (variables.connection != null) {
                variables.connection.disconnect();
            }
            try {
                if (variables.reader != null) {
                    variables.reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        MainActivity.clear_state();
        try {
            JSONObject js=new JSONObject(s);
            String state=js.getString("statewise");
            JSONArray j2=new JSONArray(state);
            for(int i=0;i<j2.length();i++) {
                JSONObject js1=j2.getJSONObject(i);
                String temp=js1.optString("state");
                variables.states.add(temp);
                temp=js1.optString("confirmed");
                variables.confirmed.add(temp);
                temp=js1.optString("deaths");
                variables.death.add(temp);
                temp=js1.optString("active");
                variables.active.add(temp);
                temp=js1.optString("recovered");
                variables.recovered.add(temp);
                temp=js1.optString("statecode");
                variables.state_code.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainActivity.create_list();
        super.onPostExecute(s);
    }
}

class Get_Data_District extends AsyncTask<String,Void,String>
{
    String state;
    @Override
    protected String doInBackground(String... strings) {
        state=strings[0];
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("https://api.covid19india.org/v2/state_district_wise.json");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        MainActivity.clear_district();
        try {
            JSONArray j2 = new JSONArray(s);
            JSONObject obj = null;
            String j3="";
            for (int i=0;i<j2.length();i++)
            {
                obj=new JSONObject(j2.optString(i));
                if(obj.getString("state").equals(state))
                    j3+=(obj.getString("districtData"));
            }
            j2=new JSONArray(j3);
            for(int i=0;i<j2.length();i++)
            {
                obj=new JSONObject(j2.optString(i));
                variables.dis.add(obj.optString("district"));
                variables.dis_active.add(obj.optString("active"));
                variables.dis_confirmed.add(obj.optString("confirmed"));
                variables.dis_death.add(obj.optString("deceased"));
                variables.dis_recovered.add(obj.optString("recovered"));
            }
        } catch (JSONException e) {
            Log.d("values",e+"");
            e.printStackTrace();
        }
        if(variables.dis.size()>0)
        MainActivity.start_district();
        super.onPostExecute(s);
    }
}
class Get_Data_World extends AsyncTask<String,Void,String>{

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
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
        if (s.length() > 0) {
            int total_recovered=0;
            int total_active=0;
            int total_death=0;
            int total_confirmed=0;
            MainActivity.clear_world();
            Log.d("values",s);
            try {
                JSONArray js = new JSONArray(s);
                for (int i = 0; i < js.length(); i++) {
                    JSONObject obj = new JSONObject(js.optString(i));
                    variables.world.add(obj.optString("country"));
                    variables.world_recovered.add(obj.optString("recovered"));
                    variables.world_death.add(obj.optString("deaths"));
                    variables.world_confirmed.add(obj.optString("cases"));
                    variables.world_active.add(obj.optString("active"));
                    total_recovered+=Integer.parseInt(obj.optString("recovered"));
                    total_active+=Integer.parseInt(obj.optString("active"));
                    total_confirmed+=Integer.parseInt(obj.optString("cases"));
                    total_death+=Integer.parseInt(obj.optString("deaths"));
                }
                variables.world.add(0,"Total");
                variables.world_recovered.add(0,String.valueOf(total_recovered));
                variables.world_death.add(0,String.valueOf(total_death));
                variables.world_active.add(0,String.valueOf(total_active));
                variables.world_confirmed.add(0,String.valueOf(total_confirmed));
                variables.world_list_made=true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

