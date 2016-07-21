package com.example.android.sunshine;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by vikashkumarbijarnia on 17/07/16.
 */
public class ForecastFragment extends Fragment{
    WeatherDataParser dataParser;
    SimpleAdapter mForecastAdapter;
    ArrayList<String> empetyArraylist;
    ListView list;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        context=getActivity();
        dataParser=new WeatherDataParser(context);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.forecast_fragment_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            FetchWeatherTask wetherInfo=new FetchWeatherTask();
            wetherInfo.execute("94043");
        }
        if(id==R.id.action_setting){
            Intent intent =new Intent(getActivity(),SettingsActivity.class);
            startActivity(intent);
        }



        return true;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_view, container, false);
        empetyArraylist=new ArrayList<String>();
        mForecastAdapter =
                new SimpleAdapter(
                        getActivity(), // The current context (this activity)
                        empetyArraylist);
        // Get a reference to the ListView, and attach this adapter to it.
        list=(ListView)rootView.findViewById(R.id.listView_forecast);
        list.setAdapter(mForecastAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str=(String) mForecastAdapter.getItem(i);
                Intent detailIntent=new Intent(getActivity(),DetailActivity.class);
                detailIntent.putExtra("info",str);
                startActivity(detailIntent);
            }
        });


        return rootView;
    }

    public void update(){
        FetchWeatherTask wetherInfo=new FetchWeatherTask();
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());
         String location =prefs.getString(getString(R.string.location_key),getString(R.string.location_default_value));
        wetherInfo.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        update();
    }

    public class FetchWeatherTask extends AsyncTask<String,Void,String[] > {
        String format = "json";
        String unit = "metric";
        String appid = "aa2b8133413c5ba1591f0676bfe36db1";
        int days = 7;


        @Override
        protected String[] doInBackground(String... strings) {
            final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNIT_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APP_ID = "APPID";

            Uri buildUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(QUERY_PARAM, strings[0])
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNIT_PARAM, unit)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(days))
                    .appendQueryParameter(APP_ID, appid)
                    .build();
            return getWeatherInfo(buildUri.toString());
        }

        @Override
        protected void onPostExecute(String[] strings) {

            if (strings != null) {
                mForecastAdapter.addAll(new ArrayList<String>(Arrays.asList(strings)));
                mForecastAdapter.notifyDataSetChanged();
            }


        }
    }
    private String[] getWeatherInfo(String urls) {
        // These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL(urls);

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
            return dataParser.getWeatherDataFromJson(forecastJsonStr, 7);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = null;
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
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
        return null;
    }
}

