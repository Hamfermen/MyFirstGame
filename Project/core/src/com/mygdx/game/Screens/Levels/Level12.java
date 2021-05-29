package com.mygdx.game.Screens.Levels;

public class Level12 {
   /* private class DialogImage extends Actor {
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(dialogImage, 0, 0, 256 / Const.SizeX, 256 / Const.SizeY);
        }
    }

    private World world;

    private TiledMap tiledMap;
    private TmxMapLoader loader;
    private TiledMapRenderer tiledMapRenderer;

    private OrthographicCamera uiCamera, playerCamera, dialogCamera, deadScreenCamera;
    private ExtendViewport uiViewport, playerViewport, dialogViewport, deadScreenViewport;

    private Box2DDebugRenderer box2DDebugRenderer;

    private SpriteBatch batch;

    private Unit unit;

    private DeadButton deadButton;

    private Stage level1, dialog, ui, deadScreen;

    private InteractiveItem items;

    private Npc Mael;

    private UI UI;

    public Label text;

    private BitmapFont font;

    private Texture dialogImage;

    @Override
    public void show() {
        batch = new SpriteBatch();

        MyPreference.setDialogPos(0);

        world = new World(new Vector2(0, -30), true);

//        tiledMap = new TiledMap();
        loader = new TmxMapLoader();
        tiledMap = loader.load("level1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Const.TiledMap_Scale);

        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "Blocks");
        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "WorldBarrier");
        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "EnemiesBarrier");

        box2DDebugRenderer = new Box2DDebugRenderer();

        playerCamera = new OrthographicCamera();
        playerCamera.setToOrtho(false, Gdx.graphics.getWidth() / Const.SizeY * Const.Unit_Scale, Gdx.graphics.getHeight() / Const.SizeY * Const.Unit_Scale);
        playerCamera.update();

        uiCamera = new OrthographicCamera(640, 360);
        uiCamera.setToOrtho(false, 640, 360);
        uiCamera.update();

        dialogCamera = new OrthographicCamera();
        dialogCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        dialogCamera.update();

        deadScreenCamera = new OrthographicCamera();
        deadScreenCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        deadScreenCamera.update();

        unit = new Unit(world, tiledMap);
        unit.createEnemies();
        unit.createPlayer();

        playerViewport = new ExtendViewport(playerCamera.viewportWidth, playerCamera.viewportHeight, playerCamera);
        uiViewport = new ExtendViewport(uiCamera.viewportWidth, uiCamera.viewportHeight, uiCamera);
        dialogViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), dialogCamera);
        deadScreenViewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), dialogCamera);

        font = new BitmapFont(Gdx.files.internal("font.fnt"));
        text = new Label("", new Label.LabelStyle(font, font.getColor()));
        text.setFontScale(1 / Const.SizeX, 1 / Const.SizeY);
        text.setPosition(300 / Const.SizeX, 200 / Const.SizeY);

        items = new InteractiveItem(world, tiledMap);

        Mael = new Npc(world, tiledMap, "Mael");

        UI = new UI(unit.player.body.getPosition(), unit.player);

        deadButton = new DeadButton(new Level_System());
        deadButton.setBounds( 250 * Const.SizeX , 250 * Const.SizeY, 150 * Const.SizeX, 37.5f * Const.SizeY);

        world.setContactListener(new WorldContact(items, Mael, unit));

        level1 = new Stage(playerViewport, batch);
        level1.addActor(Mael);
        level1.addActor(items);
        level1.addActor(unit);
        level1.setDebugAll(true);

        dialog = new Stage(dialogViewport, batch);
        dialog.addActor(text);
        dialog.addActor(new DialogImage());
        dialog.setDebugAll(true);

        ui = new Stage(uiViewport, batch);
        ui.addActor(UI);

        deadScreen = new Stage(deadScreenViewport, batch);
        deadScreen.addActor(deadButton);

        Gdx.input.setInputProcessor(level1);
        Gdx.input.setInputProcessor(deadScreen);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);

        tiledMapRenderer.setView(playerCamera);
        tiledMapRenderer.render();

        UI.UIUpdate();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            mainClass.ChangeScreen(new MenuScreen());
        }

        if (!unit.player.isPlayerDead) unit.UnitUpdate(delta);

        if (playerCamera.position.x <= 21f - 2.9f && playerCamera.viewportWidth / 2 <= unit.player.getX()) {
            playerCamera.position.x = unit.player.getX();
            playerCamera.update();
        } else if (playerCamera.position.x >= 6.6f && unit.player.getX() <= 21f - 2.9f) {
            playerCamera.position.x = unit.player.getX();
            playerCamera.update();
        }

        level1.getViewport().apply();
        level1.act();
        level1.draw();

        if(Mael.dialogStarted) {
            dialog.getViewport().apply();
            dialog.act();
            dialog.draw();
        }


        ui.getViewport().apply();
        ui.act();
        ui.draw();

        if (unit.player.isPlayerDead) {
            deadScreen.getViewport().apply();
            deadScreen.act();
            deadScreen.draw();
        }

        box2DDebugRenderer.render(world, playerCamera.combined);

        playerCamera.position.y = unit.player.getY();
        playerCamera.update();

        items.ItemsUpdate();

        Mael.NpcUpdate();

        text.setText(Mael.getDialog("Mael"));

        dialogImage = Mael.getDialogImage("Mael");

        world.step(1f / 60f, 6, 2);
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
        tiledMap.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        level1.dispose();
        ui.dispose();
        dialog.dispose();
    }*/
}
