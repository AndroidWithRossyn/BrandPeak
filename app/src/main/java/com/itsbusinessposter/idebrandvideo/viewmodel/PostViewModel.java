package com.itsbusinessposter.idebrandvideo.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.items.MainStrModel;
import com.itsbusinessposter.idebrandvideo.items.PostItem;
import com.itsbusinessposter.idebrandvideo.items.StickerItem;
import com.itsbusinessposter.idebrandvideo.repository.PostsRepository;
import com.itsbusinessposter.idebrandvideo.utils.AbsentLiveData;
import com.itsbusinessposter.idebrandvideo.utils.Constant;
import com.itsbusinessposter.idebrandvideo.utils.PrefManager;

import java.util.List;

public class PostViewModel extends AndroidViewModel {

    private PostsRepository postsRepository;
    private MutableLiveData<TmpPost> postObj = new MutableLiveData<>();
    private LiveData<Resource<List<PostItem>>> getTrendingPost;

    private MutableLiveData<TmpPost> postByIdObj = new MutableLiveData<>();
    private LiveData<Resource<List<PostItem>>> getByIdPost;

    private MutableLiveData<String> stickerObj = new MutableLiveData<>();
    private MutableLiveData<String> stickerSearchObj = new MutableLiveData<>();

    PrefManager prefManager;

    public PostViewModel(@NonNull Application application) {
        super(application);
        postsRepository = new PostsRepository(application);
        prefManager = new PrefManager(application);
        getTrendingPost = Transformations.switchMap(postObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getTrendingPost(obj.language);
        });

        getByIdPost = Transformations.switchMap(postByIdObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getById(prefManager.getString(Constant.api_key), obj.festId, obj.type, obj.language, obj.isVideo);
        });

    }

    public void setTrendingPost(boolean isTrending, String language) {
        TmpPost tmpPost = new TmpPost();
        tmpPost.isTraining = isTrending;
        tmpPost.festId = "";
        tmpPost.type = "";
        tmpPost.language = "";
        postObj.setValue(tmpPost);
    }

    public void setPostByIdObj(String id, String type, String language, boolean isVideo) {
        TmpPost tmpPost = new TmpPost();
        tmpPost.isTraining = false;
        tmpPost.isVideo = isVideo;
        tmpPost.festId = id;
        tmpPost.type = type;
        tmpPost.language = language;
        postByIdObj.setValue(tmpPost);
    }

    public LiveData<Resource<List<PostItem>>> getById() {
        return getByIdPost;
    }

    public LiveData<Resource<List<PostItem>>> getTrendingPost() {
        return getTrendingPost;
    }

    public LiveData<Resource<MainStrModel>> getStickers(){
        stickerObj.setValue("IQ");
        return Transformations.switchMap(stickerObj, obj -> {
            if(obj == null){
                return AbsentLiveData.create();
            }
            return postsRepository.getStickers(prefManager.getString(Constant.api_key));
        });
    }

    public LiveData<Resource<List<StickerItem>>> getStickersByKeyword(String keyword) {
        stickerSearchObj.setValue(keyword);
        return Transformations.switchMap(stickerSearchObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return postsRepository.getStickersByKeyword(prefManager.getString(Constant.api_key), obj);
        });
    }

    private class TmpPost {
        public String festId = "";
        public String type = "";
        public String language = "";
        public boolean isTraining = false;
        public boolean isVideo = false;
    }
}
