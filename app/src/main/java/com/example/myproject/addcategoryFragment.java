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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class addcategoryFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] selectedImageBytes;
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
        View view= inflater.inflate(R.layout.fragment_addcategory, container, false);
        EditText cat_name=view.findViewById(R.id.cat_name);
        ImageView productImageView = view.findViewById(R.id.product_image);
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        Button add=view.findViewById(R.id.Submit);
        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String name = cat_name.getText().toString();
        if (selectedImageBytes != null) {
            Boolean insert = databaseHelper.insertcategory(name,selectedImageBytes);
            if (insert) {
                openFragment(new categoryFragment());
                Toast.makeText(getContext(), "add category Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "add category failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show();
        }


    }
});

        return view;
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                String fileExtension = getFileExtension(imageUri);
                Bitmap.CompressFormat compressFormat = getCompressFormat(fileExtension);
                int quality = 100;
                bitmap.compress(compressFormat, quality, stream);
                selectedImageBytes = stream.toByteArray();
                ImageView productImageView = requireView().findViewById(R.id.product_image);
                productImageView.setImageBitmap(bitmap);
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