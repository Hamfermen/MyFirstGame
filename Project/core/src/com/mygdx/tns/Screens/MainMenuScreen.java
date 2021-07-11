package com.mygdx.tns.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.tns.Const;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;

public class MainMenuScreen implements Screen {
    private Stage menuScreen;
    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private SpriteBatch batch;
    private ButtonsforScreen newgame, play, exit, bossLevel;
    private Texture background;
    private MainClass mainClass;
    private BitmapFont title;
    private Label text;
    private Music music;

    public MainMenuScreen(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

        music = Gdx.audio.newMusic(Gdx.files.internal("BackGroundMusic1.mp3"));
        music.setVolume(MyPreference.getMusicValue());

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

       /* bossLevel = new ButtonsforScreen(mainClass.bossLevel, false, false,"Buttons\\continue.png", mainClass, true);
        bossLevel.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        bossLevel.setPosition(200 * Const.SizeX, 200f * Const.SizeY);*/

        play = new ButtonsforScreen(mainClass.level_system, false, false,"Buttons\\continue.png", mainClass, true);
        play.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        play.setPosition(960f * Const.SizeX, 200f * Const.SizeY);

        newgame = new ButtonsforScreen(mainClass.level_system, false, true,"Buttons\\newgame.png", mainClass, true);
        newgame.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        newgame.setPosition(960 * Const.SizeX, 110 * Const.SizeY);

        /*play = new ButtonsforScreen(mainClass.level_system, false, false,"Play.png", mainClass);
        play.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        play.setPosition(326.6f * Const.SizeX, 0);*/

        /*option = new ButtonsforScreen(mainClass.optionsScreen, false, false,"Option.png", mainClass);ффв
        option.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        option.setPosition(653.2f * Const.SizeX, 0);*/

        exit = new ButtonsforScreen(mainClass.optionsScreen, true, false,"Buttons\\exit.png", mainClass, true);
        exit.setSize(300f * Const.SizeX, 75f * Const.SizeY);
        exit.setPosition(960 * Const.SizeX, 20 * Const.SizeY);

        background = new Texture("background1.png");

        menuScreen = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(menuScreen);

        if (MyPreference.pref.getBoolean("isNewPreferenc", true) == false) menuScreen.addActor(play);
        menuScreen.addActor(newgame);
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
        if (!music.isPlaying()) music.play();
        batch.begin();
        batch.draw(background, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());;
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
        music.stop();
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        menuScreen.dispose();
        background.dispose();
        music.dispose();
    }
}
