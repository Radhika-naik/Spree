package com.example.spree;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    private ListView categoryListView;
    private ListView periodListView;

    private FilterFragmentListener filterFragmentListener;

    public FilterFragment() {
        // Required empty public constructor
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ImageView backButton = toolbar.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click here
                requireActivity().onBackPressed();
            }
        });

        categoryListView = view.findViewById(R.id.category_list);
        periodListView = view.findViewById(R.id.period_list);

        populateCategoryList();
        populatePeriodList();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button applyButton = view.findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the selected options
                String selectedCategory = getSelectedOption(categoryListView);
                String selectedPeriod = getSelectedOption(periodListView);

                // Pass the selected options to the listener
                if (filterFragmentListener != null) {
                    filterFragmentListener.onApplyButtonClicked(selectedCategory, selectedPeriod);
                }
            }
        });
    }


    private void populateCategoryList() {
        List<String> categories = new ArrayList<>();
        categories.add("All Categories");
        categories.add("Grocery");
        categories.add("Medicine");
        categories.add("Clothing");
        categories.add("Food");

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_radio, R.id.item_text, categories);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clearSelection(categoryListView);

                RadioButton radioButton = view.findViewById(R.id.radio_button);
                radioButton.setChecked(true);
            }
        });

        // Select the first category by default
        categoryListView.post(new Runnable() {
            @Override
            public void run() {
                categoryListView.performItemClick(
                        categoryListView.getChildAt(0),
                        0,
                        categoryListView.getItemIdAtPosition(0)
                );
            }
        });
    }

    private void populatePeriodList() {
        List<String> periods = new ArrayList<>();
        periods.add("1 Day");
        periods.add("15 Days");
        periods.add("1 Month");
        periods.add("6 Months");

        ArrayAdapter<String> periodAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item_radio, R.id.item_text, periods);
        periodListView.setAdapter(periodAdapter);

        periodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clearSelection(periodListView);

                RadioButton radioButton = view.findViewById(R.id.radio_button);
                radioButton.setChecked(true);
            }
        });

        // Select the second period by default
        periodListView.post(new Runnable() {
            @Override
            public void run() {
                periodListView.performItemClick(
                        periodListView.getChildAt(1),
                        1,
                        periodListView.getItemIdAtPosition(1)
                );
            }
        });
    }

    private void clearSelection(ListView listView) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View listItem = listView.getChildAt(i);
            RadioButton radioButton = listItem.findViewById(R.id.radio_button);
            radioButton.setChecked(false);
        }
    }

    public void setFilterFragmentListener(FilterFragmentListener listener) {
        this.filterFragmentListener = listener;
    }

    private String getSelectedOption(ListView listView) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View listItem = listView.getChildAt(i);
            RadioButton radioButton = listItem.findViewById(R.id.radio_button);
            if (radioButton.isChecked()) {
                TextView itemText = listItem.findViewById(R.id.item_text);
                return itemText.getText().toString();
            }
        }
        return null;
    }

}
