package com.example.rkrjstdio.popularmoviesbasic;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Url to get posterPath to display ImageView stored in string variable
    private static final String URL_POSTER_PATH = "http://image.tmdb.org/t/p/w500";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Movie> extractMovieDataFromJson(String newJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding new to
        List<Movie> movieData = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newJSON);
            // Extract the JSONArray associated with the key called "results",
            // which represents a list of features (or new).
            JSONArray newArray = baseJsonResponse.getJSONArray("results");

            for (int i = 0; i < newArray.length(); i++) {

                // Create a JSONObject from the JSON response string
                JSONObject jsonObject = newArray.getJSONObject(i);

                // Extract the value for the key called "original_title"
                String title = null;
                if (jsonObject.has("original_title")) {
                    title = jsonObject.getString("original_title");
                }

                // Extract the value for the key called "vote_average"
                String voteAverage = null;
                if (jsonObject.has("vote_average")) {
                    voteAverage = jsonObject.getString("vote_average");
                }

                // Extract the value for the key called "backdrop_path"
                String posterPath = null;
                if (jsonObject.has("backdrop_path")) {
                    posterPath = jsonObject.getString("backdrop_path");
                }

                // Extract the value for the key called "overview"
                String overView = null;
                if (jsonObject.has("overview")) {
                    overView = jsonObject.getString("overview");
                }

                // Extract the value for the key called "popularity"
                String popularity = null;
                if (jsonObject.has("popularity")) {
                    popularity = jsonObject.getString("popularity");
                }

                // Extract the value for the key called "release_date"
                String releaseDate = null;
                if (jsonObject.has("release_date")) {
                    releaseDate = jsonObject.getString("release_date");
                }

                // Extract the value for the key called "original_language"
                String language = null;
                if (jsonObject.has("original_language")) {
                    language = jsonObject.getString("original_language");
                }

                // Appending posterPath value to URL_POSTER_PATH to fetch ImageView correctly
                String imagePosterPath = URL_POSTER_PATH + posterPath;

                // Create a new {@link Movie} object with the title, voteAverage, popularity,
                // imagePosterPath, overView, releaseDate, language and url from the JSON response.
                Movie newFinal = new Movie(title, voteAverage, popularity, imagePosterPath, overView, releaseDate, language);

                // Add the new {@link Movie} to the list of movies.
                movieData.add(newFinal);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the new JSON results", e);
        }
        // Return the list of Movies
        return movieData;
    }

    /**
     * Query the API dataSet and return a list of {@link Movie} objects.
     */
    static List<Movie> fetchMovieData(String requestUrl) {

        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        java.net.URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Return the list of {@link Movie}s
        return extractMovieDataFromJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the new JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
