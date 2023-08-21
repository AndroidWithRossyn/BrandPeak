package com.itsbusinessposter.idebrandvideo.viewmodel;

import static com.itsbusinessposter.idebrandvideo.MyApplication.prefManager;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.items.ItemVcard;
import com.itsbusinessposter.idebrandvideo.items.UploadItem;
import com.itsbusinessposter.idebrandvideo.repository.VCardRepository;
import com.itsbusinessposter.idebrandvideo.utils.AbsentLiveData;
import com.itsbusinessposter.idebrandvideo.utils.Constant;

import java.util.List;

public class VCardViwModel extends AndroidViewModel {

    VCardRepository repository;

    public LiveData<Resource<List<ItemVcard>>> result;
    public MutableLiveData<String> cardObj = new MutableLiveData<>();
    public MutableLiveData<String> uploadObj = new MutableLiveData<>();
    public MutableLiveData<String> createObj = new MutableLiveData<>();

    public VCardViwModel(@NonNull Application application) {
        super(application);

        repository = new VCardRepository(application);

        result = Transformations.switchMap(cardObj, obj->{
            if (obj == null){
                return AbsentLiveData.create();
            }
            return repository.getVCards(prefManager().getString(Constant.api_key));
        });
    }

    public void setObj (String data){
        cardObj.setValue(data);
    }

    public LiveData<Resource<List<ItemVcard>>> getVCardsData(){
        return  result;
    }

    public LiveData<Resource<UploadItem>> uploadImage(String filePath, Uri imageUri){
        uploadObj.setValue("PS");
        return Transformations.switchMap(uploadObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.uploadImage(prefManager().getString(Constant.api_key), filePath, imageUri);
        });
    }

    public LiveData<Resource<UploadItem>> createVCard(String businessName, String yourName, String designation, String mobile,
                                                      String whatsapp, String email, String website, String location, String facebook,
                                                      String insta, String youtube, String twitter, String linkedin, String about,
                                                      String imageUrl, String tempID) {
        createObj.setValue("PS");
        return Transformations.switchMap(createObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.createVcard(prefManager().getString(Constant.api_key), businessName, yourName, designation, mobile,
                    whatsapp, email, website, location, facebook, insta, youtube, twitter, linkedin, about, imageUrl, tempID);
        });
    }
}
