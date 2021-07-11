package com.mygdx.tns.Unit.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.tns.Const;
import com.mygdx.tns.Unit.Player;
import com.mygdx.tns.WorldContact;

import java.util.ArrayList;
import java.util.List;

import sun.jvm.hotspot.debugger.win32.coff.COFFException;

public class Boss extends Actor {

    private enum State{FIA, SA, TA, FOA, FLY, FB}

    private Animation firstAttackA;
    private Animation secondAttackA;
    private Animation thirdAttackA;
    private Animation fourthAttackA;
    private Animation fireBallA;
    private Animation flyA;

    private boolean isFirstAttackA = false;
    private boolean isSecondAttackA = false;
    private boolean isThirdAttackA = false;
    private boolean isFourthAttackA = false;
    private boolean isFireBall = false;

    private Timer firstAttackT, secondAttackT, thirdAttackT, fourthAttackT, destroyT, fireBallT;
    private Timer.Task taskFirstAttackT, taskSecondAttackT, taskThirdAttackT, taskFourthAttackT, taskDestroyT, taskFireBallT;

    private TextureRegion ray;

    private Sprite test;

    private World world;

    private Body body, ray_b, fireBall, fastFireball, megaFireBall;
    private Player player;

    private TiledMap tiledMap;

    private List<Vector2> posFirstA, posSecondA;

    private float stateTimer, fireBallTimer;
    private State nowFrame;
    private State previousFrame;

    public boolean runningRight = true;

    private int nowAttack = 0, pos = 0;
    public int health = 600;
    public boolean getDamage = false;

    private RandomXS128 random;

    public Boss(Player player, TiledMap tiledMap, World world){
        this.player = player;
        this.tiledMap = tiledMap;

        /*posFirstA = findPositions(tiledMap, Const.TiledMap_Scale, "posFirstA");
        posSecondA = findPositions(tiledMap, Const.TiledMap_Scale, "posSecondA");*/

        posFirstA = new ArrayList<>();

        posFirstA.add(new Vector2(200 * Const.Unit_Scale, 313.2f * Const.Unit_Scale));
        posFirstA.add(new Vector2(1080 * Const.Unit_Scale, 313.2f * Const.Unit_Scale));

        posSecondA = new ArrayList<>();

        posSecondA.add(new Vector2(200 * Const.Unit_Scale, 500 * Const.Unit_Scale));
        posSecondA.add(new Vector2(1080 * Const.Unit_Scale, 500 * Const.Unit_Scale));

        this.world = world;

        random = new RandomXS128();

        test = new Sprite(new Texture("Play.png"));

        ray = new TextureRegion(new Texture("boss\\ray.png"), 0, 0, 800, 40);

        createBoss();

        makeFrames();

        createTimers();

    }

    private void createBoss() {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(200 * Const.Unit_Scale, 310 * Const.Unit_Scale);

        body = world.createBody(def);

        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(110 * Const.Unit_Scale, 70 * Const.Unit_Scale);
        Fixture f = body.createFixture(box, 5);
        f.setUserData("Boss");

        f.setSensor(true);

        box.dispose();

        body.setUserData("Boss");

    }

    public void createFireBall(){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(body.getPosition().x < 640  * Const.Unit_Scale ? body.getPosition().x + 20 * Const.Unit_Scale : body.getPosition().x - 20 * Const.Unit_Scale, body.getPosition().y + 110 * Const.Unit_Scale);

        fireBall = world.createBody(def);

        fireBall.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(25 * Const.Unit_Scale,  25 * Const.Unit_Scale);
        Fixture f = fireBall.createFixture(box, 5);
        f.setUserData("fireBall");

        f.setSensor(true);

        fireBall.setLinearVelocity((player.body.getPosition().x - fireBall.getPosition().x) * 1.5f, (player.body.getPosition().y - fireBall.getPosition().y) * 1.5f);
    }

