package com.mygdx.tns.InteractiveItem.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.tns.Const;
import com.mygdx.tns.GameController;
import com.mygdx.tns.Unit.Unit;

public class Item extends InteractiveItem {

    private Vector2 pos;

    private Texture texture;

    public Body body;

    public boolean ItemInteract = false;
    public boolean isRemove = false;

    private World world;

    private Unit unit;

    public int number;

    public Item(int number, World world, Vector2 pos, String bodyName, Unit unit) {
        this.pos = pos;

        this.number = number;

        //System.out.println(this.pos);

        createItems(world, pos, bodyName);

        this.world = world;

        texture = new Texture("Solntse1.png");

        this.unit = unit;
    }

    public void ItemUpdate() {
        if (ItemInteract) if (!Const.freeze && (Gdx.input.isKeyPressed(Input.Keys.E) || GameController.interact))  {
            GameController.interact = false;
            isRemove = true;
            if (unit.player.score <= 112) {
                if (unit.player.score + 10 > 112)
                    unit.player.score += 112 - unit.player.score ;
                else unit.player.score += 10;
            }
            world.destroyBody(body);
        }
    }

   @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, pos.x - 16 * Const.Unit_Scale, pos.y - 16 * Const.Unit_Scale, 32 * Const.Unit_Scale,32 * Const.Unit_Scale);
    }

    private void createItems(World world, Vector2 pos, String bodyName) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        def.position.set(pos);

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(16 * Const.Unit_Scale * Const.SizeX, 16 * Const.Unit_Scale * Const.SizeY);
        Fixture f = body.createFixture(box, 5);
        f.setUserData("ItemInteract");
        f.setSensor(true);
        box.dispose();

        body.setUserData(bodyName);
        //System.out.println(body.getUserData());
    }

}
