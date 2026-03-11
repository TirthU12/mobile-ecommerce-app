package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class registration extends AppCompatActivity {
    EditText number,pass,name,email,city,state,address;
    ImageButton i1;
    Button submit;
    DBHelper db;
    SQLiteDatabase sql;
    boolean ispasswordVisible=false;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new DBHelper(this);
        sql=db.getWritableDatabase();
        setContentView(R.layout.activity_registration);
        number = findViewById(R.id.number);
        pass=findViewById(R.id.password);
        name=findViewById(R.id.name);
        i1=findViewById(R.id.imageButton);
        //Button b1=findViewById(R.id.button);
        email=findViewById(R.id.email);
        city=findViewById(R.id.city);
        state=findViewById(R.id.state);
        address=findViewById(R.id.address);
        submit=findViewById(R.id.insert);




        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });


        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validInput();
                checknumber();

            }
        });
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
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validEmail();
                checkemail();

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validName();
                checkuser();

            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               validCity();

            }
        });
        state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validState();

            }
        });
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

               validAddress();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adduser();

            }
        });

    }
    public boolean validAddress() {
        if (TextUtils.isEmpty(address.getText().toString())) {
            address.setError("Enter Address");
            return false;
        }
        return true;
    }
    public boolean validState() {
        String rex = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";
        if (TextUtils.isEmpty(state.getText().toString()) || !state.getText().toString().matches(rex)) {
            state.setError("Enter State");
            return false;
        }
        return true;
    }
    public boolean validCity() {
        String rex = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";
        if (TextUtils.isEmpty(city.getText().toString()) || !city.getText().toString().matches(rex)) {
            city.setError("Enter City");
            return false;
        }
        return true;
    }

    public boolean validName() {
        String rex = "^(?!\\s*$)[a-zA-Z0-9\\s]*$";
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError("Enter UserName");
            return false;
        }
        if( !name.getText().toString().matches(rex))
        {
            name.setError("Enter Proper UserName");
            return false;
        }
        return true;
    }
    public boolean validEmail() {
        String emailText = email.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Enter Valid Email");
            return false;
        }
        return true;
    }
    public boolean validInput()
    {
        if(TextUtils.isEmpty(number.getText().toString()))
        {
            number.setError("Enter Number");

            return  false;
        }

        if(!isvalidMobileNumber(number.getText().toString()))
        {
            number.setError("Enter 10 Digits Number That Start With 6,7,8,9");


            return false;
        }

        return true;
    }
    public boolean isvalidMobileNumber(String ph)
    {
        String rex="^[6-9]\\d{9}$";
        return ph.matches(rex);

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
    public void toggle()
    {
        if(ispasswordVisible)
        {
            pass.setTransformationMethod(new PasswordTransformationMethod());

        }
        else{
            pass.setTransformationMethod(null);
        }
        ispasswordVisible=!ispasswordVisible;
    }
    public boolean checkuser()
    {
        db=new DBHelper(this);
        sql=db.getReadableDatabase();
        String username=name.getText().toString();
        String query="SELECT * from user where Name='"+username+"'";
        Cursor cursor=sql.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            name.setError("Username Is Already Exist");
            return false;
        }
        return true;

    }
    public boolean checknumber()
    {
        db=new DBHelper(this);
        sql=db.getReadableDatabase();
        String usernumber=number.getText().toString();
        String query="SELECT * from user where Number='"+usernumber+"'";
        Cursor cursor=sql.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            number.setError("Number is Already Exist");
            return false;
        }
        return true;

    }

    public boolean checkemail()
    {
        db=new DBHelper(this);
        sql=db.getReadableDatabase();
        String useremail=email.getText().toString();
        String query="SELECT * from user where Email='"+useremail+"'";
        Cursor cursor=sql.rawQuery(query,null);
        if(cursor.getCount()>0)
        {
            email.setError("Email is Already Exist");
            return false;
        }
        return true;

    }

    public  void adduser()
    {

        db=new DBHelper(this);
        sql=db.getWritableDatabase();

        if (!validName() || !validpassword() || !validEmail() || !validInput() || !validCity()
                || !validState() || !validAddress() || !checkuser() || !checknumber() || !checkemail())
        {
            return; // If any validation fails, exit the method
        }



        String username=name.getText().toString();
        String password=pass.getText().toString();
        String Email=email.getText().toString();
        String usernum=number.getText().toString();

        String usercity=city.getText().toString();
        String userstate=state.getText().toString();
        String Useraddress=address.getText().toString();
        String country="India";


        String insertQuery = "INSERT INTO user (Name, Password, Number, Email, City, State, Country, Address) " +
                "VALUES ('" + username + "', '" + password + "', " + usernum + ", '" + Email + "', '" +
                usercity + "', '" + userstate + "', '" + country + "', '" + Useraddress + "')";


        try {
            sql.execSQL(insertQuery);
            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(registration.this, login.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            Log.e("DatabaseError", "Failed to insert data into user table", e);
        } finally {
            sql.close(); // Close the database after the operation
        }

    }


}