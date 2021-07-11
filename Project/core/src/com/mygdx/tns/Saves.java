package com.mygdx.tns;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.tns.InteractiveItem.All_Npc.Npc;
import com.mygdx.tns.InteractiveItem.Items.InteractiveItem;
import com.mygdx.tns.Screens.Levels.Levels_Storage;
import com.mygdx.tns.Unit.Player;
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
    private World world;
    private OrthographicCamera camera;
    private Levels_Storage levels_storage;
    private JSONPaB<Save> jsonPaB;
    private Save save;

    public Saves(World world, Unit unit, Npc npc, InteractiveItem items, UI ui, OrthographicCamera camera, Levels_Storage levels_storage){
        this.items = items;
        this.ui = ui;
        this.npc = npc;
        this.unit = unit;
        this.world = world;
        this.camera = camera;
        this.levels_storage = levels_storage;
        save = new Save();
        jsonPaB = new JSONPaB("Saves.json", save, Save.class);
    }
    public void Save() {
        save.playerPos = unit.player.body.getPosition();
        save.time = ui.hours + ui.minutes;
        save.health = unit.player.health;
        save.score = unit.player.score;
        save.cameraPos = camera.position;

        switch (levels_storage.npcName){
            case "Mael":
                save.dialogPos = npc.Mael.dialogPos != 0 ? npc.Mael.dialogPos - 1 : npc.Mael.dialogPos;
                break;
            case "Merlin":
                save.dialogPos = npc.Merlin.dialogPos != 0 ? npc.Merlin.dialogPos - 1 : npc.Merlin.dialogPos;
                break;
        }

        List<Integer> itemsNumber = new ArrayList<>();
        for (int i = 0; i < items.items.size(); i++){
            itemsNumber.add(items.items.get(i).number);
        }

        save.itemsNumber = itemsNumber;
        itemsNumber.clear();

        List<Integer> enemiesNumber = new ArrayList<>();
        if (unit.enemies != null) {
            //Vector2[] pos = new Vector2[unit.enemies.size()];
            for (int i = 0; i < unit.enemies.size(); i++) {
                enemiesNumber.add(unit.enemies.get(i).number);
            }
            save.enemiesNumber = enemiesNumber;
            enemiesNumber.clear();
        }

        jsonPaB.build(save);
    }
    public void Load(){

        LoadUI();

        unit.player.body.setTransform(save.playerPos,0);

        switch (levels_storage.npcName){
             case "Mael":
                 npc.Mael.dialogPos = save.dialogPos;
                 npc.Mael.dialog.dialogPos = save.dialogPos;
                 break;
             case "Merlin":
                 npc.Merlin.dialogPos = save.dialogPos;
                 npc.Merlin.dialog.dialogPos = save.dialogPos;
                 break;
        }

        ui.player = unit.player;
        ui.pos = unit.player.body.getPosition();

        camera.position.x = save.cameraPos.x;
        camera.position.y = save.cameraPos.y;

        List<Integer> numbers = save.itemsNumber;

        for (int i = 0; i < items.items.size(); i++){
            if (Collections.binarySearch(numbers, items.items.get(i).number) < 0) {
                world.destroyBody(items.items.get(i).body);
                items.items.remove(i);
                i--;
            }
        }

        numbers.clear();

        numbers = save.enemiesNumber;
        for (int i = 0; i < unit.enemies.size(); i++){
            if (Collections.binarySearch(numbers, unit.enemies.get(i).number) < 0) {
                world.destroyBody(unit.enemies.get(i).body);
                unit.enemies.remove(i);
                i--;
            }
        }
        numbers.clear();
        //unit.enemiesSize = MyPreference.pref.getInteger("EnemiesSize");
    }

    public void LoadUI(){
        //jsonPaB.build(save);
        save = jsonPaB.parse();

        ui.time = save.time;

        unit.player.score = save.score;
        unit.player.health = save.health;
    }


    private class Save {
        public Vector2 playerPos;
        public Vector3 cameraPos;
        public String time;
        public int health, dialogPos;
        public float score;
        public List<Integer> itemsNumber, enemiesNumber;
    }
}
