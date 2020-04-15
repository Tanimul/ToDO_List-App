package com.example.todo_list;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Homelayout extends AppCompatActivity implements OnNavigationItemSelectedListener {
    private WorkViewModel workViewModel;
    private FloatingActionButton floatingActionButton2;
    private ImageView imageView;
    private TextView textView;
    WorkAdapter workAdapter1 = new WorkAdapter();
    public ArrayList<String> remindertime = new ArrayList<>();
    public ArrayList<String> reminderdate = new ArrayList<>();
    public ArrayList<String> remindername = new ArrayList<>();
    Toolbar toolbar;
    private DrawerLayout drawer;
    public static final String editid = "com.example.to_do_list.editid";
    public static final String editt1 = "com.example.to_do_list.editt1";
    public static final String editt2 = "com.example.to_do_list.editt2";
    public static final String editt3 = "com.example.to_do_list.editt3";
    public static final String edit_pri = "com.example.to_do_list.edit_pri";
    private long backpressed;
    private Toast backtost;
    private AlertDialog.Builder alretdialog;
    private LinearLayout linearLayout;
    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            Toast.makeText(this, "Dark", Toast.LENGTH_SHORT).show();
            setTheme(R.style.DarkTheme_NoActionBar);
        } else {
            Toast.makeText(this, "Light", Toast.LENGTH_SHORT).show();
            setTheme(R.style.AppTheme_NoActionBar);
        }

        setContentView(R.layout.activity_homelayout);
        setTitle("Todo_list");
        linearLayout = findViewById(R.id.empty_view);
        floatingActionButton2 = findViewById(R.id.add_work_floating_button2);
        imageView = findViewById(R.id.imagenull);
        textView = findViewById(R.id.txtnull);
        notificationManager = NotificationManagerCompat.from(this);
        call();

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homelayout.this, Addwork.class);
                startActivityForResult(intent, ListOfWork.add_note_request);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(workAdapter1);


        workViewModel = ViewModelProviders.of(this).get(WorkViewModel.class);
        workViewModel.getAllWorks().observe(this, new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> works) {
                workAdapter1.setNotes(works);
                linearLayout.setVisibility(workAdapter1.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
                remindername.clear();
                remindertime.clear();
                reminderdate.clear();

                for (int i = 0; i < works.size(); i++) {

                    if (works.get(i).isCom() != true) {
                        Log.d("dddd", "ballll" + works.get(i).getEvent_name());
                        remindertime.add(works.get(i).getDue_time());
                        reminderdate.add(works.get(i).getDate());
                        remindername.add(works.get(i).getEvent_name());
                    }

                }
                Log.d("dddd", "" + remindername.size());
                reminderdata();

            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);


        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_listof_work);

        }
        navigationView.setNavigationItemSelectedListener(this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                workViewModel.delete(workAdapter1.getWorkAt(viewHolder.getAdapterPosition()));
                Toast.makeText(Homelayout.this, "Work Completed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float imageWidth = ((BitmapDrawable) getResources().getDrawable(R.mipmap.trash)).getBitmap().getWidth();
                Paint p = new Paint();
                Bitmap icon;
                p.setColor(ContextCompat.getColor(Homelayout.this, R.color.deletecolor));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
                c.clipRect(background);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.trash);
                RectF icon_dest = new RectF((float) itemView.getLeft() + imageWidth, (float) itemView.getTop() + ((height / 2) - (imageWidth / 2)), (float) itemView.getLeft() + 2 * imageWidth, (float) itemView.getBottom() - ((height / 2) - (imageWidth / 2)));
                c.drawBitmap(bitmap, null, icon_dest, p);
                c.restore();

            }
        }).attachToRecyclerView(recyclerView);


        workAdapter1.setOnItemClickListener(new WorkAdapter.OnitemclickListener() {
            @Override
            public void onitemclick(Work work) {
                Toast.makeText(Homelayout.this, "item clicked '" + work.getEvent_name() + "'", Toast.LENGTH_SHORT).show();
                Intent data = new Intent(Homelayout.this, Addwork.class);
                data.putExtra(Addwork.eid, work.getId());
                data.putExtra(Addwork.ed1, work.getEvent_name());
                data.putExtra(Addwork.ed2, work.getDate());
                data.putExtra(Addwork.ed3, work.getDue_time());
                data.putExtra(Addwork.e_pri, work.getRating());
                startActivityForResult(data, ListOfWork.edit_note_request);

            }
//            @Override
//            public void ondelteitemclick(int position) {
//                workViewModel.delete(workAdapter.getWorkAt(position));
//            }
//        });

            @Override
            public void oncheckitemclick(Work work) {


                int id = work.getId();
                String title = work.getEvent_name();
                String date = work.getDate();
                String due_date = work.getDue_time();
                float pir = work.getRating();
                boolean com = work.isCom();
                if (com == false) {

                    Work work1 = new Work(title, date, due_date, 0, true);
                    work1.setId(id);
                    workViewModel.update(work1);
                    Toast.makeText(Homelayout.this, "work complete", Toast.LENGTH_SHORT).show();
                } else {

                    Work work1 = new Work(title, date, due_date, 4, false);
                    work1.setId(id);
                    workViewModel.update(work1);
                    Toast.makeText(Homelayout.this, "work uncomplete", Toast.LENGTH_SHORT).show();
                }

            }


        });
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (backpressed + 2000 > System.currentTimeMillis()) {
            backtost.cancel();
            super.onBackPressed();
            return;
        } else {
            backtost = Toast.makeText(Homelayout.this, "press back again to Exit", Toast.LENGTH_SHORT);
            backtost.show();
        }
        backpressed = System.currentTimeMillis();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();

        switch (menuItem.getItemId()) {
            case R.id.nav_addwork:

                intent = new Intent(Homelayout.this, Addwork.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.nav_listof_work:
                intent = new Intent(Homelayout.this, ListOfWork.class);
                startActivity(intent);
                break;

            case R.id.nav_share:
                intent = new Intent(Intent.ACTION_SEND);
                ApplicationInfo api = getApplicationContext().getApplicationInfo();
                String apkpath = api.sourceDir;
                /// intent.putExtra(Intent.EXTRA_TEXT, "apps address "))
                intent.putExtra(Intent.EXTRA_TEXT, Uri.fromFile(new File(apkpath)));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Share with"));
                break;

            case R.id.nav_setting:
                intent = new Intent(Homelayout.this, Setting.class);
                startActivity(intent);
                break;

            case R.id.nav_rate_me:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                break;

            case R.id.nav_about:
                intent = new Intent(Homelayout.this, About.class);
                startActivity(intent);
                break;

        }


        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ListOfWork.add_note_request && resultCode == RESULT_OK) {
            String title = data.getStringExtra(Addwork.ed1);
            String date = data.getStringExtra(Addwork.ed2);
            String due_date = data.getStringExtra(Addwork.ed3);
            float pir = data.getFloatExtra(Addwork.e_pri, 1);

            Work work = new Work(title, date, due_date, pir, false);
            workViewModel.insert(work);

            Toast.makeText(this, "work saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == ListOfWork.edit_note_request && resultCode == RESULT_OK) {

            int id = data.getIntExtra(Addwork.eid, -1);

            if (id == -1) {
                Toast.makeText(this, "Work can't updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(Addwork.ed1);
            String date = data.getStringExtra(Addwork.ed2);
            String due_date = data.getStringExtra(Addwork.ed3);
            float pir = data.getFloatExtra(Addwork.e_pri, 1);
            Work work = new Work(title, date, due_date, pir, false);
            work.setId(id);
            workViewModel.update(work);
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "work not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        MenuItem menuItem = menu.findItem(R.id.search_view_id);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(Homelayout.this, "hehes", Toast.LENGTH_SHORT).show();
                if (containsName(WorkAdapter.works_all, query.toUpperCase())) {
                    workAdapter1.getFilter().filter(query);
                } else {
                    Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(Homelayout.this, "he", Toast.LENGTH_SHORT).show();
                workAdapter1.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    boolean containsName(List<Work> list, String name) {
        for (Work work : list) {
            if (work.getEvent_name().toUpperCase().contains(name))
                return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteallmenu:
                alretdialog = new AlertDialog.Builder(Homelayout.this);
                alretdialog.setTitle("Delete All");
                alretdialog.setMessage("Are you sure you want to delete all Workes?");
                alretdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workViewModel.deleteallworks();
                    }
                });
                alretdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alretdialog.create();
                alretdialog.show();
                Toast.makeText(Homelayout.this, "All Works Completed", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void call() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "hasan";
            String des = "dfafafeqfwvrg3qrgoqinlfmq3po";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel3 = new NotificationChannel(
                    "fuad",
                    "Channel 1",
                    importance
            );
            channel3.setDescription(des);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel3);

        }
    }

    public void reminderdata() {
        Log.d("dddd", "ki j b");
//        for(String i:remindername){
//
//            Log.d("dddd",""+i);
//        }
//        Log.d("dddd",""+remindername.size());



        Calendar cal = Calendar.getInstance();
        cal.set(2020, 3, 11, 11, 25, 0);
        Intent intent12 = new Intent(Homelayout.this, Alertreciver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Homelayout.this, 0, intent12, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


//        String[] sp = reminderdate.get(i).split("/");
//        String[] sp1 = remindertime.get(i).split("[: ]");
//        int i0 = Integer.parseInt(sp[0]);
//        int i1 = Integer.parseInt(sp[1]) - 1;
//        int i2 = Integer.parseInt(sp[2]);
//        int i5 = 0;
//        int i3 = Integer.parseInt(sp1[0]);
//        int i4 = Integer.parseInt(sp1[1]);
//        if (sp1[2] != "AM") {
//            i5 = i3 + 12;
//        }
//        Log.d("dddd", "" + i0);
//        Log.d("dddd", "" + i1);
//        Log.d("dddd", "" + i2);
//        Log.d("dddd", "" + i3);
//        Log.d("dddd", "" + i4);
//        Log.d("dddd", "" + i5);
//        Toast.makeText(Homelayout.this, "www", Toast.LENGTH_SHORT).show();
//        Calendar cal = Calendar.getInstance();
//        //cal.set(i2,i1, i0, i5, i4, 0);
//         cal.set(2020, 3, 9, 23, 42, 0);
//        Intent intent12 = new Intent(Homelayout.this, Alertreciver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(Homelayout.this, 0, intent12, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

    }
}
