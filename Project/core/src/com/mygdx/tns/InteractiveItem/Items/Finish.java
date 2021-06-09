package com.mygdx.tns.InteractiveItem.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.tns.Const;
import com.mygdx.tns.GameController;
import com.mygdx.tns.MainClass;
import com.mygdx.tns.MyPreference;

public class Finish extends InteractiveItem {
    private Vector2 pos;

    public Body body;

    public boolean FinishInteract = false;

    private World world;

    private MainClass mainClass;

    public Finish(World world, Vector2 pos, String bodyName, MainClass mainClass) {
        this.pos = pos;

        createItems(world, pos, bodyName);

        this.mainClass = mainClass;

        this.world = world;
    }

    public void FinishUpdate() {
        if (FinishInteract)
            if (!Const.freeze && (Gdx.input.isKeyPressed(Input.Keys.E) || GameController.interact)) {
                if (!(MyPreference.getLevel_number() + 1 == Const.levels.size()))
                    MyPreference.setLevel_number(MyPreference.getLevel_number() + 1);
                /*GameController.interact = false;
                MyPreference.pref.clear();
                MyPreference.setNewgame(true);*/
                Const.newLevel = true;
                //Const.newGame = true;
                mainClass.ChangeScreen(mainClass.level_system.loadingScreen);
            }
    }

    private void createItems(World world, Vector2 pos, String bodyName) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        def.position.set(pos);

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(60 * Const.Unit_Scale, 60 * Const.Unit_Scale);
        Fixture f = body.createFixture(box, 5);
        f.setUserData("ItemInteract");
        f.setSensor(true);
        box.dispose();

        body.setUserData(bodyName);
        //System.out.println(body.getUserData());
    }
}
