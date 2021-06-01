package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Const;
import com.mygdx.game.MainClass;
import com.mygdx.game.MyPreference;
import com.mygdx.game.Screens.Levels.Level_System;

public class MainMenuScreen implements Screen {
    private Stage menuScreen;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private SpriteBatch batch;
    private ButtonsforScreen ereaseProgres, playnew, play,exit, option;
    private Texture background;
    private MainClass mainClass;
    private BitmapFont title;
    private Label text;

    public MainMenuScreen(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();

        title = new BitmapFont(Gdx.files.internal("title.fnt"));
        text = new Label("Taiyo no senshi", new Label.LabelStyle(title, title.getColor()));
        text.setFontScale(1 * Const.SizeX, 1 * Const.SizeY);
        text.setPosition(50 * Const.SizeX, 550 * Const.SizeY);
        text.setSize(500 * Const.SizeX, 100 * Const.SizeY);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        viewport = new ScreenViewport(camera);

        play = new ButtonsforScreen(mainClass.level_system, false, false,"Play.png", mainClass);
        play.setSize(100f * Const.SizeX, 100f * Const.SizeY);
        play.setPosition(580f * Const.SizeX, 405f * Const.SizeY);

        ereaseProgres = new ButtonsforScreen(mainClass.level_system, false, true,"controller\\erease.png", mainClass);
        ereaseProgres.setSize(100f * Const.SizeX, 100f * Const.SizeY);
        ereaseProgres.setPosition(0 * Const.SizeX, 0 * Const.SizeY);

        /*play = new ButtonsforScreen(mainClass.level_system, false, false,"Play.png", mainClass);
        play.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        play.setPosition(326.6f * Const.SizeX, 0);*/

        /*option = new ButtonsforScreen(mainClass.optionsScreen, false, false,"Option.png", mainClass);ффв
        option.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        option.setPosition(653.2f * Const.SizeX, 0);*/

        exit = new ButtonsforScreen(mainClass.optionsScreen, true, false,"Exit.png", mainClass);
        exit.setSize(65f * Const.SizeX, 65f * Const.SizeY);
        exit.setPosition(717f * Const.SizeX, 465f * Const.SizeY);

        background = new Texture("background.png");

        menuScreen = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(menuScreen);
        menuScreen.addActor(play);
        menuScreen.addActor(ereaseProgres);
        menuScreen.addActor(text);
        //if (MyPreference.isNewgame()) menuScreen.addActor(play);
        //menuScreen.addActor(option);
        menuScreen.addActor(exit);
        //menuScreen.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        batch.end();
        menuScreen.act();
        menuScreen.draw();
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
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        menuScreen.dispose();
        background.dispose();
    }
}
