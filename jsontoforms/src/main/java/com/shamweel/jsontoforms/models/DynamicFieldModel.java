package com.shamweel.jsontoforms.models;

public class DynamicFieldModel {
    private boolean checked;
    private String className;
    public DynamicFieldModel(boolean checked, String className){
        this.checked = checked;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }
}
