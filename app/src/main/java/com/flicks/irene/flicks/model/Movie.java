package com.flicks.irene.flicks.model;

import java.io.Serializable;

/**
 * Created by Irene on 2017/2/16.
 */

public class Movie implements Serializable {

    public String poster_path;
    public String overview;
    public String title;
    public String backdrop_path;
    public float vote_average;
    public String release_date;
    public int id;


    public String getPoster_path()
    {

        return "https://image.tmdb.org/t/p/w342"+poster_path;
    }

    public String getBackdrop_path()
    {
        return "https://image.tmdb.org/t/p/w300"+backdrop_path;
    }
}
