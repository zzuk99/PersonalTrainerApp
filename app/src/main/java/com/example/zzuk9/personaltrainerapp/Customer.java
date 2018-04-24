package com.example.zzuk9.personaltrainerapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class Customer {
    private int picID;
    private String name;

    public Customer(String name, int picID){
        this.picID = picID;
        this.name = name;
    }
    public int getCustomerImage() {return this.picID;}

    public String getName() { return this.name;}

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_ok) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDataFormat("yyyyMMdd_HHmmss").format(new Data());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }
    static final int REQUEST_TAKE_PHOTO = 1;
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this."com.example.zzuk9.personaltrainerapp.SQLiteOpenHelper",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRASTORE.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }




    public class PersonalTrainerDatabase {
        public static final String KEY_ID = "_id";
        public static final String KEY_ACCOUNTS_NAME_COLUMN = "ACCOUNTS_NAME_COULMN";
        public static final String KEY_ACOOUNTS_ACCESSIBLE_COULMN = "ACCOUNTS_ACCESSIBLE_COULMN";
        public static final String KEY_ACCOUNTS_DATE_COULMN = "ACCOUNTS_DATE_COLUMN";

        private HoardDBOpenHelper hoardDBOpenHelper;
        public PersonalTrainerDatabase(Context context) {
            hoardDBOpenHelper = new HoardDBOpenHelper(context, HoardDBOpenHelper.DATABASE_NAME, null,
                    HoardDBOpenHelper.DATABASE_VERSION);
        }

        public void closeDatabase(){
            hoardDBOpenHelper.close();
        }

        private Cursor getAccessibleHoard() {
            String[] result_columns = new String[] {
                    KEY_ID, KEY_ACOOUNTS_ACCESSIBLE_COULMN, KEY_ACCOUNTS_DATE_COULMN};
            String where = KEY_ACOOUNTS_ACCESSIBLE_COULMN +"-" + 1;
            String whereArgs[] = null;
            String groupBy = null;
            String having = null;
            String order = null;
            SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
            Cursor cursor = db.query(HoardDBOpenHelper.DATABASE_TABLE,
                    result_columns, where, whereArgs, groupBy, having, order);
            return cursor;
        }
        public float getAverageAccessibleHoardValue(){
            Cursor cursor = getAccessibleHoard();
            float totalHoard = 0f;
            float averageHoard= 0f;

            int ACCOUNTS_COLUMN_INDEX = cursor.getColumnIndexOrThrow(KEY_ACCOUNTS_NAME_COLUMN);
            while (cursor.moveToNext()) {
                float hoard = cursor.getFloat(ACCOUNTS_COLUMN_INDEX);
                totalHoard += hoard;
            }
            float cursorCount = cursor.getCount();
            averageHoard = cursorCount > 0 ?(totalHoard / cursorCount) : Float.NaN;
            cursor.close();
            return averageHoard;
        }
        public void addNewHoard(String hoardName, float hoardDate, boolean hoardAccessible){
            ContentValues newValues = new ContentValues();

            newValues.put(KEY_ACCOUNTS_NAME_COLUMN, hoardName);
            newValues.put(KEY_ACCOUNTS_DATE_COULMN, hoardDate);
            newValues.put(KEY_ACOOUNTS_ACCESSIBLE_COULMN, hoardAccessible);

            SQLiteDatabase db = hoardDBOpenHelper.getWritableDatabase();
            db.insert(HoardDBOpenHelper.DATABASE_TABLE, null, newValues);
        }
    }

    Private static class HoardDBOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "PersonalTrainerApp.db";
        private static final String DATABASE_TABLE = "Accounts";
        private static final int DATABASE_VERSION ="1";

        private static final String DATABASE_CREATE = "create Table"
                + DATABASE_TABLE + "(" + KEY_ID +
                "integer primary key autoincrement," +
                KEY_ACCOUNTS_NAME_COLUMN + "text not null,"
                + KEY_ACCOUNTS_COLUMN + "float,"
                + KEY_ACCOUNTS_ACCESSABLE_COLUMN + "integers);";

        public HoardDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("TaskDBAdapter", "Upgrading from version" + oldVersion + "to" + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF IT EXISTS" + DATABASE_TABLE);
            onCreate(db);
        }


    }
}


