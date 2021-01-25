package com.shamweel.jsontoforms.interfaces;

public interface JsonToFormClickListener {

    public void onAddAgainButtonClick(int position);

    public void onSubmitButtonClick();

    public boolean showHideField(String name, String action);


}
