package com.example.conor.senan.daos;


import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.conor.senan.SenanApp;

import java.util.ArrayList;

//adb -d shell 'run-as com.example.conor.senan cat /data/data/com.example.conor.senan/databases/SenanApp.sqlite > /sdcard/dbname.sqlite'

public final class DatabaseHelper extends SQLiteOpenHelper {

    @SuppressWarnings("unused")
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "SenanApp";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper() {
        super(SenanApp.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        final String counter = "CREATE TABLE " + SellerAdDao.TABLE + "(" +
                SellerAdDao._ID + " integer primary key, " +
                SellerAdDao.TITLE + " text, " +
                SellerAdDao.DETAILS + " text, " +
                SellerAdDao.IMAGE_NAME + " text, " +
                SellerAdDao.AMOUNT_LEFT + " text, " +
                SellerAdDao.PRICE + " int, " +
                SellerAdDao.LOCKED + " int," +

                SellerAdDao.END_TIMESTAMP + " int," +
                SellerAdDao.OLD_PRICE + " int," +
                SellerAdDao.LAT + " text," +
                SellerAdDao.LNG + " text" +
                ")";
        database.execSQL(counter);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,"Upgrading database from version"
                +oldVersion + " to " +newVersion + ", This will remove all previous data");
        db.execSQL("Drop TABLE IF EXISTS " + SellerAdDao.TABLE);
        onCreate(db);

    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }

}
