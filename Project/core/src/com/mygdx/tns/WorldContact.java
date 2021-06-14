package com.mygdx.tns;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.tns.InteractiveItem.All_Npc.Npc;
import com.mygdx.tns.InteractiveItem.Items.InteractiveItem;
import com.mygdx.tns.Unit.Unit;

public class WorldContact implements ContactListener {
    private InteractiveItem items;

    private Npc npc;

    private Unit unit;

    private World world;

    public WorldContact(InteractiveItem items, Npc npc, Unit unit, World world){
        this.items = items;
        this.npc = npc;
        this.unit = unit;
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        checkGround(fixtureA, fixtureB, true);
        checkItemInteract(fixtureA, fixtureB, true);
        checkNpcInteract(fixtureA, fixtureB, true);
        checkPlayerAttack(fixtureA, fixtureB);
        checkEnemyAttack(fixtureA, fixtureB, true);
        checkEnemiesBarrier(fixtureA, fixtureB);
        checkSplashAttack(fixtureA, fixtureB);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        checkGround(fixtureA, fixtureB, false);
        checkItemInteract(fixtureA, fixtureB, false);
        checkNpcInteract(fixtureA, fixtureB, false);
        checkEnemyCanNotAttack(fixtureA, fixtureB);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void checkGround(Fixture fixtureA, Fixture fixtureB, boolean isGrounded) {
        if (fixtureA.getUserData() == "leg" && (fixtureB.getUserData() == "Blocks" || fixtureB.getUserData() == "Platforms") || fixtureB.getUserData() == "leg" && (fixtureA.getUserData() == "Blocks" || fixtureA.getUserData() == "Platforms")) {
            unit.player.isGrounded = isGrounded;
            if (isGrounded) {
                unit.player.body.setLinearVelocity(unit.player.body.getLinearVelocity().x, 0);
            }
        }
    }

    private void checkItemInteract(Fixture fixtureA, Fixture fixtureB, boolean canInteract) {
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "ItemInteract" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "ItemInteract") {
            if (canInteract) {
                items.whoInteract = (String) (fixtureA.getUserData() == "ItemInteract" ? fixtureA.getBody().getUserData() : fixtureB.getBody().getUserData());
            }
            items.ItemInteract = canInteract;
        }
    }

