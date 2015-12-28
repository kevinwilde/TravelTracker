package kevinwilde.traveltracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Wilde on 12/26/2015.
 */
public class MemoriesDataSource {

    private DbHelper mDbHelper;
    private String[] allColumns = {
            DbHelper.COLUMN_ID, DbHelper.COLUMN_CITY,
            DbHelper.COLUMN_COUNTRY, DbHelper.COLUMN_NOTES,
            DbHelper.COLUMN_LATITUDE, DbHelper.COLUMN_LONGITUDE
    };

    public MemoriesDataSource(Context context){
        mDbHelper = DbHelper.getInstance(context);
    }

    public void CreateMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_CITY, memory.city);
        values.put(DbHelper.COLUMN_COUNTRY, memory.country);
        values.put(DbHelper.COLUMN_NOTES, memory.notes);
        values.put(DbHelper.COLUMN_LATITUDE, memory.latitude);
        values.put(DbHelper.COLUMN_LONGITUDE, memory.longitude);
        memory.id = mDbHelper.getWritableDatabase().insert(DbHelper.MEMORIES_TABLE, null, values);
    }

    public void UpdateMemory(Memory memory) {
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_CITY, memory.city);
        values.put(DbHelper.COLUMN_COUNTRY, memory.country);
        values.put(DbHelper.COLUMN_NOTES, memory.notes);
        values.put(DbHelper.COLUMN_LATITUDE, memory.latitude);
        values.put(DbHelper.COLUMN_LONGITUDE, memory.longitude);
        String[] whereArgs = { String.valueOf(memory.id) };
        mDbHelper.getWritableDatabase().update(
                DbHelper.MEMORIES_TABLE,
                values,
                DbHelper.COLUMN_ID+"=?",
                whereArgs
        );
    }

    public void DeleteMemory(Memory memory) {
        String[] whereArgs = { String.valueOf(memory.id) };
        mDbHelper.getWritableDatabase().delete(
                DbHelper.MEMORIES_TABLE,
                DbHelper.COLUMN_ID + "=?",
                whereArgs
        );
    }

    public Cursor allMemoriesCursor(){
        return mDbHelper.getReadableDatabase().query(DbHelper.MEMORIES_TABLE, allColumns, null, null, null, null, null);
    }

    public List<Memory> getAllMemories() {
        Cursor cursor = allMemoriesCursor();
        return cursorToMemoriesList(cursor);
    }

    public List<Memory> cursorToMemoriesList(Cursor cursor) {
        List<Memory> memories = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Memory memory = cursorToMemory(cursor);
            memories.add(memory);
            cursor.moveToNext();
        }
        return memories;
    }

    private Memory cursorToMemory(Cursor cursor){
        Memory memory = new Memory();
        memory.id = cursor.getLong(0);
        memory.city = cursor.getString(1);
        memory.country = cursor.getString(2);
        memory.notes = cursor.getString(3);
        memory.latitude = cursor.getDouble(4);
        memory.longitude = cursor.getDouble(5);
        return memory;
    }

}
