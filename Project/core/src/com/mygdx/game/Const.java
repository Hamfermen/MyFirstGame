package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.Levels.Levels_Storage;

import java.util.ArrayList;
import java.util.List;

public class Const {
    public static MainClass mainclass;

    public static boolean freeze = false;

    public static boolean newLevel;

    public static boolean newGame;

    public static boolean cameraCanMove = false;

    public final static float SizeX = Gdx.graphics.getWidth() / 1280f;
    public final static float SizeY = Gdx.graphics.getHeight() / 720f;

    public static final float Unit_Scale = 1f / 200f;
    public static final float TiledMap_Scale = Unit_Scale;

    public final static float playerX = 64f * SizeX, playerY = 128f * SizeY;
    public final static float MaelX = 180f * SizeX, MaelY = 140f * SizeY;

    /*final public static short CATEGORY_PLAYER = 0x0001;
    final public static short CATEGORY_ENEMY = 0x0002;
    final public static short CATEGORY_BLOCKS= 0x0004;
    final public static short CATEGORY_BORDERS = 0x0008;
    final public static short CATEGORY_CHECKER = 0x0016;
    final public static short MASK_PLAYER = CATEGORY_BLOCKS;
    final public static short MASK_CHECKER = CATEGORY_BORDERS;
    final public static short MASK_ENEMY =  CATEGORY_BLOCKS | CATEGORY_BORDERS;
    final public static short MASK_BORDER = CATEGORY_CHECKER | CATEGORY_ENEMY;
    final public static short MASK_BLOCKS = -1;*/

    public static boolean hit = false, attack = false;

    public static List<Levels_Storage> levels = new ArrayList<>();

    public static List<Vector2> points = new ArrayList<Vector2>();
}