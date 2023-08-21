package com.itsbusinessposter.idebrandvideo.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.itsbusinessposter.idebrandvideo.Config;
import com.itsbusinessposter.idebrandvideo.api.ApiClient;
import com.itsbusinessposter.idebrandvideo.api.ApiResponse;
import com.itsbusinessposter.idebrandvideo.api.common.NetworkBoundResource;
import com.itsbusinessposter.idebrandvideo.api.common.common.Resource;
import com.itsbusinessposter.idebrandvideo.database.AppDatabase;
import com.itsbusinessposter.idebrandvideo.database.NewsDao;
import com.itsbusinessposter.idebrandvideo.items.NewsItem;
import com.itsbusinessposter.idebrandvideo.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository {

    AppDatabase db;
    NewsDao newsDao;

    MediatorLiveData<Resource<List<NewsItem>>> results = new MediatorLiveData<>();

    public NewsRepository(Application application){
        db = AppDatabase.getInstance(application);
        newsDao = db.getNewsDao();
    }

    public LiveData<Resource<List<NewsItem>>> getResults() {
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem("0", "Dussehra",
                "https://brand365.io/brandbox/uploads/images/11102021101328_26553_event.jpg",
                "15th Oct, 2021", "<p>Dashain Baḍadasai बडादशैँ in Nepal, also Vijaya Dashami, is a major religious festival in Nepal.&nbsp;<br />\n" +
                "Observances: Putting tika on the forehead, prayers, religious rituals like burning an effigy of Ravana<br />\n" +
                "<strong>Date:</strong> Friday, 15 October, 2021<br />\n" +
                "<strong>Celebrations:</strong> Marks the end of Durga Puja and Ramlila<br />\n" +
                "<strong>Also called:</strong> Dashahra, Dasara, Dashain<br />\n" +
                "<strong>Significance:</strong> Celebrates the victory of good over evil</p>\n"));

        list.add(new NewsItem("1", "World Students' Day",
                "https://brand365.io/brandbox/uploads/images/11102021101547_41891_event.jpg",
                "15th Oct, 2021", "<p>World Students&#39; Day is marked on A. P. J. Abdul Kalam&#39;s birthday, 15 October. In 2010 the United Nations declared 15 October &quot;World Students&#39; Day&quot;.&nbsp;<br />\n" +
                "<strong>Date:</strong> 15 October<br />\n" +
                "<strong>Observed by:</strong> India</p>\n"));

        list.add(new NewsItem("2", "Durga Puja",
                "https://brand365.io/brandbox/uploads/images/07102021101548_87131_event.jpg",
                "11th Oct, 2021", "<p>Durga Puja, also known as Durgotsava, or Sharodotsava, is an annual Hindu festival originating in the Indian subcontinent which reveres and pays homage to the Hindu goddess, Durga.&nbsp;<br />\n" +
                "Date: Mon, 11 Oct, 2021 &ndash; Fri, 15 Oct, 2021<br />\n" +
                "Related to: Mahalaya, Navratri, Dussehra<br />\n" +
                "Observances: Ceremonial worship of goddess Durga</p>\n"));

        db.runInTransaction(()->{
            newsDao.deleteNews();
            newsDao.insertAll(list);
        });

        results.addSource(newsDao.getNews(), data->results.setValue(Resource.success(data)));

        return results;
    }

    public LiveData<Resource<List<NewsItem>>> getNews(String apiKey, String page) {
        return new NetworkBoundResource<List<NewsItem>, List<NewsItem>>() {
            @Override
            protected void saveCallResult(@NonNull List<NewsItem> item) {
                try {
                    db.runInTransaction(() -> {
                        newsDao.deleteNews();
                        newsDao.insertAll(item);
                    });
                } catch (Exception ex) {
                    Util.showErrorLog("Error at ", ex);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<NewsItem> data) {
                return Config.IS_CONNECTED;
            }

            @NonNull
            @Override
            protected LiveData<List<NewsItem>> loadFromDb() {
                return newsDao.getNews();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<NewsItem>>> createCall() {
                return ApiClient.getApiService().getNews(apiKey, page);
            }
        }.asLiveData();
    }
}
