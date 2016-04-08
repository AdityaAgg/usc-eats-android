package com.sparksc.usceats;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.sparksc.usceats.utils.CalendarUtils;
import com.sparksc.usceats.utils.DiningHallUtils;
import com.sparksc.usceats.utils.NetworkingUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // region Variables

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.toolbar_spinner) Spinner toolbarSpinner;
    @Bind(R.id.tab_layout) TabLayout tabLayout;
    @Bind(R.id.view_pager) ViewPager viewPager;

    SectionPagerAdapter sectionPagerAdapter;
    boolean isWeekend = false;
    FeastforAndroid feastforAndroid;
    public static int day_x=-1;
    public static int month_x=-1;
    public static int year_x=-1;

    DiningHallFragment fragment;
    // endregion

    // region Fundamentals

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        if(year_x==-1 || month_x==-1 || day_x==-1) {
            year_x = CalendarUtils.getYear();
            month_x = CalendarUtils.getMonth();
            day_x = CalendarUtils.getDay()+1;
        }

        feastforAndroid= FeastforAndroid.getInstance(this);
        if(NetworkingUtils.isNetworkAvailable(this))
            feastforAndroid.setMainActivity(this);
        else
            populateUI();



    }
    public void populateUI(){
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //databaseManager.refreshRestaurants();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.showOverflowMenu();
        }

        if (DiningHallUtils.isWeekend()) {
            isWeekend = true;
        }


        setupSpinner();
        setupTabs();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calendar:
                showDialog(0);

                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id==0){
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x-1);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            year_x=year;
            month_x=monthOfYear;
            day_x=dayOfMonth+1;
            String name = getFragmentTag(viewPager.getId(), viewPager.getCurrentItem());
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
            ((DiningHallFragment)fragment).setupRecyclerView();


        }
        private String getFragmentTag(int viewPagerId, int fragmentPosition)
        {
            return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
        }
    };


    // endregion

    // region Setup

    private void setupSpinner() {
        // Use different dropdown toolbar items depending on if it's the weekend or the weekday
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toolbarSpinner.setAdapter(spinnerAdapter);
        if (isWeekend) {
            spinnerAdapter.addAll(getResources().getStringArray(R.array.toolbar_spinner_weekend_items));
        } else {
            spinnerAdapter.addAll(getResources().getStringArray(R.array.toolbar_spinner_weekday_items));
        }
        spinnerAdapter.notifyDataSetChanged();

        toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // tell each fragment to refresh data
                // if it's the weekend, we are using Brunch and Dinner menus
                if (isWeekend) {
                    switch (position) {
                        case 0:
                            tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime.BRUNCH);
                            break;
                        case 1:
                            tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime.DINNER);
                            break;
                        default:
                            tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime.DINNER);
                    }
                } else {
                    switch (position) {
                        case 0:
                            tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime.BREAKFAST);
                            break;
                        case 1:
                            tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime.LUNCH);
                            break;
                        case 2:
                            tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime.DINNER);
                            break;
                        default:
                            tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime.DINNER);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }

            private void tellFragmentsNewMealTimeSelection(DiningHallUtils.MealTime mealTime) {
                for(int i = 0; i < sectionPagerAdapter.getCount(); i++) {
                    String name = getFragmentTag(viewPager.getId(), i);
                    Fragment viewPagerFragment = getSupportFragmentManager().findFragmentByTag(name);
                    if(viewPagerFragment != null) {
                        ((DiningHallFragment) viewPagerFragment).onMealTimeChanged(mealTime);
                    }
                }
            }

            private String getFragmentTag(int viewPagerId, int fragmentPosition)
            {
                return "android:switcher:" + viewPagerId + ":" + fragmentPosition;
            }
        });
    }

    private void setupTabs() {
        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    // endregion

    // region Inner Classes

    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            DiningHallFragment fragment = new DiningHallFragment();
            args.putString("Dining Hall ID",feastforAndroid.getRestaurants().get(position).getId());
            // sets the current meal time, so each fragment can show correct info accordingly
            args.putInt(DiningHallUtils.MEAL_TIME,
                    DiningHallUtils.getCurrentMealTime().ordinal());

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return feastforAndroid.getRestaurants().size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return feastforAndroid.getRestaurants().get(position).getName();
        }
    }

    // endregion
}