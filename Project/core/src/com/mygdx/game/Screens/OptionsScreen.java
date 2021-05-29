package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.game.MainClass;

public class OptionsScreen implements Screen {

    private MainClass mainClass;

    public OptionsScreen(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            mainClass.ChangeScreen(mainClass.mainMenuScreen);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) mainClass.ChangeScreen(mainClass.level_system);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
