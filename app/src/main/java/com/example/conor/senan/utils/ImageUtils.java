package com.example.conor.senan.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.example.conor.senan.SenanApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Conor on 26/01/2015.
 */
public class ImageUtils {

    //From: http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app
    public static Bitmap decodeUri(Uri selectedImage, Context context) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);

    }

    //From: http://stackoverflow.com/questions/17674634/saving-images-to-internal-memory-in-android#
    public static String saveImageToInternalStorage(Bitmap bitmapImage, String name){
        ContextWrapper cw = new ContextWrapper(SenanApp.getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory, name);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.w("conorbonor",  bitmapImage.toString() +" tjyf " + directory.getAbsolutePath());
        } catch (Exception e) {
            Log.w("conorbonor",  e.getMessage());
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    //From: http://stackoverflow.com/questions/17674634/saving-images-to-internal-memory-in-android#
    public static Bitmap loadImageFromStorage(String name)
    {


        String path = "/data/data/com.example.conor.senan/app_imageDir";
        Bitmap b = null;
        try {
            File f=new File(path, name);
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return b;

    }



    public static boolean deleteImageFromStorage(String name)
    {
        String path = "/data/data/com.example.conor.senan/app_imageDir";

         File f=new File(path, name);
         return f.delete();

    }




}
