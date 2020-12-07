package com.nemocorp.cv19.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nemocorp.cv19.R;
import com.nemocorp.cv19.adapter.ListAdapter;
import com.nemocorp.cv19.model.data;
import com.nemocorp.cv19.viewmodel.DistrictViewModel;

import java.util.ArrayList;
import java.util.List;

public class district extends AppCompatActivity {
    private List<data> DistrictData = new ArrayList<>();
    private final List<data> SearchData = new ArrayList<>();
    private final List<Integer> searchItemIndex = new ArrayList<>();
    private ListView dis_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        dis_list=findViewById(R.id.list);
        String stateName = getIntent().getExtras().getString("StateName");
        DistrictViewModel districtViewModel = new ViewModelProvider(this).get(DistrictViewModel.class);
        districtViewModel.init(stateName);
        districtViewModel.getDistrictData().observe(this, new Observer<List<data>>() {
            @Override
            public void onChanged(List<data> data) {
                create_list(data);
                DistrictData=data;
            }
        });
        districtViewModel.checkInProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                {
                    findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
                }
                else{
                    findViewById(R.id.loading_layout).setVisibility(View.GONE);
                }
            }
        });
        Toolbar yourToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(yourToolbar);
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
        txtSearch.setHint("Search by District");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                searchItemIndex.clear();
                SearchData.clear();
                for(int i=0;i<DistrictData.size();i++) {
                    String temp1=DistrictData.get(i).getName();
                    if (temp1.toLowerCase().contains(s.toLowerCase())) {
                        SearchData.add(new data(DistrictData.get(i).getName(),DistrictData.get(i).getConfirmed(),DistrictData.get(i).getDeath(),DistrictData.get(i).getActive()
                                ,DistrictData.get(i).getRecovered(),DistrictData.get(i).getConfirmed_inc(),DistrictData.get(i).getDeath_inc(),DistrictData.get(i).getRecovered_inc()));
                        searchItemIndex.add(i);
                    }
                }
                if(searchItemIndex.size()<0 || searchItemIndex.size()==DistrictData.size())
                {
                    create_list(DistrictData);
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
        ListAdapter ap = new ListAdapter(district.this, data);
        dis_list.setAdapter(ap);
    }
}
