package com.mygdx.game.Unit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Const;
import com.mygdx.game.MyPreference;
import com.mygdx.game.Unit.Enemies.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Unit extends Actor implements GetDamage, CanAttack{

    protected List<Vector2> EnemiesPos;

    public List<Enemy> enemies;
    public int enemiesSize;

    //protected List<Integer> enemiesDestroy;

    public Player player;

    private Vector2 PlayerPos;
    private boolean enemiesCreated = false;

    public boolean PlayerRight = false;
    public boolean PlayerLeft = false;
    public boolean EnemyRight = false;
    public boolean EnemyLeft = false;
    public boolean middle = false;

    public boolean isPlayerAttack = false;
    public boolean isPlayerGetDamage = false;

    public int whoEnemiesGetDamage = -1;

    public String whoAttack = "";
    public String whoGetDamage = "";
    public List<String> whoChangeDirection;

    private TiledMap tiledMap;
    private World world;

    public Vector2 position;

    public Unit() {
    }

    public Unit(World world, TiledMap tiledMap){
        this.tiledMap = tiledMap;
        this.world = world;
        whoChangeDirection = new ArrayList<>();
        //enemiesDestroy = new ArrayList<Integer>();
        position = new Vector2(MyPreference.getPositionX(), MyPreference.getPositionY());
    }

    @Override
    public void attack() {
        if (isPlayerAttack){
            player.canAttack = false;
            if ((player.runningRight && PlayerRight) || (!player.runningRight && PlayerLeft) || middle)
                player.canAttack = true;
            isPlayerAttack = false;
        }else {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).canAttack = false;
                if (whoAttack.equals("Enemy" + Integer.toString(i))) {
                    if ((enemies.get(i).runningRight && EnemyRight) || (!enemies.get(i).runningRight && EnemyLeft) || middle)
                        enemies.get(i).canAttack = true;
                }
            }
        }
    }

    @Override
    public void getDamage() {
        for (int i = 0; i < enemies.size(); i++) {
            if (player.isPlayerAttack && player.canAttack) {
                if (whoGetDamage.equals("Enemy" + Integer.toString(i))) {
                    enemies.get(i).health--;
                    whoEnemiesGetDamage = i;
                    //player.isPlayerAttack = false;
                }
            }
            if (enemies.get(i).isEnemyAttack && enemies.get(i).canAttack){
                player.health--;
                enemies.get(i).isEnemyAttack = false;
            }
        }
    }

    public void createPlayer(){
        PlayerPos = position;
        player = new Player(Const.playerX * Const.Unit_Scale, Const.playerY * Const.Unit_Scale, PlayerPos, world);
    }

    public void createEnemies() {
        enemiesCreated = true;

        EnemiesPos = new ArrayList<Vector2>();
        EnemiesPos = findPositions(tiledMap, Const.TiledMap_Scale, "EnemiesPos");

        enemies = new ArrayList<Enemy>();

        enemiesSize = 0;

        for (int i = 0; i < EnemiesPos.size(); i++) {
            enemies.add(new Enemy(i + 1, this,Const.playerX * Const.Unit_Scale, Const.playerY * Const.Unit_Scale, world, EnemiesPos.get(i), "Enemy" + Integer.toString(i), enemies));
            if (enemiesSize == 0) enemiesSize++;
        }
    }

    public void UnitUpdate(float delta) {
        if (enemies != null && enemiesSize != enemies.size()) {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).body.setUserData("Enemy" + Integer.toString(i));
            }
        }
        player.update(delta);
        if (enemiesCreated) {
            attack();
            getDamage();
            for (int i = 0; i < enemies.size(); i++) {
                if (whoEnemiesGetDamage != i) enemies.get(i).EnemyUpdate(delta);
                else whoEnemiesGetDamage = -1;
                if (whoChangeDirection.size() > 0 && enemies.size() > 0) {
                    for (int j = 0; j < whoChangeDirection.size(); j++) {
                        if (whoChangeDirection.get(j).equals("Enemy" + Integer.toString(i))) {
                            enemies.get(i).direction *= -1;
                            whoChangeDirection.remove(j);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        player.draw(batch, parentAlpha);
        if (enemies != null)
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).draw(batch, parentAlpha);
            }
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
