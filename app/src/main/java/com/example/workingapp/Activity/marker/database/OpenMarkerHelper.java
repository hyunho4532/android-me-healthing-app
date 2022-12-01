package com.example.workingapp.Activity.marker.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.workingapp.Activity.marker.data.MarkerItem;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class OpenMarkerHelper extends SQLiteOpenHelper {

    private static final String DatabaseName = "markerLocation.db";
    private static final int DBVersion = 1;

    public OpenMarkerHelper(@Nullable Context context) {
        super(context, DatabaseName, null, DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS MARKER(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, snippet TEXT, latitude DOUBLE, hardness DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

    public ArrayList<MarkerItem> getMarkerList() {
        ArrayList<MarkerItem> markerItems = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM MARKER ORDER BY id DESC", null);

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String snippet = cursor.getString(cursor.getColumnIndexOrThrow("snippet"));
                double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                double hardness = cursor.getDouble(cursor.getColumnIndexOrThrow("hardness"));

                MarkerItem markerItem = new MarkerItem();
                markerItem.setId(id);
                markerItem.setTitle(title);
                markerItem.setSnippet(snippet);
                markerItem.setLatitude(latitude);
                markerItem.setHardness(hardness);

                markerItems.add(markerItem);
            }
        }

        cursor.close();

        return markerItems;
    }

    public void onInsert(String _title, String _snippet, double _latitude, double _hardness) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String sql = "INSERT INTO MARKER(title, snippet, latitude, hardness) VALUES('" + _title + "', '" + _snippet + "', '" + _latitude + "', '" + _hardness + "');";

        sqLiteDatabase.execSQL(sql);
    }

    public void onUpdate(String _title, String _snippet, double _latitude, double _hardness, int _id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String sql = "UPDATE MARKER SET title='" + _title + "', content='" + _snippet + "', latitude='" + _latitude + "', hardness='" + _hardness + "' WHERE id='" + _id + "'";

        sqLiteDatabase.execSQL(sql);
    }

    public void onDelete(String _title) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        String sql = "DELETE FROM MARKER WHERE title='" + _title + "'";

        sqLiteDatabase.execSQL(sql);
    }
}
