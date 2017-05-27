package com.example.denyskravchenko.ubertestapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.denyskravchenko.ubertestapp.presenter.ImagesFetchingPresenter;
import com.example.denyskravchenko.ubertestapp.view.ImagesView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

/**
 * Created by denyskravchenko on 27.05.17.
 */
@RunWith(AndroidJUnit4.class)
public class ImagesViewTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            ImagesView.class);

    @Mock
    ImagesFetchingPresenter presenterMock;

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void searchPredictions() throws Exception {
        ImagesView imagesView = (ImagesView) mActivityRule.getActivity();
        imagesView.setPresenter(presenterMock);
        String query = "test";
        imagesView.searchPredictions(query);
        Mockito.verify(presenterMock, Mockito.atLeastOnce()).fetchImagesCollectionOrGetFromCache(query);
    }

}