package com.shamweel.jsontoforms.models;

public class DynamicFieldModel {
    private boolean checked;
    private String className,action;

    public DynamicFieldModel(boolean checked, String className, String action){
        this.checked = checked;
        this.className = className;
        this.action = action;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
