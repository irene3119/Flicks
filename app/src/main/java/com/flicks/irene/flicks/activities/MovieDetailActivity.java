package com.flicks.irene.flicks.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flicks.irene.flicks.R;
import com.flicks.irene.flicks.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.image_detailPoster) ImageView imageDetailPoster;
    @BindView(R.id.tvDetailTitle) TextView tvDetailTitle;
    @BindView(R.id.tvDetailReleaseDate) TextView tvDetailReleaseDate;
    @BindView(R.id.tvDetailOverview) TextView tvDetailOverview;
    @BindView(R.id.ratingBar) RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        Picasso.with(getBaseContext()).load(movie.getBackdrop_path())
                .transform(new RoundedCornersTransformation(2, 2))
                .resize(getScreenWidth(),0)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageDetailPoster);
        tvDetailTitle.setText(movie.title);
        tvDetailReleaseDate.setText("Release date: "+movie.release_date);
        tvDetailOverview.setText(movie.overview);
        ratingBar.setMax(5);
        ratingBar.setRating(movie.vote_average/2);
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
