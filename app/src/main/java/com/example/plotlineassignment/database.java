package com.example.plotlineassignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;

import androidx.annotation.Nullable;

// DATABASE : SQLite

public class database extends SQLiteOpenHelper {

    public database(Context context) {
        super(context, "myStorage.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tableimage (name text, org_image blob, res_image blob );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists tableimage");
    }

    public boolean insertdata(String username, byte[] org_img, byte[] res_img){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", username);
        contentValues.put("org_image", org_img);
        contentValues.put("res_image", res_img);
        long ins = MyDB.insert("tableimage", null, contentValues);
        if(ins==-1) return false;
        else return true;
    }

    //Fetch NAME attribute
    public String getName(String name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from tableimage where name = ?", new String[]{name});
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    //Fetch original and result Bitmap attributes
    public Pair<Bitmap,Bitmap> getImage(String name){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from tableimage where name = ?", new String[]{name});
        cursor.moveToFirst();
        byte[] org_bitmap = cursor.getBlob(1);
        byte[] res_bitmap = cursor.getBlob(2);
        return new Pair<Bitmap, Bitmap>(BitmapFactory.decodeByteArray(org_bitmap, 0 , org_bitmap.length),BitmapFactory.decodeByteArray(res_bitmap, 0 , res_bitmap.length));
    }


}
