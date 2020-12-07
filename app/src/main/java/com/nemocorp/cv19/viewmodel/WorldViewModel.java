package com.nemocorp.cv19.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nemocorp.cv19.model.data;
import com.nemocorp.cv19.repository.WorldData;


import java.util.List;

public class WorldViewModel extends ViewModel {
    private MutableLiveData<List<data>> worldData;
    private MutableLiveData<Boolean> inProgress;
    public void init(){
        if(worldData!=null)
            return;
        WorldData district = WorldData.getInstance();
        worldData=district.getWorldData();
        inProgress=district.getInProgress();
    }
    public MutableLiveData<List<data>> getWorldData(){
        return worldData;
    }
    public MutableLiveData<Boolean> checkInProgress(){ return inProgress;}
}
