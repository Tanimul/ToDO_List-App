package com.example.todo_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkHolder> {

    private List<Work> works = new ArrayList<>();
    private OnitemclickListener listener;

    @NonNull
    @Override
    public WorkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.numberofwork, parent, false);

        return new WorkHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkHolder holder, int position) {
        Work currentwork = works.get(position);
        holder.eventname.setText(currentwork.getEvent_name());
        holder.date.setText(currentwork.getDate());
        holder.duetime.setText(currentwork.getDue_time());
        holder.ratingBar.setRating(works.get(position).getRating());
    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    public void setNotes(List<Work> works) {
        this.works = works;
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
        private ImageView deleterow;


        public WorkHolder(@NonNull View itemView) {
            super(itemView);

            eventname = itemView.findViewById(R.id.rc_event_name);
            date = itemView.findViewById(R.id.rc_textview_date);
            duetime = itemView.findViewById(R.id.rc_textview_duetime);
            ratingBar = itemView.findViewById(R.id.rc_ratingBar);
            deleterow=itemView.findViewById(R.id.rc_delette_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onitemclick(works.get(position));
                    }

                }
            });

            deleterow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.ondelteitemclick(position);
                    }

                }
            });
        }
    }
    public interface OnitemclickListener {
        void onitemclick(Work work);
        void ondelteitemclick(int position);
    }

    public void setOnItemClickListener(OnitemclickListener listener) {
        this.listener = listener;
    }

}
