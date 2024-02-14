package com.example.myproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class viewcatFragment extends Fragment {

    private int catId;
    public viewcatFragment(int viewcatId) {
        this.catId = viewcatId;
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
        View view= inflater.inflate(R.layout.fragment_viewcat, container, false);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        List<Product> products = databaseHelper.getAllCATProducts(catId);
        LinearLayout parentLayout = view.findViewById(R.id.parentLayout);
        Category category =databaseHelper.getOnecat(catId);
        TextView catName=view.findViewById(R.id.catName);
        catName.setText(category.getName());
        for (Product product : products) {
            CardView cardView = new CardView(requireContext());
            cardView.setCardElevation(getResources().getDimension(R.dimen.card_elevation));
            cardView.setRadius(getResources().getDimension(R.dimen.card_corner_radius));
            cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            cardView.setMaxCardElevation(getResources().getDimension(R.dimen.card_max_elevation));
            cardView.setUseCompatPadding(true);
            cardView.setPreventCornerOverlap(true);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(20, 0, 0, 0);
            cardView.setLayoutParams(cardParams);
            LinearLayout verticalLayout = new LinearLayout(requireContext());
            verticalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            verticalLayout.setOrientation(LinearLayout.VERTICAL);
            ImageView imageView = new ImageView(requireContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.image_height)
            );

            byte[] productImage = product.getImage();
            if (productImage != null && productImage.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
                imageView.setImageBitmap(bitmap);
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(imageParams);

            TextView textView = new TextView(requireContext());
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textView.setLayoutParams(textParams);
            textView.setText(product.getName());
            textView.setTextSize(18);
            textParams.setMargins(380, 10, 16, 0);
            textView.setTextColor(Color.BLACK);
            Button detailButton = new Button(requireContext());
            LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            detailButton.setLayoutParams(deleteButtonParams);
            deleteButtonParams.setMargins(16, 10, 16, 20);
            detailButton.setLayoutParams(deleteButtonParams);
            detailButton.setText("More Details");
            detailButton.setTextSize(18);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(getResources().getColor(R.color.lavender));
            drawable.setCornerRadius(30);
            detailButton.setBackground(drawable);
            deleteButtonParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int productIdToshow = product.getId();
                    openFragment(new viewproductFragment(productIdToshow));
                }
            });
            verticalLayout.addView(imageView);
            verticalLayout.addView(textView);
            verticalLayout.addView(detailButton);
            cardView.addView(verticalLayout);
            parentLayout.addView(cardView);
        }

        return view;
    }
}