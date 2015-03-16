package com.example.conor.senan.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.conor.senan.SenanApp;
import com.example.conor.senan.models.SellerAdModel;
import com.example.conor.senan.utils.ImageUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * This is the dao to access ad data.
 * The data structure it uses is <code>SellerAdModel</code>
 *
 * At the moment it uses a database to persist data
 */
public class SellerAdDao {

    protected static final String TABLE = "SellerAdds";
    protected static final String _ID = "_id";
    protected static final String TITLE = "TITLE";
    protected static final String DETAILS = "DETAILS";
    protected static final String IMAGE_NAME = "IMAGE_NAME";
    protected static final String AMOUNT_LEFT = "AMOUNT_LEFT";
    protected static final String PRICE = "PRICE";
    protected static final String LOCKED = "LOCKED";

    protected static final String OLD_PRICE = "OLD_PRICE";
    protected static final String LAT = "LAT";
    protected static final String LNG = "LNG";

    protected static final String END_TIMESTAMP = "END_TIMESTAMP";


    public ArrayList<SellerAdModel> getAll() {
        ArrayList<SellerAdModel> list = new ArrayList<SellerAdModel>();
        SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            SellerAdModel model = new SellerAdModel();

            model.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            model.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            model.setDetails(cursor.getString(cursor.getColumnIndex(DETAILS)));
            model.setImageName(cursor.getString(cursor.getColumnIndex(IMAGE_NAME)));
            model.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE)));
            model.setAmountLeft(cursor.getInt(cursor.getColumnIndex(AMOUNT_LEFT)));
            model.setLocked(cursor.getInt(cursor.getColumnIndex(LOCKED)) == 1);

            model.setOldPrice(cursor.getInt(cursor.getColumnIndex(OLD_PRICE)));
            model.setEndTimestamp(cursor.getLong(cursor.getColumnIndex(END_TIMESTAMP)));

            LatLng latLng = new LatLng(
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(LAT))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(LNG)))
            );

            model.setLatLng(latLng);
            list.add(model);
        }

        cursor.close();
        db.close();
        return list;
    }

    public SellerAdModel getId(int id) {
        SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, _ID + "=?", new String[]{Integer.toString(id)}, null, null, null);
        SellerAdModel model = null;
        if (cursor.moveToFirst()) {

            model = new SellerAdModel();
            model.setId(cursor.getInt(cursor.getColumnIndex(_ID)));
            model.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            model.setDetails(cursor.getString(cursor.getColumnIndex(DETAILS)));
            model.setImageName(cursor.getString(cursor.getColumnIndex(IMAGE_NAME)));
            model.setPrice(cursor.getInt(cursor.getColumnIndex(PRICE)));
            model.setAmountLeft(cursor.getInt(cursor.getColumnIndex(AMOUNT_LEFT)));
            model.setLocked(cursor.getInt(cursor.getColumnIndex(LOCKED)) == 1);

            model.setOldPrice(cursor.getInt(cursor.getColumnIndex(OLD_PRICE)));
            model.setEndTimestamp(cursor.getLong(cursor.getColumnIndex(END_TIMESTAMP)));

            LatLng latLng = new LatLng(
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(LAT))),
                    Double.parseDouble(cursor.getString(cursor.getColumnIndex(LNG)))
            );

            model.setLatLng(latLng);

        }

        cursor.close();
        db.close();


        return model;
    }

    public long insert(SellerAdModel sellerAdModel) {
        SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        if (sellerAdModel.getId() > 0) values.put(_ID, sellerAdModel.getId());
        values.put(TITLE, sellerAdModel.getTitle());
        values.put(DETAILS, sellerAdModel.getDetails());
        values.put(PRICE, sellerAdModel.getPrice());
        values.put(AMOUNT_LEFT, sellerAdModel.getAmountLeft());
        values.put(LOCKED, sellerAdModel.isLocked());

        values.put(OLD_PRICE, sellerAdModel.getOldPrice());
        values.put(END_TIMESTAMP, sellerAdModel.getEndTimestamp());

        values.put(LAT, sellerAdModel.getLatLng().latitude + "");
        values.put(LNG, sellerAdModel.getLatLng().longitude + "");

        long num = db.insert(TABLE, null, values);
        db.close();
        return num;
    }

    public int update(SellerAdModel sellerAdModel) {
        SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID, sellerAdModel.getId());
        values.put(TITLE, sellerAdModel.getTitle());
        values.put(DETAILS, sellerAdModel.getDetails());
        values.put(PRICE, sellerAdModel.getPrice());
        values.put(AMOUNT_LEFT, sellerAdModel.getAmountLeft());

        values.put(OLD_PRICE, sellerAdModel.getOldPrice());
        values.put(LOCKED, sellerAdModel.isLocked());

        values.put(END_TIMESTAMP, sellerAdModel.getEndTimestamp());

        values.put(LAT, sellerAdModel.getLatLng().latitude + "");
        values.put(LNG, sellerAdModel.getLatLng().longitude + "");

        int num = db.update(TABLE, values, _ID + "=?", new String[]{Integer.toString(sellerAdModel.getId())});
        db.close();
        return num;
    }


    public void delete(int id) {

        boolean result = ImageUtils.deleteImageFromStorage("add_image_" + id);

        SQLiteDatabase db = new DatabaseHelper().getWritableDatabase();
        db.delete(TABLE, _ID + "=?", new String[]{Integer.toString(id)});
        db.close();
    }


}

