package com.example.rkrjstdio.popularmoviesbasic;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailMovieActivity extends AppCompatActivity {

    // Declaring private ViewGroup variable for setting backGround of the layout
    private ViewGroup backgroundGroup;

    // Declaring private TextView variable for displaying title of the movie
    private TextView mMovieTitleTextView;

    // Declaring private TextView variable for displaying popularity of the movie
    private TextView mMoviePopularityTextView;

    // Declaring private TextView variable for displaying VoteAverage of the movie
    private TextView mMovieVoteAverageTextView;

    // Declaring private TextView variable for displaying ReleaseDate of the movie
    private TextView mMovieReleaseDateTextView;

    // Declaring private TextView variable for displaying language of the movie
    private TextView mMovieLanguageTextView;

    // Declaring private ImageView variable for displaying Poster of the movie
    private ImageView mMoviePosterImageView;

    // Declaring private ImageView variable for displaying PopularityImageView as layout child
    private ImageView mPopularityImageView;

    // Declaring private ImageView variable for displaying VotingAverageImageView as layout child
    private ImageView mVotingAverageImageView;

    // Declaring private ImageView variable for displaying ReleaseDateImageView as layout child
    private ImageView mReleaseDateImageView;

    // Declaring private ImageView variable for displaying LanguageImageView as layout child
    private ImageView mLanguageImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity content from a layout resource views to the activity.
        setContentView(R.layout.activity_detail_movie);

        /*
          Find a reference to the display text in the layout
          All references are private declared at top level of activity
          Each view has unique ID respectively TextView, ImageView, ViewGroup
        */
        backgroundGroup = findViewById(R.id.main_background);
        mMovieTitleTextView = findViewById(R.id.title_detail);
        mMoviePopularityTextView = findViewById(R.id.popularity_detail);
        mMovieVoteAverageTextView = findViewById(R.id.vote_average_detail);
        mMoviePosterImageView = findViewById(R.id.image_view_detail);
        TextView mMovieOverviewTextView = findViewById(R.id.overView_detail);
        mMovieReleaseDateTextView = findViewById(R.id.release_day_detail);
        mMovieLanguageTextView = findViewById(R.id.language_detail);
        mPopularityImageView = findViewById(R.id.popularity_bg_rbg);
        mVotingAverageImageView = findViewById(R.id.voting_bg_rbg);
        mReleaseDateImageView = findViewById(R.id.release_date_bg_rbg);
        mLanguageImageView = findViewById(R.id.language_bg_rbg);

        /*
          We get the intent from MainActivity and we use type getIntent along getParcelableExtra
          which will get methods from Movie model class with get type.
          The values are stored in variable movie and we assign all views to it.
         */
        final Movie movie = getIntent().getParcelableExtra("Intent to Detail Activity");

        /*
        The values in variable which are stored in movie are assigned to respective TextView's
        and ImageView's from class Movie model as get type.
         */
        mMovieTitleTextView.setText(movie.getTitle());
        mMoviePopularityTextView.setText(movie.getPopularity());
        mMovieVoteAverageTextView.setText(movie.getVoteAverage());
        mMovieOverviewTextView.setText(movie.getOverview());
        mMovieReleaseDateTextView.setText(movie.getReleaseDate());
        mMovieLanguageTextView.setText(movie.getLanguage());

        /*
        Picasso library to load images from internet into our layout using activity context from
        model class Movie using get type into our target space.
         */
        Picasso.with(getApplicationContext()).load(movie.getImageView()).into(new Target() {
            // Resolving each image which is loaded from picasso into Bitmap using onBitmapLoaded
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                assert mMoviePosterImageView != null;
                // Loading bitmap image into our imageView variable using set type
                mMoviePosterImageView.setImageBitmap(bitmap);
                /*
                Palette library used to generate different colors into our layout from bitmap
                Method PaletteAsyncListener is used to load images continuously into our image items
                this improves performance of the images loading into layout
                As Palette only works with build version LOLLIPOP we should check for required API
                */
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onGenerated(@NonNull Palette palette) {
                        // This is calling method for fetching colors into layout
                        setColors(palette);
                    }
                });

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                // DO NOTHING
                // We are not adding any method for errorDrawable of images because app doesn't crash
                // and we leave background as white in default
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                // DO NOTHING
                // This shows preloaded image or background into view, we are not adding any view cause
                // we leave background as white in default
            }
        });
    }

    // Method which sets color for different views from palette
    private void setColors(Palette palette) {
        // swatch colors using getVibrantSwatch method other methods cold be DarkVibrantSwatch etc
        Palette.Swatch colorSwatch = palette.getVibrantSwatch();
        if (colorSwatch == null) {
            // DO NOTHING
            // If no color is fetched from palette default color is set by this method.
            // We are not adding any method here as we set backGround color as default white
            return;
        }

        /*
        getRBG will fetch color from colorSwatch variable from imageView
        R = Red
        G = Green
        B = Blue
        for case backgroundGroup with setBackgroundColor method it's layout background will be changed
        matching the layout imageView, similarly for each layout with different detail movie activity
        it's background will be changed based on imageView respectively.
         */
        backgroundGroup.setBackgroundColor(colorSwatch.getRgb());
        /*
        setTextColor will change the color of texView along with background as also mentioned
        setBackgroundColor matching it's imageView.
        In similar case it applies for all textViews mTitle, mPopularity, mVoteAverage, mReleaseDate
        and mLanguageTextView
         */
        mMovieTitleTextView.setTextColor(colorSwatch.getTitleTextColor());
        mMovieTitleTextView.setBackgroundColor(colorSwatch.getRgb());
        mMoviePopularityTextView.setTextColor(colorSwatch.getRgb());
        mMovieVoteAverageTextView.setTextColor(colorSwatch.getRgb());
        mMovieReleaseDateTextView.setTextColor(colorSwatch.getRgb());
        mMovieLanguageTextView.setTextColor(colorSwatch.getRgb());
        /*
        ImageViews can also be filled completely with matching backGround from getRBG for all layouts
         */
        mPopularityImageView.setColorFilter(colorSwatch.getRgb());
        mVotingAverageImageView.setColorFilter(colorSwatch.getRgb());
        mReleaseDateImageView.setColorFilter(colorSwatch.getRgb());
        mLanguageImageView.setColorFilter(colorSwatch.getRgb());
    }

}
