package com.nemocorp.cv19.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nemocorp.cv19.R;
import com.nemocorp.cv19.adapter.ListAdapter;
import com.nemocorp.cv19.model.data;
import com.nemocorp.cv19.repository.WorldData;
import com.nemocorp.cv19.viewmodel.IndiaViewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    static boolean seen=false;
    private boolean run = false;
    private boolean main_list_clicked=false;
    ImageView toggle;
    BottomNavigationView bv;
    ListView main_list;
    private List<data> StateData = new ArrayList<>();
    private final List<data> SearchData = new ArrayList<>();
    private final List<Integer> searchItemIndex = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startActivity(new Intent(MainActivity.this, Splash.class));//start splash *screen

        SharedPreferences sh = getSharedPreferences("MODE", MODE_PRIVATE);//for getting the last mode set
        int k = Integer.parseInt(Objects.requireNonNull(sh.getString("mode", "1")));
        AppCompatDelegate.setDefaultNightMode(k);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggle = findViewById(R.id.mode);
        if (k == 1)//set dark mode button image based on current mode
            toggle.setBackground(getResources().getDrawable(R.drawable.night));
        else
            toggle.setBackground(getResources().getDrawable(R.drawable.day));
        Toolbar yourToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(yourToolbar);
        isInternetAvailable();
        main_list = findViewById(R.id.list);
        bv = findViewById(R.id.bottom_navigation);
        onclick();
        bv.setSelectedItemId(R.id.india);
        bv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.india:
                        break;
                    case R.id.world:
                        Intent i = new Intent(MainActivity.this, world.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        bv.setSelectedItemId(R.id.india);
        main_list_clicked = false;
        super.onResume();
    }

    public void mode(View view) {//for dark mode
        int k = 1;
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
                k = AppCompatDelegate.MODE_NIGHT_NO;
                AppCompatDelegate.setDefaultNightMode(k);
                toggle.setBackground(getResources().getDrawable(R.drawable.night));
                recreate();
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                k = AppCompatDelegate.MODE_NIGHT_YES;
                AppCompatDelegate.setDefaultNightMode(k);
                toggle.setBackground(getResources().getDrawable(R.drawable.day));
                recreate();
                break;
        }
        run = false;//need to do this else on recreate() when it goes to the isinternetAvailble then doesn't create list.
        SharedPreferences sh = getSharedPreferences("MODE", MODE_PRIVATE);
        SharedPreferences.Editor edit = sh.edit();
        edit.putString("mode", String.valueOf(k));
        edit.apply();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void stuff_to_do() //running async task for state data and world data
    {
        if (!run) {
            IndiaViewmodel mindiaViewmodel = new ViewModelProvider(this).get(IndiaViewmodel.class);
            mindiaViewmodel.init();
            mindiaViewmodel.getIndiaData().observe(this, new Observer<List<data>>() {
                @Override
                public void onChanged(List<data> data) {
                    ListAdapter ap = new ListAdapter(MainActivity.this, data);
                    main_list.setAdapter(ap);
                    StateData = data;
                }
            });
            run = true;
            WorldData worldData = WorldData.getInstance();
            worldData.startAsyncTask();
        }
    }

    public void internet_check_dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("No Internet");
        alertDialog.setMessage("Turn on Internet");
        alertDialog.setCancelable(false);
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
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(search);
        EditText txtSearch = (sv.findViewById(R.id.search_src_text));
        ImageView searchclose = sv.findViewById(R.id.search_close_btn);
        searchclose.setImageResource(R.drawable.close);
        txtSearch.setHint("Search by State");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchItemIndex.clear();
                SearchData.clear();
                for (int i = 0; i < StateData.size(); i++) {
                    String temp1 = StateData.get(i).getName();
                    if (temp1.toLowerCase().contains(s.toLowerCase())) {
                        SearchData.add(new data(StateData.get(i).getName(),StateData.get(i).getConfirmed(),StateData.get(i).getDeath(),StateData.get(i).getActive()
                        ,StateData.get(i).getRecovered(),StateData.get(i).getConfirmed_inc(),StateData.get(i).getDeath_inc(),StateData.get(i).getRecovered_inc()));
                        searchItemIndex.add(i);
                    }
                }
                if (searchItemIndex.size() < 0 || searchItemIndex.size() == StateData.size()) {
                    ListAdapter ap = new ListAdapter(MainActivity.this, StateData);
                    main_list.setAdapter(ap);
                } else {
                    ListAdapter madapter = new ListAdapter(MainActivity.this, SearchData);
                    madapter.notifyDataSetChanged();
                    main_list.setAdapter(madapter);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void isInternetAvailable() {
        try {
            final String command = "ping -c 1 google.com";
            boolean k = Runtime.getRuntime().exec(command).waitFor() == 0;
            if (!k) {
                internet_check_dialog();
            } else if (!run) {
                stuff_to_do();
            }
        } catch (Exception e) {
            internet_check_dialog();
        }
    }

    public void onclick() //list element click
    {
        main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!main_list_clicked) { //list only clicked once so only one activity starts otherwise duplicates are created on quick clicks
                    main_list_clicked = true;
                    try {
                        if (searchItemIndex.size() > 0) {
                            position = searchItemIndex.get(position);
                            start_district(StateData.get(position).getName());
                            main_list.setClickable(false);
                        } else if (position != 0)//this is the position of total
                        {
                            start_district(StateData.get(position).getName());
                            main_list.setClickable(false);
                        } else
                            main_list_clicked = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void start_district(String StateName) {
        Intent i = new Intent(getApplicationContext(), district.class);
        i.putExtra("StateName",StateName);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
