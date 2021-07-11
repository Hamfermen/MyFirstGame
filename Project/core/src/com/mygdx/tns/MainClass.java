package com.mygdx.tns;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.tns.Screens.DeathScreen;
import com.mygdx.tns.Screens.Info.Eskanor_Info;
import com.mygdx.tns.Screens.Info.Merlin_Info;
import com.mygdx.tns.Screens.Levels.BossLevel;
import com.mygdx.tns.Screens.Levels.Level_System;
import com.mygdx.tns.Screens.Levels.Levels_Storage;
import com.mygdx.tns.Screens.MainMenuScreen;
import com.mygdx.tns.Screens.MenuScreen;
import com.mygdx.tns.Screens.OptionsScreen;
import com.mygdx.tns.Screens.ToBeContinuedScreen;
import com.mygdx.tns.Screens.WinScreen;
import com.sun.tools.javac.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class MainClass extends Game {

	private MyPreference myPreference;

	public MainMenuScreen mainMenuScreen;
	public Level_System level_system;
	public OptionsScreen optionsScreen;
	public MenuScreen menuScreen;
	public Merlin_Info merlin_info;
	public Eskanor_Info eskanor_info;
	public ToBeContinuedScreen toBeContinuedScreen;
	public DeathScreen deathScreen;
	public BossLevel bossLevel;
	public WinScreen winScreen;

	private HashMap<String, TextureRegion> words;
	private Texture wordsTexture;
	private TextureRegion wordTexture;

	private Music music;

	private Screen screenForChange = null;

	@Override
	public void create () {

		Gdx.input.setCatchBackKey(true);

		words = new HashMap<>();
		wordsTexture = new Texture("words.png");
		createWords();
		myPreference = new MyPreference();

		//MyPreference.pref.clear();
		Const.levels.add(new Levels_Storage("Level1tmx", true, "Merlin"));
		Const.levels.add(new Levels_Storage("newLevel2", true, "Merlin"));
		Const.levels.add(new Levels_Storage("level3", true, "Mael"));

		optionsScreen = new OptionsScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		level_system = new Level_System( this);
		menuScreen = new MenuScreen(this);
		merlin_info = new Merlin_Info(words, this);
		eskanor_info = new Eskanor_Info(words, this);
		toBeContinuedScreen = new ToBeContinuedScreen(this);
		deathScreen = new DeathScreen(this);
		bossLevel = new BossLevel(this);
		winScreen = new WinScreen(this);

		if (MyPreference.getIsNewGame()) clearSaves();

		setScreen(mainMenuScreen);
	}

	@Override
	public void render () {
		super.render();
		if (screenForChange != null) {
			setScreen(screenForChange);
			screenForChange = null;
		}
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

	public void clearSaves(){
			try {
				FileHandle file = Gdx.files.local("Saves.json");
				file.delete();
				file.writeString("{\"class\":com.mygdx.tns.Saves$Save,\"health\":5,\"time\":\"1630\",\"score\":0,\"dialogPos\":0}", true);
			}catch (Exception e){
				System.out.println("Build error " + e.toString());
			}
	}

	public void ChangeScreen(Screen screenForChange){
		this.screenForChange = screenForChange;
	}

	private void createWords(){
		int x = 0, y = 0;
		for (int i = 1040; i <= 1103; i++){
			wordTexture = new TextureRegion(wordsTexture, x, y, 12, 12);
			words.put(String.valueOf((char) i), wordTexture);
			x += 12;
			if (x >= 384) {
				x = 0;
				y = 12;
			}
		}
		x = 0;
		y = 24;
		for (int i = 48; i <= 57; i++){
			wordTexture = new TextureRegion(wordsTexture, x, y, 12, 12);
			words.put(String.valueOf((char) i), wordTexture);
			x += 12;
		}
		words.put(".", new TextureRegion(wordsTexture, 120, 24, 12, 12));
		words.put(",", new TextureRegion(wordsTexture, 156, 24, 12, 12));
		words.put("-", new TextureRegion(wordsTexture, 144, 24, 12, 12));
		words.put("?", new TextureRegion(wordsTexture, 132, 24, 12, 12));
		words.put(" ", new TextureRegion(wordsTexture, 168, 24, 12, 12));
		words.put("!", new TextureRegion(wordsTexture, 180, 24, 12, 12));
	}
}
