package com.nemocorp.cv19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class world extends AppCompatActivity {
    BottomNavigationView bv;
    static Activity world_activity;
    static RelativeLayout loading_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);
        Toolbar yourToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(yourToolbar);
        variables.world_list=findViewById(R.id.list);
        loading_layout=findViewById(R.id.loading);
        loading(); //shows loading symbol if list not yet made else shows list
        world_activity =this;
        create_list();
        if(!variables.world_list_made)
            new Check_list_Done().execute();
        bv=findViewById(R.id.bottom_navigation);
        bv.setSelectedItemId(R.id.world);
        bv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.india:
                        Intent i=new Intent(world.this,MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                        new Handler().postDelayed(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          finish();//so no copy of activity is present in backstack
                                                      }
                                                  },500);//after .5 sec so as to avoid unnecessary animation
                        break;
                    case R.id.world:
                        break;
                }
                return false;
            }
        });

    }
    static public void loading() //shows loading symbol if list not yet made else shows list
    {
        if(variables.world.size()>0) {
            loading_layout.setVisibility(View.GONE);
            variables.world_list.setVisibility(View.VISIBLE);
        }
        else {
            variables.world_list.setVisibility(View.GONE);
            loading_layout.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume()
    {
        bv.setSelectedItemId(R.id.world);
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem search=menu.findItem(R.id.action_search);
        SearchView sv= (SearchView) MenuItemCompat.getActionView(search);
        EditText txtSearch=(sv.findViewById(R.id.search_src_text));
        ImageView searchclose=sv.findViewById(R.id.search_close_btn);
        searchclose.setImageResource(R.drawable.close);
        txtSearch.setHint("Search by Country");
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
                List<String> temp6=new ArrayList<>();
                List<String>temp7=new ArrayList<>();
                List<String>temp8=new ArrayList<>();
                variables.temp4.clear();
                for(int i=0;i<variables.world.size();i++) {
                    String temp1=variables.world.get(i);
                    if (temp1.toLowerCase().contains(s.toLowerCase())) {
                        temp.add(variables.world.get(i));
                        temp2.add(variables.world_active.get(i));
                        temp3.add(variables.world_confirmed.get(i));
                        temp4.add(variables.world_recovered.get(i));
                        temp5.add(variables.world_death.get(i));
                        temp6.add(variables.world_confirmed_inc.get(i));
                        temp7.add(variables.world_death_inc.get(i));
                        temp8.add(variables.world_recovered_inc.get(i));
                        variables.temp4.add(i);
                    }
                }
                if(variables.temp4.size()<0 || variables.temp4.size()==variables.states.size())
                { create_list();}
                else
                {myAdapter madapter=new myAdapter(world.this,temp, temp5,temp4,temp3,temp2,temp6,temp7,temp8);
                    madapter.notifyDataSetChanged();
                    variables.world_list.setAdapter(madapter);}
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    static public void create_list()
    {
        myAdapter adapter=new myAdapter(world_activity, variables.world,variables.world_death,variables.world_recovered,
                variables.world_confirmed,variables.world_active,variables.world_confirmed_inc,variables.world_death_inc,variables.world_recovered_inc);
        adapter.notifyDataSetChanged();
        variables.world_list.setAdapter(adapter);
    }
}
class Check_list_Done extends AsyncTask<String,Void,String >//for creating list if the activity is open and as soon as world list data is collected
{

    @Override
    protected String doInBackground(String... strings) {
        while (!variables.world_list_made);
        return null;
    }
    @Override
    protected void onPostExecute(String s)
    {
        world.loading();
        world.create_list();
    }
}
