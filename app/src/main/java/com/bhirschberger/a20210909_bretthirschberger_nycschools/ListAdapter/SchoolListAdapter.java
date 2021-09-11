package com.bhirschberger.a20210909_bretthirschberger_nycschools.ListAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhirschberger.a20210909_bretthirschberger_nycschools.Activities.SchoolDetailActivity;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.R;
import com.bhirschberger.a20210909_bretthirschberger_nycschools.models.School;

import java.util.ArrayList;
import java.util.List;

public class SchoolListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    public static final String SCHOOL_LIST_INDEX = "schoollist";

    private final Context context;
    private final List<School> schools;
    private final List<School> schoolsFull;
    private int lastClick = -2;

    public SchoolListAdapter(Context context, List<School> schools) {
        this.context = context;
        this.schools = schools;
        this.schoolsFull = new ArrayList<>(schools);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        switch (viewType) {
            case 0:
                view = inflater.inflate(R.layout.school_list_cell, parent, false);
                return new SchoolViewHolder(view);
            case 1:
                view = inflater.inflate(R.layout.school_list_cell_no_email, parent, false);
                return new NoEmailSchoolViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
        // inflates different view based on if email is provided
        if (h instanceof SchoolViewHolder) {
            SchoolViewHolder holder = (SchoolViewHolder) h;
            School school = schools.get(position);
            holder.nameView.setText(school.getName());
            holder.expandableLayout.setVisibility(school.isExpanded() ? View.VISIBLE : View.GONE);
            holder.nameView.setOnClickListener(v -> {
                schools.get(position).setExpanded(!school.isExpanded());
                notifyItemChanged(position);
                // closes last item selected
                if (lastClick != -2 && lastClick != holder.getAdapterPosition()) {
                    schools.get(lastClick).setExpanded(false);
                    notifyItemChanged(lastClick);
                }
                lastClick = holder.getAdapterPosition();
            });
        } else if (h instanceof NoEmailSchoolViewHolder){
            NoEmailSchoolViewHolder holder = (NoEmailSchoolViewHolder) h;
            School school = schools.get(position);
            holder.nameView.setText(school.getName());

            holder.expandableLayout.setVisibility(school.isExpanded() ? View.VISIBLE : View.GONE);
            holder.nameView.setOnClickListener(v -> {
                schools.get(position).setExpanded(!school.isExpanded());
                notifyItemChanged(position);
                // closes last item selected
                if (lastClick != -2 && lastClick != holder.getAdapterPosition()) {
                    schools.get(lastClick).setExpanded(false);
                    notifyItemChanged(lastClick);
                }
                lastClick = holder.getAdapterPosition();
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        // determines if what type of view is necessary
        School school = schools.get(position);
        if (!school.getEmail().equals("ERROR")) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return schools.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<School> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(schoolsFull);
                } else {
                    String filter = constraint.toString().toLowerCase().trim();
                    for (School school : schoolsFull) {
                        if (school.getName().toLowerCase().contains(filter)) {
                            filteredList.add(school);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                schools.clear();
                schools.addAll((List) results.values);
                notifyDataSetChanged();
                //closes all entries on search
                for (School school : schools) {
                    school.setExpanded(false);
                }
                lastClick = -2;
            }
        };
    }

    public class SchoolViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private ImageButton callButton;
        private ImageButton emailButton;
        private ImageButton infoButton;
        private ConstraintLayout expandableLayout;

        public SchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameText);
            callButton = itemView.findViewById(R.id.callButton);
            emailButton = itemView.findViewById(R.id.mapButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            //sets listeners for button
            callButton.setOnClickListener(this::sendToPhone);
            emailButton.setOnClickListener(this::sendToEmail);
            infoButton.setOnClickListener(this::sendToDetail);
        }

        public void sendToPhone(View view) {
            School school = schools.get(getAdapterPosition());
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + school.getPhoneNumber()));
            view.getContext().startActivity(intent);
        }

        public void sendToEmail(View view) {
            School school = schools.get(getAdapterPosition());
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, school.getEmail());
            view.getContext().startActivity(intent);
        }

        public void sendToDetail(View view) {
            Intent intent = new Intent(context, SchoolDetailActivity.class);

            intent.putExtra(SCHOOL_LIST_INDEX, schools.get(getAdapterPosition()));
            view.getContext().startActivity(intent);
        }
    }

    public class NoEmailSchoolViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private ImageButton callButton;
        private ImageButton infoButton;
        private ConstraintLayout expandableLayout;

        public NoEmailSchoolViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameText);
            callButton = itemView.findViewById(R.id.callButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            //sets listeners for button
            callButton.setOnClickListener(this::sendToPhone);
            infoButton.setOnClickListener(this::sendToDetail);
        }


        public void sendToPhone(View view) {
            School school = schools.get(getAdapterPosition());
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + school.getPhoneNumber()));
            view.getContext().startActivity(intent);
        }

        public void sendToDetail(View view) {
            Intent intent = new Intent(context, SchoolDetailActivity.class);

            intent.putExtra(SCHOOL_LIST_INDEX, schools.get(getAdapterPosition()));
            view.getContext().startActivity(intent);
        }
    }

}
