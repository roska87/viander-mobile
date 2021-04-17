package com.bit.viandermobile.models;

public class CheckboxViewModel {

    private String text;
    private boolean checked;

    public CheckboxViewModel() {
    }

    public CheckboxViewModel(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}