package com.itsbusinessposter.idebrandvideo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.items.FestivalItem;
import com.itsbusinessposter.idebrandvideo.repository.FestivalRepository;
import com.itsbusinessposter.idebrandvideo.utils.AbsentLiveData;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;

import java.util.List;

public class FestivalViewModel extends AndroidViewModel {

    public LiveData<Resource<List<FestivalItem>>> result;
    public MutableLiveData<String> festivalOgj = new MutableLiveData<>();

    private final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();

    FestivalRepository festivalRepository;
    PrefManager prefManager;

    public FestivalViewModel(@NonNull Application application) {
        super(application);

        festivalRepository = new FestivalRepository(application);
        prefManager = new PrefManager(application);

        result = Transformations.switchMap(festivalOgj, obj->{
            if (obj==null){
                return AbsentLiveData.create();
            }
            return festivalRepository.getResult(prefManager.getString(Constant.api_key), obj);
        });

    }

    public void setFestivalOgj(String page){
        festivalOgj.setValue(page);
    }

    public LiveData<Resource<List<FestivalItem>>> getFestivals(){
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
