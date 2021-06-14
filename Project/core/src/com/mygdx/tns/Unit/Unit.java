package com.mygdx.tns.Unit;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.tns.Const;
import com.mygdx.tns.GameController;
import com.mygdx.tns.MyPreference;
import com.mygdx.tns.Unit.Enemies.Enemy;

import java.util.ArrayList;
import java.util.List;

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
    public String whoCanNotAttack = "";
    public String whoGetDamage = "";
    public String whoCanNotGetDamage = "";
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
        /*if ((player.runningRight && PlayerRight) || (!player.runningRight && PlayerLeft) || middle)
            player.canAttack = true;
        else player.canAttack = false;*/
        if (GameController.attack && player.hitBoxB == null) player.createHitBox();
        else if ((player.hitBoxB != null && !GameController.attack) || (GameController.attack && player.isHitBoxR != player.runningRight)) {
            world.destroyBody(player.hitBoxB);
            player.hitBoxB = null;
        }
        for (int i = 0; i < enemies.size(); i++) {
            if (whoAttack.equals("Enemy" + Integer.toString(i))) {
                if ((enemies.get(i).runningRight && EnemyRight) || (!enemies.get(i).runningRight && EnemyLeft) || middle) {
                    enemies.get(i).canAttack = true;
                    whoAttack = "";
                }
            }
            if (whoCanNotAttack.equals("Enemy" + Integer.toString(i))) {
                enemies.get(i).canAttack = false;
                whoCanNotAttack = "";
            }
        }
    }

    @Override
    public void getDamage() {
        for (int i = 0; i < enemies.size(); i++) {
            /*if (player.isPlayerAttack && player.canAttack) {
                if (enemies.get(i).canGetDamage) {
                    enemies.get(i).health--;
                    //player.isPlayerAttack = false;
                }
            }*/
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
                if (whoGetDamage.equals("Enemy" + Integer.toString(i))) {
                    System.out.println(whoGetDamage + " " + i);
                    whoGetDamage = "";
                    enemies.get(i).canGetDamage = true;
                }
                if (whoCanNotGetDamage.equals("Enemy" + Integer.toString(i))) enemies.get(i).canGetDamage = false;
                enemies.get(i).EnemyUpdate(delta);
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