    public void createMegaFireBall(){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(640 * Const.Unit_Scale, 720 * Const.Unit_Scale);

        megaFireBall = world.createBody(def);

        megaFireBall.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(160 * Const.Unit_Scale,  160 * Const.Unit_Scale);
        Fixture f = megaFireBall.createFixture(box, 5);
        f.setUserData("fireBall");

        f.setSensor(true);

        megaFireBall.setLinearVelocity(0, -8f);
    }

    public void createFastFireBall(){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(body.getPosition().x < 640  * Const.Unit_Scale ? body.getPosition().x + 135 * Const.Unit_Scale : body.getPosition().x - 135 * Const.Unit_Scale,  body.getPosition().y + 25 * Const.Unit_Scale);

        fastFireball = world.createBody(def);

        fastFireball.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(25 * Const.Unit_Scale,  25 * Const.Unit_Scale);
        Fixture f = fastFireball.createFixture(box, 5);
        f.setUserData("fireBall");

        f.setSensor(true);

        fastFireball.setLinearVelocity(body.getPosition().x < 640  * Const.Unit_Scale ? 10f : -10f, 0);
    }

    public void createRay(){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(body.getPosition().x < 640 * Const.Unit_Scale ? body.getPosition().x + 510 * Const.Unit_Scale : body.getPosition().x - 510 * Const.Unit_Scale,  body.getPosition().y + 25 * Const.Unit_Scale);

        ray_b = world.createBody(def);

        ray_b.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(400 * Const.Unit_Scale,  20 * Const.Unit_Scale);
        Fixture f = ray_b.createFixture(box, 5);
        f.setUserData("ray");

        f.setSensor(true);
    }

    public void nextAttack(){
        int nextAttack;
        do {
            nextAttack = random.nextInt(4) + 1;
        }while (nextAttack % 2 == nowAttack % 2);

        nowAttack = nextAttack;

        pos = random.nextInt(2);

        switch (nextAttack){
            case 1:
                body.setLinearVelocity((posFirstA.get(pos).x - body.getPosition().x) * 1.5f, (posFirstA.get(pos).y - body.getPosition().y) * 1.5f);
                break;
            case 2:
                body.setLinearVelocity((posSecondA.get(pos).x - body.getPosition().x) * 1.5f, (posSecondA.get(pos).y - body.getPosition().y) * 1.5f);
                break;
            case 3:
                body.setLinearVelocity((posFirstA.get(pos).x - body.getPosition().x) * 1.5f, (posFirstA.get(pos).y - body.getPosition().y) * 1.5f);
                break;
            case 4:
                body.setLinearVelocity((posSecondA.get(pos).x - body.getPosition().x) * 1.5f, (posSecondA.get(pos).y - body.getPosition().y) * 1.5f);
                break;
        }
    }

    public void firstAttack(){
        nowAttack = 1;
        stateTimer = 0;
        isFirstAttackA = true;
        firstAttackT.scheduleTask(taskFirstAttackT, 0.5f);
    }

    private void secondAttack(){
        nowAttack = 2;
        isSecondAttackA = true;
        fireBallT.scheduleTask(taskFireBallT, 0.4f);
        secondAttackT.scheduleTask(taskSecondAttackT, 0.7f);
    }

    private void thirdAttack(){
        nowAttack = 3;
        isThirdAttackA = true;
        fireBallT.scheduleTask(taskFireBallT, 0.25f);
        thirdAttackT.scheduleTask(taskThirdAttackT, 0.55f);
    }

