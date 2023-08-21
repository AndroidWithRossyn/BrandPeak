package com.itsbusinessposter.idebrandvideo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.items.StoryItem;
import com.itsbusinessposter.idebrandvideo.repository.StoryRepository;
import com.itsbusinessposter.idebrandvideo.utils.AbsentLiveData;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;

import java.util.List;

public class StoryViewModel extends AndroidViewModel {

    public LiveData<Resource<List<StoryItem>>> result;
    public MutableLiveData<String> storyObj = new MutableLiveData<>();

    public StoryRepository storyRepository;
    PrefManager prefManager;

    public StoryViewModel(@NonNull Application application) {
        super(application);
        storyRepository = new StoryRepository(application);
        prefManager = new PrefManager(application);

        result = Transformations.switchMap(storyObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return storyRepository.getStory(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<List<StoryItem>>> getStoryData() {
        return result;
    }

    public void setStoryObj(String data) {
        storyObj.setValue(data);
    }

}
