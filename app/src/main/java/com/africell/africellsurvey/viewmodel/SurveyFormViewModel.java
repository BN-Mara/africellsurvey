package com.africell.africellsurvey.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.africell.africellsurvey.helper.DateConverter;
import com.africell.africellsurvey.helper.FileManip;
import com.africell.africellsurvey.helper.SaveSharedPreference;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.model.SurveyFormResponse;
import com.africell.africellsurvey.model.User;
import com.africell.africellsurvey.repository.Repository;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.stream.*;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;


public class SurveyFormViewModel extends AndroidViewModel {
    private static final String TAG = "SurveyFormViewModel";
    private final Repository repository;
    private final MutableLiveData<ArrayList<SurveyForm>> formList = new MutableLiveData<>();
    private LiveData<List<SurveyForm>> localFormList = null;
    MutableLiveData<ArrayList<SurveyForm>> formListUnion = new MutableLiveData<>();
    private SurveyForm sf;
    private MutableLiveData<Boolean> isLogged;
    private Class fragment;
    private List<SurveyForm> locallist = null;
    private Map<String,SurveyForm> listMap = new HashMap<>();
    //private SaveSharedPreference saveSharedPreference = new SaveSharedPreference();
    //private User user;


    @ViewModelInject
    public SurveyFormViewModel(Application application, Repository repository) {
        super(application);
        this.repository = repository;
        localFormList = repository.getLocalForms();
        locallist = repository.getSurveyForms();
        //listToMap();


    }
    public Map<String,SurveyForm> listToMap(){
        for(SurveyForm surveyForm : locallist){
            listMap.put(surveyForm.getId(),surveyForm);
        }
        return listMap;
    }

    public void setCurrentForm(SurveyForm sf) {
        this.sf = sf;
    }

    public SurveyForm getCurrentForm() {
        return sf;
    }


    public MutableLiveData<ArrayList<SurveyForm>> getFormList() {
        //localFormList.setValue(repository.getLocalForms().getValue());
        // Toast.makeText(getApplication(), localFormList.getValue().get(0).getTitle(), Toast.LENGTH_SHORT).show();

       /* if(localFormList.getValue() != null && !localFormList.getValue().isEmpty()){
        ArrayList<SurveyForm> arrayList = new ArrayList<>(localFormList.getValue());
        if(formList != null) {
            //List<SurveyForm> listTwoCopy = new ArrayList<>(arrayList);
            //arrayList.removeAll(formList.getValue());
            //listOne.addAll(listTwoCopy);

            //Toast.makeText(getApplication(), localFormList.getValue().get(0).getTitle(), Toast.LENGTH_SHORT).show();
            formList.getValue().addAll(arrayList);
            HashSet<SurveyForm> filter = new HashSet(formList.getValue());
            ArrayList<SurveyForm> newList = new ArrayList<SurveyForm>(filter);
            formList.setValue(newList);
        }

        }*/
       /* if(formList == null){
            ArrayList<SurveyForm> locallist = new ArrayList<>();
            locallist.addAll(repository.getSurveyForms());
            formList.setValue(locallist);
        }*/

        return formList;
    }

