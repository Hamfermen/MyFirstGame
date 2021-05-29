package com.mygdx.game.InteractiveItem.All_Npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Const;
import com.mygdx.game.Dialog;
import com.mygdx.game.MyPreference;

import java.util.ArrayList;
import java.util.List;

public class Npc_Merlin extends Npc {
    public enum State {STATE}

    private State nowFrame;
    private State previousFrame;


    private Vector2 pos;

    public Body body;

    public boolean NpcInteract = false;

    private World world;

    private Animation MerlinState;

    private Array<TextureRegion> frames;

    private float stateTimer;

    private int dialogPos;

    public Dialog dialog;

    public boolean dialogStarted = false;

    private List<String> Merlin_dialog;

    private List<Texture> Merlin_dialogImage;

    public Npc_Merlin(World world, Vector2 pos) {
        dialogPos = DialogPos;

        setDialog();

        setDialogImage();

        dialog = new Dialog(Merlin_dialog, Merlin_dialogImage, dialogPos);

        this.pos = pos;

        createMerlin(world, pos, "Merlin");

        this.world = world;

        frames = new Array<TextureRegion>();

        for (int i = 0; i < 11; i++)
            frames.add(new TextureRegion(new Texture("Merlin_Idle.png"), i * 80, 0, 80, 130));
        MerlinState = new Animation(0.1f, frames);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - Const.playerX / 2 * Const.Unit_Scale, body.getPosition().y - Const.playerY / 2 * Const.Unit_Scale, 80 * Const.Unit_Scale, 130 * Const.Unit_Scale);
    }

    public void MerlinUpdate() {
        if (NpcInteract) {
            if (!Const.freeze && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                if (dialogPos <= DialogPos) {
                    dialogStarted = true;
                    dialogPos++;
                }
                if (dialogPos == Merlin_dialog.size()) dialogStarted = false;
                dialog.nextDialog();
            }
        } else dialogStarted = false;
    }

    private void createMerlin(World world, Vector2 pos, String bodyName) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        def.position.set(pos);

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(Const.playerX / 2 * Const.Unit_Scale, Const.playerY / 2 * Const.Unit_Scale);
        Fixture f = body.createFixture(box, 5);
        f.setUserData("NpcInteract");
        f.setSensor(true);
        box.dispose();

        body.setUserData(bodyName);
    }

    private TextureRegion getFrame(float delta) {

        nowFrame = getState();

        TextureRegion region;

        region = (TextureRegion) MerlinState.getKeyFrame(stateTimer, true);

        if (!Const.freeze)
            stateTimer = nowFrame == previousFrame ? stateTimer + delta : 0;
        previousFrame = nowFrame;

        return region;
    }

    private State getState() {
        return State.STATE;
    }

    private void setDialog() {
        Merlin_dialog = new ArrayList<String>();
        Merlin_dialog.add("");
        Merlin_dialog.add("Hello");
        Merlin_dialog.add("I`m Merlin");
    }

    private void setDialogImage() {
        Merlin_dialogImage = new ArrayList<Texture>();
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin1.png"));
    }
}
