package com.africell.africellsurvey.sync;

import android.accounts.Account;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.helper.SaveSharedPreference;
import com.africell.africellsurvey.model.FormData;
import com.africell.africellsurvey.model.FormDataResponse;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.Settings.System.getString;
import static androidx.core.content.ContextCompat.getSystemService;

//@AndroidEntryPoint
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    ContentResolver contentResolver;
    int currD = 0;
    //@Inject
    //Repository repository;
    private final String CHANNEL_ID = "AfrisurveyChannel";

    private List<SurveyForm> surveyForms;

    Context mContext;
    ApiService retrofit = null;
    NotificationCompat.Builder builder = null;
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
        this.mContext = context;
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.100.26.67:45455/api/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
        /*builder = new NotificationCompat.Builder(mContext,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_check)
                .setContentTitle("Afrisurvey")
                .setContentText("Transfering data to remote server")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/
    }



    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver = context.getContentResolver();
        this.mContext = context;
    }
   /* private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = mContext.getResources().getString(R.string.channel_name);
            String description = mContext.getResources().getString(R.string.channel_description);;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(mContext,NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
*/
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        //Log.d(TAG,"onperform sync start");
       // List<SurveyForm> surveyFormList = repository.getSurveyForms();
        System.out.print("inside onperform");
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
        //send each object containing form value, and remove
        System.out.print("inside sendData");

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
                    Log.i("FORM_ID",formId);
                    Log.i("VALUES",jos.toString());
                   int res = postRequest(formId,jo);
                    if (res == 1) {
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
    public int postRequest(String formId, JSONObject jo){
        System.out.println("inside postRequest");
        int resp =  0;
        try {
            Gson gs = new Gson();
            FormData fd = new FormData(formId,gs.fromJson(jo.toString(),JsonObject.class));
            Call<FormDataResponse> call = retrofit.sendFormData(fd);
            try{
                Response<FormDataResponse> rs = call.execute();
                if(rs.isSuccessful() && rs.body().getId()  != null && !rs.body().getId().isEmpty()){
                    resp = 1;
                }

            }catch (IOException e){
                e.printStackTrace();
            }

            /*call.enqueue(new Callback<FormDataResponse>() {
                @Override
                public void onResponse(retrofit2.Call<FormDataResponse> call, retrofit2.Response<FormDataResponse> response) {
                    //Toast.makeText(MainActivity.this, response.body().result, Toast.LENGTH_LONG).show();
                    Log.i("RESPONSE",response.body().getFormId());
                    sent(response.body().getFormId(), ja);
                }

                @Override
                public void onFailure(retrofit2.Call<FormDataResponse> call, Throwable t) {
                   // Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    Log.i("RESPONSE",t.getMessage());
                }
            });*/

          /*  URL url = new URL("http://192.168.43.199/jsonmock/server.php/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            DataOutputStream os = new DataOutputStream(con.getOutputStream());
            //os.writeBytes(URLEncoder.encode(jsonToSend, "UTF-8"));
            Log.i("JSONTOSEND",jsonToSend);
            os.writeBytes(jsonToSend);

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(con.getResponseCode()));
            Log.i("MSG" , con.getResponseMessage());
            resp = con.getResponseCode();*/

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

            ja2.put(ja.length());
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
