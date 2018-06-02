package com.example.rkrjstdio.popularmoviesbasic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterListener {

    // Declaring private variable for RecyclerView as mRecyclerView to use through out whole activity
    private RecyclerView mRecyclerView;

    // Initialize and declaring a new ArrayList from List Of model class Movie as movieList
    private List<Movie> movieList = new ArrayList<>();

    // Private variable for MovieAdapter class into our MainActivity
    private MovieAdapter mMovieAdapter;

    // Declaring private variable for TexView showing Network status.
    private TextView mNoInternetConnectionTextView;

    // Declaring private ImageView variable to show Network status
    private ImageView mNoInternetConnectionImageView;

    // Declaring private ProgressBar to improve user performance from layout
    private ProgressBar mLoadingIndicator;

    /*
    SCHEME_AUTHORITY = Encodes and sets the authority and scheme.
    APPEND_PATH_FIRST = Encodes the segment value 3 and appends it to the path
    APPEND_PATH_SECOND = Encodes the segment movie and appends it to the path
    POPULAR_MOVIE_PATH = When this path triggers it updates layout with popular movies from API
    TOP_RATED_MOVIE_PATH = When this path triggers it updates layout with top_rated movies from API
    API_KEY = This is unique key given to the user for access from their server.
     */
    private static final String SCHEME_AUTHORITY = "https://api.themoviedb.org";
    private static final String APPEND_PRIMARY_PATH = "3";
    private static final String APPEND_SECONDARY_PATH = "movie";
    private static final String POPULAR_MOVIE_PATH = "popular";
    private static final String TOP_RATED_MOVIE_PATH = "top_rated";
    private static final String API_PARAM = "api_key";
    private static final String API_KEY = "your_api_key_here";

    // This variable checks whether user changed existing order of movie
    private String mMovieCatalogue = "popular";

    /**
     * Builds the URL used to fetch movie data from the server. This data is based on the query
     * capabilities of the movie database provider that we are using.
     * API_KEY is used to query specific data from the server
     * @return The URL to use to query the movie database server.
     */
    private static String uriBuilder(String mAppendOrder) {

        // String variable mAppendPath triggers when user sorts by given options
        String mAppendPath;
        // If true, value for top_rated path is executed
        if (mAppendOrder.equals("top_rated")) {
            mAppendPath = TOP_RATED_MOVIE_PATH;
        } else {
            // In this condition, popular path is executed
            mAppendPath = POPULAR_MOVIE_PATH;
        }

        // Encodes and sets the authority.
        // Sets the scheme.
        Uri baseUri = Uri.parse(SCHEME_AUTHORITY);
        // Constructs a new Builder.
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder
                // Encodes the segment and appends it to the path.
                .appendPath(APPEND_PRIMARY_PATH)
                // Encodes the segment and appends it to the path.
                .appendPath(APPEND_SECONDARY_PATH)
                // Encodes the segment and appends it to the path.
                .appendPath(mAppendPath)
                // Encodes the key and value and then appends the parameter to the query string.
                .appendQueryParameter(API_PARAM, API_KEY);
        // Returns a string representation of the object.
        return uriBuilder.build().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the activity content from a layout resource views to the activity.
        setContentView(R.layout.activity_main);

        /*
         * Using findViewById, we get a reference to our RecyclerView from xml. This allows us to
         * do things like set the adapter of the RecyclerView and toggle the visibility.
         */
        mRecyclerView = findViewById(R.id.my_recycler_view);

        /*
         * The MovieAdapter is responsible for linking our movie data with the Views that
         * will end up displaying our movie data.
         */
        mMovieAdapter = new MovieAdapter(movieList, this, this);

        // Set count to 2, which displays 2 items for a row in recyclerView
        int count = 2;

        /*
         * GridLayoutManager can support HORIZONTAL or VERTICAL orientations. The reverse layout
         * parameter is useful mostly for HORIZONTAL layouts that should reverse for right to left
         * languages.
         */
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), count);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        // Setting the adapter attaches it to the RecyclerView in our layout.
        mRecyclerView.setAdapter(mMovieAdapter);

        // This TextView is used to display errors and will be hidden if there are no errors.
        mNoInternetConnectionTextView = findViewById(R.id.no_connection_message);
        mNoInternetConnectionTextView.setText(R.string.no_internet);

        /* This ImageView is used to display errors and will be hidden if there are no errors */
        mNoInternetConnectionImageView = findViewById(R.id.no_connection_image);

         /*
         * The ProgressBar that will indicate to the user that we are loading data. It will be
         * hidden when no data is loading.
         */
        mLoadingIndicator = findViewById(R.id.loading_indicator);

        // Load Movie data in onCreate of activity
        if (savedInstanceState == null || !savedInstanceState.containsKey("Movies")) {
            movieList = null;
            loadMovieData();
        } else {
            movieList = savedInstanceState.getParcelableArrayList("Movies");
            showMovieDataView();
            mMovieAdapter.setMovieData(movieList);
        }
    }

    /**
     * This method will get the movie data from user selected options, and then tell some
     * background method to get the movie data in the background.
     */
    private void loadMovieData() {
        // Shows the movie data and hides error TexView and ImageView
        showMovieDataView();
        // Triggers AsyncTask to perform background tasks
        FetchMovieTask task = new FetchMovieTask();
        // Executes task and builds data from uriBuilder
        task.execute(uriBuilder(mMovieCatalogue));
    }

    // For instance this method gets movie Data from parcelable arrayList from Movie class
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies", (ArrayList<? extends Parcelable>) movieList);
        super.onSaveInstanceState(outState);
    }

    // Creates options menu from layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater
        getMenuInflater().inflate(R.menu.filter_movies, menu);
        // Return true to display menu in toolbar
        return true;
    }

    // Method which performs activity based on user selected options based on their ID's
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Return each method using switch case and ID's
        switch (item.getItemId()){
            // For id equal to popular item, movie data is shown and error is hidden
            case R.id.most_popular_menu_item :
                mMovieAdapter.setMovieData(null);
                mMovieCatalogue = "popular";
                loadMovieData();
                break;
            // For id equal to top_rated items, recyclerView is visible and error is invisible
            case R.id.highest_rated_menu_item :
                mMovieAdapter.setMovieData(null);
                mMovieCatalogue = "top_rated";
                loadMovieData();
                break;
            // Refresh button will perform re-check for Network connectivity status
            case R.id.refresh_view :
                // Toast is shown after a successful load of data
                Toast.makeText(this, getString(R.string.toast_refresh), Toast.LENGTH_SHORT).show();
                mMovieAdapter.setMovieData(null);
                loadMovieData();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    // FetchMovieTask extends into AsyncTask to execute onPreExecute, doInBackground and onPostExecute
    @SuppressLint("StaticFieldLeak")
    class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        // This operation is first executed and display loading indicator on screen for user improve experience
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Loading indicator is shown by setting visibility to visible
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        //
        @Override
        protected List<Movie> doInBackground(String... urls) {

            // If there is no zip code, no need to perform any operations
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            // Fetch movie data from the server
            return QueryUtils.fetchMovieData(urls[0]);
        }

        // On finishing background operations execute data on layout
        @Override
        protected void onPostExecute(List<Movie> movieData) {
            // Loading indicator set to be invisible to show movie data
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                // If data is not null display user movie data and hide error
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }

    }

    // If true, recyclerView updates data and hides error textView and ImageView
    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mNoInternetConnectionTextView.setVisibility(View.INVISIBLE);
        mNoInternetConnectionImageView.setVisibility(View.INVISIBLE);
        /* Then, make sure the movie data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    // If true, shows the error and hides the recyclerView
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mNoInternetConnectionTextView.setVisibility(View.VISIBLE);
        mNoInternetConnectionImageView.setVisibility(View.VISIBLE);
    }

    // This passes intent to DetailActivity for each item selected from recyclerView in detail
    @Override
    public void onMovieSelected(Movie movie) {
        Intent intent = new Intent(this, DetailMovieActivity.class);
        intent.putExtra("Intent to Detail Activity", movie);
        // Starts the intent from this activity
        startActivity(intent);
    }
}
