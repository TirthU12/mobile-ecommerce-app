package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class usepassword extends AppCompatActivity {
    SharedPreferences sp;
    int userId;
    DBHelper db;
    SQLiteDatabase sq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usepassword);
        sp=getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
        boolean isLoggedIn=sp.getBoolean("isLoggedIn",false);
        if(isLoggedIn)
        {
            setContentView(R.layout.activity_usepassword);

        }
        else {
            // Redirect to the login screen
            Intent intent = new Intent(usepassword.this, login.class);
            startActivity(intent);
            finish(); // Finish the MainActivity to prevent going back to it
        }
        userId = sp.getInt(login.UserId, -1);
    }

    public void oncheckpassword(View v)
    {
        validpassword();
    }

    public void validpassword()
    {
        db=new DBHelper(this);
        sq=db.getReadableDatabase();

        if(db!=null)
        {
            EditText editText=findViewById(R.id.checkpassword2);
            String password=editText.getText().toString();

            String query="SELECT * FROM user WHERE id='"+userId+"' AND Password='"+password+"'";
            Cursor cursor=sq.rawQuery(query,null);
            if(cursor.getCount()>0)
            {
                Intent intent=new Intent(this,home.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(usepassword.this,"Password Is Not Correct",Toast.LENGTH_SHORT).show();

            }
        }

    }

}