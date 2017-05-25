package com.example.denyskravchenko.ubertestapp.view;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.denyskravchenko.ubertestapp.R;
import com.example.denyskravchenko.ubertestapp.UberApplication;
import com.example.denyskravchenko.ubertestapp.model.Photo;
import com.example.denyskravchenko.ubertestapp.presenter.ImagesFetchingPresenter;

import java.util.List;

import javax.inject.Inject;

public class ImagesView extends IImagesView {

    @Inject
    ImagesFetchingPresenter mPresenter;

    private RecyclerView mImagesGrid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((UberApplication) getApplication()).getFetchingComponent().inject(this);

        mPresenter.bindView(this);
        mPresenter.fetchImagesCollection("cat");

    }

    @Override
    public void showImagesByUrls(List<String> photos) {
        mImagesGrid.post(() -> {});
    }
}
