package com.nemocorp.cv19;

import android.app.Activity;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatDelegate;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class variables {
    static List<String> confirmed= new ArrayList<>();
    static List<String> states= new ArrayList<>();
    static List<String> death= new ArrayList<>();
    static List<String> recovered= new ArrayList<>();
    static List<String> active= new ArrayList<>();
    static List<String> state_code= new ArrayList<>();
    static List<String> dis_confirmed= new ArrayList<>();
    static List<String> dis= new ArrayList<>();
    static List<String> dis_death= new ArrayList<>();
    static List<String> dis_recovered= new ArrayList<>();
    static List<String> dis_active= new ArrayList<>();
    static List<String> world_confirmed= new ArrayList<>();
    static List<String> world= new ArrayList<>();
    static List<String> world_death= new ArrayList<>();
    static List<String> world_recovered= new ArrayList<>();
    static List<String> world_active= new ArrayList<>();
    static HttpURLConnection connection;
    static BufferedReader reader;
    static boolean seen=false;//used to display splash screen only once
    static Activity activity;
    static ListView main_list;
    static ListView dis_list;
    static ListView world_list;
    static boolean backpressed=true;//used to create state list again if activity was stopped
    static boolean world_list_made=false;//used to create world list as soon as loading is done
    static ArrayList<Integer> temp4=new ArrayList<>();//for searched item original index
    static boolean main_list_clicked=false; //to make sure main_list item is clicked once and only starts one activty
}
