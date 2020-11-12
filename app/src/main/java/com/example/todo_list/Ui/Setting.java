package com.example.todo_list.Ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.todo_list.databinding.ActivitySettingBinding;

public class Setting extends AppCompatActivity {

    private static final String TAG = "Setting_Activity";
    private ActivitySettingBinding binding;
    public static boolean switchonoff = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Settings");
        Log.d(TAG, "oncreate .");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.switchmode.setChecked(true);
        } else {
            binding.switchmode.setChecked(false);
        }

        Log.d(TAG, "" + switchonoff);

        //for reminder's all parts
        if (switchonoff == true) {
            binding.reminder.setChecked(true);
        } else {
            binding.reminder.setChecked(false);
        }

        binding.switchmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    recreate();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                }
            }
        });

        binding.reminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchonoff = true;
                    reminderset();
                } else {
                    switchonoff = false;
                    reminderremove();
                }

            }
        });


    }

    private void reminderset() {
        //Todo reminder on
    }

    private void reminderremove() {
        //Todo reminder off
    }

}
