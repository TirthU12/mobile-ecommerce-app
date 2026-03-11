package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class changepassword extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    EditText pass;
    EditText pass1;
    int userid;
    String newpassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        userid = getIntent().getIntExtra("userid", -1);

        pass=findViewById(R.id.checkpassword);
        pass1=findViewById(R.id.confirmpassword);

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validpassword();
            }
        });

        pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (pass1.getText().toString().equals(pass.getText().toString())) {

                } else {
                    // Toast.makeText(changepassword.this, "Both Passwords Should Match", Toast.LENGTH_SHORT).show();
                    pass1.setError("Both Passwords Should match");
                }

            }
        });


    }
    public boolean validpassword()
    {
        String rex="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
        if(TextUtils.isEmpty(pass.getText().toString())|| !pass.getText().toString().matches(rex))
        {
            pass.setError("At least one digit [0-9]\n" +
                    "At least one lowercase letter [a-z]\n" +
                    "At least one uppercase letter [A-Z]\n" +
                    "At least one special character from @#$%^&+=\n" +
                    "No whitespace \\S\n" +
                    "Minimum length of 8 characters.");
            return false;

        }
        return true;

    }
    public void oncickchangepassword(View view)
    {
        changepassword();
    }
    public void changepassword()
    {
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getWritableDatabase();

        if(!validpassword())
        {
            return;
        }
        newpassword=pass1.getText().toString();
        try {
            String updateQuery = "UPDATE user SET password = ? WHERE id = ?";
            SQLiteStatement statement = sqLiteDatabase.compileStatement(updateQuery);
            statement.bindString(1, newpassword);
            statement.bindLong(2, userid);

            // Execute the update
            statement.executeUpdateDelete();

            // Provide feedback to the user (e.g., using Toast)
            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,login.class);
            startActivity(intent);

        } catch (Exception e) {
            // Handle any exceptions (e.g., SQLiteException)
            e.printStackTrace();
            Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show();
        } finally {
            // Close the database connection
            sqLiteDatabase.close();
        }


    }
}