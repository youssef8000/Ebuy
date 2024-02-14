package com.example.myproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ordermainFragment extends Fragment {

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ordermain, container, false);
        TableLayout tableLayout = view.findViewById(R.id.table);
        SharedPreferences preferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        if (userId != -1) {
            DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
            List<UserOrder> UserOrders= databaseHelper.getAllUserOrders(userId);
            for (UserOrder item : UserOrders) {
                TableRow row = new TableRow(requireContext());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                );

                row.setPadding(0, 5, 0, 5);
                row.setLayoutParams(layoutParams);

                Product product = databaseHelper.getOneProduct(item.getProId());

                TextView productNameTextView = new TextView(requireContext());
                productNameTextView.setText(product.getName());
                productNameTextView.setTextColor(Color.BLACK);
                productNameTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                TextView priceTextView = new TextView(requireContext());
                priceTextView.setText(String.valueOf(item.getTotalprice()));
                priceTextView.setTextColor(Color.BLACK);
                priceTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                String feedback = item.getFeedback();
                LinearLayout buttonLayout = new LinearLayout(requireContext());
                buttonLayout.setLayoutParams(new TableRow.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));
                buttonLayout.setOrientation(LinearLayout.HORIZONTAL);


                Button updateButton = new Button(requireContext());
                LinearLayout.LayoutParams updateButtonParams = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1.0f
                );
                updateButton.setLayoutParams(updateButtonParams);
                updateButton.setText("Add Feedback");
                GradientDrawable updateDrawable = new GradientDrawable();
                int buttonColor = ContextCompat.getColor(requireContext(), R.color.lavender);
                updateDrawable.setColor(buttonColor);
                updateDrawable.setCornerRadius(12);
                updateButton.setBackground(updateDrawable);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int itemId = item.getId();
                        int itemrating= item.getRating();
                        String itemfeedback = item.getFeedback();
                        openFragment(new updateUserOrderFragment(itemId, itemrating, itemfeedback));
                    }
                });

                if (!TextUtils.isEmpty(feedback)) {
                    updateButton.setEnabled(false);
                    GradientDrawable Drawable = new GradientDrawable();
                    Drawable.setColor(Color.WHITE);
                    Drawable.setCornerRadius(12);
                    updateButton.setBackground(Drawable);
                    updateButton.setText("");
                }
                buttonLayout.addView(updateButton);
                row.addView(productNameTextView);
                row.addView(priceTextView);
                row.addView(buttonLayout);
                tableLayout.addView(row);
            }
        }
        return view;
    }
}