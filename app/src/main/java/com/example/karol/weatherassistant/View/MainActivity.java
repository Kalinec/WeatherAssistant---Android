package com.example.karol.weatherassistant.View;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.karol.weatherassistant.R;
import com.example.karol.weatherassistant.SectionsStatePagerAdapter;
import com.example.karol.weatherassistant.Services.BurzeDzisService.IWsdl2CodeEvents;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeBurza;
import com.example.karol.weatherassistant.Services.BurzeDzisService.MyComplexTypeMiejscowosc;
import com.example.karol.weatherassistant.Services.BurzeDzisService.serwerSOAPService;
import com.example.karol.weatherassistant.Services.WeatherService;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout _drawerLayout;
    private SectionsStatePagerAdapter _sectionsStatePagerAdapter;
    private ViewPager _viewPager;
    public static SearchView SearchView;
    private serwerSOAPService _stormService;
    public IWsdl2CodeEvents _eventsHandler;
    private MyComplexTypeMiejscowosc _stormLocation;
    private MyComplexTypeBurza _stormInfo;
    private boolean _stormLocationAsyncFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _eventsHandler = new IWsdl2CodeEvents() {
            @Override
            public void Wsdl2CodeStartedRequest() {

            }

            @Override
            public void Wsdl2CodeFinished(String methodName, Object Data) {
                switch (methodName)
                {
                    case "KeyAPI":
                        break;
                    case "miejscowosc":
                        _stormLocation = (MyComplexTypeMiejscowosc)Data;
                        StormSearch.City.setText(SearchView.getQuery());
                        StormSearch.Latitude.setText(String.valueOf(_stormLocation.y));
                        StormSearch.Longitude.setText(String.valueOf(_stormLocation.x));
                        break;
                    case "ostrzezenia_pogodowe":
                        break;
                    case "szukaj_burzy":
                        _stormInfo = (MyComplexTypeBurza)Data;
                        StormSearch.Time.setText(String.valueOf(_stormInfo.okres) + " min");
                        if(_stormInfo.kierunek.isEmpty())
                            StormSearch.Direction.setText("-");
                        else
                        StormSearch.Direction.setText(String.valueOf(_stormInfo.kierunek));
                        StormSearch.Number.setText(String.valueOf(_stormInfo.liczba));
                        StormSearch.Distance.setText(String.valueOf(_stormInfo.odleglosc) + " km");
                        break;
                    case "miejscowosci_lista":
                        break;
                }

            }

            @Override
            public void Wsdl2CodeFinishedWithException(Exception ex) {

            }

            @Override
            public void Wsdl2CodeEndedRequest() {

            }
        };
        _stormService = new serwerSOAPService(_eventsHandler, "https://burze.dzis.net/soap.php");
        _stormLocation = new MyComplexTypeMiejscowosc();
        _stormInfo = new MyComplexTypeBurza();

        //configuration menu button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        _drawerLayout = findViewById(R.id.drawer_layout);
        SearchView = findViewById(R.id.searchCity);

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
                {
                    _viewPager.setCurrentItem(0);
                    SearchView.setVisibility(View.VISIBLE);
                }


                else if(id == R.id.nav_StormyMap)
                {
                    _viewPager.setCurrentItem(1);
                    SearchView.setVisibility(View.GONE);
                }


                else if(id == R.id.nav_StormSearch)
                {
                    _viewPager.setCurrentItem(2);
                    SearchView.setVisibility(View.VISIBLE);
                }


                else if(id == R.id.nav_PlanTheTrip)
                {
                    _viewPager.setCurrentItem(3);
                    SearchView.setVisibility(View.GONE);
                }


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

        _viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0 || position == 2)
                    SearchView.setVisibility(View.VISIBLE);
                else
                    SearchView.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //setup the pager
        setupViewPager(_viewPager);


        SearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                switch (_viewPager.getCurrentItem())
                {
                    case 0:
                        WeatherService.getInstance().getCurrentWeatherByCityName(query);
                        break;
                    case 2:
                        try
                        {
                           _stormService.miejscowoscAsync(query,"3f04fbcac562e34c59d03cc166dc532a9451ded3");
                           //_stormService.szukaj_burzyAsync(String.valueOf(_stormLocation.y), String.valueOf(_stormLocation.x), 300, "3f04fbcac562e34c59d03cc166dc532a9451ded3");
                           // _stormService.szukaj_burzyAsync("40.28", "17,14", 300, "3f04fbcac562e34c59d03cc166dc532a9451ded3");
                        }
                        catch (Exception e)
                        {
                            Log.e(TAG, "Explanation of what was being attempted", e);
                        }
                        break;
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

}
