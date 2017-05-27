package com.example.denyskravchenko.ubertestapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.denyskravchenko.ubertestapp.UberApplication;
import com.example.denyskravchenko.ubertestapp.model.Photo;
import com.example.denyskravchenko.ubertestapp.presenter.IImagesFetchingPresenter;
import com.example.denyskravchenko.ubertestapp.presenter.ImagesFetchingPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by denyskravchenko on 25.05.17.
 */

public abstract class IImagesView extends AppCompatActivity {


    abstract public void showImagesByUrls(List<String> photos);
    abstract public void showNoNetworkMessage();
}
