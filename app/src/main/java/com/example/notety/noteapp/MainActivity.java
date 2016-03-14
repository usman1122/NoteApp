package com.example.notety.noteapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cursorAdapter;
    private int Editor_Request_Code= 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        InsertNote("New note");  123



        cursorAdapter = new mCursorAdapter(this, null,  0);
        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setAdapter(cursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                Uri uri = Uri.parse(mContentProvider.CONTENT_URI + "/" + id);
                intent.putExtra(mContentProvider.CONTENT_ITEM_TYPE,uri);
                startActivityForResult(intent,Editor_Request_Code);
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

//     sample code to insert sample data all commented out sections of this are denoted with 123
//    private void InsertNote(String noteText) {
//        ContentValues values = new ContentValues();
//        values.put(DBOpenHelper.NOTE_TEXT, noteText);
//        Uri mNotesUri = getContentResolver().insert(mContentProvider.CONTENT_URI, values);
//        mNotesUri.getLastPathSegment();
//    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        switch (id){
////   123         case R.id.CREATE_SAMPLE:
//////                InsertData();
//                break;
            case  R.id.DELETE_SAMPLE:
                DeleteData();
                break;
        }

        return true;
    }

    private void DeleteData() {

        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(mContentProvider.CONTENT_URI,null,null);
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                        restartLoader();
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

    }
//
//  123   private void InsertData() {
//        InsertNote("asdasdasdassdasdadasdddadsasdadadsads");
//        InsertNote("asdad \n asdadasda");
//        restartLoader();
//    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, mContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


    public void neW(View view) {
        Intent intent=new Intent(this,Main2Activity.class);
        startActivityForResult(intent,Editor_Request_Code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== Editor_Request_Code && resultCode==RESULT_OK){
            restartLoader();
        }
    }
}
