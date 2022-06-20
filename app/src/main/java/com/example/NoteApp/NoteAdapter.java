package com.example.NoteApp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<NoteModel> noteModelList;

    public NoteAdapter(Context context, List<NoteModel> noteModelList) {
        this.context = context;
        this.noteModelList = noteModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_title.setText(noteModelList.get(position).getTitle());
        holder.tv_description.setText(noteModelList.get(position).getDescription());
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NoteDetailesActivity.class);
            intent.putExtra("id", noteModelList.get(position).getId());
            intent.putExtra("title", noteModelList.get(position).getTitle());
            intent.putExtra("description", noteModelList.get(position).getDescription());

            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return noteModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_description;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
            cardView = itemView.findViewById(R.id.card_view);


        }
    }
}
