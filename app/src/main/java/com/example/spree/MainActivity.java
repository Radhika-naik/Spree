package com.example.spree;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterFragmentListener {

    private AppCompatTextView textViewReceipt;
    private ProgressBar progressBar;
    private GridView gridView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String currentCategory = "";
    private String currentPeriod = "15 Days";

    private String customerPhoneNumber = "9080706050";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        customerPhoneNumber = sessionManager.getKeyPhno();
        ImageView filterButton = findViewById(R.id.filter_button);
        ImageView logoutButton = findViewById(R.id.logout_button);
        progressBar = findViewById(R.id.progressBarMain);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Perform the refresh operation, such as reloading the data
                loadData();
            }
        });

        gridView = findViewById(R.id.gridView);

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                sessionManager.clearSession();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the filter button click event here
                // Open the FilterFragment or perform any desired action
                // Example: Show a message
                gridView.setVisibility(View.GONE);

                // Create an instance of FilterFragment
                FilterFragment filterFragment = FilterFragment.newInstance();

                // Set the FilterFragmentListener
                filterFragment.setFilterFragmentListener(MainActivity.this);

                // Replace the current fragment with the FilterFragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, filterFragment);
                fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                fragmentTransaction.commit();
            }
        });

        loadData();

    }

    @Override
    public void onBackPressed() {
        // Add your code here to handle the back button press

        // Call super.onBackPressed() to allow the default back button behavior
        gridView.setVisibility(View.VISIBLE);
        super.onBackPressed();
    }


    @Override
    public void onApplyButtonClicked(String selectedCategory, String selectedPeriod) {
        // Handle the selected option here
        // Return to the MainActivity or perform any desired action
        // Example: Close the FilterFragment
        currentCategory = selectedCategory;
        currentPeriod = selectedPeriod;
        loadData();
        getSupportFragmentManager().popBackStack();
        gridView.setVisibility(View.VISIBLE);

    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        JSONObject requestObject = new JSONObject();
        JSONObject dateRangeObject = new JSONObject();
        String dateRange[] = DateUtils.computeStartAndEndDate(currentPeriod);
        try {
            dateRangeObject.put("start", dateRange[0]);
            dateRangeObject.put("end", dateRange[1]);
            requestObject.put("customer_phno", customerPhoneNumber);
            requestObject.put("date_range", dateRangeObject);
            if (!currentCategory.isEmpty() && !currentCategory.equalsIgnoreCase("All Categories") )
                requestObject.put("category", currentCategory);
            System.out.println(requestObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new GetReceiptTask().execute(requestObject.toString());
    }

    private void displayReceipt(List<ReceiptDetails> receiptDetails) {
        System.out.println(receiptDetails.toString());
        if (receiptDetails != null && !receiptDetails.isEmpty()) {
            GridViewAdapter adapter = new GridViewAdapter(this, receiptDetails);
            gridView.setAdapter(adapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ReceiptDetails selectedReceipt = adapter.getItem(position);

                    // Create an intent to launch the ReceiptDetailActivity
                    Intent intent = new Intent(MainActivity.this, ReceiptViewingActivity.class);
                    Gson gson = new Gson();
                    String selectedReceiptJson = gson.toJson(selectedReceipt);
                    intent.putExtra("selectedReceipt", selectedReceiptJson);
                    startActivity(intent);
                }
            });

        } else {
            gridView.setAdapter(null);
            Toast.makeText(this, "No receipt details found", Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }



    private class GetReceiptTask extends AsyncTask<Object, Void, List<ReceiptDetails>> {
        private String errorMessage = "Oh Snap! Something Went Wrong, Try Refresh";

        @Override
        protected List<ReceiptDetails> doInBackground(Object... params) {
            try {
                // Call the GetReceipt API
                URL url = new URL("https://spreebeta.azurewebsites.net/api/getreceipt");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                // Set the request body data
                connection.setDoOutput(true);
                String bodyData = (String) params[0];
                System.out.println(bodyData);
                connection.getOutputStream().write(bodyData.getBytes());

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // API call successful, retrieve the response
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    bufferedReader.close();
                    inputStream.close();

                    // Parse the JSON response
                    JSONObject responseObject = new JSONObject(response.toString());
                    System.out.println(responseObject.toString());

                    // Extract the message response
                    JSONObject message = responseObject.getJSONObject("message");

                    // Extract the array from the response object
                    JSONArray receiptArray = message.getJSONArray("receipt_details");

                    // Parse the JSON response and convert it to ReceiptDetails object
                    List<ReceiptDetails> receiptDetails = parseJsonResponse(receiptArray.toString());

                    return receiptDetails;
                } else {
                    // API call failed
                    return new ArrayList<>(); // Return an empty list
                }

            } catch (Exception e) {
                Log.e("GetReceiptTask", "Error: " + e.getMessage());
                return new ArrayList<>(); // Return an empty list

            }
        }

        private List<ReceiptDetails> parseJsonResponse(String jsonResponse) {
            // Use Gson library to parse the JSON response
            Gson gson = new Gson();

            // Define the type of the list of ReceiptDetails
            Type receiptListType = new TypeToken<List<ReceiptDetails>>() {}.getType();

            // Parse the JSON response into a list of ReceiptDetails objects
            List<ReceiptDetails> receiptDetailsList = gson.fromJson(jsonResponse, receiptListType);

            return receiptDetailsList;
        }


        @Override
        protected void onPostExecute(List<ReceiptDetails> receiptDetails) {
            super.onPostExecute(receiptDetails);
                displayReceipt(receiptDetails);
        }
        }
    }
