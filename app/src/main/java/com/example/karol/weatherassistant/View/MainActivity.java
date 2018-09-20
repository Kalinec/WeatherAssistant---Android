package com.example.karol.weatherassistant.View;

import android.drm.DrmStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.SectionsStatePagerAdapter;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout _drawerLayout;
    private SectionsStatePagerAdapter _sectionsStatePagerAdapter;
    private ViewPager _viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configuration menu button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        _drawerLayout = findViewById(R.id.drawer_layout);

        //handle navigation click events
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                item.setChecked(true);

                int id = item.getItemId();

                if(id == R.id.nav_WeatherForecast)
                    _viewPager.setCurrentItem(0);

                else if(id == R.id.nav_StormyMap)
                    _viewPager.setCurrentItem(1);

                else if(id == R.id.nav_StormSearch)
                    _viewPager.setCurrentItem(2);

                else if(id == R.id.nav_PlanTheTrip)
                    _viewPager.setCurrentItem(3);

                _drawerLayout.closeDrawers();

                //_viewPager.setCurrentItem(item.getItemId());

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here

                return true;
            }
        });


        //fragment pager
        _sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        _viewPager = (ViewPager) findViewById(R.id.container);

        //setup the pager
        setupViewPager(_viewPager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                _drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager)
    {
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WeatherForecast(), "WeatherForecast");
        adapter.addFragment(new StormyMap(), "StormyMap");
        adapter.addFragment(new StormSearch(), "StormySearch");
        adapter.addFragment(new PlanTheTrip(), "PlanTheTrip");
        viewPager.setAdapter(adapter);
    }

    public void setViewPager(int fragmentNumber)
    {
        _viewPager.setCurrentItem(fragmentNumber);
    }
}
