package com.example.NoteApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private ArrayList<NoteModel> notes = new ArrayList<>();
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getNotes();
        setTitle(R.string.all_notes);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
    }

    public void openNoteDetailsActivity(View view) {
        Intent intent = new Intent(this, NoteDetailesActivity.class);
        startActivity(intent);

    }

    private void getNotes() {
        notes.clear();
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM note ", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String des = cursor.getString(2);
            notes.add(new NoteModel(id, title, des));


        }

        ListNote();


    }

    private void ListNote() {
        View view = findViewById(R.id.layout_no_note);

        if (notes.size() == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.INVISIBLE);
            noteAdapter = new NoteAdapter(this, notes);
            recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setAdapter(noteAdapter);
            swipeToDelete();


        }
    }

    public void swipeToDelete() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                showDeleteDialog(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void deleteFromDB(int position) {
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] args = {String.valueOf(notes.get(position).getId())};
        int deleteRow = db.delete("note", "id==?", args);
        if (deleteRow != 0)
            Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show();


    }


    @SuppressLint("NotifyDataSetChanged")
    private void showDeleteDialog(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(R.string.delete_dialog_positive, (dialog, which) -> {
                    deleteFromDB(position);
                    notes.remove(position);
                    noteAdapter.notifyDataSetChanged();
                    View view = findViewById(R.id.layout_no_note);
                    if (notes.size() == 0) {
                        view.setVisibility(View.VISIBLE);

                    }
                })
                .setNegativeButton(R.string.delete_dialog_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteAdapter.notifyItemChanged(position);
                    }
                })
                .setCancelable(false)
                .show();

    }

}