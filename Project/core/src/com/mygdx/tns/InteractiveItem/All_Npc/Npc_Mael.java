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
import com.mygdx.tns.MainClass;

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

    private Animation MaelState, MaelStateInteract;

    private Array<TextureRegion> frames;

    private float stateTimer;

    public int dialogPos;

    public Dialog dialog;

    public boolean dialogStarted = false;

    private boolean isSet = true;

    public List<String> Mael_dialog;

    public List<Texture> Mael_dialogImage;

    public Npc_Mael(World world, Vector2 pos){
        dialogPos = 0;

        isSet = true;

        Mael_dialog = new ArrayList<String>();
        Mael_dialog.add("");

        Mael_dialogImage = new ArrayList<Texture>();
        Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));

        dialog = new Dialog(Mael_dialog, Mael_dialogImage, dialogPos);

        this.pos = pos;

        createMael(world, pos, "Mael");

        this.world = world;

        frames = new Array<TextureRegion>();

        for (int i = 0; i < 9; i++)
            frames.add(new TextureRegion(new Texture("Fly.png"), i * 180, 0, 180, 180));
        MaelState = new Animation(0.1f, frames);

        frames.clear();

        for (int i = 0; i < 9; i++)
            frames.add(new TextureRegion(new Texture("FlyInteract.png"), i * 180, 0, 180, 220));
        MaelStateInteract = new Animation(0.1f, frames);
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
        if (!NpcInteract) batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - Const.MaelX / 2 * Const.Unit_Scale, body.getPosition().y - Const.MaelY / 2 * Const.Unit_Scale, Const.MaelX * Const.Unit_Scale, Const.MaelY * Const.Unit_Scale);
        else batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - Const.MaelX / 2 * Const.Unit_Scale, body.getPosition().y - Const.MaelY / 2 * Const.Unit_Scale, Const.MaelX * Const.Unit_Scale, 220 * Const.Unit_Scale);
    }

    private TextureRegion getFrame(float delta) {
        nowFrame = getState();

        TextureRegion region;

        if (!NpcInteract) region = (TextureRegion) MaelState.getKeyFrame(stateTimer, true);
        else region = (TextureRegion) MaelStateInteract.getKeyFrame(stateTimer, true);

        if (!region.isFlipX()) region.flip(true, false);

        if (!Const.freeze)
            stateTimer = nowFrame == previousFrame ? stateTimer + delta : 0;
        previousFrame = nowFrame;

        return region;
    }

    private State getState() {
        return State.STATE;
    }

    public void setDialog(){
        if (!Const.smallForm) Mael_dialog.add("Ты не должен был оказаться здесь быстрее меня.");
        else Mael_dialog.add("Как ты смог оказаться здесь быстрее меня?");
        Mael_dialog.add("Ха-ха-ха");
        Mael_dialog.add("За кого ты меня принимаешь? \n Я являюсь одним из четырёх архангелов");
        if (!Const.smallForm) Mael_dialog.add("Я не позволю тебе навредить леди Элизабет, \n я убью тебя");
        else Mael_dialog.add("Пожалуйста не трогай леди Элизабет.");
        if (!Const.smallForm) Mael_dialog.add("И это говорит тот, кто нагло отобрал \n у меня мою святыню?");
        else Mael_dialog.add("Мне она не к чему, мне нужна лишь моя святыня.");
        if (!Const.smallForm) Mael_dialog.add("Я был избранным, это по праву принадлежит мне.");
        else Mael_dialog.add("Но я был рождён таким, я не хочу расставаться с силой.");
        Mael_dialog.add("В любом случае я хочу её забрать назад, \n но дам тебе один шанс!");
        Mael_dialog.add("Если сможешь победить меня, то сила твоя.");
        Mael_dialog.add("Учти ты уже не сможешь вернуться.");
        Mael_dialog.add("Я отмотаю время до 17:00, \n ведь я хочу ощутить на себе всю силу твоего солнца!");
        Mael_dialog.add("Готов?");
        Mael_dialog.add("");
    }

    public void setDialogImage(){
        if (!Const.smallForm) Mael_dialogImage.add(new Texture("DialogImage\\eskanor_sf_4.png"));
        else Mael_dialogImage.add(new Texture("DialogImage\\eskanor_s_3.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael3.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));
        if (!Const.smallForm) Mael_dialogImage.add(new Texture("DialogImage\\eskanor_sf_4.png"));
        else Mael_dialogImage.add(new Texture("DialogImage\\eskanor_s_3.png"));
        if (!Const.smallForm) Mael_dialogImage.add(new Texture("DialogImage\\mael2.png"));
        else Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));
        if (!Const.smallForm) Mael_dialogImage.add(new Texture("DialogImage\\eskanor_sf_1.png"));
        else Mael_dialogImage.add(new Texture("DialogImage\\eskanor_s_1.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael2.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael3.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael2.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));
        Mael_dialogImage.add(new Texture("DialogImage\\mael1.png"));
    }
}
