package com.nemocorp.cv19.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nemocorp.cv19.model.data;
import com.nemocorp.cv19.repository.StateData;

import java.util.List;


public class IndiaViewmodel extends ViewModel {
    private MutableLiveData<List<data>> indiaData;

    public void init(){
        if(indiaData!=null)
            return;
        StateData stateData = StateData.getInstance();
        indiaData= stateData.getState();
    }
    public LiveData<List<data>> getIndiaData()
    {
        return indiaData;
    }
}
