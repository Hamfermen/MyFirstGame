package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;

public class Dialog {
    private List<String> dialog;
    private List<Texture> dialogImage;
    private int dialogPos;

    public Dialog(List<String> dialog, List<Texture> dialogImage, int dialogPos){
        this.dialog = dialog;

        this.dialogImage = dialogImage;

        this.dialogPos = dialogPos;
    }

    public void nextDialog(){
        if (dialogPos < dialog.size() - 1) dialogPos++;

        MyPreference.setDialogPos(dialogPos);
    }

    public String getNowDialog() {
        return dialog.get(dialogPos);
    }

    public Texture getNowDialogImage() {
        return dialogImage.get(dialogPos);
    }
}
