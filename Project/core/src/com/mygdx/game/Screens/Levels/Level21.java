package com.mygdx.game.Screens.Levels;

public class Level21 {
/*
    private class DialogImage extends Actor{
        @Override
        public void draw(Batch batch, float parentAlpha) {
            batch.draw(dialogImage, 0, 0, 256, 256);
        }
    }

    private World world;

    private TiledMap tiledMap;
    private TmxMapLoader loader;
    private TiledMapRenderer tiledMapRenderer;

    private OrthographicCamera uicamera, playercamera, dialogcamera;
    private ExtendViewport uiviewport, playerviewport, dialogviewport;

    private Box2DDebugRenderer box2DDebugRenderer;

    private SpriteBatch batch;

    private Unit player, enemies;

    private Stage level2, UI, dialog;

    private InteractiveItem items;

    private Npc Merlin;

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
        tiledMap = loader.load("level2.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Const.TiledMap_Scale);

        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "Blocks");
        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "WorldBarrier");
        TMObjectsUtils.buildBuildingsBodies(tiledMap, world, Const.TiledMap_Scale, "EnemiesBarrier");

        box2DDebugRenderer = new Box2DDebugRenderer();

        playercamera = new OrthographicCamera();
        playercamera.setToOrtho(false, 1280 * Const.Unit_Scale, 720 * Const.Unit_Scale);
        playercamera.update();

        uicamera = new OrthographicCamera();
        uicamera.setToOrtho(false, 1280, 720);
        uicamera.update();

        dialogcamera = new OrthographicCamera();
        dialogcamera.setToOrtho(false, 1280, 720);
        dialogcamera.update();

        player = new Unit(world, tiledMap);
        enemies = new Unit(world, tiledMap);

        playerviewport = new ExtendViewport(1280 * Const.Unit_Scale, 720 * Const.Unit_Scale, playercamera);
        uiviewport = new ExtendViewport(1280, 720, uicamera);
        dialogviewport = new ExtendViewport(1280, 720, dialogcamera);

        items = new InteractiveItem(world, tiledMap);

        Merlin = new Npc(world, tiledMap, "Merlin");

        font = new BitmapFont();
        text = new Label("", new Label.LabelStyle(font, new Color(16775541)));
        text.setFontScale(3, 3);
        text.setPosition(300, 200);

        world.setContactListener(new WorldContact(items, Merlin, player));

        level2 = new Stage(playerviewport, batch);
        level2.addActor(Merlin);
        level2.addActor(items);
        level2.addActor(player);
        level2.setDebugAll(true);

        dialog = new Stage(dialogviewport, batch);
        dialog.addActor(text);
        dialog.addActor(new DialogImage());
        dialog.setDebugAll(true);

        Gdx.input.setInputProcessor(level2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);

        tiledMapRenderer.setView(playercamera);
        tiledMapRenderer.render();


        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Const.mainclass.setScreen(new MenuScreen());
        }

        player.UnitUpdate(delta);

        if (playercamera.position.x <= 21f - 2.9f && playercamera.viewportWidth / 2 <= player.player.getX()) {
            playercamera.position.x = player.player.getX();
            playercamera.update();
        } else if (playercamera.position.x >= 6.6f && player.player.getX() <= 21f - 2.9f) {
            playercamera.position.x = player.player.getX();
            playercamera.update();
        }

        level2.getViewport().apply();
        level2.act();
        level2.draw();

        if(Merlin.dialogStarted) {
            dialog.getViewport().apply();
            dialog.act();
            dialog.draw();
        }

        box2DDebugRenderer.render(world, playercamera.combined);

        playercamera.position.y = player.player.getY();
        playercamera.update();

        items.ItemsUpdate();

        Merlin.NpcUpdate();

        text.setText(Merlin.getDialog("Merlin"));

        dialogImage = Merlin.getDialogImage("Merlin");

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

    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        box2DDebugRenderer.dispose();
        world.dispose();
        level2.dispose();
        UI.dispose();
    }*/
}
