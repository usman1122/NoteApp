package com.example.notety.noteapp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URI;

public class Main2Activity extends AppCompatActivity {
         private String action;
         private EditText editor;
    private String NoteFilter;
    private String OldText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editor= (EditText) findViewById(R.id.editText);




        Intent intent= getIntent();
        Uri uri = intent.getParcelableExtra(mContentProvider.CONTENT_ITEM_TYPE);

        if (uri == null){
          action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.newNote));  //check resource
        }
        else {
            action= Intent.ACTION_EDIT;
           NoteFilter= DBOpenHelper.NOTE_ID + "=" + uri.getLastPathSegment();
            Cursor cursor= getContentResolver().query(uri,DBOpenHelper.ALL_COLUMNS,NoteFilter,null,null,null);
            cursor.moveToFirst();
            OldText= cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(OldText);
            editor.requestFocus();
        }

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishedEditing();

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);}


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (action.equals(Intent.ACTION_EDIT))
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case android.R.id.home:
                finishedEditing();
                break;
            case R.id.DELETE_Icon:
                deleteNote();
                break;
        }
        return true;
    }

    private void deleteNote() {
        getContentResolver().delete(mContentProvider.CONTENT_URI,NoteFilter,null);
        Toast.makeText(this,"Note Deleted", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    public void finishedEditing(){
        String checkText= editor.getText().toString().trim(); //check user input
        switch (action){
            case Intent.ACTION_INSERT:            //if inserting blank note
                if (checkText.length()==0){
                    setResult(RESULT_CANCELED);
                }else {
                    insertNote(checkText);
                }
                break;
            case Intent.ACTION_EDIT:
           if (checkText.length()==0) {
              deleteNote();
           }
            else if (OldText.equals(checkText)){
                setResult(RESULT_CANCELED);
            }
            else {
                updateNote(checkText);
            }
        }
        finish();                         //finish this activity and go back to parent
    }

    private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        getContentResolver().update(mContentProvider.CONTENT_URI, values, NoteFilter, null);
        Toast.makeText(this, R.string.Note_Updated, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);

    }


    private void insertNote(String checkText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, checkText);
        getContentResolver().insert(mContentProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishedEditing();
    }
}