    private void fouthAttack(){
        nowAttack = 4;
        isFourthAttackA = true;
        fireBallT.scheduleTask(taskFireBallT, 0.45f);
        fourthAttackT.scheduleTask(taskFourthAttackT, 0.75f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0 || nowAttack == 0) batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - 110 * Const.Unit_Scale, body.getPosition().y - 70 * Const.Unit_Scale, 180 * Const.Unit_Scale, 180 * Const.Unit_Scale);
        else {
            switch (nowAttack) {
                case 1:
                    if (ray_b != null) {
                        if (body.getPosition().x > 640  * Const.Unit_Scale && !ray.isFlipX()) ray.flip(true, false);
                        else if (body.getPosition().x < 640  * Const.Unit_Scale && ray.isFlipX()) ray.flip(true, false);
                        batch.draw(ray, body.getPosition().x < 640  * Const.Unit_Scale ? body.getPosition().x + 100 * Const.Unit_Scale : body.getPosition().x - 900 * Const.Unit_Scale, body.getPosition().y + 8f * Const.Unit_Scale, 800 * Const.Unit_Scale, 40 * Const.Unit_Scale);
                    }
                    batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - 110 * Const.Unit_Scale, body.getPosition().y - 70 * Const.Unit_Scale, 220 * Const.Unit_Scale, 150 * Const.Unit_Scale);
                    break;
                case 2:
                    batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - 110 * Const.Unit_Scale, body.getPosition().y - 70 * Const.Unit_Scale, 200 * Const.Unit_Scale, 170 * Const.Unit_Scale);
                    if (fireBall != null){
                        batch.draw((TextureRegion) fireBallA.getKeyFrame(fireBallTimer, false), fireBall.getPosition().x - 25 * Const.Unit_Scale, fireBall.getPosition().y - 25 * Const.Unit_Scale, 50 * Const.Unit_Scale, 50 * Const.Unit_Scale);
                        fireBallTimer += Gdx.graphics.getDeltaTime();
                    }
                    else if (isFireBall) {
                        batch.draw((TextureRegion) fireBallA.getKeyFrame(fireBallTimer, false), body.getPosition().x < 640  * Const.Unit_Scale ? body.getPosition().x - 5 * Const.Unit_Scale : body.getPosition().x - 60 * Const.Unit_Scale, body.getPosition().y + 110 * Const.Unit_Scale, 50 * Const.Unit_Scale, 50 * Const.Unit_Scale);
                        fireBallTimer += Gdx.graphics.getDeltaTime();
                    }
                    break;
                case 3:
                    batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - 110 * Const.Unit_Scale, body.getPosition().y - 70 * Const.Unit_Scale, 220 * Const.Unit_Scale, 150 * Const.Unit_Scale);
                    if (fastFireball != null){
                        batch.draw((TextureRegion) fireBallA.getKeyFrame(fireBallTimer, false), fastFireball.getPosition().x - 25 * Const.Unit_Scale, fastFireball.getPosition().y - 25 * Const.Unit_Scale, 50 * Const.Unit_Scale, 50 * Const.Unit_Scale);
                        fireBallTimer += Gdx.graphics.getDeltaTime();
                    }
                    else if (isFireBall) {
                        batch.draw((TextureRegion) fireBallA.getKeyFrame(fireBallTimer, false), body.getPosition().x < 640  * Const.Unit_Scale ? body.getPosition().x + 160 * Const.Unit_Scale : body.getPosition().x - 160 * Const.Unit_Scale,  body.getPosition().y, 50 * Const.Unit_Scale, 50 * Const.Unit_Scale);
                        fireBallTimer += Gdx.graphics.getDeltaTime();
                    }
                    break;
                case 4:
                    if (megaFireBall != null){
                        batch.draw((TextureRegion) fireBallA.getKeyFrame(fireBallTimer, false), megaFireBall.getPosition().x - 160 * Const.Unit_Scale, megaFireBall.getPosition().y - 160 * Const.Unit_Scale, 320 * Const.Unit_Scale, 320 * Const.Unit_Scale);
                        fireBallTimer += Gdx.graphics.getDeltaTime();
                    }
                    else if (isFireBall) {
                        batch.draw((TextureRegion) fireBallA.getKeyFrame(fireBallTimer, false), 480 * Const.Unit_Scale, 560 * Const.Unit_Scale, 320 * Const.Unit_Scale, 320 * Const.Unit_Scale);
                        fireBallTimer += Gdx.graphics.getDeltaTime();
                    }
                    batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - 110 * Const.Unit_Scale, body.getPosition().y - 70 * Const.Unit_Scale, 200 * Const.Unit_Scale, 170 * Const.Unit_Scale);
            }
        }

    }

    public void bossUpdate(){
        if (getDamage) {
            health--;
            if (player.score < 112) player.score += 0.2f;
        }
        if (body.getLinearVelocity().x != 0 || body.getLinearVelocity().y != 0) {
            switch (nowAttack) {
                case 1:
                    if (body.getPosition().x >= posFirstA.get(pos).x && body.getPosition().y <= posFirstA.get(pos).y || body.getPosition().x <= posFirstA.get(pos).x && body.getPosition().y <= posFirstA.get(pos).y) {
                        stateTimer = 0;
                        isFirstAttackA = true;
                        body.setLinearVelocity(0, 0);
                        firstAttack();
                    }
                    break;
                case 2:
                    if (body.getPosition().x >= posSecondA.get(pos).x && body.getPosition().y >= posSecondA.get(pos).y || body.getPosition().x <= posSecondA.get(pos).x && body.getPosition().y >= posSecondA.get(pos).y) {
                        stateTimer = 0;
                        isSecondAttackA = true;
                        body.setLinearVelocity(0, 0);
                        secondAttack();
                    }
                    break;
                case 3:
                    if (body.getPosition().x >= posFirstA.get(pos).x && body.getPosition().y <= posFirstA.get(pos).y || body.getPosition().x <= posFirstA.get(pos).x && body.getPosition().y <= posFirstA.get(pos).y) {
                        stateTimer = 0;
                        isThirdAttackA = true;
                        body.setLinearVelocity(0, 0);
                        thirdAttack();
                    }
                    break;
                case 4:
                    if (body.getPosition().x >= posSecondA.get(pos).x && body.getPosition().y >= posSecondA.get(pos).y || body.getPosition().x <= posSecondA.get(pos).x && body.getPosition().y >= posSecondA.get(pos).y) {
                        stateTimer = 0;
                        isFourthAttackA = true;
                        body.setLinearVelocity(0, 0);
                        fouthAttack();
                    }
                    break;
            }
        }
    }

    public void destroyAll(){
        if (ray_b != null) {
            world.destroyBody(ray_b);
            ray_b = null;
            isFirstAttackA = false;
            stateTimer = 0;
        }
        if (fireBall != null){
            world.destroyBody(fireBall);
            fireBall = null;
            isSecondAttackA = false;
            isFireBall = false;
            stateTimer = 0;
        }
        if (fastFireball != null){
            world.destroyBody(fastFireball);
            fastFireball = null;
            isThirdAttackA = false;
            isFireBall = false;
            stateTimer = 0;
        }
        if (megaFireBall != null){
            world.destroyBody(megaFireBall);
            megaFireBall = null;
            isFourthAttackA = false;
            isFireBall = false;
            stateTimer = 0;
        }
    }

    private void createTimers() {
        firstAttackT = new Timer();

        taskFirstAttackT = new Timer.Task() {
            @Override
            public void run() {
                if (ray_b == null){
                    createRay();
                }
                destroyT.scheduleTask(taskDestroyT, 1);
                taskFirstAttackT.cancel();
            }
        };

        fireBallT = new Timer();

        taskFireBallT = new Timer.Task() {
            @Override
            public void run() {
                isFireBall = true;
                fireBallTimer = 0;
                taskFireBallT.cancel();
            }
        };

        destroyT = new Timer();

        taskDestroyT = new Timer.Task() {
            @Override
            public void run() {
                if (ray_b != null) {
                    world.destroyBody(ray_b);
                    ray_b = null;
                    isFirstAttackA = false;
                    stateTimer = 0;
                }
                if (fireBall != null){
                    world.destroyBody(fireBall);
                    fireBall = null;
                    isSecondAttackA = false;
                    isFireBall = false;
                    stateTimer = 0;
                }
                if (fastFireball != null){
                    world.destroyBody(fastFireball);
                    fastFireball = null;
                    isThirdAttackA = false;
                    isFireBall = false;
                    stateTimer = 0;
                }
                if (megaFireBall != null){
                    world.destroyBody(megaFireBall);
                    megaFireBall = null;
                    isFourthAttackA = false;
                    isFireBall = false;
                    stateTimer = 0;
                }
                nextAttack();
                taskDestroyT.cancel();
            }
        };

        secondAttackT = new Timer();

        taskSecondAttackT = new Timer.Task() {
            @Override
            public void run() {
                if (fireBall == null) {
                    createFireBall();
                }
                destroyT.scheduleTask(taskDestroyT, 2f);
                taskSecondAttackT.cancel();
            }
        };

        thirdAttackT = new Timer();

        taskThirdAttackT = new Timer.Task() {
            @Override
            public void run() {
                if (fastFireball == null) {
                    createFastFireBall();
                }
                destroyT.scheduleTask(taskDestroyT, 1f);
                taskThirdAttackT.cancel();
            }
        };

        fourthAttackT = new Timer();

        taskFourthAttackT = new Timer.Task() {
            @Override
            public void run() {
                if (megaFireBall == null) {
                    createMegaFireBall();
                }
                destroyT.scheduleTask(taskDestroyT, 1.5f);
                taskFourthAttackT.cancel();
            }
        };
    }

    private void makeFrames() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 10; i++) {
            frames.add(new TextureRegion(new Texture("boss\\firstAttack.png"), i * 220, 0, 220, 150));
        }
        firstAttackA = new Animation(0.05f, frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(new Texture("boss\\secondAttack.png"), i * 200, 0, 200, 170));
        }
        secondAttackA = new Animation(0.05f, frames);
        frames.clear();

        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(new Texture("Fly.png"), i * 180, 0, 180, 180));
        }
        flyA = new Animation(0.07f, frames);
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(new Texture("boss\\thirdAttack.png"), i * 220, 0, 220, 150));
        }
        thirdAttackA = new Animation(0.05f, frames);
        frames.clear();

        for (int i = 0; i < 9; i++) {
            frames.add(new TextureRegion(new Texture("boss\\fourthAttack.png"), i * 200, 0, 200, 170));
        }
        fourthAttackA = new Animation(0.05f, frames);
        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(new Texture("boss\\fireball.png"), i * 50, 0, 50, 50));
        }
        fireBallA = new Animation(0.05f, frames);
        frames.clear();
    }

    private TextureRegion getFrame(float delta) {
        nowFrame = getState();

        TextureRegion region;
        switch (nowFrame) {
            case FIA:
                region = (TextureRegion) firstAttackA.getKeyFrame(stateTimer, false);
                break;
            case SA:
                region = (TextureRegion) secondAttackA.getKeyFrame(stateTimer, false);
                break;
            case TA:
                region = (TextureRegion) thirdAttackA.getKeyFrame(stateTimer, false);
                break;
            case FOA:
                region = (TextureRegion) fourthAttackA.getKeyFrame(stateTimer, false);
                break;
            default:
                region = (TextureRegion) flyA.getKeyFrame(stateTimer, true);
                break;

        }
        if (pos == 1 && body.getPosition().x > 640 * Const.Unit_Scale && !region.isFlipX()) {
            region.flip(true, false);
        } else if (pos == 0 && body.getPosition().x < 640 * Const.Unit_Scale && region.isFlipX()) {
            region.flip(true, false);
        }
        if (!Const.freeze)
            stateTimer = nowFrame == previousFrame ? stateTimer + delta : 0;
        previousFrame = nowFrame;
        return region;
    }

    private State getState() {
        if (isFirstAttackA)
            return State.FIA;
        else if (isSecondAttackA)
            return State.SA;
        else if (isThirdAttackA)
            return State.TA;
        else if (isFourthAttackA)
            return State.FOA;
        else return State.FLY;
    }
}
