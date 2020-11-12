package com.example.todo_list.Ui;

import android.app.AlarmManager;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo_list.Adapter.WorkAdapter;
import com.example.todo_list.Model.Work;
import com.example.todo_list.Notification.Alertreciver;
import com.example.todo_list.R;
import com.example.todo_list.Room.WorkViewModel;
import com.example.todo_list.databinding.ActivityHomeactivityBinding;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private static final String TAG = "Home_Activity";
    private ActivityHomeactivityBinding binding;
    private WorkViewModel workViewModel;

    private NotificationManagerCompat notificationManager;
    private DrawerLayout drawer;
    private long backpressed;
    private Toast backtost;
    private AlertDialog.Builder alretdialog;

    WorkAdapter workAdapter1 = new WorkAdapter();
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Intent intent12;

    public static final int add_note_request = 1;
    public static final int edit_note_request = 2;

    public ArrayList<Boolean> tasks = new ArrayList<>();
    public static final String editid = "com.example.to_do_list.editid";
    public static final String editt1 = "com.example.to_do_list.editt1";
    public static final String editt2 = "com.example.to_do_list.editt2";
    public static final String editt3 = "com.example.to_do_list.editt3";
    public static final String edit_pri = "com.example.to_do_list.edit_pri";

    public static String[] date = {null, null, null};
    public static String[] time = {null, null, null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeactivityBinding.inflate(getLayoutInflater());
        Log.d(TAG, "onCreate .");

        setContentView(binding.getRoot());
        setTitle("To Do");

        //Set Tasks in Recyclerview
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView2.setHasFixedSize(true);
        binding.recyclerView2.setAdapter(workAdapter1);

        notificationManager = NotificationManagerCompat.from(this);
        call();

        //Get Tasks from Local Storage
        Log.d(TAG, "reminder " + Setting.switchonoff);
        retrieveTasks();

        //Delete task with Swipe
        DeleteWithSwipe();

        //Set Toolbar and Drawer Menu
        setSupportActionBar(binding.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Set Home Initially
        if (savedInstanceState == null) {
            binding.navView.setCheckedItem(R.id.nav_home);

        }

        //Create new Task
        binding.addWorkFloatingButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, Addwork.class);
                startActivityForResult(intent, add_note_request);
            }
        });

        //Task clicking by Adapter
        workAdapter1.setOnItemClickListener(new WorkAdapter.OnitemclickListener() {
            @Override
            public void onitemclick(Work work) {
                Intent data = new Intent(HomeActivity.this, Addwork.class);
                data.putExtra(Addwork.eid, work.getId());
                data.putExtra(Addwork.ed1, work.getEvent_name());
                data.putExtra(Addwork.ed2, work.getDate());
                data.putExtra(Addwork.ed3, work.getDue_time());
                data.putExtra(Addwork.e_pri, work.getRating());
                startActivityForResult(data, edit_note_request);

            }

            //task check box clicking event
            @Override
            public void oncheckitemclick(Work work) {
                int id = work.getId();
                String title = work.getEvent_name();
                String date = work.getDate();
                String due_date = work.getDue_time();
                //   float pir = work.getRating();
                boolean com = work.isCom();

                if (com == false) {
                    Work work1 = new Work(title, date, due_date, 0, true);
                    work1.setId(id);
                    workViewModel.update(work1);
                    Toast.makeText(HomeActivity.this, "The TASK is Complete", Toast.LENGTH_SHORT).show();
                } else {
                    Work work1 = new Work(title, date, due_date, 4, false);
                    work1.setId(id);
                    workViewModel.update(work1);
                    Toast.makeText(HomeActivity.this, "The TASK Incomplete", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.navView.setNavigationItemSelectedListener(this);
    }

    //--------------->>>>>>>>>>>>>>>    end on on create      <<<<<<<<<<<<<------------------------


    //On create options create with search and delete icon .
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //when tasks available then create option menus
        if (workAdapter1.getItemCount() != 0) {

            getMenuInflater().inflate(R.menu.menu2, menu);
            MenuItem menuItem = menu.findItem(R.id.search_view_id);

            //Searching any task
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    if (containsName(WorkAdapter.works_all, query.toUpperCase())) {
                        workAdapter1.getFilter().filter(query);
                    } else {
                        Toast.makeText(getBaseContext(), "Task Not Found", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    workAdapter1.getFilter().filter(newText);
                    return false;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }

    //Search task
    boolean containsName(List<Work> list, String name) {
        for (Work work : list) {
            if (work.getEvent_name().toUpperCase().contains(name))
                return true;
        }
        return false;
    }

    //Retrieve task from Local Database
    private void retrieveTasks() {
        final Calendar cal = Calendar.getInstance();
        final AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        final ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();
        final Intent intent = new Intent(HomeActivity.this, Alertreciver.class);


        workViewModel = ViewModelProviders.of(this).get(WorkViewModel.class);
        workViewModel.getAllWorks().observe(this, new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> works) {
                workAdapter1.setNotes(works);
                binding.emptyView.setVisibility(workAdapter1.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);

                //      tasks.clear();

                for (int i = 0; i < works.size(); i++) {
                    if (works.get(i).isCom() != true) {
                        Log.d(TAG, "task: " + works.get(i).getEvent_name());

                        tasks.add(true);

                        date = works.get(i).getDate().split("/");
                        int year = Integer.parseInt(date[0]);
                        int month = Integer.parseInt(date[1]);
                        int day = Integer.parseInt(date[2]);
                        Log.d(TAG, year + "/" + month + "/" + day);

                        time = works.get(i).getDue_time().split("[: ]");
                        int hr = Integer.parseInt(time[0]);
                        int min = Integer.parseInt(time[1]);
                        String am_pm = time[2];
                        Log.d(TAG, hr + ":" + min + " " + am_pm);


                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, day);
                        cal.set(Calendar.HOUR_OF_DAY, hr);
                        cal.set(Calendar.MINUTE, min);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        if(am_pm != "0"){
                            cal.set(Calendar.AM_PM,1);
                        }else {
                            cal.set(Calendar.AM_PM,0);
                        }

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, i, intent, 0);
                        mgrAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                cal.getTimeInMillis(),
                                pendingIntent);

                        intentArray.add(pendingIntent);

                    }
                }


            }
        });
    }


    //Delete by left to right swipe
    private void DeleteWithSwipe() {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                workViewModel.delete(workAdapter1.getWorkAt(viewHolder.getAdapterPosition()));
                Toast.makeText(HomeActivity.this, "Task Completed", Toast.LENGTH_SHORT).show();
            }

            //Delete's background
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float imageWidth = ((BitmapDrawable) getResources().getDrawable(R.mipmap.trash)).getBitmap().getWidth();
                Paint p = new Paint();
                Bitmap icon;
                p.setColor(ContextCompat.getColor(HomeActivity.this, R.color.deletecolor));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
                c.clipRect(background);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.trash);
                RectF icon_dest = new RectF((float) itemView.getLeft() + imageWidth, (float) itemView.getTop() + ((height / 2) - (imageWidth / 2)), (float) itemView.getLeft() + 2 * imageWidth, (float) itemView.getBottom() - ((height / 2) - (imageWidth / 2)));
                c.drawBitmap(bitmap, null, icon_dest, p);
                c.restore();

            }
        }).attachToRecyclerView(binding.recyclerView2);
    }

    //Navigation item clicking event

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();

        switch (menuItem.getItemId()) {
            case R.id.nav_addwork:
                intent = new Intent(HomeActivity.this, Addwork.class);
                startActivityForResult(intent, 1);
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
                intent = new Intent(HomeActivity.this, Setting.class);
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
                intent = new Intent(HomeActivity.this, About.class);
                startActivity(intent);
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == add_note_request && resultCode == RESULT_OK) {
            String title = data.getStringExtra(Addwork.ed1);
            String date = data.getStringExtra(Addwork.ed2);
            String due_date = data.getStringExtra(Addwork.ed3);
            float pir = data.getFloatExtra(Addwork.e_pri, 1);
            Work work = new Work(title, date, due_date, pir, false);
            workViewModel.insert(work);
            Toast.makeText(this, "work saved", Toast.LENGTH_SHORT).show();

        } else if (requestCode == edit_note_request && resultCode == RESULT_OK) {
            int id = data.getIntExtra(Addwork.eid, -1);
            if (id == -1) {
                Toast.makeText(this, "Task can't Updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(Addwork.ed1);
            String date = data.getStringExtra(Addwork.ed2);
            String due_date = data.getStringExtra(Addwork.ed3);
            float pir = data.getFloatExtra(Addwork.e_pri, 1);
            Work work = new Work(title, date, due_date, pir, false);
            work.setId(id);
            workViewModel.update(work);
            Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    //delete all works by alert dialog
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteallmenu:
                alretdialog = new AlertDialog.Builder(HomeActivity.this);

                alretdialog.setTitle("Delete All");
                alretdialog.setMessage("Are you sure you want to delete all TASKS?");
                alretdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        workViewModel.deleteallworks();
                        Toast.makeText(HomeActivity.this, "All TASKS Completed", Toast.LENGTH_SHORT).show();
                    }
                });
                alretdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alretdialog.create();
                alretdialog.show();

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


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(TAG, "onRestart");
//        if (AppCompatDelegate.MODE_NIGHT_YES == AppCompatDelegate.getDefaultNightMode()) {
//            recreate();
//        } else {
//            recreate();
//        }
//    }

    //Double back press
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
            backtost = Toast.makeText(HomeActivity.this, "Please click BACK again to exit", Toast.LENGTH_SHORT);
            backtost.show();
        }
        backpressed = System.currentTimeMillis();
    }
}
