package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class About extends AppCompatActivity implements View.OnClickListener {


   private Button facebook, email, github, instragram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            Toast.makeText(this, "Dark", Toast.LENGTH_SHORT).show();
            setTheme(R.style.DarkTheme);
        } else {
            Toast.makeText(this, "Light", Toast.LENGTH_SHORT).show();
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_about);
        setTitle("About");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        facebook = findViewById(R.id.facebook);
        email = findViewById(R.id.email);
        github = findViewById(R.id.github);
        instragram = findViewById(R.id.instragam);
        facebook.setOnClickListener(this);
        email.setOnClickListener(this);
        instragram.setOnClickListener(this);
        github.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.facebook) {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.todo_list", 0); //Checks if FB is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/fuhad.hasan.315")));  //Trys to make intent with FB's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/fuhad.hasan.315"))); //catches and opens a url to the desired page
            }
        } else if (v.getId() == R.id.email) {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.todo_list", 0); //Checks if Instagram is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mail.google.com/mail/u/0/#inbox"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://mail.google.com/mail/u/0/#inbox"))); //catches and opens a url to the desired page
            }

        } else if (v.getId() == R.id.instragam) {

            try {
                getPackageManager()
                        .getPackageInfo("com.example.todo_list", 0); //Checks if Instagram is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/fuad7594/"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/fuad7594/"))); //catches and opens a url to the desired page
            }
        } else {
            try {
                getPackageManager()
                        .getPackageInfo("com.example.todo_list", 0); //Checks if Instagram is even installed.
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Tanimul"))); //Trys to make intent with Instagram's URI
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Tanimul"))); //catches and opens a url to the desired page
            }
        }

    }
}
