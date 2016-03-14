package com.example.notety.noteapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Lenovo on 13/03/2016.
 */
public class mCursorAdapter extends CursorAdapter {
    public mCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {       //returns the view

        return LayoutInflater.from(context).inflate(R.layout.notelist,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {            //bind to a particular row of database that needs to be displayed
               String noteText=cursor.getString(                                  //by using instance of a cursor object
                       cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
        int pos=noteText.indexOf(10);
        if (pos != -1){
            noteText = noteText.substring(0,pos) + "...";
        }
        TextView tV= (TextView) view.findViewById(R.id.tvNote);
        tV.setText(noteText);
    }
    }


