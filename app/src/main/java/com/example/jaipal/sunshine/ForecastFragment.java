package com.example.jaipal.sunshine;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.net.Uri;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jaipal on 14/9/15.
 */
public class ForecastFragment extends Fragment {
    // The Array adapter parses the array and makes it displayable
    ArrayAdapter<String> mForecastAdapter;
    public ForecastFragment() {
    }

    // This is extremely important
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // This indicates that we have call backs for these methods
        // Make sure you have this line of code.
    }

    // This is the most basic method to inflate a view
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Pushes to the forecastfragment.xml file
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar clicks here
        // Specify a parent activity to re route route to home
        // This notifies us when the Menu item is selected
        int id = item.getItemId();

        if (id == R.id.action_refresh){
            // if Refresh is places call the FetchWeatherTask
            FetchWeatherTask weatherTask = new FetchWeatherTask();
            weatherTask.execute("500032");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //rootView here defines the base view for this given method
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Populate the table with some dummy data

        String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Wednesday - Cloudy - 72/63",
                "Thursday - Asteroids - 75/65",
                "Friday - Heave Rain - 65/56",
                "Saturday - Extremely Heavy - 60/51",
                "Sundya - Sunny - 80/68"
        };

        // This is where the data for the adapter comes from

        List<String> weekForecast = new ArrayList<String>(
                Arrays.asList(forecastArray)
        );

        // Initialize the adapter to read the data from the above given string
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(),

                        R.layout.list_item_forecast,
                            /*
                            This searches the tree for a layout
                            with the the name list_item_forecast
                             */

                        R.id.list_item_forecast_textview,
                            /*
                            This searches the tree for a layout
                            with an id list_item_forecast_textview
                             */

                        weekForecast
                        // Here we define what data we need to use
                );

        //Getting a reference to the ListView
        // Rootview here refers to the rootView of the fragment as defined above

            /*
            Now, we ask the adapter to generate the
            data and link it to our rootView
             */
        ListView listView = (ListView) rootView.findViewById(
                R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }
                                                    // The input is of type : String
    public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        // This method should take in String params
        protected Void doInBackground(String ... params) {
            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 7;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "node";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";

                /*
                The one's in captial letters are constants
                incase we don't have their values available
                to pass as parameters
                 */

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Build URL" + builtUri.toString());

                // Create the request to OpenWeatherM ap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecasted JSON" + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
