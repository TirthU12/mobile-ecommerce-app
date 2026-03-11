package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class home extends AppCompatActivity {
    SharedPreferences sp;
    int count=0;

    Button logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp=this.getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean isLoggedIn=sp.getBoolean("isLoggedIn",false);
        if(isLoggedIn)
        {
            setContentView(R.layout.activity_home);

        }
        else {
            // Redirect to the login screen
            Intent intent = new Intent(home.this, login.class);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent going back to it
        }
        int userId = sp.getInt(login.UserId, -1);


        /*logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp=getSharedPreferences(login.MyPREFERENCES,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.clear();
                editor.apply();
                editor.commit();
                Intent intent = new Intent(home.this, login.class);
                startActivity(intent);

            }
        });*/

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId=item.getItemId();
                if(itemId==R.id.edit_profile)
                {
                    startActivity(new Intent(getApplicationContext(), profile.class));
                    overridePendingTransition(0, 0);


                    return true;
                }
                if(itemId==R.id.home)
                {
                    return true;
                }
                if(itemId==R.id.order)
                {
                    startActivity(new Intent(getApplicationContext(), order.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (itemId==R.id.cart)
                {
                    startActivity(new Intent(getApplicationContext(), cart.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

        // Perform item selected listener



        DBHelper dbHelper = new DBHelper(this); // Initialize your database helper.
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Open the database for reading.


        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%samsung%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.ImageDynamic);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }



        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%vivo%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.vivomobiles);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }


        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%apple%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.applemobile);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }


        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%realme%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.realmemobile);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }


        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%redmi%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.redmimobile);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }


        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%oppo%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.oppomobille);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }


        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%oneplus%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.oneplusmobile);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }


        if (db != null) {
            // Query to retrieve the hexadecimal string from the "student" table
            //String query = "SELECT * FROM imag WHERE lower(company)='samsung' AND category='Mobile' ORDER BY RANDOM() LIMIT 6  "; // Adjust the column name and condition as needed
            //String query= "SELECT * FROM imag WHERE company='OnePlus' AND category='Mobile' ORDER BY  RANDOM() LIMIT 6";
            //String query = "SELECT * FROM imag WHERE company='nothing' AND category='mobile'ORDER BY RANDOM() LIMIT 6";
            String query = "SELECT * FROM imag WHERE company like '%poco%' AND category='mobile' GROUP BY name ORDER BY RANDOM() LIMIT 6";




            Cursor cursor = db.rawQuery(query, null);
            LinearLayout imageContainer = findViewById(R.id.pocomobile);
            int imageSize = 400; // Size in pixels, adjust as needed



            if (cursor != null) {
                while (cursor.moveToNext()) {
                    // Retrieve the image data as a hexadecimal string
                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("im"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    // Get the name from the cursor
                    @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));

                    // Create a horizontal LinearLayout for each image and text pair
                    LinearLayout horizontalLayout = new LinearLayout(this);
                    horizontalLayout.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    horizontalLayout.setLayoutParams(params);

                    imageContainer.addView(horizontalLayout);

                    // Create an ImageView
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);

                    // Create a TextView
                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(14);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    @SuppressLint("Range") int ImageId = cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(ImageId);

                    // Set the orientation to vertical
                    horizontalLayout.setOrientation(LinearLayout.VERTICAL);

                    // Add the ImageView and TextView to the horizontal LinearLayout
                    horizontalLayout.addView(imageView);
                    horizontalLayout.addView(textView);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage = (int) view.getTag();
                            Intent intent = new Intent(home.this, detail.class);
                            intent.putExtra("idImage", idImage);
                            startActivity(intent);
                            //Toast.makeText(home.this, "Id" + ImageId, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                cursor.close();
            }
        }
        if(db!=null)
        {
            String cartitemcountquery="SELECT * FROM cart";
            Cursor cursor = db.rawQuery(cartitemcountquery, null);

            int a=cursor.getCount();

            TextView textView=findViewById(R.id.cartitemcount);
            textView.setText(String.valueOf(a));
        }
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle the search query submission
                performSearch(query);
                return true;
            }

            private void performSearch(String query) {
                // Toast the search query
               // Toast.makeText(home.this, "Search query: " + query, Toast.LENGTH_SHORT).show();

                // Send the search query to the search activity
                Intent intent = new Intent(home.this, search.class);
                intent.putExtra("query", query);
                startActivity(intent);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle changes in the search query text
                // You may choose to perform incremental search here
                return true;
            }
        });



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
}
