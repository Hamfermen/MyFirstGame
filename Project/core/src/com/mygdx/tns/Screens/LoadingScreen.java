package com.mygdx.tns.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.tns.Const;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.Screens.Levels.Level_System;
import com.mygdx.tns.WorldTime;

public class LoadingScreen implements Screen {

    private BitmapFont font;
    private Label text;

    private SpriteBatch batch;

    private MainClass mainClass;
    private WorldTime worldTime;

    private Stage loadingScreen;
    private OrthographicCamera loadingScreenCamera;
    private ExtendViewport loadingScreenViewport;

    private Timer timer;
    private Timer.Task task;

    private boolean load = false;

    public LoadingScreen(MainClass mainClass, WorldTime worldTime){
        this.worldTime = worldTime;
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();

        loadingScreenCamera = new OrthographicCamera();
        loadingScreenCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        loadingScreenCamera.update();

        loadingScreenViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), loadingScreenCamera);

        loadingScreen = new Stage(loadingScreenViewport, batch);

        timer = new Timer();

        task = new Timer.Task() {
            @Override
            public void run() {
                load = true;
                task.cancel();
            }
        };

        font = new BitmapFont(Gdx.files.internal("test_f.fnt"));
        text = new Label("Осталось " + Integer.toString(worldTime.getMinutes() == 0 || worldTime.getHours() == 18 ? 18 - worldTime.getHours() : 18 - worldTime.getHours() - 1) + " Часов " + Integer.toString(60 - worldTime.getMinutes()) + " Минут" + " до" + (Const.smallForm ? " смерти" : " смены формы"), new Label.LabelStyle(font, font.getColor()));
        text.setFontScale(1 * Const.SizeX, 1 * Const.SizeY);
        text.setPosition(200 * Const.SizeX, 400 * Const.SizeY);

        loadingScreen.addActor(text);

        load = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0.4f, 0.7f, 1);
        loadingScreen.act();
        loadingScreen.draw();
        if (!task.isScheduled()) timer.scheduleTask(task, 3, 0);
        if (load) {
            mainClass.ChangeScreen(mainClass.level_system);
        }
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
        loadingScreen.dispose();
        font.dispose();
        batch.dispose();
    }
}
