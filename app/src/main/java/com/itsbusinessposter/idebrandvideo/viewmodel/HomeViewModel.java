package com.itsbusinessposter.idebrandvideo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.items.HomeItem;
import com.itsbusinessposter.idebrandvideo.repository.HomeRepository;
import com.itsbusinessposter.idebrandvideo.utils.AbsentLiveData;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;

public class HomeViewModel extends AndroidViewModel  {

    HomeRepository homeRepository;
    public LiveData<Resource<HomeItem>> result;
    public MutableLiveData<String> homeObj = new MutableLiveData<>();

    PrefManager prefManager;

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();

    public HomeViewModel(@NonNull Application application) {
        super(application);

        prefManager = new PrefManager(application);
        homeRepository = new HomeRepository(application);

        result = Transformations.switchMap(homeObj, obj->{
            if(obj == null){
                return AbsentLiveData.create();
            }
            return homeRepository.getHomeData(prefManager.getString(Constant.api_key));
        });

    }

    public void setHomeObj(String obj){
        homeObj.setValue(obj);
    }

    public LiveData<Resource<HomeItem>> getHomeData(){
        return result;
    }

    public boolean isLoading = false;


    //region For loading status
    public void setLoadingState(Boolean state) {
        isLoading = state;
        loadingState.setValue(state);
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }
}
