package com.mygdx.tns.Unit.Enemies;

import com.badlogic.gdx.Gdx;
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
import com.mygdx.tns.Unit.Unit;

import java.util.List;

public class Enemy extends Unit {

    public enum State {RUN, ATTACK}

    private Timer.Task task;

    private Timer timer;

    private World world;

    private List<Enemy> enemies;

    private State nowFrame;
    private State previousFrame;

    private Animation enemyRun;
    private Animation enemyAttack;

    private float stateTimer;

    public boolean runningRight = true;

    public Body body;

    public boolean canAttack = false;

    public boolean timerOff = true;

    public boolean isEnemyAttack = false;

    private boolean stopMove = false;

    public int health = 1;

    public int direction = 1;

    public int number;

    private Unit unit;

    public boolean canGetDamage = false;

    private boolean getScore = true;

    public Enemy(int number, Unit unit, float x, float y, World world, Vector2 pos, String bodyName, List<Enemy> enemies) {
        makeFrames();

        this.number = number;

        this.enemies = enemies;

        timer = new Timer();

        task = new Timer.Task() {
            @Override
            public void run() {
                isEnemyAttack = true;
                stopMove = false;
                timer.clear();
                task.cancel();
            }
        };

        CreateBody(x, y, pos, world, bodyName);
        this.world = world;

        this.setBounds(pos.x - x / 2, pos.y - y / 2, x * Const.SizeX, y * Const.SizeY);

        this.unit = unit;

        getScore = true;
    }

    private void CreateBody(float x, float y, Vector2 pos, World world, String bodyName) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true;
        def.position.set(pos);

        body = world.createBody(def);

        body.setGravityScale(0);

        PolygonShape box = new PolygonShape();
        box.setAsBox(x / 2, y / 2);
        Fixture f = body.createFixture(box, 5);
        f.setUserData("Enemy");
        f.setSensor(true);

        body.setUserData(bodyName);

        /*Filter filter = new Filter();
        filter.maskBits = Const.MASK_ENEMY;
        filter.categoryBits = Const.CATEGORY_ENEMY;
        f.setFilterData(filter);*/

        box.dispose();

        PolygonShape f_attackArea = new PolygonShape();
        f_attackArea.setAsBox(Const.playerX / 2 * Const.Unit_Scale, Const.playerY / 2 * Const.Unit_Scale, new Vector2((Const.playerX) * Const.Unit_Scale, 0), 0);
        Fixture r_attackArea = body.createFixture(f_attackArea, 0);
        r_attackArea.setUserData("r_attackArea");
        r_attackArea.setSensor(true);

        f_attackArea.setAsBox(Const.playerX / 2 * Const.Unit_Scale, Const.playerY / 2 * Const.Unit_Scale, new Vector2((-Const.playerX) * Const.Unit_Scale, 0), 0);
        Fixture l_attackArea = body.createFixture(f_attackArea, 0);
        l_attackArea.setUserData("l_attackArea");
        l_attackArea.setSensor(true);
        f_attackArea.dispose();

    }

    public void EnemyUpdate(float delta){
        if (canAttack && !task.isScheduled()) {
            stopMove = true;
            body.setLinearVelocity(0,0);
            timer.scheduleTask(task, 0.7f);
        }
        else isEnemyAttack = false;
        if (!stopMove) Move(direction * delta);
        if (health <= 0) {
            if (getScore) {
                getScore = false;
                unit.player.score += 20;
            }
            enemies.remove(this);
            world.destroyBody(body);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(getFrame(Gdx.graphics.getDeltaTime()), body.getPosition().x - 56 * Const.Unit_Scale, body.getPosition().y - 80 * Const.Unit_Scale, 56 * 2 * Const.Unit_Scale, 80 * 2 * Const.Unit_Scale);
    }

    private void Move(float direction){
        body.setLinearVelocity(100 * direction, 0);
    }

    private void makeFrames() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
            for (int i = 0; i < 5; i++)
            frames.add(new TextureRegion(new Texture("Ghost_Run.png"), i * 56, 0, 56, 80));
        enemyRun = new Animation(0.2f, frames);
        frames.clear();
        for (int i = 0; i < 6; i++)
            frames.add(new TextureRegion(new Texture("Ghost_Attack.png"), i * 56, 0, 56, 80));
        enemyAttack = new Animation(0.1f, frames);
        frames.clear();
    }

    private TextureRegion getFrame(float delta) {
        nowFrame = getState();

        TextureRegion region;
        switch (nowFrame) {
            case RUN:
                region = (TextureRegion) enemyRun.getKeyFrame(stateTimer, true);
                break;
            case ATTACK:
                region = (TextureRegion) enemyAttack.getKeyFrame(stateTimer, false);
                break;
            default:
                region = (TextureRegion) enemyRun.getKeyFrame(stateTimer, true);
                break;
        }
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        if (!Const.freeze) stateTimer = nowFrame == previousFrame ? stateTimer + delta : 0;
        previousFrame = nowFrame;
        return region;
    }

    private Enemy.State getState() {
        if (stopMove) return State.ATTACK;
        else if (body.getLinearVelocity().x != 0)
            return Enemy.State.RUN;
        else
            return State.RUN;

    }
}
