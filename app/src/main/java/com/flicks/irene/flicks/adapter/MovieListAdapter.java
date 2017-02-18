package com.flicks.irene.flicks.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flicks.irene.flicks.R;
import com.flicks.irene.flicks.activities.MovieDetailActivity;
import com.flicks.irene.flicks.activities.YoutubePlayActivity;
import com.flicks.irene.flicks.model.Movie;
import com.flicks.irene.flicks.model.Movies;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Irene on 2017/2/16.
 */

public class MovieListAdapter extends BaseAdapter {

    private Context mContext;
    private Movies mData;
    private int mOrientation;
    private int mScreenWidth;
    private int ITEM_TYPE_POPULAR = 1;
    private int ITEM_TYPE_NOT_POPULAR = 0;

    public MovieListAdapter(Context context, Movies data, int screenWidth) {
        this.mContext = context;
        this.mData = data;
        this.mScreenWidth = screenWidth;
    }

    @Override
    public int getCount() {
        if(mData != null)
            return mData.results.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return mData.results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        // Get the data item for this position
        final Movie movieItem = (Movie) getItem(position);
        int itemType = getItemViewType(position);
        mOrientation = mContext.getResources().getConfiguration().orientation;

        //check the existing view being reused
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            if(itemType == ITEM_TYPE_NOT_POPULAR)
                convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_item_view1, parent, false);
            else
                convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_item_view2, parent, false);

            holder = new ViewHolder(convertView);
            holder.imagePoster = (ImageView) convertView.findViewById(R.id.image_poster);
            holder.textTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            holder.textOverview = (TextView) convertView.findViewById(R.id.tvOverview);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(holder);

        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            holder = (ViewHolder) convertView.getTag();
        }


        // Populate the data from the data object via the viewHolder object
        // into the template view.


        Log.e("DEBUG",String.valueOf(mOrientation));
        if (mOrientation == Configuration.ORIENTATION_PORTRAIT) {
            if(itemType == ITEM_TYPE_NOT_POPULAR)
            {
                holder.textTitle.setText(movieItem.title);
                holder.textOverview.setText(movieItem.overview);

                Picasso.with(mContext).load(movieItem.getPoster_path())
                        .transform(new RoundedCornersTransformation(2, 2))
                        .fit()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imagePoster);
                Log.e("DEBUG",movieItem.getPoster_path());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchMovieDetailView(movieItem);
                    }
                });
            }
            else
            {
                Picasso.with(mContext).load(movieItem.getBackdrop_path())
                        .transform(new RoundedCornersTransformation(2, 2))
                        .resize(mScreenWidth,0)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imagePoster);
                Log.e("DEBUG",movieItem.getPoster_path());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchYoutubeView(movieItem.id);
                    }
                });
            }

        } else if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {

            holder.textTitle.setText(movieItem.title);
            holder.textOverview.setText(movieItem.overview);

            if(itemType == ITEM_TYPE_NOT_POPULAR)
            {
                Picasso.with(mContext).load(movieItem.getPoster_path())
                        .transform(new RoundedCornersTransformation(2, 2))
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .resize(300,500)
                        .centerInside()
                        .into(holder.imagePoster);
                Log.e("DEBUG",movieItem.getBackdrop_path());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchMovieDetailView(movieItem);
                    }
                });
            }
            else
            {
                Picasso.with(mContext).load(movieItem.getBackdrop_path())
                        .transform(new RoundedCornersTransformation(2, 2))
                        .fit()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.imagePoster);
                Log.e("DEBUG",movieItem.getBackdrop_path());

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launchYoutubeView(movieItem.id);
                    }
                });
            }
        }


        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {

        if(mData.results.get(position).vote_average >= 5.0f)
            return ITEM_TYPE_POPULAR;
        else
            return ITEM_TYPE_NOT_POPULAR;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    static class ViewHolder {

        @BindView(R.id.image_poster) ImageView imagePoster;
        @Nullable @BindView(R.id.tvTitle) TextView textTitle;
        @Nullable @BindView(R.id.tvOverview) TextView textOverview;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private void launchMovieDetailView(Movie movie) {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//need to open activity outside activity
        i.setClass(mContext, MovieDetailActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("movie", movie);
        // brings up the second activity
        mContext.startActivity(i);
    }


    private void launchYoutubeView(int movieId) {

        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//need to open activity outside activity
        i.setClass(mContext, YoutubePlayActivity.class);
        // put "extras" into the bundle for access in the second activity
        i.putExtra("movieId", movieId);
        // brings up the second activity
        mContext.startActivity(i);
    }

}
