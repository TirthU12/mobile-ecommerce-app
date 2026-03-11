package com.example.myapplication;

import static com.example.myapplication.cart.hexStringToByteArray;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DBHelper;
import com.example.myapplication.R;
import com.example.myapplication.SearchAdapter;

import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    public SharedPreferences sp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        // Find the RecyclerView
        recyclerView = findViewById(R.id.searchdata);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        // Retrieve the search query from the intent
        Intent intent = getIntent();
        if (intent != null) {
            String query = intent.getStringExtra("query");
            // Toast the received search query
            //Toast.makeText(this, "Received search query: " + query, Toast.LENGTH_SHORT).show();

            String[] company = {"redmi", "oneplus", "poco", "xiaomi", "samsung", "apples", "realme", "vivo", "oppo", "google"};
            String[] category = {"mobile", "tablet"};

            // Open the database for reading
            sqLiteDatabase = dbHelper.getReadableDatabase();

            // Split the query into words
            String[] queryWords = query.split("\\s+");

            // Variables to store the company name and category
            String companyName = null;
            String categoryName = null;

            // Loop through each word in the query
            for (String word : queryWords) {
                // Check if the word matches a company name
                for (String companyWord : company) {
                    if (word.equalsIgnoreCase(companyWord)) {
                        // Store the company name
                        companyName = companyWord;
                        break; // Stop processing once a match is found
                    }
                }

                // Check if the word matches a category
                for (String categoryWord : category) {
                    if (word.equalsIgnoreCase(categoryWord)) {
                        // Store the category
                        categoryName = categoryWord;
                        break; // Stop processing once a match is found
                    }
                }
            }

            // If both company name and category are found, load products for that specific category
            if (companyName != null && categoryName != null) {
                loadproduct(companyName, categoryName);
            } else {
                // If no match is found for both company name and category, load all products
                loadallproduct(query);
            }
        }
    }

    // Method to load products from the database based on company and category
    private void loadproduct(String companyName, String categoryName) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM imag WHERE company = ? AND category = ?", new String[]{companyName, categoryName});

        if (cursor != null) {
            List<Product> productList = new ArrayList<>();

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String productImageHex = cursor.getString(cursor.getColumnIndex("im"));
                @SuppressLint("Range") int productPrice = cursor.getInt(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String Camera = cursor.getString(cursor.getColumnIndexOrThrow("camera"));

                @SuppressLint("Range") String Display = cursor.getString(cursor.getColumnIndex("display"));
                @SuppressLint("Range") String Ram=cursor.getString(cursor.getColumnIndex("Ram"));
                @SuppressLint("Range") String Rom=cursor.getString(cursor.getColumnIndex("Rom"));



                // Convert hex string to byte array
                byte[] imageBytes = hexStringToByteArray(productImageHex);

                // Create a Bitmap from the byte array
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                // Create a Product object with image data as a byte array
                Product product = new Product(id, productName, decodedImage, productPrice, Camera, Display,Ram,Rom);
                productList.add(product);
            }

            cursor.close();

            // Initialize and set the adapter with the loaded products
            adapter = new SearchAdapter(productList);
            recyclerView.setAdapter(adapter);
        }
    }

    //search on basics of the searched query
    private void loadallproduct(String query) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM imag WHERE name LIKE ?", new String[]{"%" + query + "%"});

        if (cursor != null) {
            List<Product> productList = new ArrayList<>();

            // Check if the result set is empty
            if (cursor.getCount() == 0) {
                // Fetch random products from the database
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM imag ORDER BY RANDOM() LIMIT 20", null);
            }

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String productImageHex = cursor.getString(cursor.getColumnIndex("im")); // Assuming "image" is the column name for the image data
                @SuppressLint("Range") int productPrice = cursor.getInt(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String Camera = cursor.getString(cursor.getColumnIndex("camera"));
                @SuppressLint("Range") String Display = cursor.getString(cursor.getColumnIndex("display"));
                @SuppressLint("Range") String Ram=cursor.getString(cursor.getColumnIndex("Ram"));
                @SuppressLint("Range") String Rom=cursor.getString(cursor.getColumnIndex("Rom"));

                // Convert hex string to byte array
                byte[] imageBytes = hexStringToByteArray(productImageHex);

                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


                // Create a Product object with image data as a byte array
                Product product = new Product(id, productName, decodedImage, productPrice, Camera, Display,Ram,Rom);
                productList.add(product);
            }

            cursor.close();

            // Initialize and set the adapter with the loaded products
            adapter = new SearchAdapter(productList);
            recyclerView.setAdapter(adapter);
        }
    }

    public static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        dbHelper.close();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // Check the submitted query against the "imag" table
        // checkNameInDatabase(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}


