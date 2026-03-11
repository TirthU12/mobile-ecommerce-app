package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class order extends AppCompatActivity {
    public RecyclerView recyclerView;
    public OrderAdapter orderAdapter;

    public ArrayAdapter<String> arrayAdapter;
    public DBHelper dbHelper;
    public SQLiteDatabase sqLiteDatabase;
    SharedPreferences sp;
    public int userId;
    Cursor cursor;
    public List<orderitem> itemList;
    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        recyclerView=findViewById(R.id.orderdynamic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.edit_profile) {
                    startActivity(new Intent(getApplicationContext(), profile.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.cart) {
                    startActivity(new Intent(getApplicationContext(), cart.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (itemId == R.id.order) {

                    return true;
                } else if (itemId == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

        sp=getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
                userId = sp.getInt(login.UserId, -1);



        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getReadableDatabase();
        itemList=new ArrayList<>();


        if(sqLiteDatabase!=null)
        {
            String orderquery = "SELECT * FROM `order` WHERE user_id='" + userId + "'";

            cursor=sqLiteDatabase.rawQuery(orderquery,null);

            if(cursor.getCount()>0) {

                while (cursor.moveToNext()) {
                    int productid = cursor.getInt(cursor.getColumnIndex("product_id"));
                    int userid = cursor.getInt(cursor.getColumnIndex("user_id"));

                    @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("image"));
                    // Convert the hexadecimal string to a byte array
                    byte[] imageBytes = hexStringToByteArray(hexImage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    String productname = cursor.getString(cursor.getColumnIndex("product_name"));
                    int productprice = cursor.getInt(cursor.getColumnIndex("product_price"));
                    int qty = cursor.getInt(cursor.getColumnIndex("Qty"));
                    int orderid = cursor.getInt(cursor.getColumnIndex("orderid"));
                    String statues = cursor.getString(cursor.getColumnIndex("Status"));


                    orderitem oi = new orderitem(productid, userid, productname, decodedImage, productprice, qty, orderid, statues);
                    itemList.add(oi);

                }
            }
            else{

                ImageView imageView=findViewById(R.id.no_order_found);
                imageView.setVisibility(View.VISIBLE);


            }





        }

        orderAdapter=new OrderAdapter(order.this,itemList);
        recyclerView.setAdapter(orderAdapter);


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