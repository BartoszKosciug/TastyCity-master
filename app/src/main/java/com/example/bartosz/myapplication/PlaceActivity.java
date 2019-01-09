package com.example.bartosz.myapplication;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bartosz.myapplication.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PlaceActivity extends AppCompatActivity implements View.OnClickListener{

    public String website = null;
    public String phone = null;
    public String place_id = null;
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.review_add:
                Intent review = new Intent(this, ReviewActivity.class);
                review.putExtra("place_id",place_id);
                startActivity(review);
                break;
        }
    }

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recycler_view_reviews);


        place_id = getIntent().getExtras().getString("place_id");
        String name = getIntent().getExtras().getString("name");
        String address = getIntent().getExtras().getString("address");
        String distance = getIntent().getExtras().getString("distance");
        String rating = getIntent().getExtras().getString("rating");
        String uri = getIntent().getExtras().getString("uri");
        String open = getIntent().getExtras().getString("open");
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        collapsingToolbarLayout.setTitleEnabled(true);
        findViewById(R.id.review_add).setOnClickListener(this);
        TextView place_name = findViewById(R.id.name_details);
        TextView place_address = findViewById(R.id.address_details);
        TextView place_rating = findViewById(R.id.rating_details);
        TextView place_distance = findViewById(R.id.distance_details);
        TextView place_open = findViewById(R.id.open_details);
        final TextView place_website = findViewById(R.id.website);
        TextView place_phone =findViewById(R.id.telephone_number);
        ImageView img = findViewById(R.id.image_details);

        place_name.setText(name);
        place_address.setText(address);
        place_rating.setText(rating);
        place_distance.setText(distance);
        place_open.setText(open);
        place_website.setMovementMethod(LinkMovementMethod.getInstance());

        Glide.with(this).load(uri).into(img);


        JSONObject json = new JSONObject();
        try{
            json.put("place_id", place_id);
        }catch(Exception e){
            Log.d("Exception", e.toString());
        }
        String url = "http://192.168.56.1:4000/restaurants/reviews";

        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(Request.Method.POST, url, json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Review> reviews = new ArrayList<>();
                        //serverResp.setText("String Response : "+ response.toString());
                        JSONObject jsonObject = null;
                        JSONObject distance = null;
                        //Log.d("Response: ", response.toString());
                        for(int i = 0; i < response.length();i++){
                            try{
                                jsonObject = response.getJSONObject(i);
                                Review review = new Review();
                                review.setAuthor_name(jsonObject.getString("author"));
                                review.setText(jsonObject.getString("text"));
                                review.setRelative_time_description(jsonObject.getString("relative_time_description"));
                                reviews.add(review);
                                //Log.d("place: ", jsonObject.toString());
                            } catch(JSONException e){
                                Log.d("Exception:", e.toString());
                            }
                        }
                        if(!reviews.isEmpty()) {
                            setuprecyclerview(reviews);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //serverResp.setText("Error getting response");
                        Log.d("Response: ", error.toString());
                    }
        });



        String urlContact = "http://192.168.56.1:4000/restaurants/contact";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlContact, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //serverResp.setText("String Response : "+ response.toString());
                        //Log.d("Response: ", response.toString());
                            try{
                                website = response.getString("website");
                                phone = response.getString("phone");
                                place_website.setText(phone);
                                String href = String.format("<a href=%s>visit restaurant website</a>", website); //http://www.google.com>link</a>";
                                place_website.setText(Html.fromHtml(href));
                                Log.d("Phone: ",phone);
                            } catch(JSONException e){
                                Log.d("Exception:", e.toString());
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
        requestQueue.add(jsonObjectRequest);

    }


    private void setuprecyclerview(List<Review> reviews) {


        ReviewRecyclerViewAdapter myadapter = new ReviewRecyclerViewAdapter(getApplicationContext(), reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        try {
            recyclerView.setAdapter(myadapter);
        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
    }


}
