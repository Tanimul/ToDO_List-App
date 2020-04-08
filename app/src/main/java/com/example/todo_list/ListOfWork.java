package com.example.todo_list;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListOfWork extends AppCompatActivity {
    private WorkViewModel workViewModel;
    private AlertDialog.Builder alretdialog;
    final WorkAdapter workAdapter = new WorkAdapter();

    private FloatingActionButton floatingActionButton;
    public static final int add_note_request = 1;
    public static final int edit_note_request = 2;
    private int id;
    private LinearLayout linearLayout;

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
        setContentView(R.layout.activity_list_of_work);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        linearLayout = findViewById(R.id.empty_viewlist);
        floatingActionButton = findViewById(R.id.add_work_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListOfWork.this, Addwork.class);
                startActivityForResult(intent, add_note_request);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(workAdapter);

        workViewModel = ViewModelProviders.of(this).get(WorkViewModel.class);
        workViewModel.getAllWorks().observe(this, new Observer<List<Work>>() {
            @Override
            public void onChanged(List<Work> works) {
                workAdapter.setNotes(works);
                linearLayout.setVisibility(workAdapter.getItemCount() == 0 ? View.VISIBLE : View.INVISIBLE);
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
                Toast.makeText(ListOfWork.this, "Work Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float imageWidth = ((BitmapDrawable) getResources().getDrawable(R.mipmap.trash)).getBitmap().getWidth();
                Paint p = new Paint();
                Bitmap icon;
                p.setColor(ContextCompat.getColor(ListOfWork.this, R.color.deletecolor));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                c.drawRect(background, p);
                c.clipRect(background);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.trash);
                RectF icon_dest = new RectF((float) itemView.getLeft() + imageWidth, (float) itemView.getTop() + ((height / 2) - (imageWidth / 2)), (float) itemView.getLeft() + 2 * imageWidth, (float) itemView.getBottom() - ((height / 2) - (imageWidth / 2)));
                c.drawBitmap(bitmap, null, icon_dest, p);
                c.restore();

            }

        }).attachToRecyclerView(recyclerView);

        workAdapter.setOnItemClickListener(new WorkAdapter.OnitemclickListener() {
            @Override
            public void onitemclick(Work work) {
                Toast.makeText(ListOfWork.this, "item clicked '" + work.getEvent_name() + "'", Toast.LENGTH_SHORT).show();
                Intent data = new Intent(ListOfWork.this, Addwork.class);
                data.putExtra(Addwork.eid, work.getId());
                data.putExtra(Addwork.ed1, work.getEvent_name());
                data.putExtra(Addwork.ed2, work.getDate());
                data.putExtra(Addwork.ed3, work.getDue_time());
                data.putExtra(Addwork.e_pri, work.getRating());
                startActivityForResult(data, edit_note_request);

            }

            @Override
            public void oncheckitemclick(Work work) {
                int id = work.getId();
                String title = work.getEvent_name();
                String date = work.getDate();
                String due_date = work.getDue_time();
                float pir = work.getRating();
                boolean com = work.isCom();
                if (com == false) {
                    Work work1 = new Work(title, date, due_date, pir, true);
                    work1.setId(id);
                    workViewModel.update(work1);
                    Toast.makeText(ListOfWork.this, "work complete", Toast.LENGTH_SHORT).show();
                } else {
                    Work work1 = new Work(title, date, due_date, pir, false);
                    work1.setId(id);
                    workViewModel.update(work1);
                    Toast.makeText(ListOfWork.this, "work uncomplete", Toast.LENGTH_SHORT).show();
                }
            }


        });

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
                Toast.makeText(ListOfWork.this, "hehes", Toast.LENGTH_SHORT).show();
                if (containsName(WorkAdapter.works_all, query.toUpperCase())) {
                    workAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(getBaseContext(), "Not found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(ListOfWork.this, "he", Toast.LENGTH_SHORT).show();
                workAdapter.getFilter().filter(newText);
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
                alretdialog = new AlertDialog.Builder(ListOfWork.this);
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
                Toast.makeText(ListOfWork.this, "All Works Completed", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
