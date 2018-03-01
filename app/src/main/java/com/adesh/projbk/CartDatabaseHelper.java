package com.adesh.projbk;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "BooksBuddy";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Cart";
    public static final String COLUMN_uid = "uid";
    public static final String COLUMN_bkid = "bkid";
    public static final String COLUMN_bookName = "BookName";
    public static final String COLUMN_price = "price";
    public static final String COLUMN_publisher = "publisher";
    public static final String COLUMN_cartid = "cart_id";
    public static final String COLUMN_cartImage = "cart_image";

    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (" + COLUMN_cartid + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_bkid + " int(13)," + COLUMN_uid + " int(13)," + COLUMN_bookName + " VARCHAR(40)," + COLUMN_price + " int(13)," + COLUMN_cartImage + " VARCHAR(40)," + COLUMN_publisher + " VARCHAR(39))");
    }

    public boolean insertData(String bookname, int bkid, int uid, int price, String imageUrl, String publisher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_uid, uid);
        cv.put(COLUMN_bkid, bkid);
        cv.put(COLUMN_bookName, bookname);
        cv.put(COLUMN_price, price);
        cv.put(COLUMN_cartImage, imageUrl);
        cv.put(COLUMN_publisher, publisher);
        long result = db.insert(TABLE_NAME, null, cv);
        return result == 1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean deleteData(String bkid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "bkid=?", new String[]{bkid});
        return true;
    }

    public Cursor getData(String bkid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where " + COLUMN_bkid + " =?", new String[]{bkid});
        return res;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        return res;
    }

    public Long getCountSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.queryNumEntries(db, TABLE_NAME);
    }

    public boolean Deleteall() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Drop table if exists " + TABLE_NAME);
        onCreate(db);
        return true;
    }
}
