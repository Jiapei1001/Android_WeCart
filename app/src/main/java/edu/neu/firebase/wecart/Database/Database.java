package edu.neu.firebase.wecart.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import edu.neu.firebase.wecart.Order;

public class Database extends SQLiteAssetHelper {

    private static final String name = "OrderItDB.db";
    private static final int version = 1;

    public Database(Context context) {
        super(context, name, null, version);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductId","ProductName","Quantity", "Price"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor cursor = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do {
                result.add(new Order(cursor.getString(cursor.getColumnIndexOrThrow("ProductId")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ProductName")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Quantity")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Price"))
                ));
            }
            while (cursor.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail('ProductId','ProductName','Quantity','Price')" + "VALUES('%s','%s','%s','%s');",
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice());
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

}
