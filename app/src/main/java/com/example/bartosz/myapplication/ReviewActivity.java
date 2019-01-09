package com.example.bartosz.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

public class ReviewActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mAuth = FirebaseAuth.getInstance();
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final RatingBar mRatingBar =  findViewById(R.id.ratingBar);
        final TextView mRatingScale =  findViewById(R.id.tvRatingScale);
        final EditText mFeedback =  findViewById(R.id.etFeedback);
        Button mSendFeedback =  findViewById(R.id.btnSubmit);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(ReviewActivity.this, "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {
                    String feedback = mFeedback.getText().toString();
                    mFeedback.setText("");
                    mRatingBar.setRating(0);
                    Toast.makeText(ReviewActivity.this, "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                    String url = "http://192.168.56.1:4000/review/post";
                    JSONObject json = new JSONObject();
                    try{
                        Intent myIntent = getIntent();
                        String place_id = myIntent.getStringExtra("place_id");
                        json.put("place_id", place_id);
                        json.put("review",feedback);
                        json.put("review_author",mAuth.getCurrentUser().toString());
                    }catch(Exception e){
                        Log.d("Exception", e.toString());
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        Log.d("Phone: ",response.toString());
                                    } catch(Exception e){
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
                    requestQueue.add(jsonObjectRequest);


                }
            }
        });

    }


}
