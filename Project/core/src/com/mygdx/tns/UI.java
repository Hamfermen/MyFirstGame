package com.mygdx.tns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.tns.Unit.Player;

public class UI extends Actor {

    public Vector2 pos;

    public Player player;

    private BitmapFont text_health;
    private BitmapFont text_time;
    private BitmapFont text_score;

    public WorldTime worldTime;

    public String hours, minutes;
    public String time;

    private Texture heart, heart_b, clock, first_part, second_part, lava;
    public Sprite hour, minute;

    public UI(Vector2 pos, Player player){
        this.pos = pos;
        this.player = player;

        heart = new Texture("heart.png");
        heart_b = new Texture("heart_b.png");

        clock = new Texture("timer\\clock.png");
        hour = new Sprite(new Texture("timer\\hour.png"), 120, 120);
        minute = new Sprite(new Texture("timer\\minute.png"), 120, 120);

        hour.setPosition(1140, 580);
        minute.setPosition(1140, 580);
        minute.rotate90(true);
        hour.rotate90(true);

        first_part = new Texture("bottle\\first_part.png");
        second_part = new Texture("bottle\\second_part.png");
        lava = new Texture("bottle\\lava.png");

    }

    public void startTime(){
        worldTime = new WorldTime(time);
        minute.setRotation(90 - worldTime.getMinutes() * 6);
        hour.setRotation(worldTime.getHours() % 12 * 30 + 150 + (minute.getRotation() - 90) / 12f);
    }

    public void UIUpdate(){
        if (!Const.freeze) {
            worldTime.TimeUpdate();
            hour.rotate(- 1f / 120f);
            minute.rotate(-1f / 10f);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (int i = 0; i < 5; i++){
            if (player.health >= i + 1) batch.draw(heart, 70 * i + 80, 640, 60, 60);
            else batch.draw(heart_b, 70 * i + 80, 640, 60, 60);
        }
        hours = Integer.toString(worldTime.getHours()).length() == 1 ? "0" + Integer.toString(worldTime.getHours()) : Integer.toString(worldTime.getHours());
        minutes = Integer.toString(worldTime.getMinutes()).length() == 1 ? "0" + Integer.toString(worldTime.getMinutes()) : Integer.toString(worldTime.getMinutes());
        //text_time.draw(batch, hours + ":" + minutes, 4 * Const.SizeX, Gdx.graphics.getHeight());

        batch.draw(clock, 1140, 580, 120, 120);
        hour.draw(batch);
        minute.draw(batch);

        batch.draw(first_part, 0, 590, 80, 120);
        batch.draw(lava, 20, 600, 40, player.score);
        batch.draw(second_part, 0, 590, 80, 120);

        //text_score.draw(batch, Integer.toString(player.score), 20 * Const.SizeX, 30 * Const.SizeY);
    }
}
