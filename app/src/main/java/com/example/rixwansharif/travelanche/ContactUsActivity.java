package com.example.rixwansharif.travelanche;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class ContactUsActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#01AA97")));
        getSupportActionBar().setTitle("Contact Us");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        mapView=(MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady (GoogleMap map){
        LatLng coordinates_uet = new LatLng(31.579930, 74.356084);
        LatLng coordinates_st = new LatLng(31.577366, 74.336175);
        final Marker uet_m=map.addMarker(new MarkerOptions().position(coordinates_uet).title("UET")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        Marker st_m=map.addMarker(new MarkerOptions().position(coordinates_st).title("Station")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //LatLng coordinates_lhe = new LatLng(31.5204 , 74.3587);
        //map.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );


        List<Marker> markers = new ArrayList<Marker>();
        markers.add(uet_m);
        markers.add(st_m);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.moveCamera(cu);
        map.animateCamera(cu);


        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates_lhe, 15));
        //map.moveCamera(CameraUpdateFactory.);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String name= marker.getTitle();
                if (name.equalsIgnoreCase("UET"))
                {
                    Toast.makeText(getApplicationContext(), "Something has gone wrong, Try again !", Toast.LENGTH_SHORT).show();
                }

                return false;
        }});

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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
