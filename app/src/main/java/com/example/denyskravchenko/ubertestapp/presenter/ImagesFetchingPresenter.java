package com.example.denyskravchenko.ubertestapp.presenter;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.example.denyskravchenko.ubertestapp.model.ImagesCollection;
import com.example.denyskravchenko.ubertestapp.model.Photo;
import com.example.denyskravchenko.ubertestapp.model.Photos;
import com.example.denyskravchenko.ubertestapp.presenter.requests.GetImagesCollection;
import com.example.denyskravchenko.ubertestapp.view.IImagesView;

import java.io.IOException;
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
    private Retrofit mRetrofit;
    private IImagesView mView;

    @Override
    public void bindView(IImagesView view) {
        mView = view;
    }


    @Override
    public void fetchImagesCollection(String userChoice) {
        String baseUrl = "https://api.flickr.com/services/rest/";
        Retrofit retrofit = buildRetrofit(baseUrl);
        GetImagesCollection getImagesCollection = retrofit.create(GetImagesCollection.class);
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ImagesCollection> response = getImagesCollection.getImagesCollection(userChoice).execute();
                    if (response != null) {
                        ImagesCollection imagesCollection = response.body();
                        if (imagesCollection != null) {
                            Photos photos = imagesCollection.getPhotos();
                            if (photos != null) {
                                List<Photo> photosList = photos.getPhoto();
                                List<String> photosUrls = Stream.of(photosList).map(photo ->
                                        String.format("http://farm%s.static.flickr.com/%s/%s_%s.jpg", photo.getFarm(), photo.getServer(), photo.getId(), photo.getSecret())
                                ).collect(Collectors.toList());
                                if (mView != null) {
                                    mView.showImagesByUrls(photosUrls);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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


    @Override
    public List getCachedImagesCollection() {
        return null;
    }

    @Override
    public IImagesView getImageById() {
        return null;
    }

}

