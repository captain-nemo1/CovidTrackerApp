package com.nemocorp.cv19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class district extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);
        variables.main_list_clicked=false; //makes main_list items clickable again
        Toolbar yourToolbar=findViewById(R.id.toolbar);
        setSupportActionBar(yourToolbar);
        variables.dis_list=findViewById(R.id.list);
        create_list();
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
                List<String> temp=new ArrayList<>();
                List<String>temp2=new ArrayList<>();
                List<String> temp3=new ArrayList<>();
                List<String>temp4=new ArrayList<>();
                List<String>temp5=new ArrayList<>();
                variables.temp4.clear();
                for(int i=0;i<variables.dis.size();i++) {
                    String temp1=variables.dis.get(i);
                    if (temp1.toLowerCase().contains(s.toLowerCase())) {
                        temp.add(variables.dis.get(i));
                        temp2.add(variables.dis_active.get(i));
                        temp3.add(variables.dis_confirmed.get(i));
                        temp4.add(variables.dis_recovered.get(i));
                        temp5.add(variables.dis_death.get(i));
                        variables.temp4.add(i);
                    }
                }
                if(variables.temp4.size()<0 || variables.temp4.size()==variables.states.size())
                { create_list();}
                else
                {myAdapter madapter=new myAdapter(district.this,temp, temp5,temp4,temp3,temp2);
                    madapter.notifyDataSetChanged();
                    variables.dis_list.setAdapter(madapter);}
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void create_list()
    {
        myAdapter adapter=new myAdapter(this, variables.dis,variables.dis_death,variables.dis_recovered,
                variables.dis_confirmed,variables.dis_active);
        adapter.notifyDataSetChanged();
        variables.dis_list.setAdapter(adapter);
    }
    @Override
    public void onBackPressed()
    {
        variables.main_list.setClickable(true);//makes main_list items clickable again
        super.onBackPressed();
    }
}
