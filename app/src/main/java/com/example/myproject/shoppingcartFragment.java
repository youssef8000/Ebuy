package com.example.myproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.List;

public class shoppingcartFragment extends Fragment {

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
        View view= inflater.inflate(R.layout.fragment_shoppingcart, container, false);
        TableLayout tableLayout = view.findViewById(R.id.table);
        SharedPreferences preferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        int userId = preferences.getInt("user_id", -1);
        if (userId != -1) {
            DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
            List<ShoppingCartItem> shoppingCartItems = databaseHelper.getAllShoppingCartItems(userId);
            for (ShoppingCartItem item : shoppingCartItems) {
                TableRow row = new TableRow(requireContext());
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT
                );

                row.setPadding(0, 5, 0, 5);
                row.setLayoutParams(layoutParams);

                Product product = databaseHelper.getOneProduct(item.getproductId());

                TextView productNameTextView = new TextView(requireContext());
                productNameTextView.setText(product.getName());
                productNameTextView.setTextColor(Color.BLACK);
                productNameTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                TextView priceTextView = new TextView(requireContext());
                priceTextView.setText(String.valueOf(item.gettotalPrice()));
                priceTextView.setTextColor(Color.BLACK);
                priceTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                TextView quantityTextView = new TextView(requireContext());
                quantityTextView.setText(String.valueOf(item.getquantity()));
                quantityTextView.setTextColor(Color.BLACK);
                quantityTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

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
                deleteButton.setText("DEL");
                GradientDrawable deleteDrawable = new GradientDrawable();
                deleteDrawable.setColor(Color.RED);
                deleteDrawable.setCornerRadius(12);
                deleteButton.setBackground(deleteDrawable);
                deleteButton.setBackgroundColor(Color.RED);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int itemId = item.getId();
                        boolean deleted = databaseHelper.deleteShoppingCartItem(itemId);
                        if (deleted) {
                            tableLayout.removeView(row);
                            Toast.makeText(requireContext(), "Successfully to delete item", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
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
                updateButton.setText("Edit");
                GradientDrawable updateDrawable = new GradientDrawable();
                updateDrawable.setColor(Color.CYAN);
                updateDrawable.setCornerRadius(12);
                updateButton.setBackground(updateDrawable);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int itemId = item.getId();
                        int itemquantity= item.getquantity();
                        int itemprice = item.gettotalPrice();
                        openFragment(new updateproquantityFragment(itemId, itemquantity, itemprice));

                    }
                });
                buttonLayout.addView(deleteButton);
                buttonLayout.addView(updateButton);

                row.addView(productNameTextView);
                row.addView(priceTextView);
                row.addView(quantityTextView);
                row.addView(buttonLayout);

                tableLayout.addView(row);
            }
        }
        TextView totalPriceTextView = view.findViewById(R.id.price);
        double totalCartPrice = 0.0;
        if (userId != -1) {
            DatabaseHelper databaseHelperr = new DatabaseHelper(requireContext());
            List<ShoppingCartItem> shoppingCartItemss = databaseHelperr.getAllShoppingCartItems(userId);

            for (ShoppingCartItem item : shoppingCartItemss) {
                totalCartPrice += item.gettotalPrice();
            }
            totalPriceTextView.setText(String.format("Total Price: %.2f", totalCartPrice));
        }

        Button addorder=view.findViewById(R.id.addorder);
        addorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
                SharedPreferences preferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                int userId = preferences.getInt("user_id", -1);

                if (userId != -1) {
                    List<ShoppingCartItem> shoppingCartItems = databaseHelper.getAllShoppingCartItems(userId);

                    for (ShoppingCartItem item : shoppingCartItems) {
                        boolean insertResult = databaseHelper.insertUserOrder(
                                userId,
                                item.getproductId(),
                                item.getquantity(),
                                item.gettotalPrice(),
                                "", 0
                        );

                        if (insertResult) {
                            int itemId = item.getId();
                            boolean deleted = databaseHelper.deleteShoppingCartItem(itemId);

                            if (deleted) {
                                Toast.makeText(requireContext(), "Item added to orders", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }

                            int proID = item.getproductId();
                            Product getproductquantity = databaseHelper.getOneProduct(proID);
                            int newQuantity = item.getquantity();
                            int currentQuantity = Integer.parseInt(getproductquantity.getQuantity());
                            int updatedQuantity = currentQuantity - newQuantity;

                            boolean updatequantity = databaseHelper.updateProductQuantity(proID, updatedQuantity);

                            if (updatequantity) {
                                Toast.makeText(requireContext(), "Successfully add order", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Failed to add orders", Toast.LENGTH_SHORT).show();
                            }

                            openFragment(new ordermainFragment());
                        } else {
                            Toast.makeText(requireContext(), "Failed to add item to orders", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });

        return view;
    }
}