    public void getForms() {
        repository.getForms()
                .subscribeOn(Schedulers.io())
                .map(new Function<SurveyFormResponse, ArrayList<SurveyForm>>() {

                    @Override
                    public ArrayList<SurveyForm> apply(SurveyFormResponse surveyFormResponses) throws Throwable {

                        ArrayList<SurveyForm> list = surveyFormResponses.getResults();
                        Iterator<SurveyForm> iterator = list.iterator();

                        //remove draft and canceled status
                        while(iterator.hasNext()){
                            SurveyForm  surveyForm = iterator.next();
                            if(surveyForm.getStatus() != 0){
                                iterator.remove();
                            }
                        }

                        Log.i("LISTSTATUS",list.get(0).getId());



                        //Gson gson = new Gson();

                        int ix;

                         for (SurveyForm surveyForm : list) {

                        //String sch = surveyForm.getFormSchema();

                        //surveyForm.setIsDownloaded(0);
                        //surveyForm.setSchema_path("");
                        //surveyForm.setDownloadDate(null);
                        //repository.insertForm(surveyForm);
                             //Log.i("getFormsStatus",""+surveyForm.getStatus());


                                ix = locallist.indexOf(surveyForm);
                                 if(ix > -1) {
                                     if (!surveyForm.getVersion().equalsIgnoreCase(locallist.get(ix).getVersion())) {
                                         locallist.get(ix).setIsDownloaded(-1);
                                         locallist.get(ix).setNewVersion(surveyForm.getVersion());
                                         //locallist.remove(ix);
                                         //surveyForm.setIsDownloaded(-1);
                                         Gson gs  = new Gson();
                                         locallist.get(ix).setFormSchema(gs.fromJson(surveyForm.getFormSchema(),JsonArray.class));
                                         repository.updateForm(locallist.get(ix));

                                     }
                                 }




                             //surveyForm.setCountRemote(0);
                             //surveyForm.setCountLocal(0);

                        }

                        list.removeAll(locallist);
                        list.addAll(locallist);

                        return list;
                    }


                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> formList.setValue(result), error -> Log.e(TAG, "getForms:" + error.getMessage()));
    }

    public void insertForm(SurveyForm form) {
        form.setSchema_path(form.getId() + ".json");
        int isDd = form.getIsDownloaded();
        form.setIsDownloaded(1);
        //form.setCountLocal(0);
        //form.setCountRemote(0);
        //DateConverter dt = new DateConverter();
        form.setDownloadDate(DateConverter.getTimeStamp());
        JSONArray formSchema = new JSONArray();
       // Toast.makeText(getApplication(),"schema downloaded!",Toast.LENGTH_LONG).show();
        if(isDd == -1) {
            form.setVersion(form.getNewVersion());
            repository.updateForm(form);
        }else{
            repository.insertForm(form);
        }

        try{
            JSONArray  js = new JSONArray(form.getFormSchema());
            for(int i =0; i<js.length();i++){
                String v = js.getJSONObject(i).getString("version");
                if(v.equalsIgnoreCase(form.getVersion())){
                    formSchema = js.getJSONObject(i).getJSONArray("formSchema");
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        saveFile(formSchema.toString(), form, 'S');
        Toast.makeText(getApplication(),"schema downloaded!",Toast.LENGTH_LONG).show();
        //getForms();
        //localFormList = repository.getLocalForms();
        locallist  = repository.getSurveyForms();

    }
    public void deleteForm(String id){
        repository.deleteForm(id);
        locallist  = repository.getSurveyForms();
        SaveSharedPreference.deleteShared(getApplication(),id);
        deleteFile(id+".json",'R');
        deleteFile(id+".json",'S');

    }
 /*   public void getUploadForms(File file){
        FileReader fileReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
// This responce will have Json Format String
        String response = stringBuilder.toString();
        try {
            JSONObject jsonObject = new JSONObject(response);

         SurveyForm sf1 = new SurveyForm(jsonObject.getString("id"),jsonObject.getString("title"),
                 jsonObject.getString("version"),jsonObject.getString("description"),0,jsonObject.);
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return javaObject;

    }*/

    public void saveFormData(SurveyForm sf, String jsonData) throws JSONException {
        String json = loadJSONFromFile(sf.getSchema_path(),'R');

        JSONObject jo = new JSONObject(jsonData);
        JSONArray ja = null;
        if(json != null){
            //add to existing array data
            ja = new JSONArray(json);
            ja.put(jo);

        }else{
            //create new array for this form data
            ja = new JSONArray();
            ja.put(jo);


        }
       // sf.setCountLocal(ja.length());
        //repository.updateForm(sf);
        //int [] dataCount = {ja.length(),0};
        JSONArray dataCount = new JSONArray();
        int[] dc = getDataCount();
            dataCount.put(ja.length());
            dataCount.put(dc[1]);
            if(sf.getDataUrl() != null)
                dataCount.put(sf.getDataUrl());

        SaveSharedPreference.addToShared(getApplication(),sf.getId(), dataCount.toString());
        saveFile(ja.toString(), sf, 'R');
    }

    public int[] getDataCount(){
        int [] arr = {0,0};
       String dcount =  SaveSharedPreference.getShared(getApplication(),getCurrentForm().getId());
       if(dcount != null) {
           try {
               JSONArray ja = new JSONArray(dcount);
               arr[0] = ja.getInt(0);
               arr[1] = ja.getInt(1);
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       return arr;
    }

    public LiveData<List<SurveyForm>> getLocalForms() {
        return localFormList;

    }

    public ArrayList<SurveyForm> getLocalList() {
        ArrayList<SurveyForm> local = new ArrayList<>(locallist);
        return local;
    }

    public void addToList(ArrayList<SurveyForm> surveyForms) {
        ArrayList<SurveyForm> newArray = new ArrayList<>(surveyForms);
        newArray.addAll(repository.getSurveyForms());
        formListUnion.setValue(newArray);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void updateForm(SurveyForm form) {
        repository.updateForm(form);
    }

    public void deleteFile(String fileName, char type){

        String folder = "schemas";
        if (type == 'R') {
            folder = "data";
        }
        File dir = new File(getApplication().getApplicationContext().getFilesDir(), folder);
        File file = new File(dir,fileName);
        if(file.delete())
            System.out.println("file "+fileName+" deleted");
    }

    public void saveFile(String sch, SurveyForm sSf, char type) {
        //String filename = path;
        String fileContents = sch;
        String folder = "schemas";
        if (type == 'R') {
            folder = "data";
        }
        File dir = new File(getApplication().getApplicationContext().getFilesDir(), folder);
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
            File gpxfile = new File(dir, sSf.getSchema_path());
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(fileContents);
            writer.flush();
            writer.close();
            //File pt = Environment.getExternalStoragePublicDirectory()
            if(type == 'R'){

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void saveSharedFile(String fContent, String fName){
        FileManip fileManip = new FileManip(getApplication());
        fileManip.createExternalStoragePrivateFile(fContent,fName,'R');

    }

    public boolean setLoggedIn(User user) {
        //saveSharedPreference = new SaveSharedPreference();
        boolean check = false;
        if (user.getUsername().equalsIgnoreCase("africell") && user.getPasswd().equals("africell")) {
            SaveSharedPreference.setLoggedIn(getApplication(), user, true);
            check = true;
        }
        return check;
    }

    public void setLoggedOut(User user) {
        //saveSharedPreference = new SaveSharedPreference();
        SaveSharedPreference.setLoggedIn(getApplication(), user, false);
    }

    public boolean getSessionSatus() {
        final boolean loggedStatus = SaveSharedPreference.getLoggedStatus(getApplication());
        return loggedStatus;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }

    public String loadJSONFromFile(String fileName, char type) {

        String folder = "schemas";
        if (type == 'R') {
            folder = "data";
        }
        File file = new File(getApplication().getApplicationContext().getFilesDir(), folder + "/" + fileName);
        return getJsonFromFile(file);

    }

    public String getJsonFromFile(File file)
    {
        String json = null;
        try {
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
    public void loadLocalFile(Uri uri){
        //Toast.makeText(getApplication(),file.getAbsolutePath(),Toast.LENGTH_LONG).show();
        FileManip fm = new FileManip(getApplication());
        String json = null;

     try {
            json = fm.readUri(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }


       // json = getJsonFromFile(file);
       //Toast.makeText(getApplication(),json,Toast.LENGTH_LONG).show();

        if(json != null)
        try {
            JSONObject jo = new JSONObject(json);
            String url = jo.getJSONObject("states").getString("url");
            JSONObject fmJo = jo.getJSONObject("states").getJSONObject("formSchema");
            SurveyForm sf1 = new SurveyForm(
                    fmJo.getString("id"),
                    fmJo.getString("title"),
                    fmJo.getString("version"),
                    fmJo.getString("description"),
                    1,
                    "",
                    "",
                    fmJo.getString("surveyTitle"),
                    true,
                    url

            );
            Gson gs = new Gson();
            //gs.fromJson(fmJo.getJSONArray("schemas").toString(),JsonArray.class);
            sf1.setFormSchema(gs.fromJson(fmJo.getJSONArray("schemas").toString(),JsonArray.class));
            Log.i("LOCALINSET","insert local form");
            insertForm(sf1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}