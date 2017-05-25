package com.example.denyskravchenko.ubertestapp.di;

import com.example.denyskravchenko.ubertestapp.presenter.IImagesFetchingPresenter;
import com.example.denyskravchenko.ubertestapp.presenter.ImagesFetchingPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by denyskravchenko on 25.05.17.
 */
@Module
public class ImageFetchingModule {
    @Provides
    @Singleton
    ImagesFetchingPresenter providesFetchingPresenter() {
        return new ImagesFetchingPresenter();
    }


}
