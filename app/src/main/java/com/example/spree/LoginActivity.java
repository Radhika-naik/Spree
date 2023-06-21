package com.example.spree;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignup;
    private ProgressBar progressBar;
    private String customerPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignup = findViewById(R.id.textViewSignup);
        progressBar = findViewById(R.id.progressBar);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                if (!phoneNumber.isEmpty() && !password.isEmpty()) {
                    customerPhoneNumber = phoneNumber;
                    progressBar.setVisibility(View.VISIBLE);
                    // Login successful, make HTTP request
                    loginCustomer(phoneNumber, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid login details!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SignupActivity
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginCustomer(String phoneNumber, String password) {
        String url = "https://spreebeta.azurewebsites.net/api/customerlogin"; // Replace with your actual backend endpoint

        Map<String, String> postData = new HashMap<>();
        postData.put("customer_phno", phoneNumber);
        postData.put("customer_password", password);

        new LoginAsyncTask().execute(url, postData);
    }

    private class LoginAsyncTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            String url = (String) params[0];
            Map<String, String> postData = (Map<String, String>) params[1];

            try {
                // Create connection
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Convert postData to JSON
                String postDataJson = new Gson().toJson(postData);

                // Send request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postDataJson.getBytes());
                outputStream.flush();
                outputStream.close();

                // Get response
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    reader.close();
                    inputStream.close();

                    return responseBuilder.toString();
                } else {
                    // Error occurred
                    progressBar.setVisibility(View.GONE);
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                // Handle the response from the backend
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String code = jsonResponse.getString("code");
                    String message = jsonResponse.getString("message");

                    if (code.equals("SUCCESS")) {
                        // Customer logged in successfully
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        // Proceed to the next screen or perform desired actions
                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.setLoggedIn(true);
                        sessionManager.setUserInfo(customerPhoneNumber);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("customerName", "Radhika");
                        intent.putExtra("customerPhoneNumber", customerPhoneNumber);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        // Invalid phone number or password
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            } else {
                // Error occurred
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
