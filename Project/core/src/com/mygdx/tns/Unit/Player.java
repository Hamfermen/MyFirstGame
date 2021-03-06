package com.mygdx.tns.Unit;

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
import com.badlogic.gdx.utils.Timer;
import com.mygdx.tns.Const;
import com.mygdx.tns.GameController;

import java.util.ArrayList;
import java.util.List;

public class Player extends Unit {
    public enum State {STATE, RUN, JUMP, FALL, ATTACK}

    private State nowFrame;
    private State previousFrame;

    private Animation playerState;
    private Animation playerRun;
    private Animation playerHit;
    private Animation playerJump;
    private Animation playerFall;
    private Animation playerAttack;
    private Animation splashAnimR, splashAnimL;

    private float stateTimer;

    public boolean runningRight = true;

    public boolean isPlayerAttack = false;
    public boolean isPlayerDead = false;

    public boolean isAnimationAttack = false;

    public Body body;

    public List<Body> splashes;

    public int health;

    //private Vector2 position, size;

    public boolean isGrounded = true;

    public boolean canAttack = false;

    public float score;

    public boolean smallForm = false;
    private boolean isChanged = false;

    private Timer timerHeal, timerSuperAttack, timerCooldown;
    private Timer.Task taskHeal, taskSuperAttack, taskCooldown;

    private boolean heal = false;
    private boolean isHealed = true;

    private boolean superAttack = false;
    private boolean isSuperAttacked = true;

    public boolean isHitBoxR = true;

    public Body hitBoxB;

    private World world;

    private float splashST;

    public Player(float x, float y, Vector2 pos, World world) {
        splashes = new ArrayList<>();

        splashST = 0;

        makeFrames();

        this.world = world;

        CreateBody(x, y, pos);

        this.setBounds(pos.x - x / 2, pos.y - y / 2, x, y);

        timerHeal = new Timer();

        taskHeal = new Timer.Task() {
            @Override
            public void run() {
                heal = true;
                taskHeal.cancel();
            }
        };

        timerSuperAttack = new Timer();

        taskSuperAttack = new Timer.Task() {
            @Override
            public void run() {
                superAttack = true;
                if (!taskCooldown.isScheduled()) timerCooldown.scheduleTask(taskCooldown, 4, 0);
                taskSuperAttack.cancel();
            }
        };

        timerCooldown = new Timer();

        taskCooldown = new Timer.Task() {
            @Override
            public void run() {
                GameController.isCooldown = false;
                GameController.stateTimer = 0;
                taskCooldown.cancel();
            }
        };

        //position = new Vector2(pos.x - x / 2, pos.y - y / 2);
        //size = new Vector2(x, y);

        health = 1;

        isAnimationAttack = false;
        isPlayerAttack = false;
        isPlayerDead = false;
    }

    public void update(float delta) {
        this.setPosition(body.getPosition().x - Const.playerX / 2 * Const.Unit_Scale, body.getPosition().y - Const.playerY / 2 * Const.Unit_Scale);
        //position = new Vector2(body.getPosition().x - Const.playerX / 2 * Const.Unit_Scale, body.getPosition().y - Const.playerY / 2 * Const.Unit_Scale);
        if (Gdx.input.isKeyPressed(Input.Keys.A) || GameController.left) Move(-1 * delta);
        else if (Gdx.input.isKeyPressed(Input.Keys.D) || GameController.right) Move(1 * delta);
        else {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }

        if (GameController.attack && hitBoxB == null) createHitBox();
        else if ((hitBoxB != null && !GameController.attack) || (GameController.attack && isHitBoxR != runningRight)) {
            world.destroyBody(hitBoxB);
            hitBoxB = null;
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || GameController.up) && isGrounded) Jump();
        else if (/*(Gdx.input.isButtonPressed(Input.Buttons.LEFT) ||*/ GameController.attack && !smallForm) isAnimationAttack = true;
        else isAnimationAttack = false;

        if (canAttack && !smallForm) if (GameController.attack && !smallForm) isPlayerAttack = true;
        else isPlayerAttack = false;

        if (isChanged != smallForm) {
            isChanged = smallForm;
            makeFrames();
        }

        if (health <= 0) {
            isPlayerDead = true;
            Const.freeze = true;
        }

        if (hitBoxB != null) hitBoxB.setTransform(body.getPosition().x, body.getPosition().y, 0);

        Heal();

        SuperAttack();

