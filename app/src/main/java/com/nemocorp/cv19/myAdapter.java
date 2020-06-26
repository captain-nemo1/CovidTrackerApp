package com.nemocorp.cv19;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class myAdapter extends ArrayAdapter {
    Activity context;
     List<String> confirmed;
     List<String> states;
     List<String> death;
     List<String> recovered;
    List<String> confirmed_inc;
    List<String> death_inc;
    List<String> recovered_inc;
     List<String> active;
    public myAdapter(@NonNull Activity context,List<String> states,List<String> death,List<String> recovered,
                     List<String> confirmed,List<String> active,  List<String> confirmed_inc,List<String> death_inc,List<String> recovered_inc) {
        super(context,R.layout.adapter,states);
        this.context=context;
        this.active=active;
        this.states=states;
        this.death=death;
        this.recovered=recovered;
        this.confirmed=confirmed;
        this.confirmed_inc=confirmed_inc;
        this.death_inc=death_inc;
        this.recovered_inc=recovered_inc;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
        row = inflater.inflate(R.layout.adapter, null, true);
        TextView state=row.findViewById(R.id.state);
        TextView deaths=row.findViewById(R.id.dead);
        TextView recovereds=row.findViewById(R.id.recovered);
        TextView actives=row.findViewById(R.id.active);
        TextView confirm=row.findViewById(R.id.textView);
        state.setText(states.get(position));
        deaths.setText("Deaths:"+death.get(position));
        recovereds.setText("Recovered:"+recovered.get(position));
        actives.setText("Active:"+active.get(position));
        confirm.setText("Confirmed:"+confirmed.get(position));
        TextView deaths_inc=row.findViewById(R.id.dead_increase);
        TextView recovereds_inc=row.findViewById(R.id.recovered_increase);
        TextView conf_inc=row.findViewById(R.id.textView_inc);
        if(recovered_inc!=null) { //for indian states increase data
            int con= Integer.parseInt(confirmed_inc.get(position));
            if(con>0)
            conf_inc.setText("+" + confirmed_inc.get(position));
            else
            conf_inc.setText(confirmed_inc.get(position));
            deaths_inc.setText("+" + death_inc.get(position));
            recovereds_inc.setText("+" +recovered_inc.get(position));
        }
        else
        {   deaths_inc.setVisibility(View.GONE);
            recovereds_inc.setVisibility(View.GONE);
            conf_inc.setVisibility(View.GONE);
        }
        return row;
    }
}

