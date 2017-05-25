package com.example.denyskravchenko.ubertestapp.presenter.requests;

import com.example.denyskravchenko.ubertestapp.model.ImagesCollection;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by denyskravchenko on 25.05.17.
 */

public interface GetImagesCollection {

    @GET("?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&safe_search=1")
    Call<ImagesCollection> getImagesCollection(@Query("text") String searchParam);
}
