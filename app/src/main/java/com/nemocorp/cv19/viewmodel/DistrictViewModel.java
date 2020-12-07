package com.nemocorp.cv19.viewmodel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nemocorp.cv19.model.data;
import com.nemocorp.cv19.repository.DistrictData;

import java.util.List;

public class DistrictViewModel extends ViewModel {
    private MutableLiveData<List<data>> districtData;
    private MutableLiveData<Boolean> inProgress;
    public void init(String StateName){
        if(districtData!=null)
            return;
        DistrictData district = DistrictData.getInstance();
        districtData=district.getDistrictData(StateName);
        inProgress=district.getInProgress();
    }
    public MutableLiveData<List<data>> getDistrictData(){
        return districtData;
    }
    public MutableLiveData<Boolean> checkInProgress(){ return inProgress;}
}
