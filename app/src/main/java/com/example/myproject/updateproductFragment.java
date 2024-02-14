package com.example.myproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class updateproductFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST1 = 1;
    private byte[] selectedImageBytes1;

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    private int productId;
    private String productName;
    private String productDescription;
    private byte[] productImage;
    private String productbarcode;

    private int productCategory;
    private int productPrice;
    private String productQuantity;

    public updateproductFragment(int productId, String productName, String productDescription,
                                 byte[] productImage,String productbarcode, int productCategory, int productPrice,
                                 String productQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productImage = productImage;
        this.productbarcode = productbarcode;
        this.productCategory = productCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    private int findCategoryPosition(List<Category> categoryList, int categoryId) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId() == categoryId) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_updateproduct, container, false);
        EditText proNameUpdateEditText = view.findViewById(R.id.product_name);
        EditText proDescUpdateEditText = view.findViewById(R.id.product_desc);
        ImageView proImageUpdateImageView = view.findViewById(R.id.product_image);
        EditText proImageUpdatebarView = view.findViewById(R.id.product_barcode);
        Spinner catIdUpdateSpinner = view.findViewById(R.id.catId);
        EditText proPriceUpdateEditText = view.findViewById(R.id.product_price);
        EditText proQuantityUpdateEditText = view.findViewById(R.id.product_quantity);
        Button submitUpdateButton = view.findViewById(R.id.Submit_product);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());

        int productId = this.productId;
        proNameUpdateEditText.setText(productName);
        proDescUpdateEditText.setText(productDescription);
        proPriceUpdateEditText.setText(String.valueOf(productPrice));
        proQuantityUpdateEditText.setText(productQuantity);
        proImageUpdatebarView.setText(productbarcode);
        if (productImage != null && productImage.length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(productImage, 0, productImage.length);
            proImageUpdateImageView.setImageBitmap(bitmap);
        }

        List<Category> categoryList = databaseHelper.getAllCategories();
        ArrayAdapter<Category> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categoryList
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        catIdUpdateSpinner.setAdapter(spinnerAdapter);
        int selectedCategoryPosition = findCategoryPosition(categoryList, productCategory);
        catIdUpdateSpinner.setSelection(selectedCategoryPosition);
        proImageUpdateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(PICK_IMAGE_REQUEST1);
            }
        });

        submitUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedProductName = proNameUpdateEditText.getText().toString();
                String updatedProductDesc = proDescUpdateEditText.getText().toString();
                String updatedProductbarcode = proImageUpdatebarView.getText().toString();
                int updatedProductPrice = Integer.parseInt(proPriceUpdateEditText.getText().toString());
                int updatedProductQuantity = Integer.parseInt(proQuantityUpdateEditText.getText().toString());
                Category selectedCategory = (Category) catIdUpdateSpinner.getSelectedItem();
                int updatedCategoryId = selectedCategory.getId();
                byte[] updatedProductImage = (selectedImageBytes1 != null && selectedImageBytes1.length > 0) ?
                        selectedImageBytes1 : productImage;


                Boolean update = databaseHelper.UpdateProduct(
                        productId,
                        updatedProductName,
                        updatedProductDesc,
                        updatedProductImage,
                        updatedProductbarcode,
                        updatedCategoryId,
                        updatedProductPrice,
                        updatedProductQuantity
                );
                if (update) {
                    Toast.makeText(requireContext(), "Product updated successfully", Toast.LENGTH_SHORT).show();
                    openFragment(new productFragment());
                } else {
                    Toast.makeText(requireContext(), "Failed to update product", Toast.LENGTH_SHORT).show();
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
        if ((requestCode == PICK_IMAGE_REQUEST1 ) &&
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