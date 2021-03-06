package com.example.eventwithus.fragments;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;


import static com.parse.Parse.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.load.engine.Initializable;
import com.example.eventwithus.EventDetailActivity;
import com.example.eventwithus.EventMarker;
import com.example.eventwithus.GetLocation;
import com.example.eventwithus.MainActivity;
import com.example.eventwithus.R;
import com.example.eventwithus.RoundedTransformation;
import com.example.eventwithus.RsvpTag;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.parse.Parse;
import com.parse.ParseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class MapViewFragment extends Fragment implements OnMapReadyCallback, Initializable , GoogleMap.OnMarkerClickListener {
    public static final String MapViewKey = "key";
    MapView mMapView;
    private GoogleMap googleMap;
    private Marker marker;
    int pos = 0;
    private Marker myTestMarker;
    Button btnButton;
    Button leftBTNm ,rightBTN;
    int favorited = 0;
    RsvpTag rsvpTagObject;
    List<Marker> markers = new ArrayList<>();;
    LocationManager locationManager;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    ArrayList<EventMarker> eventMarkers2 = new ArrayList<>();
    //ArrayList<EventMarker> eventMarkers3 = new ArrayList<>();
    ArrayList<RsvpTag> rsvpTags2 = new ArrayList<>();
    Set<LatLng> set = new HashSet<>();
    LatLng myCoordinates;

    ParseUser currentUser = ParseUser.getCurrentUser();
    String city = currentUser.getString("city");

    //


    public interface MapViewFragmentSent {
        void onInputMapSent(String s);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment, container, false);
         FusedLocationProviderClient mFusedLocationClient;
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        btnButton = rootView.findViewById(R.id.btnCurrentCity);
        leftBTNm = rootView.findViewById(R.id.leftBTN);
        rightBTN = rootView.findViewById(R.id.rightBTN);
        mMapView.onResume(); // needed to get the map to display immediately
        if (getArguments() != null) {
            ArrayList<EventMarker> eventMarkers3 = (ArrayList<EventMarker>) getArguments().getSerializable(MapViewKey);
        }
        int count = 0;
        Log.i("onCreateView initiated ", String.valueOf(count++));




        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        leftBTNm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "markers size :"+markers.size());
                Log.i(TAG, "set size :"+set.size());
                int n = set.size();
                List<LatLng> aList = new ArrayList<LatLng>(n);
                for (LatLng x : set)
                    aList.add(x);

                if(pos > 0) {
                   --pos;
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(aList.get(pos)).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    Log.i(TAG, "pos :" + pos);
                }
               // CameraPosition cameraPosition = new CameraPosition.Builder().target(myTestMarker.getPosition()).zoom(12).build();
              //  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        rightBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Log.i(TAG, "markers size :"+markers.size());
                    Log.i(TAG, "set size :"+set.size());

                int n = set.size();
                List<LatLng> aList = new ArrayList<LatLng>(n);
                for (LatLng x : set)
                    aList.add(x);
                Log.i(TAG, "current pos before:" + pos);
            if(pos < set.size()-1) {
                pos++;
                CameraPosition cameraPosition = new CameraPosition.Builder().target(aList.get(pos)).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Log.i(TAG, "current pos :" + pos);
                 }
            }
        });

        btnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.d("mylog", "Not granted");
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else
                        requestLocation();
                } else
                    requestLocation();
            }
        });
