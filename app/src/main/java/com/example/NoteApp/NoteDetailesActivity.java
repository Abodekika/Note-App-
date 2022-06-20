package com.example.NoteApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteDetailesActivity extends AppCompatActivity {
    private EditText title_et;
    private EditText description_et;

    private int receivedId;
    private boolean openedAs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detailes);
        title_et = findViewById(R.id.et_title);
        description_et = findViewById(R.id.et_description);

        receivedId = getIntent().getIntExtra("id", -1);

        if (receivedId != -1) {
            setTitle(R.string.update_note);
            title_et.setText(getIntent().getStringExtra("title"));
            description_et.setText(getIntent().getStringExtra("description"));
            Button button = findViewById(R.id.update_button);
            button.setVisibility(View.VISIBLE);

            openedAs = true;
        } else {

            setTitle(R.string.add_note);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (openedAs) {
            return false;
        } else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.save_note_menu, menu);
            return true;
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.item_save_note) {

            saveNote();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title = title_et.getText().toString();
        String description = description_et.getText().toString();

        if (title.isEmpty()) {
            title_et.setError(getString(R.string.required_field));

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", title);
            contentValues.put("description", description);
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long id = db.insert("note", null, contentValues);
            if (id != -1) {
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    public void updateNote(View view) {
        String title = title_et.getText().toString();
        String description = description_et.getText().toString();
        if (title.isEmpty()) {
            title_et.setError(getString(R.string.required_field));
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", title);
            contentValues.put("description", description);
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] args = {String.valueOf(receivedId)};

            int id = db.update("note", contentValues, "id==?", args);
            if (id != 0) {
                Toast.makeText(this, R.string.note_saved, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}