package com.example.rkrjstdio.popularmoviesbasic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    // Declaring a private variable movieList from List Of model class Movie
    private List<Movie> moviesList;

    // Setting listener for each item in adapter
    private final MovieAdapterListener listener;

    // Declaring this activity context
    private final Context context;

    /**
     * Cache of the children views for a movieData.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {

        // Private TextView variable to show title text
        private final TextView mTitleTextView;
        // Private ImageView variable to show poster for each items
        private final ImageView mPosterImageView;

        // MovieViewHolder which fro TexView and ImageView in layout
        MovieViewHolder(View view) {
            super(view);
            // Using findViewById to get reference from their views
            mTitleTextView = view.findViewById(R.id.title_main);
            mPosterImageView = view.findViewById(R.id.image_view_main);

            // OnClickListener for each item which opens into DetailActivity for current movie.
            view.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    // Using getAdapterPosition in-order to open current movie DetailActivity
                    listener.onMovieSelected(moviesList.get(getAdapterPosition()));
                }
            });
        }
    }

    /**
     * Creates a MovieAdapter.
     *
     * @param listener The on-click handler for this adapter. This single listener is called
     *                 when an item is clicked.
     * @param context  Context for the current activity
     */
    MovieAdapter(List<Movie> moviesList, MovieAdapterListener listener, Context context) {
        this.moviesList = moviesList;
        this.listener = listener;
        this.context = context;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterListener {
        void onMovieSelected(Movie movie);
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param parent   The parent that these ViewHolders are contained within.
     * @param viewType If your RecyclerView has more than one type of item (which ours doesn't) you
     *                 can use this viewType integer to provide a different layout.
     * @return A new MovieViewHolder that holds the View for each list item
     */
    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the movie
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at
     *                 the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.MovieViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        // gets the title textView of the movie from model Movie
        holder.mTitleTextView.setText(movie.getTitle());
        // Loading ImageView using picasso library
        Picasso.with(context).load(movie.getImageView()).placeholder(R.drawable.ic_background_loading_image_view).into(holder.mPosterImageView);
    }

    /**
     * This method is used to set the movie data on a MovieAdapter if we've already created one.
     * This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param movieData The new movie data to be displayed.
     */
    public void setMovieData(List<Movie> movieData) {
        moviesList = movieData;
        notifyDataSetChanged();
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our movieList
     */
    @Override
    public int getItemCount() {
        if (moviesList == null) {
            return 0;
        }
        return moviesList.size();
    }
}
