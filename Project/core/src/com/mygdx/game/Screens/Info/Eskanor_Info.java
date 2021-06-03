package com.mygdx.game.Screens.Info;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Const;
import com.mygdx.game.MainClass;
import com.mygdx.game.MyPreference;
import com.mygdx.game.Pair;
import com.mygdx.game.Screens.ButtonsforScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Eskanor_Info implements Screen {

    private MainClass mainClass;

    private Stage eskanor_info;

    private ButtonsforScreen back;

    private OrthographicCamera camera;
    private ScreenViewport viewport;

    private HashMap<String, TextureRegion> words;
    private Pair<String, Pair<TextureRegion, Vector2>[]>[] eskanor;

    private Texture portrait, background;

    private SpriteBatch batch;

    public Eskanor_Info(HashMap<String, TextureRegion> words, MainClass mainClass){
        this.words = words;
        this.mainClass = mainClass;
    }

    private Music music;

    @Override
    public void show() {

        music = Gdx.audio.newMusic(Gdx.files.internal("BackGroundMusic1.mp3"));
        music.setVolume(MyPreference.getMusicValue());
        music.setPosition(Const.time);

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        viewport = new ScreenViewport(camera);

        portrait = new Texture("Eskanor_portrait.png");

        background = new Texture("background_info.png");

        back = new ButtonsforScreen(mainClass.menuScreen, false, false, "Play.png", mainClass);
        back.setSize(280 * Const.SizeX, 280 * Const.SizeY);
        back.setPosition(900 * Const.SizeX, 50 * Const.SizeY);

        eskanor_info = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(eskanor_info);
        eskanor_info.addActor(back);
        //eskanor_info.setDebugAll(true);

        createEskanorInfo();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, 1280 * Const.SizeX, 720 * Const.SizeY);
        for (int i = 0; i < eskanor.length; i++){
            for (int j = 0; j < eskanor[i].a.length(); j++){
                if (i == 0) batch.draw(eskanor[i].b[j].a, eskanor[i].b[j].b.x * Const.SizeX, eskanor[i].b[j].b.y * Const.SizeY, 48f * Const.SizeX, 48f * Const.SizeY);
                else batch.draw(eskanor[i].b[j].a, eskanor[i].b[j].b.x * Const.SizeX, eskanor[i].b[j].b.y * Const.SizeY, 24f * Const.SizeX, 24f * Const.SizeY);
            }
        }
        batch.draw(portrait, 900 * Const.SizeX, 50 * Const.SizeY, 280 * Const.SizeX, 280 * Const.SizeY);
        batch.end();
        //if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) mainClass.ChangeScreen(mainClass.menuScreen);

        eskanor_info.draw();
        eskanor_info.act();

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
        background.dispose();
        portrait.dispose();
        eskanor_info.dispose();
        music.dispose();
    }
    private void createEskanorInfo() {
        List<String> a = new ArrayList<>();
        a.add("РАЗЫСКИВАЕТСЯ!");
        a.add("");
        a.add("БЫВШИЙ ЧЛЕН КОРОЛЕВСКОГО ОРДЕНА СЕМЬ СМЕРТНЫХ ГРЕХОВ,");
        a.add("ПО СЛУХАМ ЯВЛЯЕТСЯ САМЫМ СИЛЬНЫМ ИЗ ВСЕЙ КОМАНДЫ,");
        a.add("ПРЕВОСХОДЯ ПО СИЛАМ ИХ КАПИТАНА.");
        a.add("НИКТО НЕ ЗНАЕТ ЕГО ИСТОРИИ И ОТКУДА У НЕГО ТАКАЯ");
        a.add("НЕВЕРОЯТНАЯ СИЛА, НО НЕКОТОРЫЕ УТВЕРЖДАЮТ, ЧТО ВИДЕЛИ");
        a.add("ЕГО НОЧЬЮ ПОЛНОСТЬЮ ОСЛАБШИМ И НЕ ИМЕЮЩИМ СИЛ.");
        a.add("НЕВЕРОЯТНО ОПАСЕН! ЕСЛИ НАЙДЕТЕ ЕГО, ТО ЛУЧШЕ БЕГИТЕ");
        a.add("И СОБЕРИТЕ АРМИЮ ИЗ СВЯТЫХ РЫЦАРЕЙ, КОТОРАЯ СМОЖЕТ");
        a.add("НА КАКОЕ-ТО ВРЕМЯ ЕГО ЗАДЕРЖАТЬ,");
        a.add("ИНАЧЕ ВАС ЖДЕТ МГНОВЕННАЯ СМЕРТЬ.");
        a.add("");
        a.add("НАГРАДА 500.000 ЗОЛОТЫХ МОНЕТ");
        float x, y = 600;
        eskanor = new Pair[a.size()];
        for (int j = 0; j < a.size(); j++) {
            if (j == 0) x = 350;
            else x = 50;
            Pair<TextureRegion, Vector2>[] pos = new Pair[a.get(j).length()];
            for (int i = 0; i < a.get(j).length(); i++) {
                if (a.get(j).charAt(i) == ',') y -= 5;
                pos[i] = new Pair(words.get(String.valueOf(a.get(j).charAt(i))), new Vector2(x, y));
                if (j == 0) x += 48;
                else switch (String.valueOf(a.get(j).charAt(i))){
                    case ".":
                        x += 5;
                        break;
                    case " ":
                        x += 10;
                        break;
                    case ",":
                        x += 5;
                        y += 5;
                        break;
                    case "-":
                        x += 10;
                        break;
                    case "!":
                        x += 5;
                        break;
                    default:
                        x += 24f;
                        break;
                }
            }
            eskanor[j] = new Pair<>(a.get(j), pos);
            y-= 28;
        }

    }
}
