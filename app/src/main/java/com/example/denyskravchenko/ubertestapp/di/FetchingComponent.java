package com.example.denyskravchenko.ubertestapp.di;

import com.example.denyskravchenko.ubertestapp.view.ImagesView;
import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by denyskravchenko on 25.05.17.
 */
@Singleton
@Component(modules = {ImageFetchingModule.class})
public interface FetchingComponent {
    void inject(ImagesView activity);
}
