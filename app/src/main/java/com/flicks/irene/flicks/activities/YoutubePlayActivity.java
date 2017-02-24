package com.flicks.irene.flicks.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.flicks.irene.flicks.R;
import com.flicks.irene.flicks.model.Youtubes;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class YoutubePlayActivity extends YouTubeBaseActivity {

    private int movieId;
    private Youtubes youtubes;
    private YouTubePlayerView youTubePlayerView;
    private String YOUTUBE_API_KEY = "AIzaSyDVRm1DbekldL41WprX9gvnpPLa_yymuZw";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_play);
        Log.e("DEBUG","test");
        init();
    }

    public void init() {
        movieId = getIntent().getIntExtra("movieId",0);
        Log.e("DEBUG",String.valueOf(movieId));
        youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        String url = getVideoAPIUrl(String.valueOf(movieId));
        Log.e("DEBUG",url);
        RequestParams params = new RequestParams();
        params.add("api_key","a07e22bc18f5cb106bfe4cc1f83ad8ed");
        getYoutubeID(url,params);
    }

    private void getYoutubeID(String url, RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url,params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                // when api success
                Gson gson = new Gson();
                youtubes = gson.fromJson(responseString, Youtubes.class);
                Log.e("DEBUG",youtubes.results.get(0).key);
                if(youtubes.results.size() > 0)
                    loadYoutube(YOUTUBE_API_KEY);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,  Throwable e) {
                // when api failed
                Toast.makeText(YoutubePlayActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void loadYoutube(String APIKey) {
        youTubePlayerView.initialize(APIKey,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideo(youtubes.results.get(0).key);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    private String getVideoAPIUrl(String videoId) {
        return "https://api.themoviedb.org/3/movie/" + videoId + "/videos";
    }
}