// Getting reference to the SupportMapFragment of activity_main.xml

        mMapView.getMapAsync(new OnMapReadyCallback() {



            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                mMap.setOnMarkerClickListener(MapViewFragment.this);
                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                Log.i("getMapAsync geteventMarkers2 : ", getEventMarkers2().toString());
                //Log.i("getMapAsync geteventMarkers3 : ", eventMarkers3.toString());



                if(eventMarkers2 != null) {
                    markers = new ArrayList<>();
                    for (int i = 0; i < eventMarkers2.size(); i++) {
                        String NAME = eventMarkers2.get(i).getEventName();
                        String VENUE_NAME = eventMarkers2.get(i).getVenueName();
                        String VENUE_IMG_URL = eventMarkers2.get(i).getVenueURL();
                        double LONG = Double.parseDouble(eventMarkers2.get(i).getLongitude());
                        double LAT = Double.parseDouble(eventMarkers2.get(i).getLatitude());
                        rsvpTagObject = rsvpTags2.get(i);
                        LatLng marker = new LatLng(LAT, LONG);
                       myTestMarker =  googleMap.addMarker(new MarkerOptions().position(marker).title(NAME).snippet(VENUE_NAME));
                      // myTestMarker.setTag(VENUE_IMG_URL);
                        myTestMarker.setTag(rsvpTagObject);
                        markers.add(myTestMarker);
                        set.add(markers.get(i).getPosition());
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    }
                }else {
                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(-34, 151);
                    // LatLng sydney = new LatLng(29.392456469494878, -98.7045790417612);
                    googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }


            }
        });



        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location mCurrentLocation = locationResult.getLastLocation();
                 myCoordinates = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                String cityName = getCityName(myCoordinates);
                Toast.makeText(getContext(), cityName, Toast.LENGTH_SHORT).show();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myCoordinates, 13.0f));
                if (marker == null) {
                    marker = googleMap.addMarker(new MarkerOptions().position(myCoordinates));
                    marker.setTag(myCoordinates);//<---------------------------------------------------------------SETTING TAG HERE*******************
                    Log.i("marker TAG: ", marker.getTag().toString());
                } else
                    marker.setPosition(myCoordinates);
            }
        };
        return rootView;
    }
    private String getCityName(LatLng myCoordinates) {
        String myCity = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
         //  List<Address> addresses = geocoder.getFromLocation(myCoordinates.latitude, myCoordinates.longitude, 1);
          List<Address> addresses = geocoder.getFromLocationName("San Antonio", 1);
            String address = addresses.get(0).getAddressLine(0);
            myCity = addresses.get(0).getLocality();
            Log.d("mylog", "Complete Address: " + addresses.toString());
            Log.d("mylog", "Address: " + address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCity;
    }


    @SuppressWarnings("deprecation")
    void showAlertDialog(final LatLng markerPosition, Marker marker) {
        String imageUrl;
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .create();
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View view = factory.inflate(R.layout.windowlayout, null);
        Button favBTN = view.findViewById(R.id.button2);
        TextView markerSnip = view.findViewById(R.id.markerSnippet);
        TextView markerName = view.findViewById(R.id.markerName);
        ImageView venueIV = view.findViewById(R.id.venueImageView);
        if(marker.getTag() instanceof RsvpTag){
            RsvpTag yourMarkerTag = ((RsvpTag)marker.getTag());
           imageUrl= yourMarkerTag.getImageUrl();
            Picasso.with(getContext()).load(imageUrl).into(venueIV);
            //Toast.makeText(getActivity, yourMarkerTag.getEmail() , Toast.LENGTH_SHORT).show();
           // Toast.makeText(getActivity, yourMarkerTag.getPhoneNumber() , Toast.LENGTH_SHORT).show();
        }
      //  String imageUrl = marker.getTag().toString();
       //= marker.getTag().;

       // Picasso.with(getContext()).load(imageUrl).into(venueIV);
        EventDetailActivity obj = new EventDetailActivity();



        markerName.setText(marker.getTitle());
        markerSnip.setText(marker.getSnippet());
        favBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(favorited == 1){
                    Toast.makeText(getApplicationContext(),
                            "UnFavorited", Toast.LENGTH_SHORT).show();
                    if(marker.getTag() instanceof RsvpTag){
                        RsvpTag yourMarkerTag = ((RsvpTag)marker.getTag());
                        obj.cancelRSVP2(yourMarkerTag.getEventId());

                    }
                            favBTN.setBackgroundColor(Color.BLUE);
                            favorited--;
//rsvpEvent(String eventId, String date, String eventDescription, String eventName, String venueName, String imageUrl, String startTime, String venueCity)
                }else if(favorited == 0){
                    if(marker.getTag() instanceof RsvpTag){
                        RsvpTag yourMarkerTag = ((RsvpTag)marker.getTag());
                       String id = yourMarkerTag.getEventId();
                     String date=  yourMarkerTag.getDate();
                     String eventdesc = yourMarkerTag.getEventDescription();
                     String name = yourMarkerTag.getEventName();
                     String venueName = yourMarkerTag.getVenueName();
                     String imgURL = yourMarkerTag.getImageUrl();
                     String startTime = yourMarkerTag.getStartTime();
                     String Venuecity = yourMarkerTag.getVenueCity();
                     obj.rsvpEvent2(id,date,eventdesc,name,venueName,imgURL,startTime,Venuecity);
                    }
                    Toast.makeText(getApplicationContext(),
                            "Event Added", Toast.LENGTH_SHORT).show();
                    favBTN.setBackgroundColor(Color.RED);
                    favorited++;
                }


            }
        });
/**
        alertDialog.setTitle("Location Selected");

        alertDialog.setMessage("Add this Location to your");

       // alertDialog.seti
        //alertDialog.setIcon(getResources().getDrawable(R.drawable.eventwithme));
        alertDialog.setButton2("Favorites", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),
                        "Event Added", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setButton3("Activities",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent act_intent = new Intent();
                        act_intent.setClass(getActivity().getBaseContext(),
                                MainActivity.class);
                        act_intent.putExtra("username", "test");
                        act_intent
                                .putExtra("latitude", markerPosition.latitude);
                        act_intent.putExtra("longitude",
                                markerPosition.longitude);
                        startActivity(act_intent);

                    }
                });
 **/
        alertDialog.setView(view);
        alertDialog.show();
    }

    

    @SuppressLint("WrongConstant")
    private void requestLocation() {

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d("mylog", "In Requesting Location");
        if (location != null && (System.currentTimeMillis() - location.getTime()) <= 1000 * 2) {
            LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
            String cityName = getCityName(myCoordinates);
            Toast.makeText(getContext(), cityName, Toast.LENGTH_SHORT).show();
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(1);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            Log.d("mylog", "Last location too old getting new location!");
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            mFusedLocationClient.requestLocationUpdates(locationRequest,
                    mLocationCallback, Looper.myLooper());
        }

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if(marker.equals(marker)){

            showAlertDialog(marker.getPosition(), marker );
            Log.i("onMarkerClick", "clicked on marker "+ marker.getSnippet());
            Log.i(" onMarkerClick", "marker Tag = "+marker.getTag().toString());
        }

        return false;
    }

    static class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }
    public ArrayList<EventMarker> getEventMarkers2() {
        return eventMarkers2;
    }

    public void setEventMarkers2(ArrayList<EventMarker> eventMarkers2) {
        this.eventMarkers2 = eventMarkers2;
    }

    public void updateEventMarkers(ArrayList<EventMarker> eventMarkers, ArrayList<RsvpTag> rsvpTags){
       // Collections.copy(eventMarkers,eventMarkers2);
        setEventMarkers2(eventMarkers);
       eventMarkers2 = eventMarkers;
       rsvpTags2 = rsvpTags;
        Log.i("MapViewFragment : updateEventMarkers eventMarkers2 : ", eventMarkers2.toString());
        Log.i("MapViewFragment : updateEventMarkers eventMarkers : ", eventMarkers.toString());

    }



    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    @Override
    public void initialize() {


    }
}