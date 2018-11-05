package com.example.karol.weatherassistant.View;


import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
// classes needed to initialize map
import com.example.karol.weatherassistant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.geocoder.MapboxGeocoder;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
// classes needed to add location layer
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import android.location.Location;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.mapbox.mapboxsdk.geometry.LatLng;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
// classes needed to add a marker
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
// classes to calculate a route
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;
// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.ui.geocoder.GeocoderAutoCompleteView;
import com.mapbox.services.api.geocoding.v5.models.CarmenFeature;

public class PlanTheTrip extends Fragment implements OnMapReadyCallback, MapboxMap.OnMapClickListener, LocationEngineListener, PermissionsListener {
    private MapView mapView;
    // variables for adding location layer
    private MapboxMap mapboxMap;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationLayerPlugin;
    //private LocationEngine locationEngine;
    private Location originLocation;
    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;
    // variables for calculating and drawing a route
    private Point originPoint;
    private Point destinationPoint;
    private Position originPosition;
    private Position destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private ImageButton _imageButton;
    private CardView _routeCardView;
    private Button _showRouteButton;
    private RadioButton _criteriaWalkingMode;
    private RadioButton _criteriaCyclingMode;
    private MapboxGeocoder _mapboxGeocoder;
    private GeocoderAutoCompleteView autoCompleteOrigin;
    private GeocoderAutoCompleteView autoCompleteDestination;
    private Button _startNavigateButton;

    //Route details
    private CardView _routeDetails;
    private TextView _routeTime;
    private TextView _routeDistance;

