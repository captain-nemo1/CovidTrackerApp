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
     List<String> active;
    public myAdapter(@NonNull Activity context,List<String> states,List<String> death,List<String> recovered,
                     List<String> confirmed,List<String> active) {
        super(context,R.layout.adapter,states);
        this.context=context;
        this.active=active;
        this.states=states;
        this.death=death;
        this.recovered=recovered;
        this.confirmed=confirmed;
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
        return row;
    }
}

