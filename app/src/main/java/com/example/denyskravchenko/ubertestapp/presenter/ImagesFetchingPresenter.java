package com.example.denyskravchenko.ubertestapp.presenter;

import android.text.TextUtils;
import android.util.LruCache;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.denyskravchenko.ubertestapp.model.ImagesCollection;
import com.example.denyskravchenko.ubertestapp.model.Photo;
import com.example.denyskravchenko.ubertestapp.model.Photos;
import com.example.denyskravchenko.ubertestapp.presenter.requests.GetImagesCollection;
import com.example.denyskravchenko.ubertestapp.view.IImagesView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by denyskravchenko on 25.05.17.
 */

public class ImagesFetchingPresenter implements IImagesFetchingPresenter<IImagesView> {

    private WeakReference<IImagesView> mView;
    private List<String> mPhotosUrls;
    private LruCache<String, List<String>> mCache;

    public ImagesFetchingPresenter() {
        int cacheSize = 5;
        mCache = new LruCache<>(cacheSize);
    }

    @Override
    public void bindView(IImagesView view) {
        if (view == null)
            throw new IllegalArgumentException("View and queryString can't be null");
        mView = new WeakReference<>(view);

    }


    @Override
    public void unbindView() {
        mView = null;
    }


    @Override
    public void fetchImagesCollectionOrGetFromCache(String userChoice) {
        if (!TextUtils.isEmpty(userChoice)) {
            List<String> cache = mCache.get(userChoice);
            if (cache != null && !cache.isEmpty()) {
                IImagesView view = mView.get();
                if (view!= null) {
                    view.showImagesByUrls(mPhotosUrls);
                }
            } else {
                String baseUrl = "https://api.flickr.com/services/rest/";
                Retrofit retrofit = buildRetrofit(baseUrl);
                GetImagesCollection getImagesCollection = retrofit.create(GetImagesCollection.class);
                Executors.newSingleThreadExecutor().submit(() -> {
                    try {
                        Response<ImagesCollection> response = getImagesCollection.getImagesCollection(userChoice).execute();
                        if (response != null) {
                            ImagesCollection imagesCollection = response.body();
                            if (imagesCollection != null) {
                                Photos photos = imagesCollection.getPhotos();
                                if (photos != null) {
                                    List<Photo> photosList = photos.getPhoto();
                                    mPhotosUrls = Stream.of(photosList).map(photo ->
                                            String.format("http://farm%s.static.flickr.com/%s/%s_%s.jpg", photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret())
                                    ).collect(Collectors.toList());
                                    IImagesView view = mView.get();
                                    if (view != null) {
                                        mCache.put(userChoice, mPhotosUrls);
                                        view.showImagesByUrls(mPhotosUrls);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private Retrofit buildRetrofit(String url) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url).client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}

