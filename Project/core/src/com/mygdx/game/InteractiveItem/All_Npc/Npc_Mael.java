package com.mygdx.game.InteractiveItem.All_Npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
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
import com.mygdx.game.GameController;
import com.mygdx.game.MyPreference;

import java.util.ArrayList;
import java.util.List;

public class Npc_Mael extends Npc{
    public enum State {STATE}

    private Vector2 pos;

    public Body body;

    public boolean NpcInteract = false;

    private World world;

    public String nowDialog;

    public Texture nowDialogImage;

    private State nowFrame;
    private State previousFrame;

    private Animation MaelState;

    private Array<TextureRegion> frames;

    private float stateTimer;

    private int dialogPos;

    public Dialog dialog;

    public boolean dialogStarted = false;

    private List<String> Mael_dialog;

    private List<Texture> Mael_dialogImage;

    public Npc_Mael(World world, Vector2 pos){
        dialogPos = DialogPos;

        setDialog();

        setDialogImage();

        dialog = new Dialog(Mael_dialog, Mael_dialogImage, dialogPos);

        this.pos = pos;

        createMael(world, pos, "Mael");

        this.world = world;

        frames = new Array<TextureRegion>();

        for (int i = 0; i < 11; i++)
            frames.add(new TextureRegion(new Texture("Mael_Idle.png"), i * 180, 0, 180, 140));
        MaelState = new Animation(0.1f, frames);
    }

    public void MaelUpdate() {
        if (NpcInteract) {
            if (!Const.freeze && (Gdx.input.isKeyJustPressed(Input.Keys.E) || GameController.interact)) {
                GameController.interact = false;
                if (dialogPos < Mael_dialog.size()){
                    dialogStarted = true;
                    dialogPos++;
                }
                if (dialogPos == Mael_dialog.size()) dialogStarted = false;
                dialog.nextDialog();
            }
        }
    }

    private void createMael(World world, Vector2 pos, String bodyName) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        def.position.set(pos);

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(Const.MaelX / 2 * Const.Unit_Scale, Const.MaelY / 2 * Const.Unit_Scale);
        Fixture f = body.createFixture(box, 5);
        f.setUserData("NpcInteract");
        f.setSensor(true);
        box.dispose();

        body.setUserData(bodyName);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - Const.MaelX / 2 * Const.Unit_Scale, body.getPosition().y - Const.MaelY / 2 * Const.Unit_Scale, Const.MaelX * Const.Unit_Scale, Const.MaelY * Const.Unit_Scale);
    }

    private TextureRegion getFrame(float delta) {
        nowFrame = getState();

        TextureRegion region;

        region = (TextureRegion) MaelState.getKeyFrame(stateTimer, true);

        if (!Const.freeze)
            stateTimer = nowFrame == previousFrame ? stateTimer + delta : 0;
        previousFrame = nowFrame;

        return region;
    }

    private State getState() {
        return State.STATE;
    }

    private void setDialog(){
        Mael_dialog = new ArrayList<String>();
        Mael_dialog.add("");
        Mael_dialog.add("Привет");
        Mael_dialog.add("Я Маель");
    }

    private void setDialogImage(){
        Mael_dialogImage = new ArrayList<Texture>();
        Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael3.png"));
    }
}
