package com.mygdx.tns.InteractiveItem.All_Npc;

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
import com.mygdx.tns.Const;
import com.mygdx.tns.Dialog;
import com.mygdx.tns.GameController;

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

    private Animation MerlinState, MerlinStateInteract;

    private Array<TextureRegion> frames;

    private float stateTimer;

    public int dialogPos;

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

        frames.clear();

        for (int i = 0; i < 11; i++)
            frames.add(new TextureRegion(new Texture("merlin_idle_interact.png"), i * 80, 0, 80, 170));
        MerlinStateInteract = new Animation(0.1f, frames);
    }

    public void MerlinUpdate() {
        if (NpcInteract) {
            if (!Const.freeze && (Gdx.input.isKeyJustPressed(Input.Keys.E) || GameController.interact)) {
                GameController.interact = false;
                if (dialogPos < Merlin_dialog.size()){
                    dialogStarted = true;
                    dialogPos++;
                }
                if (dialogPos == Merlin_dialog.size()) dialogStarted = false;
                dialog.nextDialog();
            }
        }
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!NpcInteract) batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - Const.playerX / 2 * Const.Unit_Scale, body.getPosition().y - Const.playerY / 2 * Const.Unit_Scale, 80 * Const.Unit_Scale, 130 * Const.Unit_Scale);
        else batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - Const.playerX / 2 * Const.Unit_Scale, body.getPosition().y - Const.playerY / 2 * Const.Unit_Scale, 80 * Const.Unit_Scale, 170 * Const.Unit_Scale);
    }

    private TextureRegion getFrame(float delta) {

        nowFrame = getState();

        TextureRegion region;

        if (!NpcInteract) region = (TextureRegion) MerlinState.getKeyFrame(stateTimer, true);
        else region = (TextureRegion) MerlinStateInteract.getKeyFrame(stateTimer, true);

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
        Merlin_dialog.add("Наконец-то ты добрался до сюда.");
        Merlin_dialog.add("Сколько у меня осталось времени?");
        Merlin_dialog.add("До заката солнца остался час, после заката у тебя \n будет ещё час, чтобы добраться до Элизабет.");
        Merlin_dialog.add("Где сейчас Элизабет?");
        Merlin_dialog.add("Тебе нужно пройти через лес и поднятся на \n небесный остров.");
        Merlin_dialog.add("Хорошо не буду терять времени.");
        Merlin_dialog.add("Будь осторожен и не забудь, \nчто в тебе скрыта великая сила, убивая врагов, \nты накапливаешь силу с них.");
        Merlin_dialog.add("Эту силу ты можешь потратить на лечение своих ран, \nесли ты зажмёшь кнопку \"Interact\" на 3 секунды");
        Merlin_dialog.add("Или на высвобождение этой силы против врагов, \nесли ты зажмёшь кнопку \"Splash\" на 1.5 секунды");
    }

    private void setDialogImage() {
        Merlin_dialogImage = new ArrayList<Texture>();
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin1.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\eskanor_sf_1.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\eskanor_sf_4.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\eskanor_sf_1.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin2.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin2.png"));
        Merlin_dialogImage.add(new Texture("DialogImage\\merlin2.png"));
    }
}
