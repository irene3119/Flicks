package com.flicks.irene.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.flicks.irene.flicks.R;
import com.flicks.irene.flicks.adapter.MovieListAdapter;
import com.flicks.irene.flicks.model.Movies;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.lvMovies) ListView lvMovies;
    private MovieListAdapter lvMoviesAdapter;
    private Movies mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getMovies();
    }


    private void setList() {
        lvMovies = (ListView) findViewById(R.id.lvMovies);
        lvMoviesAdapter = new MovieListAdapter(getBaseContext(), mData, getScreenWidth());
        lvMovies.setAdapter(lvMoviesAdapter);
    }

    private void getMovies() {
        RequestParams params = new RequestParams();
        params.add("api_key","a07e22bc18f5cb106bfe4cc1f83ad8ed");
        getMovieByRequest("now_playing",params);
    }

    private void getMovieByRequest(String url, RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.themoviedb.org/3/movie/"+url,params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // when api success
                Gson gson = new Gson();
                mData = gson.fromJson(responseString, Movies.class);
                Log.e("DEBUG",mData.results.get(0).poster_path);
                setList();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,  Throwable e) {
                // when api failed
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private int getScreenWidth()
    {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }
}
