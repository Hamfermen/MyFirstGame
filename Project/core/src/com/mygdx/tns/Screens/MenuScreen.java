package com.mygdx.tns.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.tns.Const;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;

public class MenuScreen implements Screen {

    private class MusicButton extends Actor {
        Texture on, off;

        public MusicButton(){
            on = new Texture("on.png");
            off = new Texture("off.png");

            this.setBounds(475 * Const.SizeX, 377.5f * Const.SizeY, 300 * Const.SizeX, 75 * Const.SizeY);

            addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (MyPreference.getMusicValue() == 0.5f) {
                        MyPreference.setMusicValue(0);
                        music.setVolume(MyPreference.getMusicValue());
                    }
                    else {
                        MyPreference.setMusicValue(0.5f);
                        music.setVolume(MyPreference.getMusicValue());
                    }
                    return true;
                }
            });
        }
    }

    private Stage menuScreen;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private SpriteBatch batch;
    private Texture background, mael_info, merlin_info, eskanor_info;
    private MainClass mainClass;
    private Label text;
    private BitmapFont font;

    private MusicButton musicButton;

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

        font = new BitmapFont(Gdx.files.internal("test_f.fnt"));
        text = new Label("Подробнее", new Label.LabelStyle(font, font.getColor()));
        text.setFontScale(1 * Const.SizeX, 1 * Const.SizeY);
        text.setPosition(90 * Const.SizeX, 590 * Const.SizeY);

        musicButton = new MusicButton();

        play = new ButtonsforScreen(mainClass.level_system, false, false,"Play.png", mainClass, false);
        play.setSize(300f * Const.SizeX, 80f * Const.SizeY);
        play.setPosition(475f * Const.SizeX, 480f * Const.SizeY);

        /*options = new ButtonsforScreen(mainClass.optionsScreen, false, false,"Play.png", mainClass, false);
        options.setSize(300f * Const.SizeX, 80f * Const.SizeY);
        options.setPosition(475f * Const.SizeX, 377.5f * Const.SizeY);*/

        exit = new ButtonsforScreen(mainClass.mainMenuScreen, false, false,"Play.png", mainClass, false);
        exit.setSize(300f * Const.SizeX, 80f * Const.SizeY);
        exit.setPosition(475f * Const.SizeX, 275f * Const.SizeY);

        switch (portrait) {
            case 0:
                merlin_portrait = new ButtonsforScreen(mainClass.merlin_info, false, false, "Play.png", mainClass, false);
                merlin_portrait.setSize(280 * Const.SizeX, 280f * Const.SizeY);
                merlin_portrait.setPosition(50 * Const.SizeX, 300 * Const.SizeY);
                break;
            case 1:
                eskanor_portrait = new ButtonsforScreen(mainClass.eskanor_info, false, false, "Play.png", mainClass, false);
                eskanor_portrait.setSize(280 * Const.SizeX, 280f * Const.SizeY);
                eskanor_portrait.setPosition(50 * Const.SizeX, 300 * Const.SizeY);
                break;
        }

        menuScreen = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(menuScreen);
        menuScreen.addActor(play);
        //menuScreen.addActor(options);
        menuScreen.addActor(exit);
        menuScreen.addActor(musicButton);
        menuScreen.addActor(text);
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
                batch.draw(merlin_info, 50 * Const.SizeX, 300 * Const.SizeY, 280 * Const.SizeX, 280 * Const.SizeY);
                break;
            case 1:
                batch.draw(eskanor_info, 50 * Const.SizeX, 300 * Const.SizeY, 280 * Const.SizeX, 280 * Const.SizeY);
                break;
        }
        batch.end();
        menuScreen.act();
        menuScreen.draw();
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) mainClass.setScreen(mainClass.level_system);
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
