package com.mygdx.tns.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.tns.Const;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;

public class ToBeContinuedScreen implements Screen {

    private Texture background;

    private Music music;

    private Timer timer;
    private Timer.Task task;

    private boolean load = false;

    private SpriteBatch batch;

    private MainClass mainClass;

    public ToBeContinuedScreen(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

        music = Gdx.audio.newMusic(Gdx.files.internal("ToBeContinued.mp3"));

        music.setVolume(0.5f);

        batch = new SpriteBatch();

        timer = new Timer();

        task = new Timer.Task() {
            @Override
            public void run() {
                load = true;
                timer.stop();
                task.cancel();
            }
        };

        background = new Texture("ToBeContinued.png");

        load = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        if (music.getPosition() >= 21.5f) {
            batch.begin();
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        }

        if (!task.isScheduled()) timer.scheduleTask(task, 35, 0);

        if (load){
            MyPreference.pref.clear();
            MyPreference.setIsNewPreference(false);
            MyPreference.setNewgame(true);
            Const.newLevel = true;
            Const.newGame = true;

            load = false;

            music.stop();

            mainClass.ChangeScreen(mainClass.mainMenuScreen);
        }

        if (!music.isPlaying()) music.play();
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
        music.dispose();
    }
}
