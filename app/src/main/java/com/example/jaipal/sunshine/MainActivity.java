package com.example.jaipal.sunshine;

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

            return rootView;
        }
    }
}
