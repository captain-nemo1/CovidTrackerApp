package com.nemocorp.cv19.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nemocorp.cv19.R;
import com.nemocorp.cv19.model.data;

import java.util.List;

public class ListAdapter extends ArrayAdapter {
    Activity context;
    List<data> data;
    public ListAdapter(@NonNull Activity context, List<data> data) {
        super(context, R.layout.adapter,data);
        this.context=context;
        this.data=data;
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
        state.setText(data.get(position).getName());
        deaths.setText("Deaths:"+data.get(position).getDeath());
        recovereds.setText("Recovered:"+data.get(position).getRecovered());
        actives.setText("Active:"+data.get(position).getActive());
        confirm.setText("Confirmed:"+data.get(position).getConfirmed());
        TextView deaths_inc=row.findViewById(R.id.dead_increase);
        TextView recovereds_inc=row.findViewById(R.id.recovered_increase);
        TextView conf_inc=row.findViewById(R.id.textView_inc);
        if(data.get(position).getRecovered_inc()!=null) { //for indian states increase data
            int con= Integer.parseInt(data.get(position).getConfirmed_inc());
            if(con>0)
                conf_inc.setText("+" + con);
            else
                conf_inc.setText(String.valueOf(con));
            deaths_inc.setText("+" + data.get(position).getDeath_inc());
            recovereds_inc.setText("+" +data.get(position).getRecovered_inc());
        }
        else
        {   deaths_inc.setVisibility(View.GONE);
            recovereds_inc.setVisibility(View.GONE);
            conf_inc.setVisibility(View.GONE);
        }
        return row;
    }
}


