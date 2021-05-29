package com.mygdx.game.Screens.Levels;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.Const;
import com.mygdx.game.MainClass;
import com.mygdx.game.MyPreference;

public class DeadButton extends Actor {

    private MainClass mainClass;

    public DeadButton(final Screen screen, final MainClass mainClass){
        this.mainClass = mainClass;
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mainClass.ChangeScreen(screen);
                return true;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(new Texture("Play.png"), this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
