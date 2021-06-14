package com.mygdx.tns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.mygdx.tns.Screens.Levels.Levels_Storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Const {
    public static MainClass mainclass;

    public static boolean freeze = false;

    public static boolean cameraCanMove = false;

    public static int getScore = 0;

    public final static float SizeX = Gdx.graphics.getWidth() / 1280f;
    public final static float SizeY = Gdx.graphics.getHeight() / 720f;

    public static final float Unit_Scale = 1f / 200f;
    public static final float TiledMap_Scale = Unit_Scale;

    public final static float playerX = 64f, playerY = 128f;
    public final static float MaelX = 180f, MaelY = 180f;

    public static boolean smallForm = false;

    public static HashSet<Body> toDestroy = new HashSet<>();

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

    public static float time = 0;
}
