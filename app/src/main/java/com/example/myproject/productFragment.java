package com.example.myproject;

import android.os.Bundle;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class productFragment extends Fragment {
    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public productFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        Button addPro_btn = view.findViewById(R.id.addproduct);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        addPro_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment(new addproductFragment());
            }
        });
        List<Product> products = databaseHelper.getAllProducts();
        TableLayout tableLayout = view.findViewById(R.id.table);
        for (Product product : products) {
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
            productNameTextView.setLayoutParams(textParams);
            productNameTextView.setText(product.getName());
            productNameTextView.setTextSize(18);
            productNameTextView.setTextColor(Color.BLACK);

            TextView productPriceTextView = new TextView(requireContext());
            productPriceTextView.setLayoutParams(textParams);
            productPriceTextView.setText(String.valueOf(product.getPrice()));
            productPriceTextView.setTextSize(18);
            productPriceTextView.setTextColor(Color.BLACK);

            LinearLayout buttonLayout = new LinearLayout(requireContext());
            buttonLayout.setLayoutParams(new TableRow.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

            Button deleteButton = new Button(requireContext());
            LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButton.setText("Delete");
            GradientDrawable deleteDrawable = new GradientDrawable();
            deleteDrawable.setColor(Color.RED);
            deleteDrawable.setCornerRadius(12);
            deleteButton.setBackground(deleteDrawable);
            deleteButton.setBackgroundColor(Color.RED);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productIdToDelete = product.getId();
                    boolean isDeleted = databaseHelper.deleteproductById(productIdToDelete);
                    if (isDeleted) {
                        tableLayout.removeView(tableRow);
                    } else {

                    }
                }
            });

            Button updateButton = new Button(requireContext());
            LinearLayout.LayoutParams updateButtonParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            updateButton.setLayoutParams(updateButtonParams);
            updateButton.setText("Update");
            GradientDrawable updateDrawable = new GradientDrawable();
            updateDrawable.setColor(Color.CYAN);
            updateDrawable.setCornerRadius(12);
            updateButton.setBackground(updateDrawable);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productName = product.getName();
                    int productId = product.getId();
                    String productDescription=product.getDescription();
                     byte[] productImage=product.getImage();
                    String productbar=product.getbarcodeImage();
                    int productCategory=product.getCategory();
                     int productPrice=product.getPrice();
                     String productQuantity=product.getQuantity();
                    openFragment(new updateproductFragment(productId, productName, productDescription, productImage, productbar, productCategory,
                            productPrice,productQuantity));
                }
            });

            buttonLayout.addView(deleteButton);
            buttonLayout.addView(updateButton);

            tableRow.addView(productNameTextView);
            tableRow.addView(productPriceTextView);
            tableRow.addView(buttonLayout);

            tableLayout.addView(tableRow);
        }
        return view;
    }
}