    private void checkNpcInteract(Fixture fixtureA, Fixture fixtureB, boolean canInteract) {
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "NpcInteract" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "NpcInteract") {
            if (canInteract) {
                npc.whoInteract = (String) (fixtureA.getUserData() == "NpcInteract" ? fixtureA.getBody().getUserData() : fixtureB.getBody().getUserData());
            }
            npc.NpcInteract = canInteract;
        }
    }

    private void checkPlayerAttack(Fixture fixtureA, Fixture fixtureB){
        /*if ((fixtureA.getUserData() == "right" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "right" && fixtureA.getUserData() == "Enemy" )){
            unit.PlayerRight = canAttack;
            if (fixtureA.getUserData() == "right") {
                unit.isPlayerAttack = true;
                if (canAttack) unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
                else unit.whoCanNotAttack = (String) fixtureB.getBody().getUserData();
            }else {
                unit.isPlayerAttack = true;
                if (canAttack) unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
                else unit.whoCanNotAttack = (String) fixtureA.getBody().getUserData();
            }
        }
        if ((fixtureA.getUserData() == "left" && fixtureB.getUserData() == "Enemy"|| fixtureB.getUserData() == "left" && fixtureA.getUserData() == "Enemy")){
            unit.PlayerLeft = canAttack;
            if (fixtureA.getUserData() == "left") {
                unit.isPlayerAttack = true;
                if (canAttack) unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
                else unit.whoCanNotAttack = (String) fixtureB.getBody().getUserData();
            }else {
                unit.isPlayerAttack = true;
                if (canAttack) unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
                else unit.whoCanNotAttack = (String) fixtureA.getBody().getUserData();
            }
        }
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "Enemy"){
            unit.middle = canAttack;
            if (fixtureA.getUserData() == "Player") {
                unit.isPlayerAttack = true;
                if (canAttack) unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
                else unit.whoCanNotAttack = (String) fixtureB.getBody().getUserData();
            }else {
                unit.isPlayerAttack = true;
                if (canAttack) unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
                else unit.whoCanNotAttack = (String) fixtureA.getBody().getUserData();
            }
        }*/
        if (fixtureA.getUserData() == "hitbox" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "hitbox" && fixtureA.getUserData() == "Enemy"){
            if (fixtureA.getUserData() == "Enemy") {
                Const.toDestroy.add(fixtureA.getBody());
                Const.getScore++;
            } else {
                Const.toDestroy.add(fixtureB.getBody());
                Const.getScore++;
            }
        }
    }

    private void checkEnemyAttack(Fixture fixtureA, Fixture fixtureB, boolean canAttack){
        if ((fixtureA.getUserData() == "r_attackArea" && fixtureB.getUserData() == "Player" || fixtureB.getUserData() == "r_attackArea" && fixtureA.getUserData() == "Player")) {
            unit.EnemyRight = canAttack;
            if (canAttack) {
                if (fixtureA.getUserData() == "r_attackArea") {
                    unit.whoAttack = (String) fixtureA.getBody().getUserData();
                    //unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
                } else {
                    unit.whoAttack = (String) fixtureB.getBody().getUserData();
                    //unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
                }
            }else unit.whoAttack = "";
        }
        if ((fixtureA.getUserData() == "l_attackArea" && fixtureB.getUserData() == "Player" || fixtureB.getUserData() == "l_attackArea" && fixtureA.getUserData() == "Player")){
            unit.EnemyLeft = canAttack;
            if (canAttack) {
                if (fixtureA.getUserData() == "l_attackArea") {
                    unit.whoAttack = (String) fixtureA.getBody().getUserData();
                    //unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
                } else {
                    unit.whoAttack = (String) fixtureB.getBody().getUserData();
                    //unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
                }
            }else unit.whoAttack = "";
        }
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "Enemy"){
            unit.middle = canAttack;
            if (canAttack) {
                if (fixtureA.getUserData() == "Enemy") {
                    unit.whoAttack = (String) fixtureA.getBody().getUserData();
                    unit.isPlayerGetDamage = true;
                } else {
                    unit.whoAttack = (String) fixtureB.getBody().getUserData();
                    unit.isPlayerGetDamage = true;
                }
            }else unit.whoAttack = "";
        }
    }

    private void checkEnemyCanNotAttack(Fixture fixtureA, Fixture fixtureB){
        if ((fixtureA.getUserData() == "r_attackArea" && fixtureB.getUserData() == "Player" || fixtureB.getUserData() == "r_attackArea" && fixtureA.getUserData() == "Player")) {
                if (fixtureA.getUserData() == "r_attackArea") {
                    unit.whoCanNotAttack = (String) fixtureA.getBody().getUserData();
                } else {
                    unit.whoCanNotAttack = (String) fixtureB.getBody().getUserData();
                }
        }
        if ((fixtureA.getUserData() == "l_attackArea" && fixtureB.getUserData() == "Player" || fixtureB.getUserData() == "l_attackArea" && fixtureA.getUserData() == "Player")){
                if (fixtureA.getUserData() == "l_attackArea") {
                    unit.whoCanNotAttack = (String) fixtureA.getBody().getUserData();
                } else {
                    unit.whoCanNotAttack = (String) fixtureB.getBody().getUserData();
                }
        }
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "Enemy"){
                if (fixtureA.getUserData() == "Enemy") {
                    unit.whoCanNotAttack = (String) fixtureA.getBody().getUserData();
                } else {
                    unit.whoCanNotAttack = (String) fixtureB.getBody().getUserData();
                }
        }
    }

    private void checkEnemiesBarrier(Fixture fixtureA, Fixture fixtureB){
        if (fixtureA.getUserData() == "EnemiesBarrier" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "EnemiesBarrier" && fixtureA.getUserData() == "Enemy"){
            unit.whoChangeDirection.add((String) (fixtureB.getUserData() == "Enemy" ? fixtureB.getBody().getUserData() : fixtureA.getBody().getUserData()));
        }
    }

    private void checkSplashAttack(Fixture fixtureA, Fixture fixtureB){
        if ((fixtureA.getUserData() == "Splash" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "Splash" && fixtureA.getUserData() == "Enemy")){
            Const.toDestroy.add(fixtureA.getBody());
            Const.getScore++;
            Const.toDestroy.add(fixtureB.getBody());
        }
    }
}
