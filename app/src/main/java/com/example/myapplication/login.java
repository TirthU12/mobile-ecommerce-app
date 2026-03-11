package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends AppCompatActivity {
    DBHelper dbHelper ; // Initialize your database helper.
    SQLiteDatabase db;// Open the database for reading
    Cursor cursor;
    public static final String MyPREFERENCES="MyPrefs";
    public static final String UserId="UserId";
    SharedPreferences sp;
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this); // Initialize your database helper.
        db = dbHelper.getReadableDatabase();

        Button b1;
        setContentView(R.layout.activity_login);
        b1=findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkuser();


            }
        });

        TextView link=findViewById(R.id.textView);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login.this,registration.class);
                startActivity(intent);
            }
        });




    }
    public void forgerpassword(View view)
    {
        Intent intent=new Intent(this,forgetpassword.class);
        startActivity(intent);
    }

    public void checkuser()
    {
        if(db!=null)
        {
            EditText e1=findViewById(R.id.editTextText2);
            String name=e1.getText().toString();
            String query="select * from user where Email='"+name+"' OR Number='"+name+"'";
            Cursor cursor=db.rawQuery(query,null);
            if(cursor.getCount()>0)
            {

                checkuserpassword();
            }
            else
            {
                Toast.makeText(login.this, "Email Or Number  does not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("Range")
    public void checkuserpassword()
    {
        if(db!=null)
        {
            EditText e1=findViewById(R.id.editTextText2);
            EditText e2=findViewById(R.id.editTextText3);
             String email = e1.getText().toString(); // Get the text from the EditText

             String password=e2.getText().toString();

            String query = "SELECT * FROM user WHERE Email='" + email + "' AND Password='"+password+"' OR Number='"+email+"' AND Password='"+password+"'";
             cursor=db.rawQuery(query,null);
            if(cursor.moveToNext())
            {
                 userid=cursor.getInt(cursor.getColumnIndex("id"));
                 sp=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);

                 SharedPreferences.Editor editor=sp.edit();
                 editor.putInt(UserId,userid);
                 editor.putBoolean("isLoggedIn",true);
                 editor.apply();
                Toast.makeText(login.this, "Welcome TO Mobile Mart!!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(login.this, screen.class);
                startActivity(intent);


            }
            else{
                Toast.makeText(login.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
            cursor.close();

        }
    }

}