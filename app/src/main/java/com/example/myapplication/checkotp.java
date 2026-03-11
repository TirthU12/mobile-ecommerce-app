package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class checkotp extends AppCompatActivity {
    String randomOtp;
    String useremail;
    String otp = generateOTP();
    int userid;
    EditText editText1, editText2, editText3, editText4, editText5, editText6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkotp);
        userid = getIntent().getIntExtra("USER_ID", -1);

        randomOtp = getIntent().getStringExtra("RANDOM_OTP");

        useremail = getIntent().getStringExtra("USER_EMAIL");


        editText1 = findViewById(R.id.editTextNumber);
        editText2 = findViewById(R.id.editTextNumber2);
        editText3 = findViewById(R.id.editTextNumber3);
        editText4 = findViewById(R.id.editTextNumber4);
        editText5 = findViewById(R.id.editTextNumber5);
        editText6 = findViewById(R.id.editTextNumber6);

        setEditTextListeners();


    }

    private void setEditTextListeners() {
        editText1.addTextChangedListener((TextWatcher) new GenericTextWatcher(editText1, editText2));
        editText2.addTextChangedListener(new GenericTextWatcher(editText2, editText3));
        editText3.addTextChangedListener(new GenericTextWatcher(editText3, editText4));
        editText4.addTextChangedListener(new GenericTextWatcher(editText4, editText5));
        editText5.addTextChangedListener(new GenericTextWatcher(editText5, editText6));
        // You may omit the following line if you don't want the last EditText to trigger any action
        editText6.addTextChangedListener(new GenericTextWatcher(editText6, null));
    }

    private class GenericTextWatcher implements TextWatcher {
        private View viewCurrent, viewNext;

        private GenericTextWatcher(View viewCurrent, View viewNext) {
            this.viewCurrent = viewCurrent;
            this.viewNext = viewNext;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 1) {
                // Move to the next EditText when one character is entered
                if (viewNext != null) {
                    viewNext.requestFocus();
                }
            }
        }
    }


    public void checkotp(View view)
    {
        if(areAllFieldsValid())
        {
            Toast.makeText(this,"Otp Is Correct",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,changepassword.class);
            intent.putExtra("userid",userid);
            startActivity(intent);


        }
        else {
            Toast.makeText(this,"Otp Is Incorrect",Toast.LENGTH_SHORT).show();
        }

    }

    public void resentotp(View view)
    {
        sendreotpmail();
    }

    private boolean areAllFieldsValid() {
        EditText editText1 = findViewById(R.id.editTextNumber);
        EditText editText2 = findViewById(R.id.editTextNumber2);
        EditText editText3 = findViewById(R.id.editTextNumber3);
        EditText editText4 = findViewById(R.id.editTextNumber4);
        EditText editText5 = findViewById(R.id.editTextNumber5);
        EditText editText6 = findViewById(R.id.editTextNumber6);

        String inputOtp = editText1.getText().toString() +
                editText2.getText().toString() +
                editText3.getText().toString() +
                editText4.getText().toString() +
                editText5.getText().toString() +
                editText6.getText().toString();
        if (inputOtp.trim().isEmpty()) {
            Toast.makeText(this, "Please enter the complete OTP", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Compare the entered OTP with the random OTP
        return inputOtp.equals(randomOtp);
    }

    public void sendreotpmail()
    {
        try {
            String stringSenderEmail ="karanthesia53@gmail.com";
            String stringReceiverEmail=useremail;
            String stringPasswordSenderEmail = "<password>";

            String stringHost = "<host>";

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
            Toast.makeText(this,"Email Resend Successfully To "+useremail,Toast.LENGTH_SHORT).show();
            randomOtp=otp;
            /*Intent intent=new Intent(this,checkotp.class);
            intent.putExtra("USER_EMAIL",stroreemail);
            intent.putExtra("USER_ID",userid);
            intent.putExtra("RANDOM_OTP",otpsent);
            startActivity(intent);*/



        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
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