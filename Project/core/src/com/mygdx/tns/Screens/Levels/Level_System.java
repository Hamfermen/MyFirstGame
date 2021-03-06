package com.mygdx.tns.Screens.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.tns.Const;
import com.mygdx.tns.GameController;
import com.mygdx.tns.InteractiveItem.All_Npc.Npc;
import com.mygdx.tns.InteractiveItem.Items.InteractiveItem;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;
import com.mygdx.tns.Saves;
import com.mygdx.tns.Screens.LoadingScreen;
import com.mygdx.tns.TMObjectsUtils;
import com.mygdx.tns.UI;
import com.mygdx.tns.Unit.Unit;
import com.mygdx.tns.WorldContact;

import java.util.ArrayList;
import java.util.List;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class Level_System implements Screen {

    private class DialogImage extends Actor {
        Texture bg;

        public DialogImage(){
            bg = new Texture("dialog_bg.png");
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(dialogImage, 0, 0, 256 * Const.SizeX, 256 * Const.SizeY);
            batch.draw(bg, 256 * Const.SizeX, 0, 1024 * Const.SizeX, 256 * Const.SizeY);
        }
    }

    private class BackGround extends Actor {
        private Texture t, ts, lv2;

        public BackGround() {
            t = new Texture("TiledMapBackGround.png");
            ts = new Texture("Sky.png");
            lv2 = new Texture("level2bg.png");
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (MyPreference.getLevel_number() == 0) {
                batch.draw(t, 0, -0.2f * Const.SizeY, playerCamera.viewportWidth * 4.6875f, playerCamera.viewportHeight * 1.805556f);
                batch.draw(ts, 0, playerCamera.viewportHeight * 1.805556f - 0.2f * Const.SizeY, playerCamera.viewportWidth * 4.6875f, playerCamera.viewportHeight * 1.805556f);
            }else batch.draw(lv2, 0, 0,6000 * Const.Unit_Scale, 3525 * Const.Unit_Scale);
        }
    }

    private World world;

    private MainClass mainClass;

    private Levels_Storage levelStorage;

    public LoadingScreen loadingScreen;

    private TiledMap tiledMap;
    private TmxMapLoader loader;
    private TiledMapRenderer tiledMapRenderer;

    private OrthographicCamera uiCamera, playerCamera, dialogCamera, deadScreenCamera, controlCamera;
    private ExtendViewport playerViewport, dialogViewport, deadScreenViewport, controlViewport;
    private StretchViewport uiViewport;

    private Box2DDebugRenderer box2DDebugRenderer;

    private SpriteBatch batch;

    private Unit unit;

    private Stage level, dialog, ui, BackGround, control;

    private InteractiveItem items;

    private Npc Mael, Merlin;

    private UI UI;

    public Label text;
    private BitmapFont font;

    private Texture dialogImage;

    private List<Vector2> cameraCriticalPos;

    private BackGround background;

    private Saves saves;

    private Music music;

    private GameController gameController[];

    public Level_System(MainClass mainClass){
        this.mainClass = mainClass;
    }

    @Override
    public void show() {

        Const.freeze = false;

        music = Gdx.audio.newMusic(Gdx.files.internal("BackGroundMusic2.mp3"));
        music.setVolume(MyPreference.getMusicValue());

        batch = new SpriteBatch();

        levelStorage = Const.levels.get(MyPreference.getLevel_number());

        background = new BackGround();

        world = new World(new Vector2(0, -30), true);

//        tiledMap = new TiledMap();
        loader = new TmxMapLoader();
        tiledMap = loader.load(levelStorage.levelName + ".tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Const.TiledMap_Scale);


        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "Blocks");
        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "WorldBarrier");
        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "EnemiesBarrier");
        //if (MyPreference.getLevel_number() == 1) TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "Platforms");
        cameraCriticalPos = new ArrayList<>(findPositions(tiledMap, Const.TiledMap_Scale, "Camera"));

        box2DDebugRenderer = new Box2DDebugRenderer();

        /*handler = new RayHandler(world);
        handler.setAmbientLight(0.8f);
        handler.setBlurNum(3);

        //light = new ConeLight(handler, 128, new Color(255, 204, 51, 1), 20, 0, 10, -90, 90);

        //PointLight light = new PointLight(handler, 128, new Color(255, 204, 51, 1), 20,0,10);

        handler.setShadows(true);
        light.setStaticLight(false);
        light.setSoft(true);*/

        playerCamera = new OrthographicCamera();
        playerCamera.setToOrtho(false, 1280 * Const.Unit_Scale, 720 * Const.Unit_Scale);
        playerCamera.update();

        uiCamera = new OrthographicCamera(1280, 720);
        uiCamera.setToOrtho(false, 1280, 720);
        uiCamera.update();

        dialogCamera = new OrthographicCamera();
        dialogCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dialogCamera.update();

        deadScreenCamera = new OrthographicCamera();
        deadScreenCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        deadScreenCamera.update();

        unit = new Unit(world, tiledMap);
        unit.position = findPositions(tiledMap, Const.TiledMap_Scale, "PlayerStart").get(0);
        playerCamera.position.x = 10f;
        if (levelStorage.haveEnemies) unit.createEnemies();
        unit.createPlayer();

        playerViewport = new ExtendViewport(playerCamera.viewportWidth, playerCamera.viewportHeight, playerCamera);
        uiViewport = new StretchViewport(1280, 720, uiCamera);
        dialogViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), dialogCamera);
        deadScreenViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), dialogCamera);

        font = new BitmapFont(Gdx.files.internal("test_f.fnt"));
        text = new Label("", new Label.LabelStyle(font, font.getColor()));
        text.setFontScale(0.9f * Const.SizeX, 0.9f * Const.SizeY);
        text.setPosition(280 * Const.SizeX, 0 * Const.SizeY);
        text.setSize(1000 * Const.SizeX, 240 * Const.SizeY);
        text.setAlignment(Align.topLeft);

        items = new InteractiveItem(world, tiledMap, mainClass, unit);

        BackGround = new Stage(playerViewport, batch);

        BackGround.addActor(background);

        level = new Stage(playerViewport, batch);

        switch (levelStorage.npcName){
            case "Mael":
                Mael = new Npc(world, tiledMap, "Mael");
                level.addActor(Mael);
                levelStorage.npc = Mael;
                Mael.Mael.setDialog();
                Mael.Mael.setDialogImage();
                break;
            case "Merlin":
                Merlin = new Npc(world, tiledMap, "Merlin");
                level.addActor(Merlin);
                levelStorage.npc = Merlin;
                break;
        }

        level.addActor(items);
        level.addActor(unit);
        //level.setDebugAll(true);

        UI = new UI(unit.player.body.getPosition(), unit.player);

        world.setContactListener(new WorldContact(items, levelStorage.npc, unit, world));

        dialog = new Stage(dialogViewport, batch);
        dialog.addActor(new DialogImage());
        dialog.addActor(text);
        //dialog.setDebugAll(true);

        ui = new Stage(uiViewport, batch);
        ui.addActor(UI);

        Gdx.input.setInputProcessor(level);

        saves = new Saves(world, unit, levelStorage.npc, items, UI, playerCamera, Const.levels.get(MyPreference.getLevel_number()));

        if (!MyPreference.getIsNewGame() && !MyPreference.getIsNewLevel()) saves.Load();
        if (MyPreference.getIsNewLevel()) saves.LoadUI();
        MyPreference.setIsNewLevel(false);

        UI.startTime();

        gameController = new GameController[6];
        gameController[0] = new GameController(GameController.Direction.RIGHT);
        gameController[1] = new GameController(GameController.Direction.LEFT);
        gameController[2] = new GameController(GameController.Direction.UP);
        gameController[3] = new GameController(GameController.Direction.ATTACK);
        gameController[4] = new GameController(GameController.Direction.INTERACT);
        gameController[5] = new GameController(GameController.Direction.SUPERATTACK);

        controlCamera = new OrthographicCamera();
        controlCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        controlCamera.update();

        controlViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), controlCamera);

        control = new Stage(controlViewport, batch);

        for (int i = 0; i < 6; i++) control.addActor(gameController[i]);
        //control.setDebugAll(true);

        Gdx.input.setInputProcessor(control);

        loadingScreen = new LoadingScreen(mainClass, UI.worldTime);

        music.setLooping(true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0.4f, 0.7f, 1);

        if (!music.isPlaying()) music.play();

        if (!Const.toDestroy.isEmpty()) {
            while (!Const.toDestroy.isEmpty()) {
                Body body = Const.toDestroy.iterator().next();
                world.destroyBody(body);
                Const.toDestroy.remove(body);
            }
        }



       /* batch.begin();
        batch.draw(background, 0,50, 640f, 340f);
        batch.end();*/

       BackGround.draw();

        tiledMapRenderer.setView(playerCamera);
        tiledMapRenderer.render();

        UI.UIUpdate();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            mainClass.ChangeScreen(mainClass.menuScreen);
        }

        if (!Const.freeze) unit.UnitUpdate(delta);

        if (unit.player.getX() <= (6000 * Const.Unit_Scale) - playerCamera.viewportWidth / 2 - 0.1f && playerCamera.viewportWidth / 2 <= unit.player.getX()) {
            playerCamera.position.x = unit.player.getX();
            playerCamera.update();


        } else if (playerCamera.position.x >= playerCamera.viewportWidth / 2 + 0.1f && playerCamera.position.x <= (6000 * Const.Unit_Scale) - playerCamera.viewportWidth / 2 - 0.1f) {
            playerCamera.position.x = unit.player.getX();
            playerCamera.update();
        }

        level.getViewport().apply();
        level.act();
        level.draw();

        ui.getViewport().apply();
        ui.act();
        ui.draw();

        control.getViewport().apply();
        control.act();
        control.draw();

        switch (levelStorage.npcName){
            case "Mael":
                if(Mael.dialogStarted) {
                    dialog.getViewport().apply();
                    dialog.act();
                    dialog.draw();
                    if (!(Mael.Mael.dialog.dialogPos < Mael.Mael.dialog.dialog.size() - 1)) {
                        Mael.Mael.dialog.dialogPos--;
                        mainClass.ChangeScreen(mainClass.bossLevel);
                    }
                    if (Const.smallForm) {
                        Mael.Mael.Mael_dialog.clear();
                        Mael.Mael.Mael_dialog.add("");
                        Mael.Mael.setDialog();
                        Mael.Mael.Mael_dialogImage.clear();
                        Mael.Mael.Mael_dialogImage.add(new Texture("DialogImage\\eskanor_sf_4.png"));
                        Mael.Mael.setDialogImage();
                    }
                }
                break;
            case "Merlin":
                if(Merlin.dialogStarted) {
                    dialog.getViewport().apply();
                    dialog.act();
                    dialog.draw();
                }
                break;
        }

        if (unit.player.isPlayerDead) {
            MyPreference.pref.clear();
            MyPreference.setIsNewGame(true);
            MyPreference.setIsNewLevel(true);
            mainClass.clearSaves();
            mainClass.ChangeScreen(mainClass.deathScreen);
        }

        if (UI.worldTime.getHours() >= 8 && UI.worldTime.getHours() < 18) {
            unit.player.smallForm = false;
            Const.smallForm = false;
        }
        else if (UI.worldTime.getHours() >= 18) {
            unit.player.smallForm = true;
            Const.smallForm = true;
            GameController.attack = false;
        }

        if (UI.worldTime.getHours() >= 19) unit.player.isPlayerDead = true;

        //box2DDebugRenderer.render(world, playerCamera.combined);

        playerCamera.position.y = unit.player.getY() + 0.6f;
        playerCamera.update();

        items.ItemsUpdate();

        switch (levelStorage.npcName){
            case "Mael":
                Mael.NpcUpdate();
                break;
            case "Merlin":
                Merlin.NpcUpdate();
                break;
        }

        switch (levelStorage.npcName){
            case "Mael":
                text.setText(Mael.getDialog("Mael"));
                dialogImage = Mael.getDialogImage("Mael");
                break;
            case "Merlin":
                text.setText(Merlin.getDialog("Merlin"));
                dialogImage = Merlin.getDialogImage("Merlin");
                break;
        }

        while (Const.getScore > 0) {
            Const.getScore--;
            if (unit.player.score <= 112) {
                if (unit.player.score + 16 > 112)
                    unit.player.score += 112 - unit.player.score ;
                else unit.player.score += 16;
            }
        }

        //if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) Const.freeze = !Const.freeze;

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
        /*MyPreference.setScore(unit.player.score);
        MyPreference.setTime(UI.hours + UI.minutes);
        MyPreference.setPositionX(unit.player.body.getPosition().x);
        MyPreference.setPositionY(unit.player.body.getPosition().y);*/
        if (!unit.player.isPlayerDead) {
            MyPreference.setIsNewGame(false);
            saves.Save();
        }
        GameController.allFalse();
        dispose();
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        font.dispose();
        //box2DDebugRenderer.dispose();
        level.dispose();
        ui.dispose();
        control.dispose();
        BackGround.dispose();
        dialog.dispose();
        batch.dispose();
        music.dispose();
        dialogImage.dispose();
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