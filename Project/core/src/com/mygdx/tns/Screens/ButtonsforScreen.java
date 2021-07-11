package com.mygdx.tns.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.tns.Const;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;

public class ButtonsforScreen extends Actor {
    private String buttonName;
    private Texture button;
    private boolean isDraw;
    public ButtonsforScreen(final Screen screen, final boolean exit, final boolean isNewGame, String buttonName, final MainClass mainClass, final boolean isDraw){
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!exit) {
                    if (isNewGame) {
                        MyPreference.pref.clear();
                        MyPreference.setIsNewPreference(false);
                        MyPreference.setIsNewGame(true);
                        MyPreference.setIsNewLevel(true);
                        mainClass.clearSaves();
                    }
                    mainClass.ChangeScreen(screen);
                }
                else Gdx.app.exit();
                return true;
            }
        });
        this.buttonName = buttonName;
        button = new Texture(buttonName);
        this.isDraw = isDraw;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isDraw) batch.draw(button, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
