package com.example.myproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addproductFragment extends Fragment {
    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private static final int PICK_IMAGE_REQUEST1 = 1;
    private byte[] selectedImageBytes1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_addproduct, container, false);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        ImageView productImageView = view.findViewById(R.id.product_image);

        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(PICK_IMAGE_REQUEST1);
            }
        });
        Spinner catIdSpinner = view.findViewById(R.id.catId);
        List<Category> categories = databaseHelper.getAllCategories();
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categoryNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catIdSpinner.setAdapter(spinnerAdapter);
        Button submitProductButton = view.findViewById(R.id.Submit_product);
        submitProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int productcat = -1;
                String productName = ((EditText) view.findViewById(R.id.product_name)).getText().toString();
                String productDescription = ((EditText) view.findViewById(R.id.product_desc)).getText().toString();
                String productbarcode = ((EditText) view.findViewById(R.id.product_barcode)).getText().toString();

                String productPrice = ((EditText) view.findViewById(R.id.product_price)).getText().toString();
                String selectedCategoryName = catIdSpinner.getSelectedItem().toString();
                for (Category category : categories) {
                    if (category.getName().equals(selectedCategoryName)) {
                        productcat = category.getId();
                        break;
                    }
                }
                String productQuantity = ((EditText) view.findViewById(R.id.product_quantity)).getText().toString();
                if (selectedImageBytes1 != null ) {
                    boolean isInserted = databaseHelper.insertProduct(productName, productDescription, selectedImageBytes1,productbarcode,productcat ,productPrice, productQuantity);
                    if (isInserted) {
                        openFragment(new productFragment());
                        Toast.makeText(requireContext(), "Product inserted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Error inserting product", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PICK_IMAGE_REQUEST1) &&
                resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                String fileExtension = getFileExtension(imageUri);
                Bitmap.CompressFormat compressFormat = getCompressFormat(fileExtension);
                int quality = 100;
                bitmap.compress(compressFormat, quality, stream);
                byte[] selectedImageBytes = stream.toByteArray();

                if (requestCode == PICK_IMAGE_REQUEST1) {
                    selectedImageBytes1 = selectedImageBytes;
                    ImageView productImageView1 = requireView().findViewById(R.id.product_image);
                    productImageView1.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getFileExtension(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath.substring(filePath.lastIndexOf(".") + 1);
        }
        return "png";
    }
    private Bitmap.CompressFormat getCompressFormat(String fileExtension) {
        switch (fileExtension.toLowerCase()) {
            case "jpeg":
            case "jpg":
                return Bitmap.CompressFormat.JPEG;
            case "png":
                return Bitmap.CompressFormat.PNG;
            case "webp":
                return Bitmap.CompressFormat.WEBP;
            default:
                return Bitmap.CompressFormat.PNG;
        }
    }
}