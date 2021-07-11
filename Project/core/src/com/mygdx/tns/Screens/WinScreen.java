package com.mygdx.tns.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.tns.MainClass;

public class WinScreen implements Screen {
    private Music music;

    private Texture win;

    private SpriteBatch batch;

    private Timer timer;
    private Timer.Task task;

    private MainClass mainClass;

    boolean load = false;

    public WinScreen(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

        music = Gdx.audio.newMusic(Gdx.files.internal("win.mp3"));

        music.setVolume(0.5f);

        win = new Texture("win.png");

        batch = new SpriteBatch();

        timer = new Timer();

        load = false;

        task = new Timer.Task() {
            @Override
            public void run() {
                load = true;
                timer.stop();
                task.cancel();
            }
        };
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!music.isPlaying()) music.play();
        batch.begin();
        batch.draw(win, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (!task.isScheduled()) timer.scheduleTask(task, 8, 0);

        if (load){
            load = false;
            music.stop();
            mainClass.ChangeScreen(mainClass.mainMenuScreen);
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
        music.dispose();
        win.dispose();
        batch.dispose();
    }
}
