package com.example.myproject;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class updateUserOrderFragment extends Fragment {

    private int itemId;
    private int itemrating;
    private String itemfeedback;
    public updateUserOrderFragment(int itemId, int itemrating, String itemfeedback) {
        this.itemId = itemId;
        this.itemrating = itemrating;
        this.itemfeedback = itemfeedback;
    }
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
        View view = inflater.inflate(R.layout.fragment_update_user_order, container, false);
        int itemId = this.itemId;
        EditText feedback=view.findViewById(R.id.feedback);
        Spinner ratingSpinner = view.findViewById(R.id.ratingSpinner);
        // Use Integer instead of int
        Integer[] ratingValues = {1, 2, 3, 4, 5};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ratingValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter);
        Button submitUpdateButton = view.findViewById(R.id.updatefeedback);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        submitUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = Integer.parseInt(ratingSpinner.getSelectedItem().toString());
                String feedbacktext = feedback.getText().toString();
                Boolean update = databaseHelper.updateUserOrderFeedbackAndRating(itemId, feedbacktext, rating);
                if (update) {
                    Toast.makeText(requireContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
                    openFragment(new ordermainFragment());
                } else {
                    Toast.makeText(requireContext(), "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}