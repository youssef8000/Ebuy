package com.example.myproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class viewproductFragment extends Fragment {
    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private int productId;
    private int productcatId;
    public viewproductFragment(int viewproductId) {
        this.productId = viewproductId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_viewproduct, container, false);
        TextView proNameUpdateEditText = view.findViewById(R.id.productNameTextView);
        TextView proDescUpdateEditText = view.findViewById(R.id.descTextView);
        ImageView proImageUpdateImageView = view.findViewById(R.id.productImageView);
        TextView procatUpdateEditText = view.findViewById(R.id.categoryTextView);
        TextView proPriceUpdateEditText = view.findViewById(R.id.priceTextView);
        Button submitUpdateButton = view.findViewById(R.id.moreDetailsButton);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        Product product = databaseHelper.getOneProduct(productId);
        productcatId=product.getCategory();
        Category category=databaseHelper.getOnecatProduct(productcatId);
        if (product != null) {
            proNameUpdateEditText.setText("Name: "+product.getName());
            proDescUpdateEditText.setText("Description: "+product.getDescription());
            if (category!=null){
                procatUpdateEditText.setText("Category: "+category.getName());
            }
            proPriceUpdateEditText.setText("Price: "+String.valueOf(product.getPrice()));
            byte[] productImage = product.getImage();
            if (productImage != null && productImage.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
                proImageUpdateImageView.setImageBitmap(bitmap);
            }
        }
        submitUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = requireActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                int userId = preferences.getInt("user_id", -1);
                if (userId != -1) {
                    boolean inserted = databaseHelper.insertShoppingCartItem(userId, productId, 1, product.getPrice());
                    if (inserted) {
                        Toast.makeText(requireContext(), "Product added to shopping cart", Toast.LENGTH_SHORT).show();
                        openFragment(new shoppingcartFragment());
                    } else {
                        Toast.makeText(requireContext(), "Failed to add product to shopping cart", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Please log in to add the product to the shopping cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}