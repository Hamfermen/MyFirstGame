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

    private MainClass mainClass;

    private Stage options;

    private OrthographicCamera camera;
    private ScreenViewport viewport;
    private SpriteBatch batch;

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

        gameController = new GameController(GameController.Direction.PAUSE);

        options = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(options);
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