        if (!splashes.isEmpty()){
            for (int i = 0; i < splashes.size(); i++){
                if (splashes.get(i).getFixtureList().isEmpty()) splashes.remove(i);
            }
        }

    }

    private void Move(float direction) {
        body.setLinearVelocity(new Vector2(direction * 200, body.getLinearVelocity().y));
    }

    private void Jump() {
        isGrounded = false;
        body.applyLinearImpulse(new Vector2(0, 10), new Vector2(0, 0), true);
        if (hitBoxB != null) hitBoxB.applyLinearImpulse(new Vector2(0, 10), new Vector2(0, 0), true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float direction = 1 * Const.SizeX;
        if (!runningRight) direction = 20 * Const.SizeX;
        else direction = 0;
        if (!smallForm) {
            if (getState() != State.RUN && getState() != State.ATTACK)
                batch.draw(getFrame(Gdx.graphics.getDeltaTime()), this.getX() - (10 + direction) * Const.Unit_Scale, this.getY() - 2 * Const.Unit_Scale, 120 * Const.Unit_Scale, 160 * Const.Unit_Scale);
            else if (getState() != State.ATTACK)
                batch.draw(getFrame(Gdx.graphics.getDeltaTime()), this.getX() - 100 * Const.Unit_Scale, this.getY() - 115 * Const.Unit_Scale, 240 * Const.Unit_Scale, 320 * Const.Unit_Scale);
            else if (getState() == State.ATTACK)
                batch.draw(getFrame(Gdx.graphics.getDeltaTime()), this.getX() - 100 * Const.Unit_Scale, this.getY(), 300 * Const.Unit_Scale, 200 * Const.Unit_Scale);
        }else {
            if (getState() == State.RUN) {
                batch.draw(getFrame(Gdx.graphics.getDeltaTime()), this.getX(), this.getY(), 100 * Const.Unit_Scale, 120 * Const.Unit_Scale);
            }else {
                batch.draw(getFrame(Gdx.graphics.getDeltaTime()), this.getX(), this.getY(),  60 * Const.Unit_Scale, 120 * Const.Unit_Scale);
            }
        }
        if (!splashes.isEmpty()) {
            splashST += Gdx.graphics.getDeltaTime();
            for (int i = 0; i < splashes.size(); i++)
                batch.draw(splashes.get(i).getLinearVelocity().x > 0 ? (TextureRegion) splashAnimR.getKeyFrame(splashST, true) : (TextureRegion) splashAnimL.getKeyFrame(splashST, true), splashes.get(i).getPosition().x - 80 * Const.Unit_Scale, splashes.get(i).getPosition().y - 80 * Const.Unit_Scale, 160 * Const.Unit_Scale, 160 * Const.Unit_Scale);
        }
    }

    private void Heal(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.E) || !GameController.interact) heal = false;

        if ((Gdx.input.isKeyPressed(Input.Keys.E) || GameController.interact) && score >= 20){
            if (!taskHeal.isScheduled()) {
                timerHeal.scheduleTask(taskHeal, 3, 0);
            }
            if (heal && isHealed && health < 5){
                heal = false;
                isHealed = false;
                score -= 20;
                health++;
            }
        }else{
            isHealed = true;
            taskHeal.cancel();
            timerHeal.clear();
        }
    }

    private void SuperAttack(){
        if (!GameController.superAttack) superAttack = false;

        if ((GameController.superAttack) && score >= 40 && !GameController.isCooldown ){
            if (!taskSuperAttack.isScheduled()) {
                timerSuperAttack.scheduleTask(taskSuperAttack, 0.75f, 0);
            }
            if (superAttack && isSuperAttacked){
                GameController.isCooldown = true;
                superAttack = false;
                isSuperAttacked = false;
                score -= 40;
                createSlash();
            }
        }else{
            isSuperAttacked = true;
            timerSuperAttack.clear();
            taskSuperAttack.cancel();
        }
    }

    private void CreateBody(float x, float y, Vector2 pos) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(pos);

        body = world.createBody(def);

        PolygonShape box = new PolygonShape();
        box.setAsBox(x / 2, y / 2);
        Fixture f = body.createFixture(box, 5);
        f.setUserData("Player");

        /*Filter filter = new Filter();
        filter.maskBits = Const.MASK_PLAYER;
        filter.categoryBits = Const.CATEGORY_PLAYER;
        f.setFilterData(filter);*/

        box.dispose();

        PolygonShape legs = new PolygonShape();
        legs.setAsBox((Const.playerX / 2 - 1) * Const.Unit_Scale, 0.5f * Const.Unit_Scale, new Vector2(0, (Const.playerY / 2 * -1 - 1) * Const.Unit_Scale), 0);
        Fixture leg = body.createFixture(legs, 0);
        leg.setUserData("leg");
        leg.setSensor(true);

        /*PolygonShape f_attackArea = new PolygonShape();
        f_attackArea.setAsBox((Const.playerX + 20) / 2 * Const.Unit_Scale, Const.playerY / 2 * Const.Unit_Scale, new Vector2((Const.playerX + 10) * Const.Unit_Scale, 0), 0);
        Fixture r_attackArea = body.createFixture(f_attackArea, 0);
        r_attackArea.setUserData("right");
        r_attackArea.setSensor(true);

        f_attackArea.setAsBox((Const.playerX + 20) / 2 * Const.Unit_Scale, Const.playerY / 2 * Const.Unit_Scale, new Vector2((-Const.playerX - 10) * Const.Unit_Scale, 0), 0);
        Fixture l_attackArea = body.createFixture(f_attackArea, 0);
        l_attackArea.setUserData("left");
        l_attackArea.setSensor(true);
        f_attackArea.dispose();*/

        body.setUserData("Player");

    }

    public void createHitBox(){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(body.getPosition().x, body.getPosition().y);

        hitBoxB = world.createBody(def);

        hitBoxB.setGravityScale(0);

        isHitBoxR = runningRight;

        PolygonShape f_attackArea = new PolygonShape();
        f_attackArea.setAsBox((Const.playerX + 20) * Const.Unit_Scale, Const.playerY / 2 * Const.Unit_Scale, new Vector2((runningRight ? (Const.playerX / 2 + 20) : -(Const.playerX / 2 + 20)) * Const.Unit_Scale, 0), 0);
        Fixture hitBox = hitBoxB.createFixture(f_attackArea, 0);
        hitBox.setUserData("hitbox");
        hitBox.setSensor(true);
        f_attackArea.dispose();
    }

    private void createSlash(){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(body.getPosition().x + 92 * Const.Unit_Scale, body.getPosition().y + 16 * Const.Unit_Scale);

        Body splash = world.createBody(def);

        splash.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(80 * Const.Unit_Scale,  80 * Const.Unit_Scale);
        Fixture f = splash.createFixture(box, 5);
        f.setUserData("Splash");

        f.setSensor(true);

        splash.setLinearVelocity(runningRight ? 300f * Gdx.graphics.getDeltaTime() : -300f * Gdx.graphics.getDeltaTime(), 0);

        splashes.add(splash);
    }

    private void makeFrames() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        if (!smallForm) {
            for (int i = 0; i < 57; i++)
                frames.add(new TextureRegion(new Texture("Attack/Attack" + Integer.toString(i + 1) + ".png"), 0, 0, 300, 200));
            playerAttack = new Animation(0.03f, frames);
            frames.clear();

            for (int i = 0; i < 10; i++)
                frames.add(new TextureRegion(new Texture("Run3.png"), i * 240, 0, 240, 320));
            playerRun = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i < 10; i++)
                frames.add(new TextureRegion(new Texture("Idle.png"), i * 120, 0, 120, 160));
            playerState = new Animation(0.1f, frames);
            frames.clear();

        /*for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(new Texture("Hit (32x32).png"), i * 32, 0, 32, 32));
        playerHit = new Animation(0.05f, frames);
        frames.clear();*/

            for (int i = 0; i < 9; i++)
                frames.add(new TextureRegion(new Texture("Jump.png"), i * 120, 0, 120, 160));
            playerJump = new Animation(0.03f, frames);
            frames.clear();

            for (int i = 0; i < 2; i++)
                frames.add(new TextureRegion(new Texture("Fall.png"), i * 130, 0, 130, 160));
            playerFall = new Animation(0.05f, frames);
            frames.clear();
        }else {
            for (int i = 0; i < 9; i++)
                frames.add(new TextureRegion(new Texture("run_eskanor.png"), i * 100, 0, 100, 120));
            playerRun = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i < 6; i++)
                frames.add(new TextureRegion(new Texture("jump_eskanor.png"), i * 60, 0, 60, 120));
            playerJump = new Animation(0.03f, frames);
            frames.clear();

            for (int i = 0; i < 6; i++)
                frames.add(new TextureRegion(new Texture("fall_eskanor.png"), i * 60, 0, 60, 120));
            playerFall = new Animation(0.05f, frames);
            frames.clear();

            for (int i = 0; i < 5; i++)
                frames.add(new TextureRegion(new Texture("shivering_eskanor.png"), i * 60, 0, 60, 120));
            playerState = new Animation(0.1f, frames);
            frames.clear();
        }
        for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(new Texture("splash.png"), i * 160, 0, 160, 160));
        splashAnimR = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(new Texture("splash.png"), i * 160, 0, 160, 160));
            frames.get(i).flip(true, false);
        }
        splashAnimL = new Animation(0.1f, frames);
        frames.clear();
    }

    private TextureRegion getFrame(float delta) {
        nowFrame = getState();

        TextureRegion region;
        switch (nowFrame) {
            case RUN:
                region = (TextureRegion) playerRun.getKeyFrame(stateTimer, true);
                break;
            case JUMP:
                region = (TextureRegion) playerJump.getKeyFrame(stateTimer, false);
                break;
            case FALL:
                region = (TextureRegion) playerFall.getKeyFrame(stateTimer, false);
                break;
            case ATTACK:
                region = (TextureRegion) playerAttack.getKeyFrame(stateTimer, true);
                break;
            default:
                region = (TextureRegion) playerState.getKeyFrame(stateTimer, true);
                break;

        }
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        if (!Const.freeze)
            stateTimer = nowFrame == previousFrame ? stateTimer + delta : 0;
        previousFrame = nowFrame;
        return region;
    }

    private State getState() {
        if (isAnimationAttack)
            return State.ATTACK;
        else if (body.getLinearVelocity().y > 0 && !Const.hit)
            return State.JUMP;
        else if (body.getLinearVelocity().y < 0)
            return State.FALL;
        else if (body.getLinearVelocity().x != 0)
            return State.RUN;
        else
            return State.STATE;

    }

}
