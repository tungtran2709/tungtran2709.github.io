package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLite_Product extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Product.db";
    private static final int DATABASE_VERSION = 1;

    public SQLite_Product(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreateDatabase="CREATE TABLE product(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "img INTEGER," +
                "type TEXT," +
                "name TEXT," +
                "count INTEGER," +
                "price TEXT," +
                "date TEXT," +
                "des TEXT)";
        db.execSQL(sqlCreateDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public long addProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put("img", product.getImg());
        values.put("type", product.getType());
        values.put("name", product.getName());
        values.put("count", product.getCount());
        values.put("price", product.getPrice());
        values.put("date", product.getDate());
        values.put("des", product.getDes());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("product", null, values);
    }

    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("product", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int productId = cursor.getInt(0);
                byte[] productImg = cursor.getBlob(1);
                String productType = cursor.getString(2);
                String productName = cursor.getString(3);
                int productCount = cursor.getInt(4);
                String productPrice = cursor.getString(5);
                String productDate = cursor.getString(6);
                String productDes = cursor.getString(7);
                Product p = new Product(productId, productImg, productType, productName, productCount, productPrice, productDate, productDes);
                products.add(p);
            } while (cursor.moveToNext());
        }
        return products;
    }

    public int deleteProduct(int id) {
        String whereClause = "id = ?";
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("product", whereClause, whereArgs);
    }

    public List<Product> getProductByName(String name) {
        List<Product> list=new ArrayList<>();
        String whereClause = "name LIKE ?";
        String[] whereArgs = {"%" + name + "%"};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("product", null, whereClause, whereArgs, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            int productId = cursor.getInt(0);
            byte[] productImg = cursor.getBlob(1);
            String productType = cursor.getString(2);
            String productName = cursor.getString(3);
            int productCount = cursor.getInt(4);
            String productPrice = cursor.getString(5);
            String productDate = cursor.getString(6);
            String productDes = cursor.getString(7);
            list.add(new Product(productId, productImg, productType, productName, productCount, productPrice, productDate, productDes));
        }
        cursor.close();
        return list;
    }

    public Product getProduct(int id) {
        String whereClause = "id = ?";
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("product", null, whereClause, whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int productId = cursor.getInt(0);
            byte[] productImg = cursor.getBlob(1);
            String productType = cursor.getString(2);
            String productName = cursor.getString(3);
            int productCount = cursor.getInt(4);
            String productPrice = cursor.getString(5);
            String productDate = cursor.getString(6);
            String productDes = cursor.getString(7);
            cursor.close();
            return new Product(productId, productImg, productType, productName, productCount, productPrice, productDate, productDes);
        }
        return null;
    }

    public int updateProduct(Product product) {
        ContentValues values = new ContentValues();
        values.put("img", product.getImg());
        values.put("type", product.getType());
        values.put("name", product.getName());
        values.put("count", product.getCount());
        values.put("price", product.getPrice());
        values.put("date", product.getDate());
        values.put("des", product.getDes());
        String whereClause = "id = ?";
        String[] whereArgs = {Integer.toString(product.getId())};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.update("product", values, whereClause, whereArgs);
    }
}
