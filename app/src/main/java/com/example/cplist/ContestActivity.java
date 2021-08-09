package com.example.cplist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Contest>>{

    private static final String LOG_TAG = ContestActivity.class.getName();

    private final String CONTEST_REQUEST_URL = "https://kontests.net/api/v1/codeforces";

    private ContestAdapter adapter;

    private TextView mEmptyStateTextView;

    private static final int CONTEST_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contest_activity);

        // Define ActionBar object
        ActionBar actionBar;
        actionBar = getSupportActionBar();

        // Define ColorDrawable object and parse color
        // using parseColor method
        // with color hash code as its parameter
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#FF03DAC5"));

        // Set BackgroundDrawable
        assert actionBar != null;
        actionBar.setBackgroundDrawable(colorDrawable);

        ListView contestListView = findViewById(R.id.list);

        adapter = new ContestAdapter(this, new ArrayList<Contest>());

        contestListView.setAdapter(adapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        contestListView.setEmptyView(mEmptyStateTextView);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(CONTEST_LOADER_ID, null, this);
        }else{
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText("No Internet Connection");
        }

        contestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Contest currentContest = adapter.getItem(position);

                Uri contestUri = Uri.parse(currentContest.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, contestUri);

                startActivity(websiteIntent);

            }
        });

    }


    @Override
    public Loader<List<Contest>> onCreateLoader(int i, Bundle bundle) {
        return new ContestLoader(this, CONTEST_REQUEST_URL);
    }


    @Override
    public void onLoadFinished(Loader<List<Contest>> loader, List<Contest> contest) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText("No Contest Found");

        adapter.clear();

        if(contest != null && !contest.isEmpty()){
            adapter.addAll(contest);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Contest>> loader) {
        adapter.clear();
    }

    public static class ContestLoader extends AsyncTaskLoader<List<Contest>> {
        private static final String LOG_TAG  = ContestLoader.class.getName();

        private String mUrl;

        public ContestLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading(){
            forceLoad();
        }


        @Override
        public List<Contest> loadInBackground() {
            if(mUrl == null){
                return null;
            }

            List<Contest> contest = QueryUtils.fetchContestData(mUrl);
            return contest;
        }
    }




}