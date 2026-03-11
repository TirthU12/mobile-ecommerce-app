package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class cart extends AppCompatActivity implements CartAdapter.OnTotalPriceChangeListener,CartAdapter.NoItemInCart {
    public RecyclerView recyclerView;
    public CartAdapter cartAdapter;

    public ArrayAdapter<String> arrayAdapter;
    public DBHelper dbHelper;
    public SQLiteDatabase sqLiteDatabase;
    SharedPreferences sp;
    public int userId;
    Cursor cursor;
    public List<cartitem> itemList;
    public TextView textView3;

    @SuppressLint({"Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        
        recyclerView=findViewById(R.id.cartitemdynamic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.cart);
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
                if(itemId==R.id.cart)
                {
                    return true;
                }
                if(itemId==R.id.order)
                {
                    startActivity(new Intent(getApplicationContext(), order.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (itemId==R.id.home)
                {
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
            String cartquery="SELECT * FROM cart WHERE user_id='"+userId+"'";
            cursor=sqLiteDatabase.rawQuery(cartquery,null);

            int totalProductPrice = 0;
            //textView3 = findViewById(R.id.Total);

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
                    int product_qty = cursor.getInt(cursor.getColumnIndex("qty"));
                    int productprice1 = cursor.getInt(cursor.getColumnIndex("price"));

                    // Calculate the product price for the current item
                    int currentProductPrice = productprice * product_qty;
                    totalProductPrice += currentProductPrice;

                    cartitem ci = new cartitem(productid, userid, productname, decodedImage, productprice, product_qty, productprice1, textView3);
                    itemList.add(ci);
                }



            }
            else{

            }



        }


        cartAdapter=new CartAdapter(cart.this,itemList,this,this
        );
        recyclerView.setAdapter(cartAdapter);
// Place Order Button
        Button placeOrderButton = findViewById(R.id.button);
        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> productIds = getProductIdsFromCart();

                if (!productIds.isEmpty()) {
                    // Calculate the total product price



                    Intent paymentIntent = new Intent(cart.this, payment.class);
                    paymentIntent.putIntegerArrayListExtra("productIds", (ArrayList<Integer>) productIds);
                    //paymentIntent.putExtra("totalProductPrice", totalPrice);
                    startActivity(paymentIntent);
                } else {
                    showToast("Your cart is empty. Add products before placing an order.");

                }
            }
        });
        CartAdapter cartAdapter1=new CartAdapter(cart.this,itemList,this,this);
        //cartAdapter1.setOnTotalPriceChangeListener(this);
        cartAdapter1.calculatePrice();
        cartAdapter1.checkItemInCart();


    }


    private List<Integer> getProductIdsFromCart() {
        List<Integer> productIds = new ArrayList<>();

        if (sqLiteDatabase != null) {
            String cartQuery = "SELECT product_id FROM cart WHERE user_id='" + userId + "'";
            cursor = sqLiteDatabase.rawQuery(cartQuery, null);

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int productId = cursor.getInt(cursor.getColumnIndex("product_id"));
                productIds.add(productId);
            }

            cursor.close();
        }

        return productIds;
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

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTotalPriceChanged(double newTotalPrice) {
        textView3 = findViewById(R.id.Total);
        textView3.setText(String.valueOf(newTotalPrice));


    }

    @Override
    public void onNoItemInCart() {
        CardView view=findViewById(R.id.view3);
        view.setVisibility(View.INVISIBLE);
        ImageView imageView=findViewById(R.id.empty_cart);
        imageView.setVisibility(View.VISIBLE);

    }
}