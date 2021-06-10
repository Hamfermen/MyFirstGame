package com.mygdx.tns;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.tns.InteractiveItem.All_Npc.Npc;
import com.mygdx.tns.InteractiveItem.Items.InteractiveItem;
import com.mygdx.tns.Screens.Levels.Levels_Storage;
import com.mygdx.tns.Unit.Unit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//Todo: Убедиться в полной работе этого класса
public class Saves {

    private Unit unit;
    private Npc npc;
    private InteractiveItem items;
    private UI ui;
    private String itemsNumber, enemiesNumber;
    private World world;
    private OrthographicCamera camera;
    private Levels_Storage levels_storage;

    public Saves(World world, Unit unit, Npc npc, InteractiveItem items, UI ui, OrthographicCamera camera, Levels_Storage levels_storage){
        this.items = items;
        this.ui = ui;
        this.npc = npc;
        this.unit = unit;
        this.world = world;
        this.camera = camera;
        this.levels_storage = levels_storage;
    }
    public void Save(){

        MyPreference.setTime(ui.hours + ui.minutes);

        MyPreference.setScore(unit.player.score);
        MyPreference.setHealth(unit.player.health);
        MyPreference.setPositionX(unit.player.body.getPosition().x);
        MyPreference.setPositionY(unit.player.body.getPosition().y);

        MyPreference.setCameraPositionX(camera.position.x);
        MyPreference.setCameraPositionY(camera.position.y);

        MyPreference.setDialogPos(MyPreference.getDialogPos());

        itemsNumber = "";
        for (int i = 0; i < items.items.size(); i++){
            itemsNumber += Integer.toString(items.items.get(i).number) + " ";
        }
        MyPreference.setItemNumber(itemsNumber);

        enemiesNumber = "";
        Vector2[] pos = new Vector2[unit.enemies.size()];
        for (int i = 0; i < unit.enemies.size(); i++) {
            enemiesNumber += Integer.toString(unit.enemies.get(i).number) + " ";
        }
        MyPreference.setEnemyNumber(enemiesNumber);

    }
    public void Load(){
        LoadUI();

        unit.position = new Vector2(MyPreference.getPositionX(), MyPreference.getPositionY());
        switch (levels_storage.npcName){
             case "Mael":
                 npc.Mael.dialogPos = MyPreference.getDialogPos();
                 npc.Mael.dialog.dialogPos = MyPreference.getDialogPos();
                 break;
             case "Merlin":
                 npc.Merlin.dialogPos = MyPreference.getDialogPos();
                 npc.Merlin.dialog.dialogPos = MyPreference.getDialogPos();
                 break;
        }

        ui.player = unit.player;
        ui.pos = unit.player.body.getPosition();

        camera.position.x = MyPreference.getCameraPositionX();
        camera.position.y = MyPreference.getCameraPositionY();

        itemsNumber = MyPreference.getItemNumber();
        int i = 0;
        List<Integer> numbers = new ArrayList<>();
        while (!itemsNumber.isEmpty() && i < itemsNumber.length()){
            String s = "";
            while (itemsNumber.charAt(i) != ' ') {
                s += itemsNumber.charAt(i);
                i++;
            }
            numbers.add(Integer.parseInt(s));
            i++;
        }
        for (i = 0; i < items.items.size(); i++){
            if (Collections.binarySearch(numbers, items.items.get(i).number) < 0) {
                world.destroyBody(items.items.get(i).body);
                items.items.remove(i);
                i--;
            }
        }

        enemiesNumber = MyPreference.getEnemyNumber();
        i = 0;
        numbers.clear();
        while (!enemiesNumber.isEmpty() && i < enemiesNumber.length()){
            String s = "";
            while (enemiesNumber.charAt(i) != ' ') {
                s += enemiesNumber.charAt(i);
                i++;
            }
            numbers.add(Integer.parseInt(s));
            i++;
        }
        for (i = 0; i < unit.enemies.size(); i++){
            if (Collections.binarySearch(numbers, unit.enemies.get(i).number) < 0) {
                world.destroyBody(unit.enemies.get(i).body);
                unit.enemies.remove(i);
                i--;
            }
        }
        //unit.enemiesSize = MyPreference.pref.getInteger("EnemiesSize");
    }

    public void LoadUI(){
        ui.time = MyPreference.getTime();
        unit.player.score = MyPreference.getScore();
        unit.player.health = MyPreference.getHealth();
    }
}