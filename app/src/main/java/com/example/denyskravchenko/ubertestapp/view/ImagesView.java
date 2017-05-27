package com.example.denyskravchenko.ubertestapp.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.denyskravchenko.ubertestapp.R;
import com.example.denyskravchenko.ubertestapp.UberApplication;
import com.example.denyskravchenko.ubertestapp.events.NetworkChangedEvent;
import com.example.denyskravchenko.ubertestapp.presenter.ImagesFetchingPresenter;
import com.example.denyskravchenko.ubertestapp.presenter.SuggestionProvider;
import com.example.denyskravchenko.ubertestapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesView extends IImagesView implements SearchView.OnSuggestionListener {

    @Inject
    ImagesFetchingPresenter mPresenter;

    @BindView(R.id.images_grid)
    RecyclerView mImagesGrid;
    @BindView(R.id.error_label)
    TextView errorLabel;

    private ImagesAdapter mAdapter;
    private SearchView mSearchView;

    private List<String> mUrls = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((UberApplication) getApplication()).getFetchingComponent().inject(this);

        mPresenter.bindView(this);

        int columnsNumber = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnsNumber);
        mImagesGrid.setLayoutManager(layoutManager);
        mAdapter = new ImagesAdapter(mUrls);
        mImagesGrid.setAdapter(mAdapter);
        mImagesGrid.setHasFixedSize(true);

        handleSearchIntent();

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void handleSearchIntent() {
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchPredictions(query);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    SuggestionProvider.AUTHORITY, SuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
        }
    }

    private void searchPredictions(String query) {
        if (!Utils.isNetworkAvailable(this)) {
            showNoNetworkMessage();
            return;
        }
        mPresenter.fetchImagesCollectionOrGetFromCache(query);
    }


    @Override
    public void showImagesByUrls(List<String> photos) {
        if (photos != null && !photos.isEmpty()) {
            mImagesGrid.post(() -> {
                mUrls.clear();
                mUrls.addAll(photos);
                mAdapter.notifyDataSetChanged();
                errorLabel.setVisibility(View.GONE);
                mImagesGrid.setVisibility(View.VISIBLE);
            });
        } else {
            errorLabel.setVisibility(View.VISIBLE);
            mImagesGrid.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {
        if (event instanceof NetworkChangedEvent) {
            NetworkChangedEvent networkChangedEvent = (NetworkChangedEvent) event;
            String message = networkChangedEvent.isIsNetworkAvailable() ? getString(R.string.nothing_to_show) : getString(R.string.check_your_internet_connection_message);
            errorLabel.setVisibility(View.VISIBLE);
            errorLabel.setText(message);
            mImagesGrid.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNoNetworkMessage() {
        errorLabel.setVisibility(View.VISIBLE);
        errorLabel.setText(R.string.check_your_internet_connection_message);
        mImagesGrid.setVisibility(View.GONE);
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return true;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        android.support.v4.widget.CursorAdapter selectedView = mSearchView.getSuggestionsAdapter();
        Cursor cursor = (Cursor) selectedView.getItem(position);
        int index = cursor.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1);
        String query = cursor.getString(index);
        mSearchView.setQuery(query, true);
        searchPredictions(query);
        return true;
    }

    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
        private List<String> mUrls;
        private int mImageSize = Utils.dp2px(ImagesView.this, 200);

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mPhotoItem;

            public ViewHolder(ImageView v) {
                super(v);
                mPhotoItem = v;
            }
        }

        public ImagesAdapter(List<String> urls) {
            mUrls = urls;
        }

        @Override
        public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
            ImageView v = (ImageView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.image_item, parent, false);
            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String imageUrl = mUrls.get(position);
            if (!TextUtils.isEmpty(imageUrl))
                Glide.with(ImagesView.this)
                        .load(imageUrl)
                        .override(mImageSize, mImageSize)
                        .centerCrop()
                        .into(holder.mPhotoItem);
            else
                Glide.clear(holder.mPhotoItem);

        }

        @Override
        public int getItemCount() {
            return mUrls.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnSuggestionListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
