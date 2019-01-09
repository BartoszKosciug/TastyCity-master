package com.example.bartosz.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    TextView locationText;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = (long) (1000 * 30 * 1); // 30 seconds
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);

        setSupportActionBar(toolbar);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences settings =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String rating = settings.getString("list_rating","");
                String price = settings.getString("list_price","");
                boolean open = settings.getBoolean("open_res",true);
                String radius = settings.getString("list_radius","");

                JSONObject json = new JSONObject();
                try{
                    json.put("uid", mAuth.getUid());
                    json.put("lat",location.getLatitude());
                    json.put("lon",location.getLongitude());
                    json.put("rating",rating);//json.put("price",price);
                    json.put("open",open);
                    json.put("radius",radius);
                } catch(Exception e){
                    Log.d("Exception",e.toString());
                }
                Log.d("location", location.toString());
                String url = "http://192.168.56.1:4000/restaurants/neighbour";

                MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(Request.Method.POST, url, json,
                        new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Place> places = new ArrayList<>();
                        //serverResp.setText("String Response : "+ response.toString());
                        JSONObject jsonObject = null;
                        JSONObject distance = null;
                        //Log.d("Response: ", response.toString());
                        for(int i = 0; i < response.length();i++){
                            try{
                                jsonObject = response.getJSONObject(i);
                                Place place = new Place();
                                place.setPlace_id(jsonObject.getString("place_id"));
                                place.setName(jsonObject.getString("name"));
                                place.setOpen(jsonObject.getString("open"));
                                place.setAddress(jsonObject.getString("address"));
                                place.setRating(jsonObject.getString("rating"));
                                distance = jsonObject.getJSONObject("distance");
                                place.setDistance(distance.getString("length")+distance.getString("unit"));
                                place.setUri(jsonObject.getString("uri"));
                                places.add(place);
                                //Log.d("place: ", jsonObject.toString());
                            } catch(JSONException e){
                                //Log.d("Exception:", e.toString());
                            }
                        }
                        if(!places.isEmpty()) {
                            setuprecyclerview(places);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //serverResp.setText("Error getting response");
                        Log.d("Response: ", error.toString());
                    }
                });

                requestQueue.add(jsonArrayRequest);
            }

            private void setuprecyclerview(List<Place> places) {


                RecyclerViewAdapter myadapter = new RecyclerViewAdapter(getApplicationContext(), places);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                try {
                    recyclerView.setAdapter(myadapter);
                }catch(Exception e){
                    Log.d("Exception",e.toString());
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("location","GPS is disabled");
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGps();
                }
            }
        };


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 0, locationListener);
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater infalter = getMenuInflater();
        infalter.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.menuSettings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED){

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,0, locationListener);
                    if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        buildAlertMessageNoGps();
                    }
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        if(!isFinishing()) alert.show();
    }
}


