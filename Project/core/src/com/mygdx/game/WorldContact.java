package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.InteractiveItem.All_Npc.Npc;
import com.mygdx.game.InteractiveItem.Items.InteractiveItem;
import com.mygdx.game.Unit.Unit;

public class WorldContact implements ContactListener {
    private InteractiveItem items;

    private Npc npc;

    private Unit unit;

    public WorldContact(InteractiveItem items, Npc npc, Unit unit){
        this.items = items;
        this.npc = npc;
        this.unit = unit;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        checkGround(fixtureA, fixtureB, true);
        checkItemInteract(fixtureA, fixtureB, true);
        checkNpcInteract(fixtureA, fixtureB, true);
        checkPlayerAttack(fixtureA, fixtureB, true);
        checkEnemyAttack(fixtureA, fixtureB, true);
        checkEnemiesBarrier(fixtureA, fixtureB);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        checkGround(fixtureA, fixtureB, false);
        checkItemInteract(fixtureA, fixtureB, false);
        checkNpcInteract(fixtureA, fixtureB, false);
        checkPlayerAttack(fixtureA, fixtureB, false);
        checkEnemyAttack(fixtureA, fixtureB, false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void checkGround(Fixture fixtureA, Fixture fixtureB, boolean isGrounded) {
        if (fixtureA.getUserData() == "leg" && fixtureB.getUserData() == "Blocks" || fixtureB.getUserData() == "leg" && fixtureA.getUserData() == "Blocks") {
            unit.player.isGrounded = isGrounded;
            if (isGrounded) unit.player.body.setLinearVelocity(unit.player.body.getLinearVelocity().x, 0);
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

    private void checkPlayerAttack(Fixture fixtureA, Fixture fixtureB, boolean canAttack){
        if ((fixtureA.getUserData() == "r_attackArea" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "r_attackArea" && fixtureA.getUserData() == "Enemy" )){
            unit.PlayerRight = canAttack;
            if (fixtureA.getUserData() == "r_attackArea") {
                unit.isPlayerAttack = true;
                unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
            }else {
                unit.isPlayerAttack = true;
                unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
            }
        }
        if ((fixtureA.getUserData() == "l_attackArea" && fixtureB.getUserData() == "Enemy"|| fixtureB.getUserData() == "l_attackArea" && fixtureA.getUserData() == "Enemy")){
            unit.PlayerLeft = canAttack;
            if (fixtureA.getUserData() == "l_attackArea") {
                unit.isPlayerAttack = true;
                unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
            }else {
                unit.isPlayerAttack = true;
                unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
            }
        }
        if (fixtureA.getUserData() == "Player" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "Player" && fixtureA.getUserData() == "Enemy"){
            unit.middle = canAttack;
            if (fixtureA.getUserData() == "Player") {
                unit.isPlayerAttack = true;
                unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
            }else {
                unit.isPlayerAttack = true;
                unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
            }
        }
    }

    private void checkEnemyAttack(Fixture fixtureA, Fixture fixtureB, boolean canAttack){
        if ((fixtureA.getUserData() == "r_attackArea" && fixtureB.getUserData() == "Player" || fixtureB.getUserData() == "r_attackArea" && fixtureA.getUserData() == "Player")) {
            unit.EnemyRight = canAttack;
            if (canAttack) {
                if (fixtureA.getUserData() == "r_attackArea") {
                    unit.whoAttack = (String) fixtureA.getBody().getUserData();
                    unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
                } else {
                    unit.whoAttack = (String) fixtureB.getBody().getUserData();
                    unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
                }
            }else unit.whoAttack = "";
        }
        if ((fixtureA.getUserData() == "l_attackArea" && fixtureB.getUserData() == "Player" || fixtureB.getUserData() == "l_attackArea" && fixtureA.getUserData() == "Player")){
            unit.EnemyLeft = canAttack;
            if (canAttack) {
                if (fixtureA.getUserData() == "l_attackArea") {
                    unit.whoAttack = (String) fixtureA.getBody().getUserData();
                    unit.whoGetDamage = (String) fixtureB.getBody().getUserData();
                } else {
                    unit.whoAttack = (String) fixtureB.getBody().getUserData();
                    unit.whoGetDamage = (String) fixtureA.getBody().getUserData();
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

    private void checkEnemiesBarrier(Fixture fixtureA, Fixture fixtureB){
        if (fixtureA.getUserData() == "EnemiesBarrier" && fixtureB.getUserData() == "Enemy" || fixtureB.getUserData() == "EnemiesBarrier" && fixtureA.getUserData() == "Enemy"){
            unit.whoChangeDirection.add((String) (fixtureB.getUserData() == "Enemy" ? fixtureB.getBody().getUserData() : fixtureA.getBody().getUserData()));
        }
    }
}
