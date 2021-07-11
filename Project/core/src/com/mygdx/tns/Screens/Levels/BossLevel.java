package com.mygdx.tns.Screens.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.tns.BossContact;
import com.mygdx.tns.Const;
import com.mygdx.tns.GameController;
import com.mygdx.tns.InteractiveItem.All_Npc.Npc;
import com.mygdx.tns.InteractiveItem.Items.InteractiveItem;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;
import com.mygdx.tns.Saves;
import com.mygdx.tns.TMObjectsUtils;
import com.mygdx.tns.UI;
import com.mygdx.tns.Unit.Enemies.Boss;
import com.mygdx.tns.Unit.Unit;
import com.mygdx.tns.WorldContact;

import java.util.ArrayList;
import java.util.List;

public class BossLevel implements Screen {

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private TiledMap tiledMap;
    private TiledMapRenderer tiledMapRenderer;
    private TmxMapLoader loader;

    private SpriteBatch batch;

    private OrthographicCamera playerCamera, uiCamera, controlCamera;

    private ExtendViewport controlViewport;
    private StretchViewport uiViewport, playerViewport;

    private Stage bossLevel, ui, control;

    private MainClass mainClass;

    private Unit unit;

    private UI UI;

    private Saves saves;

    private Boss boss;

    private Npc mael;

    private InteractiveItem items;

    private Music music;

    private GameController gameController[];

    public BossLevel(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        world = new World(new Vector2(0, -30), true);

        //box2DDebugRenderer = new Box2DDebugRenderer();

        music = Gdx.audio.newMusic(Gdx.files.internal("bossFight.mp3"));
        music.setVolume(MyPreference.getMusicValue());
        music.setLooping(true);

//        tiledMap = new TiledMap();
        loader = new TmxMapLoader();
        tiledMap = loader.load("bossLevel.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Const.TiledMap_Scale);

        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "Blocks");
        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "WorldBarrier");
        //TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "EnemiesBarrier");

        playerCamera = new OrthographicCamera();
        playerCamera.setToOrtho(false, 1280 * Const.Unit_Scale, 720 * Const.Unit_Scale);
        playerCamera.update();

        uiCamera = new OrthographicCamera(1280, 720);
        uiCamera.setToOrtho(false, 1280, 720);
        uiCamera.update();

        uiViewport = new StretchViewport(1280, 720, uiCamera);

        playerViewport = new StretchViewport(1280 * Const.Unit_Scale, 720 * Const.Unit_Scale, playerCamera);

        bossLevel = new Stage(playerViewport, batch);

        unit = new Unit(world, tiledMap);
        unit.position = findPositions(tiledMap, Const.TiledMap_Scale, "PlayerStart").get(0);

        unit.createPlayer();

        items = new InteractiveItem(world, tiledMap, mainClass, unit);

        UI = new UI(unit.player.body.getPosition(), unit.player);

        ui = new Stage(uiViewport, batch);
        ui.addActor(UI);

        mael = new Npc(world, tiledMap, "Merlin");

        saves = new Saves(world, unit, mael, items, UI, playerCamera, Const.levels.get(MyPreference.getLevel_number()));

        items = null;

        saves.LoadUI();

        UI.time = "1700";

        UI.startTime();

        boss = new Boss(unit.player, tiledMap, world);

        world.setContactListener(new BossContact(unit, boss));

        bossLevel.addActor(unit);
        bossLevel.addActor(boss);

        controlCamera = new OrthographicCamera();
        controlCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        controlCamera.update();

        controlViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), controlCamera);

        control = new Stage(controlViewport, batch);

        gameController = new GameController[6];
        gameController[0] = new GameController(GameController.Direction.RIGHT);
        gameController[1] = new GameController(GameController.Direction.LEFT);
        gameController[2] = new GameController(GameController.Direction.UP);
        gameController[3] = new GameController(GameController.Direction.ATTACK);
        gameController[4] = new GameController(GameController.Direction.INTERACT);
        gameController[5] = new GameController(GameController.Direction.SUPERATTACK);

        for (int i = 0; i < 6; i++) control.addActor(gameController[i]);

        Gdx.input.setInputProcessor(control);

        boss.firstAttack();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0.4f, 0.7f, 1);

        if (boss.health <= 0) {
            MyPreference.pref.clear();
            MyPreference.setIsNewGame(true);
            MyPreference.setIsNewLevel(true);
            mainClass.clearSaves();
            mainClass.ChangeScreen(mainClass.winScreen);
        }

        if (!music.isPlaying()) music.play();

        if (unit.player.health <= 0) {
            MyPreference.pref.clear();
            MyPreference.setIsNewGame(true);
            MyPreference.setIsNewLevel(true);
            mainClass.clearSaves();
            mainClass.ChangeScreen(mainClass.deathScreen);
        }

        if (!Const.toDestroy.isEmpty()) {
            while (!Const.toDestroy.isEmpty()) {
                Body body = Const.toDestroy.iterator().next();
                world.destroyBody(body);
                Const.toDestroy.remove(body);
            }
        }

        tiledMapRenderer.setView(playerCamera);
        tiledMapRenderer.render();

        /*if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || GameController.pause) {
            GameController.pause = false;
            mainClass.ChangeScreen(mainClass.menuScreen);
        }*/

        if (UI.worldTime.getHours() >= 8 && UI.worldTime.getHours() < 18) {
            unit.player.smallForm = false;
            Const.smallForm = false;
        }
        else if (UI.worldTime.getHours() >= 18) {
            unit.player.smallForm = true;
            Const.smallForm = true;
            GameController.attack = false;
        }

        if (!Const.freeze) unit.UnitUpdate(delta);

        //box2DDebugRenderer.render(world, playerCamera.combined);

        bossLevel.getViewport().apply();
        bossLevel.draw();
        bossLevel.act();

        ui.getViewport().apply();
        ui.act();
        ui.draw();

        control.getViewport().apply();
        control.act();
        control.draw();

        boss.bossUpdate();

        UI.UIUpdate();

        if (!Const.freeze) world.step(1f / 60f, 6, 2);
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
        //saves.Save();
        dispose();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        bossLevel.dispose();
        ui.dispose();
        //box2DDebugRenderer.dispose();
        music.dispose();
        boss.destroyAll();
        world.dispose();
    }

    private List<Vector2> findPositions(TiledMap tiledMap, float density, String layerName) {
        MapObjects objects = tiledMap.getLayers().get(layerName).getObjects();
        List<Vector2> pos = new ArrayList<>();
        for (MapObject object : objects) {
            pos.add(getPosition((RectangleMapObject) object, density, layerName));
        }
        return pos;
    }

    private Vector2 getPosition(RectangleMapObject rectangleObject, float density, String layername) {
        Rectangle rectangle = rectangleObject.getRectangle();
        return new Vector2(rectangle.x * density, rectangle.y * density);
    }
}
