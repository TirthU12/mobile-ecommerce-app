package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

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
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class detail extends AppCompatActivity {
TextView textView;
DBHelper dbHelper;
public String color;
public String ram;
public String rom;
public String productname;
public Cursor cursor;
public TextView ramtext;
public TextView romtext;
    public int ImageId;
    public String hexImage;
    public String price;
    public int product_id;
    public int userId;
   public Button addtocart;
   public int qty=1;
    SharedPreferences sp;
SQLiteDatabase sqLiteDatabase;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getReadableDatabase();

        sp=getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean isLoggedIn=sp.getBoolean("isLoggedIn",false);
        if(isLoggedIn)
        {
            setContentView(R.layout.activity_detail);

        }
        else {
            // Redirect to the login screen
            Intent intent = new Intent(detail.this, login.class);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent going back to it
        }
         userId = sp.getInt(login.UserId, -1);




        int ImageId=getIntent().getIntExtra("idImage",-1);


        if(sqLiteDatabase!=null)
        {
            String query = "SELECT * FROM imag WHERE id='" + ImageId + "'";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if(cursor!=null)
            {
                if(cursor.moveToFirst())
                {
                     hexImage=cursor.getString(cursor.getColumnIndex("im"));
                    byte[] imageBytes = hexStringToByteArray(hexImage);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    ImageView imageView=findViewById(R.id.imageView5);
                    imageView.setImageBitmap(decodedImage);

                    @SuppressLint("Range") String hexImage1=cursor.getString(cursor.getColumnIndex("im1"));
                    byte[] imageBytes1 = hexStringToByteArray(hexImage1);
                    Bitmap decodedImage1 = BitmapFactory.decodeByteArray(imageBytes1, 0, imageBytes1.length);
                    ImageView imageView1=findViewById(R.id.imageView6);
                    imageView1.setImageBitmap(decodedImage1);

                    @SuppressLint("Range") String hexImage2=cursor.getString(cursor.getColumnIndex("im2"));
                    byte[] imageBytes2 = hexStringToByteArray(hexImage2);
                    Bitmap decodedImage2 = BitmapFactory.decodeByteArray(imageBytes2, 0, imageBytes2.length);
                    ImageView imageView2=findViewById(R.id.imageView7);
                    imageView2.setImageBitmap(decodedImage2);

                    product_id=cursor.getInt(cursor.getColumnIndex("id"));

                     productname=cursor.getString(cursor.getColumnIndex("name"));
                    TextView textView1=findViewById(R.id.mobilename);
                    textView1.setText(productname);
                     price=cursor.getString(cursor.getColumnIndex("price"));
                    TextView textView2=findViewById(R.id.price);
                    textView2.setText(price);

                    color=cursor.getString(cursor.getColumnIndex("color"));
                    TextView textView3=findViewById(R.id.color);
                    textView3.setText(color);
                     ram=cursor.getString(cursor.getColumnIndex("Ram"));
                    TextView textView4=findViewById(R.id.ram);
                    textView4.setText(ram);

                      rom=cursor.getString(cursor.getColumnIndex("Rom"));
                    TextView textView5=findViewById(R.id.rom);
                    textView5.setText(rom);

                    @SuppressLint("Range") String camera=cursor.getString(cursor.getColumnIndex("camera"));
                    TextView textView6=findViewById(R.id.camera);
                    textView6.setText(camera);
                    /*textView6.setMaxLines(2);
                    textView6.setEllipsize(TextUtils.TruncateAt.END); // Ellipsize at the end if the text exceeds max lines
                    textView6.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);*/

                    @SuppressLint("Range") String pro=cursor.getString(cursor.getColumnIndex("processor"));
                    TextView textView7=findViewById(R.id.processor);
                    textView7.setText(pro);

                    @SuppressLint("Range") String battery=cursor.getString(cursor.getColumnIndex("Battery"));
                    TextView textView8=findViewById(R.id.battery);
                    textView8.setText(battery);

                    @SuppressLint("Range") String os=cursor.getString(cursor.getColumnIndex("Operating_System"));
                    TextView textView9=findViewById(R.id.os);
                    textView9.setText(os);
                    String display=cursor.getString(cursor.getColumnIndex("display"));
                    TextView displaytext=findViewById(R.id.display);
                    displaytext.setText(display);
                    String core=cursor.getString(cursor.getColumnIndex("Processor_core"));
                    TextView pro_core=findViewById(R.id.core);
                    pro_core.setText(core);

                    String primary_clock_speed=cursor.getString(cursor.getColumnIndex("Primary_Clock_Speed"));
                    TextView  pcs=findViewById(R.id.primaryclock);
                    pcs.setText(primary_clock_speed);

                    String networktype=cursor.getString(cursor.getColumnIndex("Network_type"));
                    TextView nt=findViewById(R.id.network_type);
                    nt.setText(networktype);

                    String warrenty=cursor.getString(cursor.getColumnIndex("warranty"));
                    TextView wrr=findViewById(R.id.warrenty);
                    wrr.setText(warrenty);





                }
            }





        }


        if(sqLiteDatabase!=null)
        {
            //String colourquery="SELECT * FROM imag WHERE name LIKE '"+ImageId+"' AND color NOT LIKE '"+color+"' AND Ram LIKE '"+ram+"' AND Rom LIKE '"+rom+"'";



                String colourquery = "SELECT * FROM imag WHERE name LIKE '%" +productname + "%' AND color NOT LIKE '%" + color + "%' AND Ram LIKE '%" + ram + "%' AND Rom LIKE '%" + rom + "%' GROUP BY color";
                 cursor = sqLiteDatabase.rawQuery(colourquery, null);

            //LinearLayout imageDynamic=findViewById(R.id.colorDynamic);

            LinearLayout colorDynamic = findViewById(R.id.colorDynamic);
            int imagesize=200;

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {

                    String colourimage=cursor.getString(cursor.getColumnIndex("im"));
                    byte[] colourimage2=hexStringToByteArray(colourimage);
                    Bitmap decodeImage=BitmapFactory.decodeByteArray(colourimage2,0,colourimage2.length);
                    String colourname=cursor.getString(cursor.getColumnIndex("color"));



                    LinearLayout vertical=new LinearLayout(this);
                    vertical.setOrientation(LinearLayout.VERTICAL);
                    vertical.setGravity(Gravity.CENTER);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    layoutParams.setMargins(0,0,20,0);
                    vertical.setLayoutParams(layoutParams);
                    colorDynamic.addView(vertical);



                    ImageView imageView=new ImageView(this);
                    imageView.setImageBitmap(decodeImage);
                    LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(
                            imagesize,
                            imagesize
                    );
                    layoutParams1.setMargins(0,0,0,0);
                    imageView.setLayoutParams(layoutParams1);

                    vertical.addView(imageView);

                    TextView textView1=new TextView(this);
                    textView1.setText(colourname);
                    LinearLayout.LayoutParams layoutParams2=new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    layoutParams2.setMargins(0,0,0,0);
                    textView1.setLayoutParams(layoutParams2);
                    textView1.setGravity(Gravity.CENTER);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setTextSize(12);

                    vertical.addView(textView1);


                    int mobileid=cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(mobileid);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage=(int) view.getTag();
                            Intent intent=new Intent(detail.this,detail.class);
                            intent.putExtra("idImage",idImage);
                            startActivity(intent);



                        }
                    });





                }
            }
            else{
                TextView noVariantColor = new TextView(this);
                noVariantColor.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                noVariantColor.setText("No Variant Available");
                noVariantColor.setTextColor(Color.BLACK);
                noVariantColor.setTextSize(20);
                colorDynamic.addView(noVariantColor);
            }
        }

        if(sqLiteDatabase!=null)
        {
            String ramquery="SELECT * FROM imag WHERE name LIKE '%" +productname + "%' AND color  LIKE '%" + color + "%' AND Ram NOT  LIKE '%" + ram + "%' AND Rom LIKE '%" + rom + "%' ";
            cursor = sqLiteDatabase.rawQuery(ramquery, null);
            LinearLayout ramdynamic=findViewById(R.id.RamDynamic);
            TextView textView1=findViewById(R.id.textView15);
            textView1.setText(ram);
            textView1.setTextColor(Color.BLUE);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String ram=cursor.getString(cursor.getColumnIndex("Ram"));

                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


                    CardView cardView=new CardView(this);
                    LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    layoutParams1.setMargins(0,0,30,0);
                    cardView.setLayoutParams(layoutParams1);
                    cardView.setRadius(10);
                    cardView.setCardBackgroundColor(Color.WHITE);
                    cardView.setCardElevation(8);
                    FrameLayout frameLayout=new FrameLayout(this);
                    frameLayout.setBackgroundColor(Color.GRAY);


                    frameLayout.setPadding(3,3,3,3);
                    frameLayout.setLayoutParams(layoutParams1);


                    ramdynamic.addView(frameLayout);
                    frameLayout.addView(cardView);


                    ramtext=new TextView(this);
                    ramtext.setLayoutParams(layoutParams);
                    ramtext.setText(ram);
                    ramtext.setTextSize(20);
                    ramtext.setTextColor(Color.BLACK);
                    ramtext.setGravity(Gravity.CENTER);
                    cardView .addView(ramtext);
                    int ramid=cursor.getInt(cursor.getColumnIndex("id"));
                    ramtext.setTag(ramid);
                    ramtext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage=(int) view.getTag();
                            Intent intent=new Intent(detail.this,detail.class);
                            intent.putExtra("idImage",idImage);
                            startActivity(intent);
                        }
                    });

                }


            }
           /* else{
                TextView noVariantText = new TextView(this);
                noVariantText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                noVariantText.setText("No Variant Available");
                noVariantText.setTextColor(Color.BLACK);
                noVariantText.setTextSize(20);
                ramdynamic.addView(noVariantText);

            }*/
        }



        if(sqLiteDatabase!=null)
        {
            String romquery="SELECT * FROM imag WHERE name LIKE '%" +productname + "%' AND color  LIKE '%" + color + "%' AND Ram LIKE '%" + ram + "%' AND Rom NOT LIKE '%" + rom + "%' GROUP BY Rom";
            cursor = sqLiteDatabase.rawQuery(romquery, null);
            LinearLayout romdynamic=findViewById(R.id.RomDynamic);
            TextView textView1=findViewById(R.id.textView18);
            textView1.setText(rom);
            textView1.setTextColor(Color.BLUE);

            if(cursor.getCount()>0)
            {
                while(cursor.moveToNext())
                {
                    String rom=cursor.getString(cursor.getColumnIndex("Rom"));

                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


                    CardView cardView=new CardView(this);
                    LinearLayout.LayoutParams layoutParams1=new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    layoutParams1.setMargins(0,0,30,0);
                    cardView.setLayoutParams(layoutParams1);
                    cardView.setRadius(10);
                    cardView.setCardBackgroundColor(Color.WHITE);
                    cardView.setCardElevation(8);
                    FrameLayout frameLayout=new FrameLayout(this);
                    frameLayout.setBackgroundColor(Color.GRAY);


                    frameLayout.setPadding(3,3,3,3);
                    frameLayout.setLayoutParams(layoutParams1);


                    romdynamic.addView(frameLayout);
                    frameLayout.addView(cardView);


                    romtext=new TextView(this);
                    romtext.setLayoutParams(layoutParams);
                    romtext.setText(rom);
                    romtext.setTextSize(20);
                    romtext.setTextColor(Color.BLACK);
                    romtext.setGravity(Gravity.CENTER);
                    cardView .addView(romtext);
                    int romid=cursor.getInt(cursor.getColumnIndex("id"));
                    romtext.setTag(romid);
                    romtext.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage=(int) view.getTag();
                            Intent intent=new Intent(detail.this,detail.class);
                            intent.putExtra("idImage",idImage);
                            startActivity(intent);
                        }
                    });

                }

            }
            /*else{
                TextView noVariantText = new TextView(this);
                noVariantText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                noVariantText.setText("No Variant Available");
                noVariantText.setTextColor(Color.BLACK);
                noVariantText.setTextSize(20);
                romdynamic.addView(noVariantText);

            }*/
        }
         addtocart=findViewById(R.id.addtocart);
        if (isItemInCart(product_id)) {
            addtocart.setText("Go To Cart");
        } else {
            addtocart.setText("Add to Cart");
        }

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkitemisalreadythere();
            }
        });


        if(sqLiteDatabase!=null)
        {
            String similarquery = "SELECT * FROM imag WHERE Rom LIKE '%" + rom + "%' ORDER BY RANDOM() LIMIT 30";
            //"SELECT * FROM imag WHERE Ram LIKE '%$i%' AND Rom LIKE '%$rom%' AND (price BETWEEN $price AND ($price + 10000) OR price BETWEEN $price AND ($price - 10000)) ORDER BY rand() LIMIT 5"

            //String similarquery="SELECT * FROM imag WHERE Rom LIKE '%" + rom + "%' AND (price BETWEEN '"+price+"' AND ('"+price+"'+ 20000)) OR (price BETWEEN '"+price+"' AND ('"+price+"'- 10000)) ORDER BY RANDOM()";

            cursor = sqLiteDatabase.rawQuery(similarquery,null);

            LinearLayout similarproduct = findViewById(R.id.similar_product);

             int imageSize = 300;
             int imageWidth=300;

            if(cursor.getCount()>0)
            {

                while(cursor.moveToNext())
                {
                    LinearLayout linearLayout=new LinearLayout(this);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    linearLayout.setGravity(Gravity.CENTER);

                    // Set the padding for spacing between images
                    params.setMargins(0, 0, 10, 0);
                    linearLayout.setLayoutParams(params);



                    similarproduct.addView(linearLayout);

                    String Hexaimage = cursor.getString(cursor.getColumnIndex("im"));
                    byte[] imageBytes = hexStringToByteArray(Hexaimage);

                    // Create a Bitmap from the byte array
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    ImageView imageView=new ImageView(this);
                    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                            imageSize, // Width
                            imageSize  // Height
                    );
                    imageParams.setMargins(0,0,0,0);

                    imageView.setLayoutParams(imageParams);
                    imageView.setImageBitmap(decodedImage);


                    TextView textView = new TextView(this);
                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                            imageWidth,
                            ViewGroup.LayoutParams.MATCH_PARENT
                    );
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(textParams);
                    textView.setText(name);
                    textView.setTextSize(12);
                    textView.setSingleLine(true);
                    textView.setEllipsize(TextUtils.TruncateAt.END);
                    textView.setTextColor(Color.BLACK);

                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);


                    int spid=cursor.getInt(cursor.getColumnIndex("id"));
                    imageView.setTag(spid);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int idImage=(int) view.getTag();
                            Intent intent=new Intent(detail.this,detail.class);
                            intent.putExtra("idImage",idImage);
                            startActivity(intent);
                        }
                    });

                }
            }

        }




    }


    public void saveItemInCartStatus(int productId, boolean isItemInCart) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ITEM_IN_CART_" + productId, isItemInCart);
        editor.apply();
    }

    public boolean isItemInCart(int productId) {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("ITEM_IN_CART_" + productId, false);
    }

    public void additemtocart() {
        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        String addtocartquery = "INSERT INTO cart (product_id,user_id,image,product_name,product_price,price,qty)" +
                "VALUES('" + product_id + "','" + userId + "','" + hexImage + "','" + productname + "','" + price + "','" + price + "','" + qty + "')";

        try {
            sqLiteDatabase.execSQL(addtocartquery);
            //Toast.makeText(this, "Item Added Successful TO Your Cart", Toast.LENGTH_SHORT).show();

            // Check if the item is already in the cart and update the button text
            if (isItemInCart(product_id)) {
                addtocart.setText("Go To Cart");
            } else {
                addtocart.setText("Add to Cart");
            }

        } catch (Exception e) {
            Toast.makeText(this, "Item Not Able TO Add ", Toast.LENGTH_SHORT).show();
            Log.e("DatabaseError", "Failed to insert data into user table", e);
        } finally {
            sqLiteDatabase.close(); // Close the database after the operation
        }
    }

    public void checkitemisalreadythere() {
        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        String itemcheckquery = "SELECT product_id from cart WHERE user_id='" + userId + "'";
        cursor = sqLiteDatabase.rawQuery(itemcheckquery, null);

        boolean isItemAlreadyInCart = false;

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int cartProductId = cursor.getInt(cursor.getColumnIndex("product_id"));
            if (cartProductId == product_id) {
                isItemAlreadyInCart = true;

                break;
            }
        }

        if (isItemAlreadyInCart) {
            addtocart.setText("Go To Cart");
            //Toast.makeText(this, "Item Already Thereee", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(detail.this,cart.class);
            startActivity(intent);
            saveItemInCartStatus(product_id, true);

        }
        else {
            addtocart.setText("Add to Cart");
            //Toast.makeText(this, "ADD TO CART", Toast.LENGTH_SHORT).show();
            saveItemInCartStatus(product_id, true);
            additemtocart();
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
}