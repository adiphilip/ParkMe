package com.example.myapp.parkme;


import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static com.example.myapp.parkme.R.layout.fragment_home;

public class HomeActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View rootView;
    private SharedPreferences setting;
    private DataBaseService dbh;
    private LocationService locService;
    public String driverName, carId, carDescription, location = "0,0", parkName ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        //upgrade
        //new DataBaseHelper(this).onUpgrade(new DataBaseHelper(this).getWritableDatabase(),1,2);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        rootView = findViewById(android.R.id.content);
        dbh = new DataBaseService(this);
        locService = new LocationService(this);
        setting = getSharedPreferences(Helper.PREFS_NAME, MODE_PRIVATE);
        driverName = setting.getString(Helper.PreferencesNames.DRIVER_NAME, "Yossi Choen");
        carId = setting.getString(Helper.PreferencesNames.CAR_ID, "11-222-33");
        carDescription = setting.getString(Helper.PreferencesNames.DESCRIPTION, "my Honda");

        MediaPlayer.create(this ,R.raw.car_sound).start();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setLastPark();
                }
                if (position == 1) {
                    setSaveFragment(rootView);
                }
                if (position == 2) {
                    setListHistoryByDriverName(rootView);
                }
                if (position == 3) {
                    setMapFragment();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
        mViewPager.setCurrentItem(0,true);
    }

    @Override
    public void onPause(){
        super.onPause();
        try{
            if(mViewPager.getCurrentItem() == 1){
                savePark(rootView);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
            case R.id.action_home:
                mViewPager.setCurrentItem(0,true);
                return true;
            case R.id.action_save:
                mViewPager.setCurrentItem(1, true);
                return true;
            case R.id.action_history:
                mViewPager.setCurrentItem(2, true);
                return true;
            case R.id.action_map:
                mViewPager.setCurrentItem(3,true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setListHistoryByCarId(View view){
        DataBaseService dbh = new DataBaseService(this);
        ListView l = (ListView)rootView.findViewById(R.id.history_list);
        ArrayList values = dbh.getParksByCarId(carId);
        ParkListAdapter adapter = new ParkListAdapter(this, values);
        l.setAdapter(adapter);

        findViewById(R.id.history_driver_button).setBackgroundColor(getResources().getColor(R.color.base_light_yellow));
        findViewById(R.id.history_car_button).setBackgroundColor(getResources().getColor(R.color.base_green));
    }
    public void setListHistoryByDriverName(View view){
        DataBaseService dbh = new DataBaseService(this);
        ListView l = (ListView)rootView.findViewById(R.id.history_list);
        ArrayList values = dbh.getParksByDriverName(driverName);
        ParkListAdapter adapter = new ParkListAdapter(this, values);
        l.setAdapter(adapter);

        findViewById(R.id.history_driver_button).setBackgroundColor(getResources().getColor(R.color.base_green));
        findViewById(R.id.history_car_button).setBackgroundColor(getResources().getColor(R.color.base_light_yellow));
    }

    public boolean savePark(View view){
        parkName = ((TextView)findViewById(R.id.save_park_name_edit)).getText().toString();
        boolean b = dbh.savePark(carId, carDescription, driverName, location, parkName, new Timestamp(new Date().getTime()).toString());
        if(b == true){
            Toast.makeText(view.getContext(), getString(R.string.save_toast), Toast.LENGTH_SHORT).show();
        }
        return  b;
    }
    public void deleteList(View view){
        dbh.clearDb();
        dbh = new DataBaseService(this);
        setListHistoryByDriverName(view);
    }

    public void setLastPark(){
        JSONObject data = dbh.getLastPark();
        String strUri = "";
        try {
            ((TextView)findViewById(R.id.home_driver_name)).setText(data.getString(Helper.TablePark.COLUMN_NAME_DRIVER_NAME));
            ((TextView) findViewById(R.id.home_carId)).setText(data.getString(Helper.TablePark.COLUMN_NAME_CAR_ID));
            ((TextView) findViewById(R.id.home_description)).setText(data.getString(Helper.TablePark.COLUMN_NAME_DESCRIPTION));
            ((TextView) findViewById(R.id.home_park_location)).setText(data.getString(Helper.TablePark.COLUMN_NAME_PARK_NAME));
            ((TextView) findViewById(R.id.home_park_time)).setText(data.getString(Helper.TablePark.COLUMN_NAME_TIMESTAMP));
            strUri = "https://www.google.com/maps/search/" + data.getString(Helper.TablePark.COLUMN_NAME_PARK_LOC);

        } catch (Exception e) {
            e.printStackTrace();
        }
        WebView webView=(WebView)findViewById(R.id.home_webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings wSettings=webView.getSettings();
        wSettings.setJavaScriptEnabled(true);
        webView.loadUrl(strUri);
    }

    public void setMapFragment(){
        location = locService.getLocation();
        WebView webView=(WebView)findViewById(R.id.map_webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);
        String strUri = "https://www.google.com/maps/search/parking/@" + location + ",14z";
        webView.loadUrl(strUri);
    }

    public void setSaveFragment(View view){
        location = locService.getLocation();
        WebView webView=(WebView)findViewById(R.id.save_webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings wSettings=webView.getSettings();
        wSettings.setJavaScriptEnabled(true);
        String strUri="https://www.google.com/maps/place/" + location;
        webView.loadUrl(strUri);
    }




















    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(fragment_home, container, false);
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (position) {
                case 0:
                    View v = inflater.inflate(R.layout.fragment_home,container, false);
                    setLastPark(v);
                    return v;

                case 1:
                    return inflater.inflate(R.layout.fragment_save,container, false);
                case 2:
                    return inflater.inflate(R.layout.fragment_history,container, false);
                case 3:
                    return inflater.inflate(R.layout.fragment_map,container, false);

            }
            return rootView;
        }
        public void setLastPark(View view){
            DataBaseService dbService = new DataBaseService(getContext());
            JSONObject data = dbService.getLastPark();
            String strUri = "";
            try {
                ((TextView)view.findViewById(R.id.home_driver_name)).setText(data.getString(Helper.TablePark.COLUMN_NAME_DRIVER_NAME));
                ((TextView)view.findViewById(R.id.home_carId)).setText(data.getString(Helper.TablePark.COLUMN_NAME_CAR_ID));
                ((TextView)view.findViewById(R.id.home_description)).setText(data.getString(Helper.TablePark.COLUMN_NAME_DESCRIPTION));
                ((TextView)view.findViewById(R.id.home_park_location)).setText(data.getString(Helper.TablePark.COLUMN_NAME_PARK_NAME));
                ((TextView)view.findViewById(R.id.home_park_time)).setText(data.getString(Helper.TablePark.COLUMN_NAME_TIMESTAMP));
                strUri = "https://www.google.com/maps/search/" + data.getString(Helper.TablePark.COLUMN_NAME_PARK_LOC);

            } catch (Exception e) {
                e.printStackTrace();
            }
            WebView webView=(WebView)view.findViewById(R.id.home_webView);
            webView.setWebViewClient(new WebViewClient());
            WebSettings wSettings=webView.getSettings();
            wSettings.setJavaScriptEnabled(true);
            webView.loadUrl(strUri);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOME";
                case 1:
                    return "SAVE";
                case 2:
                    return "HISTORY";
                case 3:
                    return "MAP";
            }
            return null;
        }

    }
}
