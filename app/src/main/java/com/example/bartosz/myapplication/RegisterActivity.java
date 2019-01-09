package com.example.bartosz.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    private final static String url =  "http://192.168.56.1:4000/usersettings/post";
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.buttonRegister).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonRegister:
                registerUser();
                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;

        }

        if(password.isEmpty()){
            editTextPassword.setError("Passoword is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            editTextPassword.setError("Minimum length of the pasword is six");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    String uid = mAuth.getUid();

                    org.json.JSONObject json = new org.json.JSONObject();
                    try{
                        json.put("userid",uid);
                    } catch (Exception e){
                        Log.d("Exception",e.toString());
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url, json,new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //serverResp.setText("String Response : "+ response.toString());
                            Log.d("Response: ", response.toString());
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //serverResp.setText("Error getting response");
                            Log.d("Response: ", error.toString());
                        }
                    });

                    requestQueue.add(jsonObjectRequest);

                    Toast.makeText(getApplicationContext(),"User Registered Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{

                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"You are registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
