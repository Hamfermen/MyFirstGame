package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.Screens.Info.Eskanor_Info;
import com.mygdx.game.Screens.Info.Merlin_Info;
import com.mygdx.game.Screens.Levels.Level_System;
import com.mygdx.game.Screens.Levels.Levels_Storage;
import com.mygdx.game.Screens.MainMenuScreen;
import com.mygdx.game.Screens.MenuScreen;
import com.mygdx.game.Screens.OptionsScreen;
import com.mygdx.game.Screens.ToBeContinuedScreen;

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

	private HashMap<String, TextureRegion> words;
	private Texture wordsTexture;
	private TextureRegion wordTexture;

	private Music music;

	private Screen screenForChange = null;

	@Override
	public void create () {
		words = new HashMap<>();
		wordsTexture = new Texture("words.png");
		createWords();
		myPreference = new MyPreference();
		//MyPreference.pref.clear();
		Const.levels.add(new Levels_Storage("Level1tmx", true, "Merlin"));
		Const.levels.add(new Levels_Storage("Level2tmx", false, "Mael"));

		optionsScreen = new OptionsScreen(this);
		mainMenuScreen = new MainMenuScreen(this);
		level_system = new Level_System( this);
		menuScreen = new MenuScreen(this);
		merlin_info = new Merlin_Info(words, this);
		eskanor_info = new Eskanor_Info(words, this);
		toBeContinuedScreen = new ToBeContinuedScreen(this);

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
