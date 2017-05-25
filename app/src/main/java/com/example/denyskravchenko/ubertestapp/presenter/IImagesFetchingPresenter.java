package com.example.denyskravchenko.ubertestapp.presenter;

import com.example.denyskravchenko.ubertestapp.view.IImagesView;

import java.util.List;

/**
 * Created by denyskravchenko on 25.05.17.
 */

public interface IImagesFetchingPresenter<T extends IImagesView> {

    void bindView(T view);

    void fetchImagesCollection(String userChoice);

    List<T> getCachedImagesCollection();

    T getImageById();


}
