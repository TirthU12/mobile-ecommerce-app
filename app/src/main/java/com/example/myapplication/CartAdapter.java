package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<cartitem> itemList;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    public double totalPrice;
    public OnTotalPriceChangeListener onTotalPriceChangeListener;
    public NoItemInCart noItemInCart;
    int quantity = 0;

    public CartAdapter(Context context, List<cartitem> itemList,OnTotalPriceChangeListener listener ,NoItemInCart listener1) {
        this.context = context;
        this.itemList = itemList;
        this.onTotalPriceChangeListener=listener;
        this.noItemInCart=listener1;

        // Initialize database helper and shared preferences
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        sharedPreferences = context.getSharedPreferences("CartQuantities", Context.MODE_PRIVATE);

    }
    public interface OnTotalPriceChangeListener {
        void onTotalPriceChanged(double total);
    }

    public interface NoItemInCart{
        void onNoItemInCart();

    }



    @NonNull

    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlayout, parent, false);
        return new CartViewHolder(view);

    }




    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        // Bind data to views for each item in the RecyclerView
        cartitem cartItem = itemList.get(position);
        holder.imageView.setImageBitmap(cartItem.getProductimage());
        holder.textView.setText(cartItem.getProductname());
        int initialProductPrice = cartItem.getProductprice();
        holder.textView1.setText(String.valueOf(initialProductPrice));


        // Retrieve stored quantity from SharedPreferences or database
        int storedQuantity = getStoredQuantity(cartItem.getId());
        ArrayAdapter<String> spinnerAdapter = createSpinnerAdapter();
        holder.quantitySpinner.setAdapter(spinnerAdapter);

        // Set the default quantity or the stored quantity
        holder.quantitySpinner.setSelection(getQuantityIndex(storedQuantity != -1 ? storedQuantity : 1));

        // Set spinner listener to save the selected quantity and update the cart
        holder.quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                int selectedQuantity = Integer.parseInt(spinnerAdapter.getItem(pos));
                holder.textView41.setText(String.valueOf(selectedQuantity));

                saveQuantityToDatabase(cartItem.getId(), selectedQuantity);
                updateCart(cartItem.getId(), selectedQuantity, initialProductPrice * selectedQuantity);

                holder.textView1.setText(String.valueOf(initialProductPrice * selectedQuantity));

            }


            private void updateCart(int productId, int quantity, int updatedProductPrice) {

                    if (sqLiteDatabase != null) {
                        try {
                            sqLiteDatabase.beginTransaction();

                            String updateQuery = "UPDATE cart SET qty = ?, price = ? WHERE product_id = ?";
                            sqLiteDatabase.execSQL(updateQuery, new Object[]{quantity, updatedProductPrice, productId});

                            sqLiteDatabase.setTransactionSuccessful();
                            calculatePrice();


                        } catch (Exception e) {
                            Log.e("CartAdapter", "Error updating cart", e);
                        } finally {
                            sqLiteDatabase.endTransaction();
                        }
                    }
                }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Button listeners for removing item from cart or buying now
        holder.buttonRemove.setOnClickListener(view -> {
            int productId = cartItem.getId();
            int adapterPosition = holder.getAdapterPosition();
            removeItem(adapterPosition);
            removeProductFromCart(productId);
            SharedPreferences sharedPreferences1= context.getSharedPreferences("detail",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences1.edit();
            //String keytoremove="ITEM_IN_CART_" + productId;
            editor.remove("ITEM_IN_CART_"+productId);
            editor.apply();

            calculatePrice();
            checkItemInCart();
            notifyDataSetChanged();




        });

        holder.buttonBuyNow.setOnClickListener(view -> {
            int productId = cartItem.getId();
            int productPrice = cartItem.getProductprice();
            int selectedQuantity = Integer.parseInt(holder.quantitySpinner.getSelectedItem().toString());
            int updatedProductPrice = productPrice * selectedQuantity;

            Intent paymentIntent = new Intent(context, payment.class);
            paymentIntent.putExtra("productId", productId);
            paymentIntent.putExtra("selectedQuantity", selectedQuantity);
            paymentIntent.putExtra("updatedProductPrice", updatedProductPrice);
            paymentIntent.putExtra("initialProductPrice", productPrice);
            paymentIntent.putExtra("buyNowClicked", true);
            context.startActivity(paymentIntent);
        });
    }

    private int getStoredQuantity(int productId) {
        // Retrieve quantity from the cart table in the database

        if (sqLiteDatabase != null) {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT qty FROM cart WHERE product_id = ?", new String[]{String.valueOf(productId)});
            if (cursor.moveToFirst()) {
                quantity = cursor.getInt(0);
            }
            cursor.close();
        }
        return quantity;
    }

    private void saveQuantityToDatabase(int productId, int quantity) {
        // Save or update quantity in the cart table of the database
        if (sqLiteDatabase != null) {
            ContentValues values = new ContentValues();
            values.put("qty", quantity);
            long result = sqLiteDatabase.update("cart", values, "product_id = ?", new String[]{String.valueOf(productId)});
            if (result == 0) {
                // If no rows were updated, insert a new row
                values.put("product_id", productId);
                sqLiteDatabase.insert("cart", null, values);
            }
        }
    }

    private ArrayAdapter<String> createSpinnerAdapter() {

        List<String> quantities = Arrays.asList("1", "2", "3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, quantities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private int getQuantityIndex(int quantity) {
        // Get the index of the quantity in the spinner adapter
        List<String> quantities = Arrays.asList("1", "2", "3");
        return quantities.indexOf(String.valueOf(quantity));
    }
    public double calculatePrice()
    {

        String query = "SELECT SUM(price) FROM cart";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        double total = 0;

        if (cursor.moveToNext()) {
            total = cursor.getDouble(0);
        }

        if(onTotalPriceChangeListener!=null)
        {
            onTotalPriceChangeListener.onTotalPriceChanged(total);

        }
        return totalPrice;
    }

    public boolean checkItemInCart(){
        String query="SELECT * from cart";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);

            if(cursor.getCount()==0)
            {
                if(noItemInCart!=null)
                {
                    noItemInCart.onNoItemInCart();
                }
                return true;
            }



        return false;
    }





    private void removeItem(int position) {
        // Remove item from the itemList and notify adapter
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    private void removeProductFromCart(int productId) {
        // Remove product from the cart table in the database
        if (sqLiteDatabase != null) {
            String deleteQuery = "DELETE FROM cart WHERE product_id='" + productId + "'";
            sqLiteDatabase.execSQL(deleteQuery);
        }
    }


    private void showToast(String message) {
        // Display a toast message
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the itemList
        return itemList.size();
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textView;
        public TextView textView1;
        public Spinner quantitySpinner;
        public Button buttonRemove;
        public Button buttonBuyNow;

        public TextView textView41;



        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productimage);
            textView = itemView.findViewById(R.id.productname);
            textView1 = itemView.findViewById(R.id.productprice);
            quantitySpinner = itemView.findViewById(R.id.quantitySpinner);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
            buttonBuyNow = itemView.findViewById(R.id.buttonBuyNow);
            textView41 = itemView.findViewById(R.id.textView41);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
