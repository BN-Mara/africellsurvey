package com.africell.africellsurvey.helper;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.annotation.Resource;

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
    }

    /**
     * load json file, and return json data as string
     * @param context
     * @param fileName
     * @return
     */
    public static String loadJSONFromAsset(Context context, String fileName){
        String json = null;
        try {

            File file = new File(context.getFilesDir(),"schemas/"+fileName);

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
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
       try {
            JSONArray js =  new JSONArray(json) ;
            js = adaptSchema(js);
            json =  js.toString();
           //Toast.makeText(context,json,Toast.LENGTH_LONG);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;

    }

    /**
     * adapt json schema to match json format of jsonToform library
     * @param   json
     * @return JSONArray
     * @throws JSONException
     */
    public static JSONArray adaptSchema(JSONArray json) throws JSONException {


        JSONArray js = new JSONArray();
        String key ="";
        String value ="";

        String lang = Resources.getSystem().getConfiguration().locale.getLanguage();
        Log.i("LOCALE_LNG",lang);
        for(int i=0 ;i<json.length();i++){
            JSONObject jo = new JSONObject();
            if(json.getJSONObject(i).getString("typeField").equalsIgnoreCase("header")){
                key = json.getJSONObject(i).getString("headertext");
                jo.put("_id", key);
                jo.put("text",json.getJSONObject(i).getString("headertext"));
               // jo.put("hint",json.getJSONObject(i).getString("headertext"));
            }
            else{
                if(json.getJSONObject(i).has("language") && !json.getJSONObject(i).getJSONArray("language").getJSONObject(0).getString("value").equalsIgnoreCase("") ) {
                    JSONArray lng = json.getJSONObject(i).getJSONArray("language");
                    for(int z=0; z<lng.length();z++){
                        if(lang.equalsIgnoreCase(lng.getJSONObject(z).getString("label"))){
                            key = lng.getJSONObject(z).getString("value");
                            break;
                        }else{
                            key = json.getJSONObject(i).getString("label");
                        }
                    }
                }else{
                    key = json.getJSONObject(i).getString("label");
                    //value  = json.getJSONObject(i).getString("");
                }




                jo.put("_id", json.getJSONObject(i).getString("label"));
                jo.put("text", key);
                if(!json.getJSONObject(i).getString("typeField").equalsIgnoreCase("inputCheckbox"))
                {
                    jo.put("condition",json.getJSONObject(i).getString("condition"));
                    jo.put("name",json.getJSONObject(i).getString("class"));
                    
                }

            }
            //jo.put("name",json.getJSONObject(i).getString("name"));

            switch(json.getJSONObject(i).getString("typeField")){
                case "text":
                    jo.put("type",2);
                    jo.put("input_type","text");
                    jo.put("max_length",255);
                    jo.put("hint", key);
                    jo.put("is_required",json.getJSONObject(i).getBoolean("required"));

                    break;
                case "number":
                    jo.put("type",2);
                    jo.put("input_type","text");
                    jo.put("max_length",30);
                    jo.put("hint", key);

                    jo.put("is_required",json.getJSONObject(i).getBoolean("required"));

                    break;
                case "inputRadio":
                    JSONArray jsList= new JSONArray();
                    JSONArray options = json.getJSONObject(i).getJSONArray("options");
                    JSONObject joList = null;
                    for(int j = 0; j < options.length(); j++){
                        joList = new JSONObject();
                        joList.put("index",j);
                        joList.put("index_text",options.getJSONObject(j).getString("label"));
                        joList.put("indexValue",options.getJSONObject(j).getString("value"));
                        jsList.put(joList);
                    }
                    jo.put("list",jsList);
                    jo.put("hint", key);
                    jo.put("is_required",json.getJSONObject(i).getBoolean("required"));

                    jo.put("type",4);
                    break;
                case "date":
                    jo.put("type",5);
                    jo.put("is_required",json.getJSONObject(i).getBoolean("required"));

                    break;
                case "header":
                    jo.put("type",1);

                    break;
                case "inputCheckbox":
                    jo.put("type",7);
                    jo.put("hint", key);
                    jo.put("is_required",json.getJSONObject(i).getBoolean("required"));

                    break;
                case "inputCheckboxx":
                    JSONArray jsList2= new JSONArray();
                    JSONArray options2 = json.getJSONObject(i).getJSONArray("options");
                    JSONObject joList2 = null;
                    for(int j = 0; j < options2.length(); j++){
                        joList2 = new JSONObject();
                        joList2.put("index",j);
                        joList2.put("index_text",options2.getJSONObject(j).getString("label"));
                        joList2.put("indexValue",options2.getJSONObject(j).getString("value"));
                        jsList2.put(joList2);
                    }
                    jo.put("list",jsList2);
                    jo.put("hint", key);
                    jo.put("is_required",json.getJSONObject(i).getBoolean("required"));

                    jo.put("type",11);
                    break;
                case "select":
                    JSONArray jsList1= new JSONArray();
                    JSONArray options1 = json.getJSONObject(i).getJSONArray("options");
                    JSONObject joList1 = null;
                    for(int k = 0; k < options1.length(); k++){
                        joList1 = new JSONObject();
                        joList1.put("index",k);
                        joList1.put("index_text",options1.getJSONObject(k).getString("label"));
                        joList1.put("indexValue",options1.getJSONObject(k).getString("value"));
                        jsList1.put(joList1);
                    }
                    jo.put("list",jsList1);
                    jo.put("type",3);
                    jo.put("is_required",json.getJSONObject(i).getBoolean("required"));

                    break;
                case "fileUpload":
                    //jo.put("text","Add again");
                    jo.put("type",9);
                    break;

                default:
                    jo.put("type",1);
                    break;
            }



            js.put(jo);


        }
        /*JSONObject jo0 = new JSONObject();
        jo0.put("_id","mydate");
        jo0.put("text","add date");
        jo0.put("type",5);
        js.put(jo0);*/

       /* JSONObject jo1 = new JSONObject();
        jo1.put("_id","again_button");
        jo1.put("text","Add again");
        jo1.put("type",9);
        js.put(jo1);*/
        JSONObject jo2 = new JSONObject();
        jo2.put("_id","submit_button");
        jo2.put("text","Submit");
        jo2.put("name","submit_button");
        jo2.put("condition","");
        jo2.put("type",10);
        js.put(jo2);



        return js;

    }

}