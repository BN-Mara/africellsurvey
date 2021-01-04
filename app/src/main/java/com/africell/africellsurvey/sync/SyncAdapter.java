package com.africell.africellsurvey.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.africell.africellsurvey.db.SurveyFormDao;
import com.africell.africellsurvey.helper.SaveSharedPreference;
import com.africell.africellsurvey.model.FormData;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.repository.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Scheduler;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

//@AndroidEntryPoint
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    ContentResolver contentResolver;
    int currD = 0;
    //@Inject
    //Repository repository;

    private List<SurveyForm> surveyForms;

    Context mContext;
    @Inject
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
        this.mContext = context;
    }



    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG,"onperform sync start");
       // List<SurveyForm> surveyFormList = repository.getSurveyForms();
        File[] files = loadAllFormFile("data");
        String fname = null;
        String id = null;
        if(files != null && files.length > 0) {
            for (int i=0;i<files.length; i++) {
                fname = files[i].getName();
                if (!fname.contains(".json")) {
                    id = fname;
                    fname = fname + ".json";
                } else {

                    id = fname.substring(0, fname.indexOf(".json"));
                }

                String jsonData = loadJSONFromFile(fname, 'R');
                sendData(id, jsonData);
            }
        }

       /* for(SurveyForm sf : surveyForms){
            String jsons = loadJSONFromFile(sf.getSchema_path(),'R');
            JSONArray ja = null;
            if(jsons != null){
                sendData(sf.getId(),jsons);

            }


        }*/
    }

    public void sendData(String formId, String json){
        //send each object containing form value, and remove it

        String jsonInputString = null;
        JSONObject jos;
        try {
            JSONArray ja = new JSONArray(json);
            if(ja.length() > 0) {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    //jsonInputString = jo.toString();
                    //FormData fd = new FormData(formId,jo);
                    jos = new JSONObject();
                    jos.put("formId", formId);
                    jos.put("values", jo);
                    jsonInputString = jos.toString();
                    currD = i;
                    int res = postRequest(jsonInputString);
                    if (res == 200) {
                        sent(formId, ja);
                    }
               /* repository.sendData(fd)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> sent(formId,ja), error -> Log.e(TAG, "SyncAdapter sendData:" + error.getMessage()));
                //ja.remove(i);

                */


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public int postRequest(String jsonToSend){
        int resp =  0;
        try {
            URL url = new URL("http://192.168.43.199:45455/api/app/formData/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            DataOutputStream os = new DataOutputStream(con.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
            os.writeBytes(jsonToSend);

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(con.getResponseCode()));
            Log.i("MSG" , con.getResponseMessage());
            resp = con.getResponseCode();

        }catch (Exception e){
            e.printStackTrace();
        }
        return resp;
    }

    private void sent(String formId, JSONArray ja) {
        ja.remove(currD);
        saveFile(ja.toString(),formId+".json",'R');
        String dataCount = SaveSharedPreference.getShared(mContext,formId);
        JSONArray ja2 = new JSONArray();
        try{
            JSONArray ja1 = new JSONArray(dataCount);

            ja2.put(ja1.getInt(0) - 1);
            ja2.put(ja1.getInt(1) + 1);
        }catch(JSONException e){
            e.printStackTrace();
        }

        SaveSharedPreference.addToShared(mContext,formId,ja2.toString());
    }
    public File[] loadAllFormFile(String folder){
        File mdir = new File(mContext.getFilesDir(), folder );
        File[] files = mdir.listFiles();
        //String file1 = files[0].getPath();
        //Toast.makeText(mContext,file1,Toast.LENGTH_LONG);
        return files;

    }

    public String loadJSONFromFile(String fileName, char type) {
        String json = null;
        String folder = "schemas";
        if (type == 'R') {
            folder = "data";
        }
        try {

            File file = new File(mContext.getFilesDir(), folder + "/" + fileName);

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
// This responce will have Json Format String
            json = stringBuilder.toString();
        } catch (IOException ex) {
            Log.d(TAG, "Exception Occurred : " + ex.getMessage());
            return null;
        }

        return json;

    }

    public void saveFile(String sch, String path, char type) {
        String filename = path;
        String fileContents = sch;
        String folder = "schemas";
        if (type == 'R') {
            folder = "data";
        }
        File dir = new File(mContext.getFilesDir(), folder);
        //Toast.makeText(getApplication(), getApplication().getApplicationContext().getFilesDir().toString(), Toast.LENGTH_SHORT).show();
        if (!dir.exists())
            dir.mkdir();
        try {
           /*FileOutputStream outputStream =
                   getApplication().openFileOutput
                           (filename, Context.MODE_PRIVATE);
           outputStream.write(fileContents.getBytes());
           outputStream.flush();
           outputStream.close();*/
            File gpxfile = new File(dir, path);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(fileContents);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
