package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Unit.Player;

import java.awt.Font;

public class UI extends Actor {

    public Vector2 pos;

    public Player player;

    private BitmapFont text_health;
    private BitmapFont text_time;
    private BitmapFont text_score;

    private WorldTime worldTime;

    public String hours, minutes;
    public String time = MyPreference.getTime();

    public UI(Vector2 pos, Player player){
        this.pos = pos;
        this.player = player;
        text_health = new BitmapFont(Gdx.files.internal("font.fnt"));
        text_health.getData().setScale(1, 1);

        text_time = new BitmapFont(Gdx.files.internal("font.fnt"));
        text_time.getData().setScale(1, 1);

        text_score = new BitmapFont(Gdx.files.internal("font.fnt"));
        text_score.getData().setScale(1, 1);

        if (Const.newGame) time = "0800";

        worldTime = new WorldTime(time);
    }

    public void UIUpdate(){
        if (!Const.freeze) worldTime.TimeUpdate();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        text_health.draw(batch, Integer.toString(player.health), 4 * Const.SizeX, 30 * Const.SizeY);
        hours = Integer.toString(worldTime.getHours()).length() == 1 ? "0" + Integer.toString(worldTime.getHours()) : Integer.toString(worldTime.getHours());
        minutes = Integer.toString(worldTime.getMinutes()).length() == 1 ? "0" + Integer.toString(worldTime.getMinutes()) : Integer.toString(worldTime.getMinutes());
        text_time.draw(batch, hours + ":" + minutes, 4 * Const.SizeX, Gdx.graphics.getHeight());
        text_score.draw(batch, Integer.toString(player.score), 20 * Const.SizeX, 30 * Const.SizeY);
    }
}
