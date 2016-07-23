package com.example.android.sunshine;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by vikashkumarbijarnia on 21/07/16.
 */
public class DetailActivity extends AppCompatActivity {
    Intent shareIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);

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
            Intent intent =new Intent(DetailActivity.this,SettingsActivity.class);
            startActivity(intent);
        }


        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static String  LOG_TAG=PlaceholderFragment.class.getSimpleName();

        private static String  HASHTAG_STRING=" #sunshineapp";

        private String mForecast;


        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.detailfragment,menu);

            MenuItem shareItem=menu.findItem(R.id.action_share);

            ShareActionProvider mShareActionProvider=(ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

            if(mShareActionProvider!= null){
                mShareActionProvider.setShareIntent(createShareIntent());
            }
            else{
                Log.e(LOG_TAG,"there is someproblem");
            }
        }

        public PlaceholderFragment() {
            setHasOptionsMenu(true);
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Intent intent=getActivity().getIntent();
            mForecast=(String)intent.getExtras().get("info");
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            TextView textDetail=(TextView) rootView.findViewById(R.id.detail_text_detail_fragment);
            textDetail.setText(mForecast);
            return rootView;
        }
        private Intent createShareIntent(){
           Intent  shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//get backs to the app where share intent is called.
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast+HASHTAG_STRING);
            return  shareIntent;

        }
    }
}
