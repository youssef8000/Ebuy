package com.example.myproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class updateproquantityFragment extends Fragment {
    private int itemId;
    private int itemquantity;
    private int itemprice;
    public updateproquantityFragment(int itemId, int itemquantity, int itemprice) {
        this.itemId = itemId;
        this.itemquantity = itemquantity;
        this.itemprice = itemprice;
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
        View view = inflater.inflate(R.layout.fragment_updateproquantity, container, false);
        int itemId = this.itemId;
        int itemprice = this.itemprice;
       EditText quantity =view.findViewById(R.id.quantity);
        quantity.setText(String.valueOf(itemquantity));
        Button submitUpdateButton = view.findViewById(R.id.updatequantity);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        submitUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int updatedProductQuantity = Integer.parseInt(quantity.getText().toString());
                int updatedProductPrice =updatedProductQuantity*itemprice;
                Boolean update = databaseHelper.updateShoppingCartItem(itemId, updatedProductQuantity, updatedProductPrice);
                if (update) {
                    Toast.makeText(requireContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
                    openFragment(new shoppingcartFragment());
                } else {
                    Toast.makeText(requireContext(), "Failed to update product", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}