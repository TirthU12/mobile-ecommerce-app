package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;

public class payment extends AppCompatActivity {

    public DBHelper dbHelper;
    public SQLiteDatabase sqLiteDatabase;
    SharedPreferences sp;
    public int userId;
    public String hexImage;
    Cursor cursor;
    Log log;
   public String orderidd;

    private static final String COLUMN_QUANTITY_CART = "qty";
    private static final String COLUMN_QUANTITY_ORDER = "Qty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);

        sp = getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean isLoggedIn=sp.getBoolean("isLoggedIn",false);
        if(isLoggedIn)
        {
            setContentView(R.layout.payment);

        }
        else {
            // Redirect to the login screen
            Intent intent = new Intent(payment.this, login.class);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent going back to it
        }
        userId = sp.getInt(login.UserId, -1);

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        TextView textView24 = findViewById(R.id.textView24);
        textView24.setText("Deliver To:");
        EditText addressEditText = findViewById(R.id.editTextTextMultiLine);

        if (sqLiteDatabase != null) {
            String adds = "SELECT * FROM user WHERE id='" + userId + "'";
            cursor = sqLiteDatabase.rawQuery(adds, null);

            if (cursor.moveToFirst()) {
                @SuppressLint("Range") String userAddress = cursor.getString(cursor.getColumnIndex("Address"));
                addressEditText.setText(userAddress);
            }

            cursor.close();
        }

