package com.example.todo_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView1, textView2;
    private Button savebutton;
    private RatingBar ratingBar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calander;
    private int editRequest = 0, id;

    private WorkViewModel workViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WorkRepository repository = new WorkRepository((Application) getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        editText = findViewById(R.id.event_name);
        textView1 = findViewById(R.id.textview_date);
        textView2 = findViewById(R.id.textview_duetime);
        ratingBar = findViewById(R.id.ratingBar);
        savebutton = findViewById(R.id.saveButton);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker datePicker = new DatePicker(MainActivity.this);
                int currentDay = datePicker.getDayOfMonth();
                int currentYear = (datePicker.getMonth()) + 1;
                int currentMonth = datePicker.getYear();
                datePickerDialog = new DatePickerDialog(MainActivity.this,
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
                TimePicker timePicker = new TimePicker(MainActivity.this);
                int currentHour = timePicker.getCurrentHour();
                int currentMinute = timePicker.getCurrentMinute();
                timePickerDialog = new TimePickerDialog(MainActivity.this,

                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                textView2.setText(hourOfDay + ":" + minute);
                            }
                        }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final WorkAdapter workAdapter = new WorkAdapter();
        recyclerView.setAdapter(workAdapter);

        workViewModel = ViewModelProviders.of(this).get(WorkViewModel.class);
        workViewModel.getAllWorks().observe(this, new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> works) {
                workAdapter.setNotes(works);
            }
        });


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addwork = editText.getText().toString();
                String date = textView1.getText().toString();
                String duetime = textView2.getText().toString();
                Float rating = ratingBar.getRating();
                if (editRequest == 0) {
                    repository.insert(new Work(addwork, date, duetime, Float.parseFloat(String.valueOf(rating))));
                    editText.setText(" ");
                    editText.setHint(" Event Name");
                    textView1.setText("Date");
                    textView2.setText("Due Time");
                    savebutton.setText("Save");
                } else {
                    Toast.makeText(MainActivity.this, "next", Toast.LENGTH_SHORT).show();
                    Work work1 = new Work(addwork, date, duetime, Float.parseFloat(String.valueOf(rating)));
                    work1.setId(id);
                    repository.update(work1);
                    Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    editRequest = 0;
                    editText.setText(" ");
                    editText.setHint(" Event Name");
                    textView1.setText("Date");
                    textView2.setText("Due Time");
                    savebutton.setText("Save");
                }


            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                workViewModel.delete(workAdapter.getWorkAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Work Completed", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        workAdapter.setOnItemClickListener(new WorkAdapter.OnitemclickListener() {
            @Override
            public void onitemclick(Work work) {
                editText.setText(work.getEvent_name());
                textView1.setText(work.getDate());
                textView2.setText(work.getDue_time());
                ratingBar.setRating(work.getRating());
                Toast.makeText(MainActivity.this, "processing", Toast.LENGTH_SHORT).show();
                id = work.getId();
                editRequest = 1;
                savebutton.setText("Update");

            }

            @Override
            public void ondelteitemclick(int position) {
                workViewModel.delete(workAdapter.getWorkAt(position));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteallmenu:
                workViewModel.deleteallworks();
                Toast.makeText(MainActivity.this, "All Work Completed", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


}
