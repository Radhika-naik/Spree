package com.example.spree;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private Button buttonSignup;
    private TextView textViewLogin;

    private String customerPhoneNumber;
    private String customerName;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignup = findViewById(R.id.buttonSignup);
        textViewLogin = findViewById(R.id.textViewLogin);
        progressBar = findViewById(R.id.progressBar);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                customerName = name;
                customerPhoneNumber = phoneNumber;
                progressBar.setVisibility(View.VISIBLE);

                // Validate the signup data
                if (validateSignupData(name, phoneNumber, password)) {
                    // Call the backend API for customer registration
                    registerCustomer(name, phoneNumber, password);
                } else {
                    // Signup failed
                    Toast.makeText(SignupActivity.this, "Invalid signup data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the LoginActivity
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateSignupData(String name, String phoneNumber, String password) {
        // Implement your logic for validating the signup data
        // Return true if the data is valid, otherwise return false
        // Example validation logic:
        return !name.isEmpty() && !phoneNumber.isEmpty() && !password.isEmpty();
    }

    private void registerCustomer(String name, String phoneNumber, String password) {
        String url = "https://spreebeta.azurewebsites.net/api/customerregistration";

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("customer_name", name);
            requestObject.put("customer_phno", phoneNumber);
            requestObject.put("customer_password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SignupAsyncTask().execute(url, requestObject.toString());
    }

    private class SignupAsyncTask extends AsyncTask<Object, Void, String> {

        @Override
        protected String doInBackground(Object... params) {
            String url = (String) params[0];
            String requestBody = (String) params[1];

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            StringBuilder responseBuilder = new StringBuilder();

            try {
                URL requestUrl = new URL(url);
                connection = (HttpURLConnection) requestUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(requestBody.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return responseBuilder.toString();
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                JSONObject jsonResponse = new JSONObject(response);
                String code = jsonResponse.getString("code");
                String message = jsonResponse.getString("message");

                if (code.equals("SUCCESS")) {
                    progressBar.setVisibility(View.GONE);
                    // Signup successful
                    Toast.makeText(SignupActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    // Proceed to the next screen or perform desired actions
                    // Proceed to the next screen or perform desired actions
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.setLoggedIn(true);
                    sessionManager.setUserInfo(customerPhoneNumber);
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.putExtra("customerName", customerName);
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    // Signup failed
                    Toast.makeText(SignupActivity.this, "Signup failed: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}
