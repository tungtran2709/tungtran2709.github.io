package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLite_Staff extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Staff.db";
    private static final int DATABASE_VERSION = 1;

    public SQLite_Staff(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateDatabase="CREATE TABLE staff(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "img INTEGER," +
                "name TEXT," +
                "pass TEXT," +
                "phone TEXT," +
                "mail TEXT," +
                "date TEXT," +
                "gender TEXT," +
                "position TEXT)";
        db.execSQL(sqlCreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public long addStaff(Staff staff) {
        ContentValues values = new ContentValues();
        values.put("img", staff.getImg());
        values.put("name", staff.getName());
        values.put("pass", staff.getPassword());
        values.put("phone", staff.getPhone());
        values.put("mail", staff.getMail());
        values.put("date", staff.getDate());
        values.put("gender", staff.getGender());
        values.put("position", staff.getPosition());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("staff", null, values);
    }

    public List<Staff> getAllStaff() {
        List<Staff> staffs = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("staff", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int staffId = cursor.getInt(0);
                byte[] staffImg = cursor.getBlob(1);
                String staffName = cursor.getString(2);
                String staffPass = cursor.getString(3);
                String staffPhone = cursor.getString(4);
                String staffMail = cursor.getString(5);
                String staffDate = cursor.getString(6);
                String staffGender = cursor.getString(7);
                String staffPosition = cursor.getString(8);
                Staff s = new Staff(staffId, staffImg, staffName, staffPass, staffPhone, staffMail, staffDate, staffGender, staffPosition);
                staffs.add(s);
            } while (cursor.moveToNext());
        }
        return staffs;
    }

    public Staff getStaff(int id) {
        String whereClause = "id = ?";
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("staff", null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int staffId = cursor.getInt(0);
            byte[] staffImg = cursor.getBlob(1);
            String staffName = cursor.getString(2);
            String staffPass = cursor.getString(3);
            String staffPhone = cursor.getString(4);
            String staffMail = cursor.getString(5);
            String staffDate = cursor.getString(6);
            String staffGender = cursor.getString(7);
            String staffPosition = cursor.getString(8);
            cursor.close();
            return new Staff(staffId, staffImg, staffName, staffPass, staffPhone, staffMail, staffDate, staffGender, staffPosition);
        }
        return null;
    }

    public int updateStaff(Staff staff) {
        ContentValues values = new ContentValues();
        values.put("img", staff.getImg());
        values.put("phone", staff.getPhone());
        values.put("mail", staff.getMail());
        values.put("date", staff.getDate());
        values.put("gender", staff.getGender());
        values.put("position", staff.getPosition());
        String whereClause = "id = ?";
        String[] whereArgs = {Integer.toString(staff.getId())};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.update("staff", values, whereClause, whereArgs);
    }

    public int deleteStaff(int id) {
        String whereClause = "id = ?";
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("staff", whereClause, whereArgs);
    }

    public List<Staff> getStaffByName(String name) {
        List<Staff> list=new ArrayList<>();
        String whereClause = "name LIKE ?";
        String[] whereArgs = {"%" + name + "%"};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("staff", null, whereClause, whereArgs, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            int staffId = cursor.getInt(0);
            byte[] staffImg = cursor.getBlob(1);
            String staffName = cursor.getString(2);
            String staffPass = cursor.getString(3);
            String staffPhone = cursor.getString(4);
            String staffMail = cursor.getString(5);
            String staffDate = cursor.getString(6);
            String staffGender = cursor.getString(7);
            String staffPosition = cursor.getString(8);
            list.add(new Staff(staffId, staffImg, staffName, staffPass, staffPhone, staffMail, staffDate, staffGender, staffPosition));
        }
        cursor.close();
        return list;
    }

    public int changePassStaff(Staff staff, String pass_new) {
        ContentValues values = new ContentValues();
        values.put("pass", pass_new);
        String whereClause = "id = ?";
        String[] whereArgs = {Integer.toString(staff.getId())};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.update("staff", values, whereClause, whereArgs);
    }
}
