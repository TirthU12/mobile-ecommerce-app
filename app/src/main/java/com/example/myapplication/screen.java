package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class screen extends AppCompatActivity {
    public static final int TimeOut = 2000;
    public int attempts=0;
    SharedPreferences sp;
    public int maxattempts=3;
    SharedPreferences bio;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);


        // Check if biometric authentication is enabled

        bio = getSharedPreferences("biometric", MODE_PRIVATE);
        boolean isBioAuthenticationEnabled = bio.getBoolean("bioauth", false);

        if (isBioAuthenticationEnabled) {
            // Show the biometric prompt
            showBiometricPrompt();
        } else {
            // Wait for the splash screen duration and navigate to home.java
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(screen.this, home.class);
                    startActivity(intent);
                    Toast.makeText(screen.this, "Welcome To Mobile Mart", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, TimeOut);
        }
    }

    private void showBiometricPrompt() {

        BiometricManager biometricManager = BiometricManager.from(this);

        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            Executor executor = getMainExecutor();

            BiometricPrompt biometricPrompt = new BiometricPrompt(screen.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);


                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {

                        Toast.makeText(screen.this,"Clicked On Use Password"+userId,Toast.LENGTH_SHORT).show();
                        Intent passwordIntent = new Intent(screen.this, usepassword.class);
                        startActivity(passwordIntent);
                        finish();
                    }
                    else{
                        Toast.makeText(screen.this, "Welcome To Mobile Mart", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(screen.this,usepassword.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    attempts= 0;
                    Toast.makeText(screen.this, "Welcome To Mobile Mart", Toast.LENGTH_SHORT).show();

                    // Navigate to home.java on successful biometric authentication
                    Intent intent = new Intent(screen.this, home.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();

                    attempts++;

                    if(attempts>=maxattempts)
                    {
                        Toast.makeText(screen.this, "Maximum attempts reached. Redirecting to login.", Toast.LENGTH_SHORT).show();
                        sp=getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.clear();
                        editor.apply();
                        editor.commit();

                        bio=getSharedPreferences("biometric",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1=bio.edit();
                        editor1.clear();
                        editor1.apply();
                        editor1.commit();
                        Intent intent = new Intent(screen.this, login.class);
                        startActivity(intent);


                    }
                    else if (attempts==1)
                    {
                        Toast.makeText(screen.this, "2 attempts remaining", Toast.LENGTH_SHORT).show();
                    }
                    else if (attempts==2)
                    {
                        Toast.makeText(screen.this, "1 attempts remaining", Toast.LENGTH_SHORT).show();
                    }

                }
            });




            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Use your fingerprint")
                    .setNegativeButtonText("Use Password")
                    .build();

            // Show the biometric prompt
            biometricPrompt.authenticate(promptInfo);


        }
        else {
            // If biometric is not available, wait for the splash screen duration and navigate to home.java
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(screen.this, home.class);
                    startActivity(intent);
                    Toast.makeText(screen.this, "Welcome To Mobile Mart", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }, TimeOut);
        }
    }
}