package com.example.playbingo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "UserInfo";
    private static final String DATABASE_TABLE = "UserName";

    private static final String KEY_ID = "id";
    private static final String USER_NAME = "name";
    private static final String PASSWORD = "pass";


    UserDatabase(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table;
        String query = "CREATE TABLE "+DATABASE_TABLE+"("+KEY_ID+"INT PRIMARY KEY,"+
                USER_NAME+" TEXT, "+
                PASSWORD+" TEXT "+")";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion>=newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
        onCreate(db);

    }


    public long addUser(String name, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(USER_NAME,name);
        c.put(PASSWORD,pass);


        long ID = db.insert(DATABASE_TABLE,null,c);

        return ID;
    }

    public String getcurrentuser()
    {
        SQLiteDatabase db = this.getReadableDatabase();


        String name1;
        name1="";
        String query = "SELECT * FROM "+DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            name1 = cursor.getString(cursor.getColumnIndex("name"));
        }
        return name1;

    }

}
