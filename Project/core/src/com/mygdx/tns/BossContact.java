package com.mygdx.tns;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.tns.InteractiveItem.All_Npc.Npc;
import com.mygdx.tns.InteractiveItem.Items.InteractiveItem;
import com.mygdx.tns.Unit.Enemies.Boss;
import com.mygdx.tns.Unit.Unit;

public class BossContact implements ContactListener {

    private Unit unit;
    private Boss boss;

    public BossContact(Unit unit, Boss boss){
        this.unit = unit;
        this.boss = boss;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        checkRay(fixtureA, fixtureB);
        checkFireBalls(fixtureA, fixtureB);
        checkGround(fixtureA, fixtureB, true);
        checkPlayerAttack(fixtureA, fixtureB, true);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        checkGround(fixtureA, fixtureB, false);
        checkPlayerAttack(fixtureA, fixtureB, false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void checkRay(Fixture fixtureA, Fixture fixtureB){
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "ray" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "ray") {
            unit.player.health--;
        }
    }

    private void checkFireBalls(Fixture fixtureA, Fixture fixtureB){
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "fireBall" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "fireBall") {
            unit.player.health--;
        }
    }

    private void checkGround(Fixture fixtureA, Fixture fixtureB, boolean isGrounded) {
        if (fixtureA.getUserData() == "leg" && (fixtureB.getUserData() == "Blocks" || fixtureB.getUserData() == "Platforms") || fixtureB.getUserData() == "leg" && (fixtureA.getUserData() == "Blocks" || fixtureA.getUserData() == "Platforms")) {
            unit.player.isGrounded = isGrounded;
            if (isGrounded) {
                unit.player.body.setLinearVelocity(unit.player.body.getLinearVelocity().x, 0);
            }
        }
    }

    private void checkPlayerAttack(Fixture fixtureA, Fixture fixtureB, boolean getDamage){
        if (fixtureA.getUserData() == "hitbox" && fixtureB.getUserData() == "Boss" || fixtureB.getUserData() == "hitbox" && fixtureA.getUserData() == "Boss"){
            boss.getDamage  = getDamage;
        }
        if (fixtureA.getUserData() == "Splash" && fixtureB.getUserData() == "Boss" || fixtureB.getUserData() == "Splash" && fixtureA.getUserData() == "Boss"){
            if (fixtureA.getUserData() == "Splash"){
                Const.toDestroy.add(fixtureA.getBody());
                boss.health -= 30;
            }
            else {
                Const.toDestroy.add(fixtureB.getBody());
                boss.health -= 30;
            }
        }

        }

}
