package com.example.spree;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReceiptViewingActivity extends AppCompatActivity {

    private TextView tvReceiptAscii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_viewing);

        // Retrieve the JSON string from the intent
        String selectedReceiptJson = getIntent().getStringExtra("selectedReceipt");

        // Convert the JSON string back to a ReceiptDetails object
        Gson gson = new Gson();
        ReceiptDetails selectedReceipt = gson.fromJson(selectedReceiptJson, ReceiptDetails.class);

        tvReceiptAscii = findViewById(R.id.tvReceiptAscii);

        // Get the ReceiptDetails object from the intent or any other source
        //ReceiptDetails receiptDetails = generateReceiptDetails();

        // Generate the receipt ASCII
        ReceiptDetailsDecorator decorator = new ReceiptDetailsDecorator(selectedReceipt);
        String receiptAscii = decorator.getReceiptAscii();

        System.out.println(receiptAscii);
        // Set the receipt ASCII to the TextView
        tvReceiptAscii.setText(receiptAscii);
    }

    public static ReceiptDetails generateReceiptDetails() {
        ReceiptDetails receiptDetails = new ReceiptDetails();
        receiptDetails.setShopName("Dacathalon Store");
        receiptDetails.setShopCategory("Grocery");
        receiptDetails.setShopPhno("123-456-7890");
        receiptDetails.setShopEmail("abcstore@example.com");
        receiptDetails.setShopAddress("123 Main Street, City");
        receiptDetails.setShopGstNumber("GST1234567890");

// Create a list of ItemDetails
        List<ReceiptDetails.ItemDetails> itemDetailsList = new ArrayList<>();

// Create the first item
        ReceiptDetails.ItemDetails item1 = new ReceiptDetails.ItemDetails();
        item1.setItemName("Product 1");
        item1.setQuantity(2);
        item1.setTotalPrice(10.00);
        item1.setDateOfPurchase("2023-06-16");
        itemDetailsList.add(item1);

// Create the second item
        ReceiptDetails.ItemDetails item2 = new ReceiptDetails.ItemDetails();
        item2.setItemName("Product 2");
        item2.setQuantity(1);
        item2.setTotalPrice(5.50);
        itemDetailsList.add(item2);

// Create the third item
        ReceiptDetails.ItemDetails item3 = new ReceiptDetails.ItemDetails();
        item3.setItemName("Product 3");
        item3.setQuantity(3);
        item3.setTotalPrice(15.75);
        itemDetailsList.add(item3);

// Set the list of ItemDetails to the ReceiptDetails object
        receiptDetails.setItemsDetails(itemDetailsList);
        return receiptDetails;
    }
}