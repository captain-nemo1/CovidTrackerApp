package com.nemocorp.cv19.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nemocorp.cv19.R;
import com.nemocorp.cv19.adapter.ListAdapter;
import com.nemocorp.cv19.model.data;
import com.nemocorp.cv19.viewmodel.WorldViewModel;

import java.util.ArrayList;
import java.util.List;

public class world extends AppCompatActivity {
    BottomNavigationView bv;
    ConstraintLayout loading_layout;
    private List<data> WorldData = new ArrayList<>();
    private final List<data> SearchData = new ArrayList<>();
    private final List<Integer> searchItemIndex = new ArrayList<>();
    private ListView world_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world);
        Toolbar yourToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(yourToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        world_list=findViewById(R.id.list);
        loading_layout=findViewById(R.id.loading_layout);

        WorldViewModel mWorldViewModel = new ViewModelProvider(this).get(WorldViewModel.class);
        mWorldViewModel.init();
        mWorldViewModel.getWorldData().observe(this, new Observer<List<data>>() {
            @Override
            public void onChanged(List<data> data) {
                create_list(data);
                WorldData=data;
            }
        });
        mWorldViewModel.checkInProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    loading_layout.setVisibility(View.VISIBLE);
                else
                    loading_layout.setVisibility(View.GONE);
            }
        });
        bv=findViewById(R.id.bottom_navigation);
        bv.setSelectedItemId(R.id.world);
        bv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.india:
                        Intent i=new Intent(world.this, MainActivity.class);
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
                SearchData.clear();
                searchItemIndex.clear();
                for(int i=0;i<WorldData.size();i++) {
                    String temp1=WorldData.get(i).getName();
                    if (temp1.toLowerCase().contains(s.toLowerCase())) {
                        SearchData.add(new data(WorldData.get(i).getName(),WorldData.get(i).getConfirmed(),WorldData.get(i).getDeath(),WorldData.get(i).getActive()
                                ,WorldData.get(i).getRecovered(),WorldData.get(i).getConfirmed_inc(),WorldData.get(i).getDeath_inc(),WorldData.get(i).getRecovered_inc()));
                        searchItemIndex.add(i);
                    }
                }
                if(searchItemIndex.size()<0 || searchItemIndex.size()==WorldData.size())
                {
                    create_list(WorldData);
                }
                else
                {
                    create_list(SearchData);
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void create_list(List<data> data)
    {
        ListAdapter adapter=new ListAdapter(world.this,data);
        world_list.setAdapter(adapter);
    }
}
