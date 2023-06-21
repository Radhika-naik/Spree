package com.example.spree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<ReceiptDetails> items;

    public GridViewAdapter(Context context, List<ReceiptDetails> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public ReceiptDetails getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.grid_item_layout, parent, false);
        }

        TextView shopNameText = convertView.findViewById(R.id.shop_name_text);
        TextView dateText = convertView.findViewById(R.id.date_text);
        TextView totalSpendingText = convertView.findViewById(R.id.total_spending_text);

        ReceiptDetails item = items.get(position);
        shopNameText.setText(item.getShopName());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("d MMMM", Locale.US);
        String formattedDate = "";
        try {
            Date date = inputFormat.parse(item.getItemsDetails().get(0).getDateOfPurchase());
            if (date!=null) {
                formattedDate = outputFormat.format(date);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(item.getItemsDetails().get(0).getDateOfPurchase());
        dateText.setText(formattedDate);
        totalSpendingText.setText(formatPrice(calculateTotalSpending(item.getItemsDetails())));

        return convertView;
    }

    private double calculateTotalSpending(List<ReceiptDetails.ItemDetails> itemDetails) {
        return itemDetails.stream()
                .mapToDouble(ReceiptDetails.ItemDetails::getTotalPrice)
                .sum();
    }

    private String formatPrice(double price) {
        return String.format("\u20B9%.2f", price);
    }

}
