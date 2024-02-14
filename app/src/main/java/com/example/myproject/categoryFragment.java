package com.example.myproject;

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
import android.widget.TextView;

import java.util.List;

public class categoryFragment extends Fragment {
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        Button add_btn = view.findViewById(R.id.add_category);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
       add_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openFragment(new addcategoryFragment());
           }
       });
        List<Category> categories = databaseHelper.getAllCategories();
        LinearLayout parentLayout = view.findViewById(R.id.mainlayout);
        for (Category category : categories) {
            LinearLayout linearLayout = new LinearLayout(requireContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(20, 10, 0, 0);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView textView = new TextView(requireContext());
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(textParams);
            textView.setText("- "+ category.getName());
            textView.setTextSize(18);
            textView.setTextColor(Color.BLACK);
            Button deleteButton = new Button(requireContext());
            LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            deleteButton.setLayoutParams(deleteButtonParams);
            deleteButton.setText("Delete");
            deleteButtonParams.setMargins(16, 0, 16, 0);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.RED);
            drawable.setCornerRadius(12);
            deleteButton.setBackground(drawable);
            deleteButton.setBackgroundColor(Color.RED);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   int categoryIdToDelete = category.getId();

                    // Call the delete method from DatabaseHelper
                    boolean isDeleted = databaseHelper.deleteCategoryById(categoryIdToDelete);

                    // Check if the category was successfully deleted
                    if (isDeleted) {
                        parentLayout.removeView(linearLayout);
                    } else {
                        // Handle the case where deletion failed
                        // You might want to show an error message
                    }
                }
            });
            Button updateButton = new Button(requireContext());
            LinearLayout.LayoutParams updateButtonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            updateButton.setLayoutParams(updateButtonParams);
            updateButton.setText("Update");
            updateButton.setBackgroundColor(Color.CYAN);
            GradientDrawable drawableupdate = new GradientDrawable();
            drawableupdate.setColor(Color.RED);
            drawableupdate.setCornerRadius(12);
            deleteButton.setBackground(drawableupdate);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String categoryname = category.getName();
                    int categoryid = category.getId();
                    byte[] catImage=category.getImage();

                    openFragment(new updatecategoryFragment(categoryid,categoryname,catImage));
                }
            });
            linearLayout.addView(textView);
            linearLayout.addView(deleteButton);
            linearLayout.addView(updateButton);
            parentLayout.addView(linearLayout);
        }
        return view;
    }

}