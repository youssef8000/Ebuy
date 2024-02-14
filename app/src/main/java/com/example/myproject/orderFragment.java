package com.example.myproject;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class orderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        List<UserOrder> userOrders = databaseHelper.getAllOrders();
        TableLayout tableLayout = view.findViewById(R.id.table);
        for (UserOrder userOrder : userOrders) {
            TableRow tableRow = new TableRow(requireContext());
            TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            tableRow.setLayoutParams(rowParams);

            TextView productNameTextView = new TextView(requireContext());
            TableRow.LayoutParams textParams = new TableRow.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );

            User user=databaseHelper.getUserById(userOrder.getUserId());
            productNameTextView.setLayoutParams(textParams);
            productNameTextView.setText(user.getEmail());
            productNameTextView.setTextSize(18);
            productNameTextView.setTextColor(Color.BLACK);

            TextView productPriceTextView = new TextView(requireContext());
            TableRow.LayoutParams textParams2 = new TableRow.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            Product product=databaseHelper.getOneProduct(userOrder.getProId());
            productPriceTextView.setLayoutParams(textParams2);
            productPriceTextView.setText(String.valueOf(product.getName()));
            productPriceTextView.setTextSize(18);
            productPriceTextView.setTextColor(Color.BLACK);

            TextView orderquantityTextView = new TextView(requireContext());
            TableRow.LayoutParams textParams3 = new TableRow.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            orderquantityTextView.setLayoutParams(textParams3);
            orderquantityTextView.setText(String.valueOf(userOrder.getQuantity()));
            orderquantityTextView.setTextSize(18);
            orderquantityTextView.setTextColor(Color.BLACK);

            TextView orderpriceTextView = new TextView(requireContext());
            TableRow.LayoutParams textParams4 = new TableRow.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            orderpriceTextView.setLayoutParams(textParams4);
            orderpriceTextView.setText(String.valueOf(userOrder.getTotalprice()));
            orderpriceTextView.setTextSize(18);
            orderpriceTextView.setTextColor(Color.BLACK);


            tableRow.addView(productNameTextView);
            tableRow.addView(productPriceTextView);
            tableRow.addView(orderquantityTextView);
            tableRow.addView(orderpriceTextView);

            tableLayout.addView(tableRow);
        }

        return view;
    }
}