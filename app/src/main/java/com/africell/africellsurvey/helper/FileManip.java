package com.africell.africellsurvey.helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class FileManip {
    Context mContext;
    public FileManip(Context mContext){
        this.mContext = mContext;


    }
    public void createExternalStoragePrivateFile(String content, String fileName, char type) {
        // Create a path where we will place our private file on external
        // storage.
        String folder = "schemas";
        if (type == 'R') {
            folder = "data";
        }

        File dir = new File(mContext.getExternalFilesDir(null), folder);
        // File dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!dir.exists())
            dir.mkdir();
        try {
            // Very simple code to copy a picture from the application's
            // resource into the external file.  Note that this code does
            // no error checking, and assumes the picture is small (does not
            // try to copy it in chunks).  Note that if external storage is
            // not currently mounted this will silently fail.
            File gpxfile = new File(dir, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.i("ExternalStorage", "Error writing " + fileName, e);
        }
    }

    void deleteExternalStoragePrivateFile(String fileName) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), fileName);
        file.delete();
    }

    boolean hasExternalStoragePrivateFile(String fileName) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File file = new File(mContext.getExternalFilesDir(null), fileName);
        return file.exists();
    }
    public File getExternalFile(String fileName){
        File file = new File(mContext.getExternalFilesDir(null), fileName);
        if(file.exists()){
            return file;
        }else
            return null;
    }

    public String readUri(Uri uri) throws IOException {
        ParcelFileDescriptor pdf = mContext.getContentResolver().openFileDescriptor(uri, "r");

        assert pdf != null;
        assert pdf.getStatSize() <= Integer.MAX_VALUE;
        byte[] data = new byte[(int) pdf.getStatSize()];
        StringBuffer fileContent = new StringBuffer("");
        String newText = "";

        FileDescriptor fd = pdf.getFileDescriptor();
        FileInputStream fileStream = new FileInputStream(fd);
        //File tmpFile = new File(mContext.getCacheDir(),"temp_file.txt");
        //FileOutputStream fos = new FileOutputStream(tmpFile);

        int n;
        while ((n = fileStream.read(data)) != -1)
        {
            fileContent.append(new String(data, 0, n));
            //newText += Character.toString((char) n);
            //fos.write(data,0,n);
        }
        //fileStream.read(data);

        fileStream.close();
        //fos.close();

        return fileContent.toString();
    }


}
