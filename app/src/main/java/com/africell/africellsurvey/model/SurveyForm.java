package com.africell.africellsurvey.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.sql.Date;
import java.util.Objects;

@Entity(tableName = "form_table")
public class SurveyForm {

    @NonNull
    @PrimaryKey
    private String id;

    private String title;
    private String version;
    private String description;
    private int isDownloaded;
    private String schema_path;
    private String downloadDate;
    private String newVersion;
    private String surveyTitle;
    private boolean isFromLocal = false;
    private String dataUrl;
    //private int countRemote = 0;
    //private  int countLocal = 0;
    @Ignore
    private JsonArray schemas;

    @Ignore
    private int status;

    public SurveyForm(String id,String title, String version, String description, int isDownloaded,
                      String schema_path,String downloadDate,String surveyTitle, boolean isFromLocal, String dataUrl){
        this.id = id;
        this.title = title;
        this.version = version;
        this.description = description;
        this.isDownloaded = isDownloaded;
        this.schema_path = schema_path;
        this.downloadDate = downloadDate;
        this.surveyTitle = surveyTitle;

        //this.status = status;
        //this.countLocal = countLocal;
        //this.countRemote = countRemote;
        this.isFromLocal = isFromLocal;
        this.dataUrl = dataUrl;


    }
    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchema_path() {
        return schema_path;
    }

    public void setSchema_path(String schema_path) {
        this.schema_path = schema_path;
    }

    public void setIsDownloaded(int isDownloaded) {
        this.isDownloaded = isDownloaded;
    }

    public int getIsDownloaded() {
        return isDownloaded;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getFormSchema() {
        Gson gson = new Gson();
        String s = gson.toJson(schemas);
        return s;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    /*public int getCountLocal() {
        return countLocal;
    }

    public void setCountLocal(int countLocal) {
        this.countLocal = countLocal;
    }

    public int getCountRemote() {
        return countRemote;
    }

    public void setCountRemote(int countRemote) {
        this.countRemote = countRemote;
    }
*/

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public String getSurveyTitle() {
        return surveyTitle;
    }

    public void setSurveyTitle(String surveyTitle) {
        this.surveyTitle = surveyTitle;
    }

    public void setFormSchema(JsonArray schemas) {
        this.schemas = schemas;
    }

    public boolean isFromLocal() {
        return isFromLocal;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public void setFromLocal(boolean fromLocal) {
        isFromLocal = fromLocal;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof SurveyForm)){
            //implicit null check
            return false;
        }
        return this.id.equals(((SurveyForm) o).id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.getId());
    }
}
