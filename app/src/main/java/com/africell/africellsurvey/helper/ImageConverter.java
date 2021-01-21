package com.africell.africellsurvey.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * class holding methods to convert image from bitmap to string,  and vice-versa
 */
public class ImageConverter {
    /**
     * convert base4 string to bitmap image
     * @param base64Str
     * @return
     * @throws IllegalArgumentException
     */
    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    /**
     * convert bitmap image to base4 string
     * @param bitmap
     * @return
     */
    public static String convert(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] imageBytes = baos.toByteArray();

        String base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return base64String;
    }

}
