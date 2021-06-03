package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Const;
import com.mygdx.game.MainClass;
import com.mygdx.game.MyPreference;
import com.mygdx.game.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuScreen implements Screen {

    private Stage menuScreen;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private SpriteBatch batch;
    private Texture background, mael_info, merlin_info, eskanor_info;
    private MainClass mainClass;

    private int portrait;

    private ButtonsforScreen play, exit, options, merlin_portrait, eskanor_portrait;

    public MenuScreen(MainClass mainClass) {
        this.mainClass = mainClass;
    }

    private Music music;

    @Override
    public void show() {

        batch = new SpriteBatch();

        music = Gdx.audio.newMusic(Gdx.files.internal("BackGroundMusic1.mp3"));
        music.setVolume(MyPreference.getMusicValue());

        RandomXS128 random = new RandomXS128();

        portrait = random.nextInt(2);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        viewport = new ScreenViewport(camera);

        background = new Texture("menu_screen.png");

        merlin_info = new Texture("Merlin_portrait.png");

        eskanor_info = new Texture("Eskanor_portrait.png");

        play = new ButtonsforScreen(mainClass.level_system, false, false,"Play.png", mainClass);
        play.setSize(300f * Const.SizeX, 80f * Const.SizeY);
        play.setPosition(475f * Const.SizeX, 480f * Const.SizeY);

        options = new ButtonsforScreen(mainClass.optionsScreen, false, false,"Play.png", mainClass);
        options.setSize(300f * Const.SizeX, 80f * Const.SizeY);
        options.setPosition(475f * Const.SizeX, 377.5f * Const.SizeY);

        exit = new ButtonsforScreen(mainClass.mainMenuScreen, false, false,"Play.png", mainClass);
        exit.setSize(300f * Const.SizeX, 80f * Const.SizeY);
        exit.setPosition(475f * Const.SizeX, 275f * Const.SizeY);

        switch (portrait) {
            case 0:
                merlin_portrait = new ButtonsforScreen(mainClass.merlin_info, false, false, "Play.png", mainClass);
                merlin_portrait.setSize(280 * Const.SizeX, 280f * Const.SizeY);
                merlin_portrait.setPosition(50 * Const.SizeX, 400 * Const.SizeY);
                break;
            case 1:
                eskanor_portrait = new ButtonsforScreen(mainClass.eskanor_info, false, false, "Play.png", mainClass);
                eskanor_portrait.setSize(280 * Const.SizeX, 280f * Const.SizeY);
                eskanor_portrait.setPosition(50 * Const.SizeX, 400 * Const.SizeY);
                break;
        }

        menuScreen = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(menuScreen);
        menuScreen.addActor(play);
        menuScreen.addActor(options);
        menuScreen.addActor(exit);
        switch (portrait){
            case 0:
                menuScreen.addActor(merlin_portrait);
                break;
            case 1:
                menuScreen.addActor(eskanor_portrait);
                break;
        }
        //menuScreen.setDebugAll(true);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!music.isPlaying()) music.play();
        batch.begin();
        batch.draw(background, 0, 0, 1280 * Const.SizeX, 720 * Const.SizeY);
        switch (portrait){
            case 0:
                batch.draw(merlin_info, 50 * Const.SizeX, 400 * Const.SizeY, 280 * Const.SizeX, 280 * Const.SizeY);
                break;
            case 1:
                batch.draw(eskanor_info, 50 * Const.SizeX, 400 * Const.SizeY, 280 * Const.SizeX, 280 * Const.SizeY);
                break;
        }
        batch.end();
        menuScreen.act();
        menuScreen.draw();
        Const.time = music.getPosition();
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
        background.dispose();
        merlin_info.dispose();
        menuScreen.dispose();
    }


}
