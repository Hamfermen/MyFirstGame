package com.mygdx.game.InteractiveItem.All_Npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.InteractiveItem.Interact;

public class Npc extends Actor implements Interact {
    private Vector2 posMerlin;

    private Npc_Merlin Merlin;

    private Vector2 posMael;

    private Npc_Mael Mael;

    private String npcName;

    public boolean NpcInteract;

    public boolean dialogStarted = false;

    public String whoInteract = "";

    public int DialogPos;

    public Npc(World world, TiledMap tiledMap, String npcName) {
        this.npcName = npcName;
        switch (npcName){
            case "Merlin":
                posMerlin = findPositions(tiledMap, Const.TiledMap_Scale, "Merlin");
                Merlin = new Npc_Merlin(world, posMerlin);
                break;
            case "Mael":
                posMael = findPositions(tiledMap, Const.TiledMap_Scale, "Mael");
                if (Gdx.graphics.getWidth() != 1280) posMael.y += 0.09f;
                Mael = new Npc_Mael(world, posMael);
                break;
        }
    }

    public Npc() {
    }

    @Override
    public void interact() {
            if (NpcInteract) {
                switch (whoInteract) {
                    case "Merlin":
                        Merlin.NpcInteract = true;
                        break;
                    case "Mael":
                        Mael.NpcInteract = true;
                        break;
                }
            }
            else if (!NpcInteract) {
                switch (whoInteract){
                    case "Merlin":
                        Merlin.NpcInteract = false;
                        break;
                    case "Mael":
                        Mael.NpcInteract = false;
                        break;
                }
                whoInteract = "";
            }
    }

    public void NpcUpdate(){
        interact();
        if (NpcInteract) {
            switch (npcName) {
                case "Merlin":
                    Merlin.MerlinUpdate();
                    dialogStarted = Merlin.dialogStarted;
                    break;
                case "Mael":
                    Mael.MaelUpdate();
                    dialogStarted = Mael.dialogStarted;
                    break;
            }
        } else {
            switch (npcName) {
                case "Merlin":
                    Merlin.dialogStarted = false;
                    dialogStarted = false;
                    break;
                case "Mael":
                    Mael.dialogStarted = false;
                    dialogStarted = false;
                    break;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        switch (npcName) {
            case "Merlin":
                Merlin.draw(batch, parentAlpha);
                break;
            case "Mael":
                Mael.draw(batch, parentAlpha);
                break;
        }
    }

    private Vector2 findPositions(TiledMap tiledMap, float density, String layerName) {
        MapObjects objects = tiledMap.getLayers().get(layerName).getObjects();
        Vector2 pos = new Vector2();
        for (MapObject object : objects) {
            pos.add(getPosition((RectangleMapObject) object, density, layerName));
        }
        return pos;
    }

    private Vector2 getPosition(RectangleMapObject rectangleObject, float density, String layername) {
        Rectangle rectangle = rectangleObject.getRectangle();
        return new Vector2(rectangle.x * density, rectangle.y * density);
    }

    public String getDialog(String npcName){
        switch (npcName){
            case "Merlin":
                return Merlin.dialog.getNowDialog();
            case "Mael":
                return Mael.dialog.getNowDialog();
            default:
                return "";
        }
    }

    public Texture getDialogImage(String npcName){
        switch (npcName){
            case "Merlin":
                return Merlin.dialog.getNowDialogImage();
            case "Mael":
                return Mael.dialog.getNowDialogImage();
            default:
                return new Texture("");
        }
    }
}
