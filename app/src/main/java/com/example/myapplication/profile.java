package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profile extends AppCompatActivity {
    SharedPreferences sp;
    Button logout;
    public Switch fingerprint;
    SharedPreferences bio;

    private static final int PICK_IMAGE_REQUEST = 1;
    public ImageView uploadimage;
    public ImageButton uploadimagebutton;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String LAST_SAVED_IMAGE_KEY = "last_saved_image";

    public EditText update_name;
    public ImageButton update_button_name;

    public EditText update_num;
    public ImageButton update_button_num;


    public EditText update_emaill;
    public ImageButton update_button_email;
    public int userId;

    public AlertDialog.Builder alertDialog;


    public EditText update_add;
    public ImageButton update_button_add;

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sp=getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean isLoggedIn=sp.getBoolean("isLoggedIn",false);
        if(isLoggedIn)
        {
            setContentView(R.layout.activity_profile);

        }
        else {
            // Redirect to the login screen
            Intent intent = new Intent(profile.this, login.class);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent going back to it
        }

        userId = sp.getInt(login.UserId, -1);
        bio=getSharedPreferences("biometric",MODE_PRIVATE);
        boolean isbioauthentication=bio.getBoolean("bioauth",false);

        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp=getSharedPreferences(login.MyPREFERENCES,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.clear();
                editor.apply();
                editor.commit();

                bio=getSharedPreferences("biometric",MODE_PRIVATE);
                SharedPreferences.Editor editor1=bio.edit();
                editor1.clear();
                editor1.apply();
                editor1.commit();

                SharedPreferences sharedPreferences=getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2=sharedPreferences.edit();
                editor2.clear();
                editor2.apply();
                editor2.commit();




                Intent intent = new Intent(profile.this, login.class);
                startActivity(intent);

            }
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.edit_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId=item.getItemId();
                if(itemId==R.id.home)
                {
                    startActivity(new Intent(getApplicationContext(), home.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if(itemId==R.id.edit_profile)
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







        fingerprint=findViewById(R.id.biometric);
        BiometricManager biometricManager=BiometricManager.from(this);
        fingerprint.setChecked(isbioauthentication);




        fingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS)
                {

                    Toast.makeText(profile.this, "Biometric Not Set on Mobile", Toast.LENGTH_SHORT).show();
                    fingerprint.setEnabled(false);


                }
                else {




                    SharedPreferences.Editor editor = bio.edit();
                    editor.putBoolean("bioauth", isChecked);
                    editor.apply();
                    if (isChecked)
                        Toast.makeText(profile.this, "Biometric set Successful", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(profile.this, "Biometric Disabled Successsful", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        uploadimage=findViewById(R.id.image_add);
        uploadimagebutton=findViewById(R.id.image_upload);

        uploadimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();


            }
        });

        loadImageFromPreferences();


        update_name=findViewById(R.id.username_edit);
        update_button_name=findViewById(R.id.update_name);


        update_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    update_button_name.setVisibility(View.VISIBLE);
                } else {
                    update_button_name.setVisibility(View.INVISIBLE);
                }

            }
        });

        update_button_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkname())
                    updatename();
            }
        });


        update_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                checkname();
            }
        });


        update_num=findViewById(R.id.mobilenumber_edit);
        update_button_num=findViewById(R.id.update_number);

        update_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    update_button_num.setVisibility(View.VISIBLE);
                }
                else {
                    update_button_num.setVisibility(View.INVISIBLE);
                }
            }
        });


        alertDialog=new AlertDialog.Builder(this);

        update_button_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checknumber()) {

                    alertDialog.setMessage("Warning: If you update your mobile number, you must use the updated number for future logins.")
                            .setCancelable(false)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    updatenumber();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    alertDialog.create();
                    alertDialog.setTitle("Update Mobile Number");
                    alertDialog.show();
                }


            }
        });


        update_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checknumber();


            }
        });


        update_emaill=findViewById(R.id.email_edit);
        update_button_email=findViewById(R.id.update_email);

        update_emaill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    update_button_email.setVisibility(View.VISIBLE);
                }
                else {
                    update_button_email.setVisibility(View.INVISIBLE);
                }
            }
        });

        /*update_button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkemail())
                {
                    updateemail();
                }

            }
        });*/

        update_button_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkemail()) {

                    alertDialog.setMessage("Warning: If you update your E-Mail, you must use the updated E-Mail for future logins.")
                            .setCancelable(false)
                            .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();

                                    updateemail();

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    alertDialog.create();
                    alertDialog.setTitle("Update E-Mail Address");
                    alertDialog.show();
                }


            }
        });



        update_emaill.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkemail();

            }
        });


        update_add=findViewById(R.id.address_edit);
        update_button_add=findViewById(R.id.update_address);

        update_add.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    update_button_add.setVisibility(View.VISIBLE);
                }
                else {
                    update_button_add.setVisibility(View.INVISIBLE);
                }
            }
        });



        update_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                checkaddress();
            }
        });

        update_button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkaddress()) {
                    updateaddress();
                }

            }
        });

        getdata();





    }

    public boolean checknumber(){
        String rex="^[6-9]\\d{9}$";
        if(TextUtils.isEmpty(update_num.getText().toString())){
            update_num.setError("Enter Number");
            return false;

        }
        else if(!update_num.getText().toString().matches(rex))
        {
            update_num.setError("Enter 10 Digits Number That Start With 6,7,8,9");
            return false;
        }

        return true;
    }

    public boolean checkemail()
    {
        if(!Patterns.EMAIL_ADDRESS.matcher(update_emaill.getText().toString()).matches())
        {
            update_emaill.setError("Email Invalid");
            return false;

        } else if (TextUtils.isEmpty(update_emaill.getText().toString())) {
            update_emaill.setError("Enter Email");
            return false;

        }
        return true;
    }

    public boolean checkname()
    {
        String pattern = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)?$";

        if(TextUtils.isEmpty(update_name.getText().toString()))
        {
            update_name.setError("Enter Name");
            return false;
        } else if (!update_name.getText().toString().matches(pattern)) {
            update_name.setError("Invalid Format");
            return false;

        }
        return true;

    }
    public boolean checkaddress()
    {
        if(TextUtils.isEmpty(update_add.getText().toString()) )
        {
            update_add.setError("Enter Address");
            return false;
        }
        return true;
    }



    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                uploadimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                uploadimage.setImageBitmap(bitmap);

                // Convert bitmap to Base64 and save to SharedPreferences
                String encodedImage = encodeImage(bitmap);
                saveImageToPreferences(encodedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void saveImageToPreferences(String encodedImage) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_SAVED_IMAGE_KEY, encodedImage);
        editor.apply();
    }

    private void loadImageFromPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String encodedImage = preferences.getString(LAST_SAVED_IMAGE_KEY, null);

        if (encodedImage != null) {
            // If the encoded image exists, decode and set the image in the ImageView
            Bitmap decodedBitmap = decodeImage(encodedImage);
            uploadimage.setImageBitmap(decodedBitmap);
        }
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void getdata(){
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getReadableDatabase();
        if(sqLiteDatabase!=null)
        {
            String query="SELECT * FROM user WHERE id='"+userId+"'";
            Cursor cursor=sqLiteDatabase.rawQuery(query,null);

            if(cursor.getCount()>0)
            {
                while (cursor.moveToNext())
                {
                    @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("Name"));
                    @SuppressLint("Range") String usermobilenumber = cursor.getString(cursor.getColumnIndex("Number"));
                    @SuppressLint("Range") String useremail = cursor.getString(cursor.getColumnIndex("Email"));
                    @SuppressLint("Range") String useraddress = cursor.getString(cursor.getColumnIndex("Address"));


                    update_name.setText(username);
                    update_num.setText(usermobilenumber);
                    update_emaill.setText(useremail);
                    update_add.setText(useraddress);


                }
            }

        }
    }

    public void updatename()
    {
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        String updatename=update_name.getText().toString();
        String updatequery="UPDATE user SET Name='"+updatename+"' WHERE id='"+userId+"'";

        try {
            sqLiteDatabase.execSQL(updatequery);
            Toast.makeText(this, "Name Update  Successful", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(profile.this, home.class);
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(this, "Name updation  Failed", Toast.LENGTH_SHORT).show();

        }

    }

    public void updatenumber()
    {
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        String updatenumber=update_num.getText().toString();
        String updatequery="UPDATE user SET Number='"+updatenumber+"' WHERE id='"+userId+"'";

        try {
            sqLiteDatabase.execSQL(updatequery);
            Toast.makeText(this, "Number Update  Successful", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(profile.this, home.class);
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(this, "Number updation  Failed", Toast.LENGTH_SHORT).show();

        }

    }


    public void updateemail()
    {
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        String updateemail=update_emaill.getText().toString();
        String updatequery="UPDATE user SET Email='"+updateemail+"' WHERE id='"+userId+"'";

        try {
            sqLiteDatabase.execSQL(updatequery);
            Toast.makeText(this, "Email Update  Successful", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(profile.this, home.class);
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(this, "Email Updation  Failed", Toast.LENGTH_SHORT).show();

        }

    }

    public void updateaddress()
    {
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getWritableDatabase();
        String updateaddressr=update_add.getText().toString();
        String updatequery="UPDATE user SET Address='"+updateaddressr+"' WHERE id='"+userId+"'";

        try {
            sqLiteDatabase.execSQL(updatequery);
            Toast.makeText(this, "Address Update  Successful", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(profile.this, home.class);
            startActivity(intent);
        }
        catch (Exception e){
            Toast.makeText(this, "Address updation  Failed", Toast.LENGTH_SHORT).show();

        }

    }
}