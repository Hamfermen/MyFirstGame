package com.mygdx.tns.InteractiveItem.Items;

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
import com.mygdx.tns.InteractiveItem.Interact;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.Unit.Unit;

import java.util.ArrayList;
import java.util.List;

public class InteractiveItem extends Actor implements Interact {
    protected List<Vector2> ItemPos;

    public List<Item> items;

    public Finish finish;

    private List<Vector2> FinishPos;

    public String whoInteract = "";

    public boolean ItemInteract = false;

    private MainClass mainClass;

    public InteractiveItem() {
    }

    public InteractiveItem(World world, TiledMap tiledMap, MainClass mainClass, Unit unit){
        ItemPos = new ArrayList<Vector2>();
        ItemPos = findPositions(tiledMap, Const.TiledMap_Scale, "ItemPos");

        this.mainClass = mainClass;

        FinishPos = findPositions(tiledMap, Const.TiledMap_Scale, "Finish");
        finish = new Finish(world, FinishPos.get(0), "Finish", this.mainClass);

        items = new ArrayList<>();

        for (int i = 0; i < ItemPos.size(); i++){
            items.add(new Item(i + 1, world, ItemPos.get(i), "Item" + Integer.toString(i), unit));
        }
    }

    @Override
    public void interact() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).ItemInteract = false;
            if (whoInteract == items.get(i).body.getUserData())
                items.get(i).ItemInteract = true;
            else if (!ItemInteract) whoInteract = "";
        }
        if (whoInteract == "Finish")
            finish.FinishInteract = true;
        else finish.FinishInteract = false;
    }

    public void ItemsUpdate() {
        interact();
        finish.FinishUpdate();
        for (int i = 0; i < items.size(); i++)
            if (!items.get(i).isRemove) items.get(i).ItemUpdate();
            else {
                items.remove(items.get(i));
                i--;
            }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).body.getFixtureList().size != 0)
                items.get(i).draw(batch, parentAlpha);
        }
        finish.draw(batch, parentAlpha);
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

    public List<Item> getItems() {
        return items;
    }
}
