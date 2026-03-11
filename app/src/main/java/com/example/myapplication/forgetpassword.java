package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import javax.mail.*;
import javax.mail.internet.*;

public class forgetpassword extends AppCompatActivity {
    EditText email;
    String stroreemail;
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    int userid;

    String otp=generateOTP();
    String otpsent=otp;
    String userEmailToReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        dbHelper=new DBHelper(this);
        sqLiteDatabase=dbHelper.getReadableDatabase();

    }

    public void sentotp(View view)
    {
        checkemail();
        //sendmail();
    }


    @SuppressLint("Range")
    public void checkemail()
    {
        email=findViewById(R.id.checkemail);
        stroreemail=email.getText().toString();
        String query="SELECT * from user WHERE email='"+stroreemail+"'";
        cursor=sqLiteDatabase.rawQuery(query,null);

        if (cursor.getCount() > 0) {
            /*if (cursor.moveToFirst()) {
                userid = cursor.getInt(cursor.getColumnIndex("id"));
                useremail = cursor.getString(cursor.getColumnIndex("email"));

                userEmailToReset = useremail;

            }*/
            cursor.moveToFirst(); // Move to the first row
            userid = cursor.getInt(cursor.getColumnIndex("id"));



            try {
                String stringSenderEmail = "karanthesia53@gmail.com";
                String stringReceiverEmail =stroreemail;
                String stringPasswordSenderEmail = "hycy xttu afwy kqqz";

                String stringHost = "smtp.gmail.com";

                Properties properties = System.getProperties();

                properties.put("mail.smtp.host", stringHost);
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                    }
                });

                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                mimeMessage.setSubject("Subject:Reset Password");
                mimeMessage.setText(otp);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Transport.send(mimeMessage);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
                Toast.makeText(this,"Email Send Successfully To "+stroreemail,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,checkotp.class);
                intent.putExtra("USER_EMAIL",stroreemail);
                intent.putExtra("USER_ID",userid);
                intent.putExtra("RANDOM_OTP",otpsent);
                startActivity(intent);



            } catch (AddressException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }



        } else {
            Toast.makeText(this, "Email does not exist", Toast.LENGTH_SHORT).show();
        }


    }


    private String generateOTP() {
        int otpLength = 6;
        StringBuilder otp = new StringBuilder(otpLength);
        Random random = new Random();

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}