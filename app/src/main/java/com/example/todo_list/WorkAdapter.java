package com.example.todo_list;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkHolder> implements Filterable {
    private List<Work> works = new ArrayList<>();
    public static List<Work> works_all;
    private OnitemclickListener listener;


    @NonNull

    @Override
    public WorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.numberofwork, parent, false);

        return new WorkHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WorkHolder holder, final int position) {
        final Work currentwork = works.get(position);
        holder.eventname.setText(currentwork.getEvent_name());
        holder.date.setText(currentwork.getDate());
        holder.duetime.setText(currentwork.getDue_time());


        if (currentwork.isCom() == true) {
            holder.eventname.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.date.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.duetime.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.ratingBar.setRating(0);
            holder.complete_work.setChecked(true);
        } else {
            holder.eventname.setPaintFlags(Paint.SUBPIXEL_TEXT_FLAG);
            holder.date.setPaintFlags(Paint.SUBPIXEL_TEXT_FLAG);
            holder.duetime.setPaintFlags(Paint.SUBPIXEL_TEXT_FLAG);
            holder.ratingBar.setRating(currentwork.getRating());
            holder.complete_work.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    public void setNotes(List<Work> works) {
        this.works = works;
        works_all = works;
        notifyDataSetChanged();
    }

    public Work getWorkAt(int position) {
        return works.get(position);
    }


    public class WorkHolder extends RecyclerView.ViewHolder {

        private TextView eventname;
        private TextView date;
        private TextView duetime;
        private RatingBar ratingBar;
        private CheckBox complete_work;
        //private ImageView deleterow;


        public WorkHolder(@NonNull View itemView) {
            super(itemView);

            eventname = itemView.findViewById(R.id.rc_event_name);
            date = itemView.findViewById(R.id.rc_textview_date);
            duetime = itemView.findViewById(R.id.rc_textview_duetime);
            ratingBar = itemView.findViewById(R.id.rc_ratingBar);
            complete_work = itemView.findViewById(R.id.checkbox_meat);
            //deleterow=itemView.findViewById(R.id.rc_delette_button);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onitemclick(works.get(position));
                    }

                }
            });

            complete_work.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.oncheckitemclick(works.get(position));


                    }

                }
            });


//            deleterow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (listener != null && position != RecyclerView.NO_POSITION) {
//                        listener.ondelteitemclick(position);
//                    }
//
//                }
//            });

        }
    }

    public interface OnitemclickListener {
        void onitemclick(Work work);
        //void ondelteitemclick(int position);
        void oncheckitemclick(Work work);
    }

    public void setOnItemClickListener(OnitemclickListener listener) {
        this.listener = listener;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {

        @Override

        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.count = works_all.size();
                Log.d("dd","dd"+results.count);
                results.values = works_all;
                Log.d("dd","dd"+results.values);
            } else {
                String searchStr = constraint.toString().toUpperCase();
                List<Work> resultsData = new ArrayList<>();
                for (Work work : works_all) {
                    if (work.getEvent_name().toUpperCase().contains(searchStr))
                        resultsData.add(work);
                }
                results.count = resultsData.size();
                results.values = resultsData;
                Log.d("dd","d"+results.count);
                Log.d("dd","d"+results.values);
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            works = (List<Work>) results.values;
            notifyDataSetChanged();
        }
    };

}