    private MapboxDirections client;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_the_trip, container, false);
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        mapView = view.findViewById(R.id.mapView);
        _imageButton = (ImageButton) view.findViewById(R.id.imageButton_route);
        _routeCardView = (CardView) view.findViewById(R.id.route_options_cardView);
       // _routeOrigin = (EditText) view.findViewById(R.id.originValue);
       // _routeDestination = (EditText) view.findViewById(R.id.destinationValue);
        _showRouteButton = (Button) view.findViewById(R.id.button_show_route);
        _criteriaWalkingMode = view.findViewById(R.id.radioButton_planTheTrip_walk_mode);
        _criteriaCyclingMode = view.findViewById(R.id.radioButton_planTheTrip_bike_mode);
        autoCompleteOrigin = (GeocoderAutoCompleteView) view.findViewById(R.id.origin);
        autoCompleteDestination = view.findViewById(R.id.destination);
        _routeDetails = view.findViewById(R.id.cardView_planTheTrip_route_details);
        _routeTime = view.findViewById(R.id.textView_planTheTrip_time);
        _routeDistance = view.findViewById(R.id.textView_planTheTrip_distance);
        _startNavigateButton = view.findViewById(R.id.button_planTheTrip_start_navigate);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        autoCompleteOrigin.setAccessToken(Mapbox.getAccessToken());
        autoCompleteOrigin.setType(GeocodingCriteria.TYPE_PLACE);
        autoCompleteOrigin.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
            @Override
            public void onFeatureClick(CarmenFeature feature) {
                originPosition = feature.asPosition();
                //updateMap(position.getLatitude(), position.getLongitude());
            }
        });

        autoCompleteDestination.setAccessToken(Mapbox.getAccessToken());
        autoCompleteDestination.setType(GeocodingCriteria.TYPE_PLACE);
        autoCompleteDestination.setOnFeatureListener(new GeocoderAutoCompleteView.OnFeatureListener() {
            @Override
            public void onFeatureClick(CarmenFeature feature) {
                destinationPosition = feature.asPosition();

            }
        });

        _imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(_routeCardView.getVisibility() == View.GONE)
                    _routeCardView.setVisibility(View.VISIBLE);
                else if(_routeCardView.getVisibility() == View.VISIBLE)
                    _routeCardView.setVisibility(View.GONE);
            }
        });

        _showRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //delete any markers
                if (destinationMarker != null)
                    mapboxMap.removeMarker(destinationMarker);
                //destinationPoint = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
                //originPoint = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
               if(originPosition.getLatitude() != 0.00 && originPosition.getLongitude() != 0.00 && destinationPosition.getLatitude() != 0.00 && destinationPosition.getLongitude() != 0.00)
               {
                   originPoint = Point.fromLngLat(originPosition.getLongitude(),originPosition.getLatitude());
                   destinationPoint = Point.fromLngLat(destinationPosition.getLongitude(),destinationPosition.getLatitude());

                   try
                   {
                       getRoute(originPoint,destinationPoint);


                   }
                   catch (Exception e)
                   {
                       Log.e(TAG,"getRoute exception");
                   }
               }

               _routeCardView.setVisibility(View.GONE);
               MainActivity.hideKeyboardFrom(getContext(),getView());
            }
        });

        return view;
    };

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        enableLocationPlugin();
        originCoord = new LatLng(51.14, 22.34);
        // Toast.makeText(getActivity(), "Latitude: " + originLocation.getLatitude() + "  Longitude: " + originLocation.getLongitude(), Toast.LENGTH_SHORT).show();

        mapboxMap.addOnMapClickListener(this);

        _startNavigateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean simulateRoute = true;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(simulateRoute)
                        .build();
                // Call this method with Context from within an Activity
                NavigationLauncher.startNavigation(getActivity(), options);
            }
        });

    }

    @Override
    public void onMapClick(@NonNull LatLng point){
        if (destinationMarker != null) {
            mapboxMap.removeMarker(destinationMarker);
        }
        destinationCoord = point;
        destinationMarker = mapboxMap.addMarker(new MarkerOptions()
                .position(destinationCoord)
        );
        destinationPoint = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
        originPoint = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
        getRoute(originPoint, destinationPoint);
        //button.setEnabled(true);
    }

    private void getRoute(Point origin, Point destination) {
        String routeProfile = setRouteProfile();

        NavigationRoute.builder(getActivity())
                .accessToken(Mapbox.getAccessToken())
                .profile(routeProfile)
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);

                        //update UI - time and distance
                        _routeCardView.setVisibility(View.GONE);
                        _routeTime.setText(durationToDisplay(currentRoute.duration()));
                        _routeDistance.setText(distanceToDisplay(currentRoute.distance()));
                        _routeDetails.setVisibility(View.VISIBLE);


                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });

       /* CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(origin.latitude(), origin.longitude()))
                .target(new LatLng(destination.latitude(), destination.longitude()))
                .zoom(15)
                .build();
        mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 5000, null); */

        LatLngBounds latLngBounds = new LatLngBounds.Builder()
                .include(new LatLng(origin.latitude(), origin.longitude()))
                .include(new LatLng(destination.latitude(), destination.longitude()))
                .build();

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 20), 5000, null);
    }

    private String distanceToDisplay(Double distance)
    {

        Double toDisplay;
        String metric;

        if(distance < 1000)
        {
            toDisplay = distance;
            metric = getString(R.string.PlanTheTrip_m);
        }
        else
        {
            toDisplay = distance / 1000;
            metric = getString(R.string.PlanTheTrip_km);
        }

        StringBuilder distanceToDisplay = new StringBuilder();
        distanceToDisplay.append("(");
        distanceToDisplay.append(toDisplay.intValue());
        distanceToDisplay.append(" ");
        distanceToDisplay.append(metric);
        distanceToDisplay.append(")");

        return distanceToDisplay.toString();
    }

    @NonNull
    private String durationToDisplay(Double duration)
    {
        int hour = (int)(duration / 3600);
        int minutes = (int)((duration / 60) % 60);

        StringBuilder timeToDisplay = new StringBuilder();

        if(hour != 0)
        {
            timeToDisplay.append(hour);
            timeToDisplay.append(" ");
            timeToDisplay.append(getString(R.string.PlanTheTrip_hour));
            timeToDisplay.append(" ");
        }
        timeToDisplay.append(minutes);
        timeToDisplay.append(" ");
        timeToDisplay.append(getString(R.string.PlanTheTrip_min));

        return timeToDisplay.toString();
    }

    private String setRouteProfile()
    {
        if(_criteriaCyclingMode.isChecked())
            return DirectionsCriteria.PROFILE_CYCLING;
        else
            return DirectionsCriteria.PROFILE_WALKING;
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            initializeLocationEngine();
            // Create an instance of the plugin. Adding in LocationLayerOptions is also an optional
            // parameter
            //LocationLayerPlugin locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap);
            locationLayerPlugin = new LocationLayerPlugin(mapView, mapboxMap);

            // Set the plugin's camera mode
            locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
            getLifecycle().addObserver(locationLayerPlugin);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void initializeLocationEngine() {
       // LocationEngineProvider locationEngineProvider = new LocationEngineProvider(getContext());
       /// locationEngine = locationEngineProvider.obtainBestLocationEngineAvailable();
       // locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
       // locationEngine.activate();

        //Location lastLocation = locationEngine.getLastLocation();
//        if (lastLocation != null) {
        //    originLocation = lastLocation;
       // } else {
            //locationEngine.addLocationEngineListener(this);

           // Location location = new Location("");
            MainActivity._fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                   originCoord.setLatitude(task.getResult().getLatitude());
                   originCoord.setLongitude(task.getResult().getLongitude());
                }
            });
        }
   // }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            Toast.makeText(getContext(), R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        originLocation = location;
    }
}
