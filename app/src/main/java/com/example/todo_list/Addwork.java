package com.example.todo_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Addwork extends AppCompatActivity {
    private EditText editText;
    private TextView textView1, textView2;
    private Button savebutton;
    private RatingBar ratingBar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calander;
    private int id;
    private String min;


    public static final String eid = "com.example.to_do_list.eid";
    public static final String ed1 = "com.example.to_do_list.ed1";
    public static final String ed2 = "com.example.to_do_list.ed2";
    public static final String ed3 = "com.example.to_do_list.ed3";
    public static final String e_pri = "com.example.to_do_list.e_pri";


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
        setContentView(R.layout.activity_addwork);

        editText = findViewById(R.id.event_name);
        textView1 = findViewById(R.id.textview_date);
        textView2 = findViewById(R.id.textview_duetime);
        ratingBar = findViewById(R.id.ratingBar);
        savebutton = findViewById(R.id.saveButton);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(eid)) {
            setTitle("Edit_Work");
            editText.setText(intent.getStringExtra(ed1));
            textView1.setText(intent.getStringExtra(ed2));
            textView2.setText(intent.getStringExtra(ed3));
            ratingBar.setRating(intent.getFloatExtra(e_pri, 1));

        } else {
            setTitle("Add_Work");
        }


        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentDate = Calendar.getInstance();
                int currentDay = 0;
                int currentYear = 0;
                int currentMonth = 0;
                if (currentYear == 0 || currentMonth == 0 || currentDay == 0) {

                    currentYear = mcurrentDate.get(Calendar.YEAR);
                    currentMonth = mcurrentDate.get(Calendar.MONTH);
                    currentDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                }
                datePickerDialog = new DatePickerDialog(Addwork.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                                textView1.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePicker timePicker = new TimePicker(Addwork.this);
                final int currentHour = timePicker.getCurrentHour();
                final int currentMinute = timePicker.getCurrentMinute();

                timePickerDialog = new TimePickerDialog(Addwork.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                int CurHour = hourOfDay;
                                if (minute < 10) {
                                    min = "0" + minute;
                                    if (hourOfDay == 0) {
                                        textView2.setText("12:" + min + " AM");
                                    } else if (hourOfDay < 12) {
                                        textView2.setText(CurHour + ":" + min + " AM");
                                    } else if (hourOfDay == 12) {
                                        textView2.setText("12:" + min + " AM");
                                    } else {
                                        CurHour = hourOfDay - 12;
                                        textView2.setText(CurHour + ":" + min + " PM");
                                    }
                                } else {
                                    if (hourOfDay == 0) {
                                        textView2.setText("12:" + minute + " AM");
                                    } else if (hourOfDay < 12) {
                                        textView2.setText(CurHour + ":" + minute + " AM");
                                    } else if (hourOfDay == 12) {
                                        textView2.setText("12:" + minute + " PM");
                                    } else {
                                        CurHour = hourOfDay - 12;
                                        textView2.setText(CurHour + ":" + minute + " PM");
                                    }
                                }


                            }
                        }, currentHour, currentMinute, false);
                timePickerDialog.show();
            }
        });


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addwork = editText.getText().toString();
                String date = textView1.getText().toString();
                String duetime = textView2.getText().toString();
                float rating = ratingBar.getRating();

                if (addwork.isEmpty()) {
                    editText.setError("Enter a  WORK  name please");
                    editText.requestFocus();
                    return;
                }

                Intent data = new Intent();
                data.putExtra(ed1, addwork);
                data.putExtra(ed2, date);
                data.putExtra(ed3, duetime);
                data.putExtra(e_pri, rating);
                id = getIntent().getIntExtra(eid, -1);
                if (id != -1) {
                    data.putExtra(eid, id);
                }

                setResult(RESULT_OK, data);
                finish();


            }
        });


    }


}
