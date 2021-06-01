package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class GameController extends Actor {

    public enum Direction {RIGHT, LEFT, UP, ATTACK, INTERACT, PAUSE}

    static public boolean right = false;
    static public boolean left = false;
    static public boolean up = false;
    static public boolean attack = false;
    static public boolean interact = false;
    static public boolean pause = false;

    private Texture texture;

    public GameController(final Direction direction){
        switch (direction) {
            case RIGHT:
                texture = new Texture("controller\\right.png");
                setBounds(100 * Const.SizeX, 40 * Const.SizeY, 62 * Const.SizeX, 62 * Const.SizeY);
                break;
            case UP:
                texture = new Texture("controller\\up.png");
                setBounds(1050 * Const.SizeX, 120 * Const.SizeY, 62 * Const.SizeX, 62 * Const.SizeY);
                break;
            case LEFT:
                texture = new Texture("controller\\left.png");
                setBounds(20 * Const.SizeX, 40 * Const.SizeY, 62 * Const.SizeX, 62 * Const.SizeY);
                break;
            case ATTACK:
                texture = new Texture("controller\\attack.png");
                setBounds(1100 * Const.SizeX, 20 * Const.SizeY, 120 * Const.SizeX, 100 * Const.SizeY);
                break;
            case INTERACT:
                texture = new Texture("controller\\arm.png");
                setBounds(950 * Const.SizeX, 40 * Const.SizeY, 100 * Const.SizeX, 40 * Const.SizeY);
                break;
            case PAUSE:
                texture = new Texture("controller\\pause.png");
                setBounds(1200 * Const.SizeX, 660 * Const.SizeY, 80 * Const.SizeX, 80 * Const.SizeY);
                break;
        }
        addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switch (direction) {
                    case RIGHT:
                        right = true;
                        left = false;
                        break;
                    case UP:
                        up = true;
                        break;
                    case LEFT:
                        left = true;
                        right = false;
                        break;
                    case INTERACT:
                        interact = true;
                        break;
                    case ATTACK:
                        attack = true;
                        break;
                    case PAUSE:
                        pause = true;
                        break;
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                switch (direction) {
                    case RIGHT:
                        right = false;
                        break;
                    case UP:
                        up = false;
                        break;
                    case LEFT:
                        left = false;
                        break;
                    case ATTACK:
                        attack = false;
                        break;
                    case INTERACT:
                        interact = false;
                        break;
                    case PAUSE:
                        pause = false;
                        break;

                }
            }
        });
    }

    public static void allFalse(){
        left = false;
        right = false;
        up = false;
        attack = false;
        interact = false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
