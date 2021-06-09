package com.mygdx.tns.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.tns.Const;
import com.mygdx.tns.GameController;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;

public class OptionsScreen implements Screen {

    private class MusicButton extends Actor {
        Texture on, off;

        public MusicButton(){
            on = new Texture("on.png");
            off = new Texture("off.png");

            this.setBounds(400 * Const.SizeX, 300 * Const.SizeY, 360 * Const.SizeX, 360 * Const.SizeY);

            addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (MyPreference.getMusicValue() == 0.5f) MyPreference.setMusicValue(0);
                    else MyPreference.setMusicValue(0.5f);
                    return true;
                }
            });
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (MyPreference.getMusicValue() == 0.5f) batch.draw(on, 450 * Const.SizeX, 250 * Const.SizeY, 360 * Const.SizeX, 360 * Const.SizeY);
            else batch.draw(off, 450 * Const.SizeX, 250 * Const.SizeY, 360 * Const.SizeX, 360 * Const.SizeY);
        }
    }

    private MainClass mainClass;

    private Stage options;

    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private SpriteBatch batch;

    private MusicButton musicButton;

    private GameController gameController;

    private Texture background;

    public OptionsScreen(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        viewport = new ScreenViewport(camera);

        background = new Texture("background_info.png");

        musicButton = new MusicButton();

        gameController = new GameController(GameController.Direction.PAUSE);

        options = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(options);
        options.addActor(musicButton);
        options.addActor(gameController);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1);

        batch.begin();
        batch.draw(background, 0, 0, 1280 * Const.SizeX, 720 * Const.SizeY);
        batch.end();
        options.act();
        options.draw();
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || GameController.pause){
            mainClass.ChangeScreen(mainClass.menuScreen);
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
        background.dispose();
        options.dispose();
        batch.dispose();
    }
}
