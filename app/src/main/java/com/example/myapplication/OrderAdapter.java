package com.example.myapplication;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.widget.Toast.LENGTH_SHORT;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<orderitem> itemList;
    private Context context;
    private FragmentManager fragmentManager; // Added FragmentManager field

    private SQLiteDatabase sqLiteDatabase;

    private DBHelper dbHelper;
    public String status = "";
    public  String orderStatus;

    public OrderAdapter(Context context, List<orderitem> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.dbHelper = new DBHelper(context);
        this.sqLiteDatabase = dbHelper.getWritableDatabase();
    }


    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlayout, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        orderitem orderitem = itemList.get(position);
        holder.imageView.setImageBitmap(orderitem.getProductimage());
        holder.textView.setText(orderitem.getProductname());
        holder.textView1.setText(String.valueOf(orderitem.getProductprice()));
        holder.qty.setText(String.valueOf(orderitem.getQty()));


        int orderId = orderitem.getOrderid();
        // Assuming you have access to the order status from your database or model
        // Assuming you have access to the order status from your database or model
         orderStatus= getOrderStatus(orderId); // Replace this with your method to get order status
            holder.sta.setText(orderStatus);
        //holder.sta.setText(orderStatus);


// Check the status of the order and set visibility of cancel order button accordingly
        if (orderStatus.equals("Approved")) {
            holder.cancleorder.setVisibility(View.VISIBLE);
        } else {
            holder.cancleorder.setVisibility(View.INVISIBLE);
        }


// Click listener for cancel order button
        holder.cancleorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to cancel the order?");

                // Add buttons for Yes and No
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update the status of the order to "Canceled" in the database
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String query = "UPDATE `order` SET Status = 'Canceled' WHERE orderid = ?";
                        db.execSQL(query, new String[]{String.valueOf(orderId)});
                        db.close();

                        // Update the UI to hide the "cancel order" button
                        holder.cancleorder.setVisibility(View.INVISIBLE);
                        holder.sta.setText("Canceled");

                        holder.returnorder.setVisibility(View.INVISIBLE);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, just dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        if(orderStatus.equals("Canceled"))
        {
            holder.sta.setText("Canceled");
        }






// Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

// Get the current time in HH:mm format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTimeHHmm = sdf.format(new Date(currentTimeMillis));

// Get the time of the order in HH:mm format from the database
        String orderTimeHHmm = getOrderTime(orderId); // Assuming you have a method to get order time from the database

// Parse the HH:mm strings to Date objects
        Date currentDate = null;
        try {
            currentDate = sdf.parse(currentTimeHHmm);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date orderDate = null;
        try {
            orderDate = sdf.parse(orderTimeHHmm);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

// Calculate the difference in milliseconds between the current time and the time of the order
        long timeDifferenceMillis = currentDate.getTime() - orderDate.getTime();

// Check if the time difference is greater than or equal to 1 minute (60000 milliseconds)
        if(orderStatus.equals("Approved")) {
            if (timeDifferenceMillis >= 21600000) {
                holder.returnorder.setVisibility(View.VISIBLE);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String query = "UPDATE `order` SET Status = 'Delivered' WHERE orderid = ?";
                db.execSQL(query, new String[]{String.valueOf(orderId)});
                db.close();
            } else {
                holder.returnorder.setVisibility(View.INVISIBLE);

            }
        }



        String returnStatus = getReturnStatus(orderId); // Replace this with your method to get order status

// Check the status of the order and set visibility of cancel order button accordingly
        if (orderStatus.equals("Delivered")) {
            holder.returnorder.setVisibility(View.VISIBLE);
            holder.sta.setText("Delivered");
        } else {
            holder.returnorder.setVisibility(View.INVISIBLE);
        }


// Click listener for return order button
        holder.returnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to return the order?");

                // Add buttons for Yes and No
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        String query = "UPDATE `order` SET Status = 'Returned' WHERE orderid = ?";
                        db.execSQL(query, new String[]{String.valueOf(orderId)});
                        db.close();

                        holder.cancleorder.setVisibility(View.INVISIBLE);
                        //holder.sta.setText(orderStatus);
                        holder.sta.setText("Returned");
                        holder.returnorder.setVisibility(View.INVISIBLE);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing, just dismiss the dialog
                        dialog.dismiss();
                    }
                });

                // Show the AlertDialog
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    // Method to retrieve the status of an order from the database
    @SuppressLint("Range")
    private String getOrderStatus(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String status = "";
        Cursor cursor = db.rawQuery("SELECT Status FROM `order` WHERE orderid = ?", new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndex("Status"));
        }
        cursor.close();
        db.close();
        return status;
    }


    @SuppressLint("Range")
    public String getReturnStatus(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT Status FROM `order` WHERE orderid = ?", new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndex("Status"));
        }
        cursor.close();
        db.close();
        return status;
    }

    @SuppressLint("Range")
    private String getOrderTime(int orderId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String time = "";
        Cursor cursor = db.rawQuery("SELECT time FROM `order` WHERE orderid = ?", new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            time = cursor.getString(cursor.getColumnIndex("Time"));
        }
        cursor.close();
        db.close();
        return time;
    }







    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public TextView qty;
        public Button cancleorder;
        public Button returnorder;
        public TextView sta;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = itemView.findViewById(R.id.productimage);
            textView = itemView.findViewById(R.id.productname);
            textView1 = itemView.findViewById(R.id.productprice);
            qty = itemView.findViewById(R.id.qty);
            cancleorder = itemView.findViewById(R.id.cancelorder);
            returnorder = itemView.findViewById(R.id.returnorder);
            sta=itemView.findViewById(R.id.Statues);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
