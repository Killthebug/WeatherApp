package com.example.jaipal.sunshine;

import java.io.*;
import java.net.*;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * Place Holder fragment with basic view
     */

    public static class PlaceholderFragment extends Fragment {
        // The Array adapter parses the array and makes it displayable
        ArrayAdapter<String> mForecastAdapter;
        public PlaceholderFragment() {
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

            // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=500032&mode=json&units=metric&cnt=7");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
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
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                forecastJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return rootView;
        }
    }
}
