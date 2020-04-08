package com.example.todo_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Setting extends AppCompatActivity {
    private Switch aSwitch, aSwitch2;
    private CheckBox checkBox, checkBox2, checkBox3, checkBox4;
    private WorkViewModel workViewModel;
    private static boolean switchonoff = true;
    private static boolean check = true;
    private static boolean check2 = false;
    private static boolean check3 = false;
    private static boolean check4 = false;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            Toast.makeText(this, "Dark", Toast.LENGTH_SHORT).show();
            setTheme(R.style.DarkTheme);
        } else {
            Toast.makeText(this, "Light", Toast.LENGTH_SHORT).show();
            setTheme(R.style.LightTheme);
        }
        setContentView(R.layout.activity_setting);
        setTitle("Settings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





        aSwitch = findViewById(R.id.switchmode);
        aSwitch2 = findViewById(R.id.reminder);
        checkBox = findViewById(R.id.switchmoderemider1);
        checkBox2 = findViewById(R.id.switchmoderemider2);
        checkBox3 = findViewById(R.id.switchmoderemider3);
        checkBox4 = findViewById(R.id.switchmoderemider4);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }

        if (switchonoff == true) {
            Toast.makeText(this, "reminder on", Toast.LENGTH_SHORT).show();
            aSwitch2.setChecked(true);
            checkBox.setClickable(true);
            checkBox2.setClickable(true);
            checkBox3.setClickable(true);
            checkBox4.setClickable(true);
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                checkBox.setTextColor(Color.parseColor("#FFFFFF"));
                checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                checkBox2.setTextColor(Color.parseColor("#FFFFFF"));
                checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                checkBox3.setTextColor(Color.parseColor("#FFFFFF"));
                checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                checkBox4.setTextColor(Color.parseColor("#FFFFFF"));
                checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            } else {
                checkBox.setTextColor(Color.parseColor("#000000"));
                checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                checkBox2.setTextColor(Color.parseColor("#000000"));
                checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                checkBox3.setTextColor(Color.parseColor("#000000"));
                checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                checkBox4.setTextColor(Color.parseColor("#000000"));
                checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
            }

        } else {
            Toast.makeText(this, "reminder off", Toast.LENGTH_SHORT).show();
            aSwitch2.setChecked(false);
            checkBox.setClickable(false);
            checkBox2.setClickable(false);
            checkBox3.setClickable(false);
            checkBox4.setClickable(false);

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                checkBox.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                checkBox2.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                checkBox3.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                checkBox4.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
            } else {
                checkBox.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                checkBox2.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                checkBox3.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                checkBox4.setTextColor(Color.parseColor("#C0BBBB"));
                checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
            }
        }

        if (check == true) {
            checkBox.setChecked(true);



        } else {
            checkBox.setChecked(false);
        }
        if (check2 == true) {
            checkBox2.setChecked(true);
        } else {
            checkBox2.setChecked(false);
        }
        if (check3 == true) {
            checkBox3.setChecked(true);
        } else {
            checkBox3.setChecked(false);
        }
        if (check4 == true) {
            checkBox4.setChecked(true);
        } else {
            checkBox4.setChecked(false);
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Toast.makeText(Setting.this, "black", Toast.LENGTH_SHORT).show();
                    restartApp();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Toast.makeText(Setting.this, "light", Toast.LENGTH_SHORT).show();
                    restartApp();
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    check = true;
                } else {
                    check = false;
                }
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox2.isChecked()) {
                    check2 = true;
                } else {
                    check2 = false;
                }
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox3.isChecked()) {
                    check3 = true;
                } else {
                    check3 = false;
                }
            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox4.isChecked()) {
                    check4 = true;
                } else {
                    check4 = false;
                }
            }
        });

        aSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (aSwitch2.isChecked()) {
                    switchonoff = true;
                    checkBox.setClickable(true);
                    checkBox2.setClickable(true);
                    checkBox3.setClickable(true);
                    checkBox4.setClickable(true);

                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        checkBox.setTextColor(Color.parseColor("#FFFFFF"));
                        checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        checkBox2.setTextColor(Color.parseColor("#FFFFFF"));
                        checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        checkBox3.setTextColor(Color.parseColor("#FFFFFF"));
                        checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        checkBox4.setTextColor(Color.parseColor("#FFFFFF"));
                        checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                    } else {
                        checkBox.setTextColor(Color.parseColor("#000000"));
                        checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        checkBox2.setTextColor(Color.parseColor("#000000"));
                        checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        checkBox3.setTextColor(Color.parseColor("#000000"));
                        checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                        checkBox4.setTextColor(Color.parseColor("#000000"));
                        checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#000000")));
                    }
                    
                    Toast.makeText(Setting.this, "reminder on", Toast.LENGTH_SHORT).show();
                } else {
                    switchonoff = false;
                    checkBox.setClickable(false);
                    checkBox.setChecked(false);
                    checkBox2.setClickable(false);
                    checkBox2.setChecked(false);
                    checkBox3.setClickable(false);
                    checkBox3.setChecked(false);
                    checkBox4.setClickable(false);
                    checkBox4.setChecked(false);

                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        checkBox.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                        checkBox2.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                        checkBox3.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                        checkBox4.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                    } else {
                        checkBox.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                        checkBox2.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox2.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                        checkBox3.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox3.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                        checkBox4.setTextColor(Color.parseColor("#C0BBBB"));
                        checkBox4.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#C0BBBB")));
                    }
                    Toast.makeText(Setting.this, "reminder off", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void restartApp() {
        Intent intent = new Intent(getApplicationContext(), Setting.class);
        startActivity(intent);
        finish();
    }
}
