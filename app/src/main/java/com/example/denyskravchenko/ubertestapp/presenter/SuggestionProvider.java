package com.example.denyskravchenko.ubertestapp.presenter;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by denyskravchenko on 27.05.17.
 */

public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.example.denyskravchenko.ubertestapp.presenter.SuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