        // Button for updating the address
        Button updateButton = findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newAddress = addressEditText.getText().toString();
                if (isValidAddress(newAddress)) {
                    newAddress = newAddress.replaceAll("^\\s+", "");
                    updateAddress(newAddress);
                } else {
                    showToast("Enter a proper address");
                }
            }
        });

        // Button for making payment
        Button makePaymentButton = findViewById(R.id.makepayment);
        makePaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPaymentOptionSelected()) {
                    showToast("Please select a payment option");
                } else {
                    // Check if "Buy Now" button was clicked
                    if (isBuyNowButtonClicked()) {
                        makePaymentForBuyNow();
                    } else {
                        // "Place Order" button was clicked
                        makePaymentForPlaceOrder();
                    }
                }
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            int productId = intent.getIntExtra("productId", 0);
            int selectedQuantity = intent.getIntExtra("selectedQuantity", 0);
            int initialProductPrice = intent.getIntExtra("initialProductPrice", -1);
            int updatedProductPrice = intent.getIntExtra("updatedProductPrice", -1);

            boolean buyNowClicked = intent.getBooleanExtra("buyNowClicked", false);

            TextView textView35 = findViewById(R.id.textView35);
            TextView textView37 = findViewById(R.id.textView37);

            if (buyNowClicked) {
                textView35.setText(String.valueOf(updatedProductPrice));
                textView37.setText(String.valueOf(updatedProductPrice));
            } else {
                ArrayList<Integer> productIds = intent.getIntegerArrayListExtra("productIds");
                int totalPriceFromCart = getTotalPriceFromCart(productIds);
                textView35.setText(String.valueOf(totalPriceFromCart));
                textView37.setText(String.valueOf(totalPriceFromCart));

            }
        }
    }

    private boolean isValidAddress(String address) {
        boolean isNotBlank = !TextUtils.isEmpty(address.trim());
        boolean containsOnlyNumbers = !address.matches(".*\\d.*");
        return isNotBlank && !containsOnlyNumbers;
    }

    private boolean isPaymentOptionSelected() {
        RadioGroup cashOnDelivery = findViewById(R.id.cashondelivery);
        int selectedId = cashOnDelivery.getCheckedRadioButtonId();
        return selectedId != -1;
    }

    private boolean isBuyNowButtonClicked() {
        Intent intent = getIntent();
        return intent != null && intent.getBooleanExtra("buyNowClicked", false);
    }


    private void removeProductFromCart(int productId) {
        if (sqLiteDatabase != null) {
            String deleteQuery = "DELETE FROM cart WHERE product_id='" + productId + "'";
            sqLiteDatabase.execSQL(deleteQuery);
        }
    }

    @SuppressLint("Range")
    private void insertProductIntoOrder(int productId) {
        if (sqLiteDatabase != null) {
            String selectQuery = "SELECT * FROM cart WHERE product_id='" + productId + "'";
            Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                @SuppressLint("Range") String userName = cursor.getString(cursor.getColumnIndex("user_id"));
                @SuppressLint("Range") String hexImage = cursor.getString(cursor.getColumnIndex("image"));
                @SuppressLint("Range") String productName = cursor.getString(cursor.getColumnIndex("product_name"));
                @SuppressLint("Range") int productPrice = cursor.getInt(cursor.getColumnIndex("price"));

                int quantity;

                if (cursor.getColumnIndex(COLUMN_QUANTITY_CART) != -1) {
                    // If column exists in cart table
                    quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY_CART));
                } else if (cursor.getColumnIndex(COLUMN_QUANTITY_ORDER) != -1) {
                    // If column exists in order table
                    quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY_ORDER));
                } else {
                    showToast("Invalid Input");
                    return;
                }


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yy");
                String date = dateFormat.format(new Date());

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                String time = timeFormat.format(new Date());
                String selectQuery1 = "SELECT * FROM 'order'";
                Cursor cursor1 = sqLiteDatabase.rawQuery(selectQuery1, null);
                if(cursor1.getCount()>0 && cursor1.moveToLast())
                {
                    String orderid=cursor1.getString(cursor1.getColumnIndex("orderid"));

                    int orid=Integer.parseInt(orderid);
                    int norid=orid+1;
                     orderidd= String.valueOf(norid);

                }
                else{
                     orderidd="1";
                }


                String insertQuery = "INSERT INTO `order` (product_id, user_id, image, product_name, product_price, Qty, Date, Time,orderid) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
                SQLiteStatement statement = sqLiteDatabase.compileStatement(insertQuery);
                statement.bindString(1, String.valueOf(productId));
                statement.bindString(2, userName);
                statement.bindString(3, hexImage);
                statement.bindString(4, productName);
                statement.bindDouble(5, productPrice);
                statement.bindDouble(6, quantity);
                statement.bindString(7 ,date);
                statement.bindString(8, time);
                statement.bindString(9,orderidd);

                statement.executeInsert();
            } else {
                showToast("Invalid Input");
            }

            cursor.close();
        }
    }

    private int getTotalPriceFromCart(List<Integer> productIds) {
        int totalPrice = 0;

        if (sqLiteDatabase != null && productIds != null) {
            for (int productId : productIds) {
                String query = "SELECT price FROM cart WHERE product_id='" + productId + "'";
                Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                if (cursor.moveToFirst()) {
                    @SuppressLint("Range") int price = cursor.getInt(cursor.getColumnIndex("price"));
                    totalPrice += price;
                }
                cursor.close();
            }
        }

        return totalPrice;
    }

    private void updateAddress(String newAddress) {
        if (sqLiteDatabase != null) {
            String updateQuery = "UPDATE user SET Address=? WHERE id=?";
            sqLiteDatabase.execSQL(updateQuery, new Object[]{newAddress, userId});
            showToast("Address updated successfully");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void makePaymentForBuyNow() {
        RadioGroup cashOnDelivery = findViewById(R.id.cashondelivery);
        int selectedId = cashOnDelivery.getCheckedRadioButtonId();

        if (selectedId != -1) {
            Intent intent = getIntent();

            if (intent != null) {
                int productId = intent.getIntExtra("productId", 0);
               /* int selectedQuantity = intent.getIntExtra("selectedQuantity", 0);
                int initialProductPrice = intent.getIntExtra("initialProductPrice", -1);
                int updatedProductPrice = intent.getIntExtra("updatedProductPrice", -1);
*/
                processPaymentForBuyNow(productId);

                RadioButton selectedRadioButton = findViewById(selectedId);
                String paymentOption = selectedRadioButton.getText().toString();
                showToast("Thank You for Order😊😊");

                // Update order status to Approved
                if (sqLiteDatabase != null) {
                    String updateQuery = "UPDATE `order` SET Status='Approved' WHERE user_id=? AND orderid=?";
                    sqLiteDatabase.execSQL(updateQuery, new Object[]{userId,orderidd});
                    Intent intent1=new Intent(this,home.class);
                    startActivity(intent1);
                }

                finish();
            } else {
                showToast("Error: Intent is null.");
            }
        } else {
            showToast("Error: Payment option not selected.");
        }
    }


    private void processPaymentForBuyNow(int productId) {
        try {
            sqLiteDatabase.beginTransaction();
            insertProductIntoOrder(productId);
            removeProductFromCart(productId);
            SharedPreferences sharedPreferences1= getSharedPreferences("detail",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences1.edit();
            String keytoremove="ITEM_IN_CART_" + productId;
            editor.remove(keytoremove);
            editor.apply();
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("PaymentActivity", "Error during payment process", e);
            showToast("Payment failed. Retry.");
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    private void makePaymentForPlaceOrder() {
        RadioGroup cashOnDelivery = findViewById(R.id.cashondelivery);
        int selectedId = cashOnDelivery.getCheckedRadioButtonId();

        if (selectedId != -1) {
            Intent intent = getIntent();

            if (intent != null) {
                ArrayList<Integer> productIds = intent.getIntegerArrayListExtra("productIds");

                if (productIds != null && !productIds.isEmpty()) {
                    try {
                        sqLiteDatabase.beginTransaction();
                        for (int productId : productIds) {
                            insertProductIntoOrder(productId);
                            removeProductFromCart(productId);
                            if (sqLiteDatabase != null) {
                                String updateQuery = "UPDATE `order` SET status='Approved' WHERE user_id=? AND orderid=?";
                                sqLiteDatabase.execSQL(updateQuery, new Object[]{userId,orderidd});
                            }
                            SharedPreferences sharedPreferences1=getSharedPreferences("detail",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences1.edit();
                            String keytoremove="ITEM_IN_CART_" + productId;
                            editor.remove(keytoremove);
                            editor.apply();

                        }
                        sqLiteDatabase.setTransactionSuccessful();
                        Intent intent1=new Intent(this,home.class);
                        startActivity(intent1);
                    } catch (Exception e) {
                        Log.e("PaymentActivity", "Error during payment process", e);
                        showToast("Payment failed. Retry.");
                    } finally {
                        sqLiteDatabase.endTransaction();
                    }

                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String paymentOption = selectedRadioButton.getText().toString();
                    showToast("Thank You For Order😊😊");

                    // Update order status to Approved

                    finish();
                } else {
                    showToast("Error: ProductIds list is null or empty.");
                }
            } else {
                showToast("Error: Intent is null.");
            }
        } else {
            showToast("Error: Payment option not selected.");
        }
    }

}
