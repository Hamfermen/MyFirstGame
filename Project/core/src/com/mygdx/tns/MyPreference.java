package com.mygdx.tns;

import com.badlogic.gdx.Gdx;

public class MyPreference {
    public static com.badlogic.gdx.Preferences pref;

    public MyPreference(){
        pref = Gdx.app.getPreferences("My Preferences");
    }

    public static float getMusicValue(){
        return pref.getFloat("musicValue", 0.5f);
    }

    public static void setMusicValue(float musicValue){
        pref.putFloat("musicValue", musicValue);
        pref.flush();
    }

    public static void setIsNewPreference(boolean isNewPreference){
        pref.putBoolean("isNewPreferenc", isNewPreference);
        pref.flush();
    }

    public static int getLevel_number() {
        return pref.getInteger("levelnumber", 0);
    }

    public static void setLevel_number(int level_number) {
        pref.putInteger("levelnumber", level_number);
        pref.flush();
    }

    public static boolean getIsNewLevel(){return pref.getBoolean("isNewLevel", true);}

    public static void setIsNewLevel(boolean isNewLevel){
        pref.putBoolean("isNewLevel", isNewLevel);
        pref.flush();
    }

    public static boolean getIsNewGame(){return pref.getBoolean("isNewGame", true);}

    public static void setIsNewGame(boolean isNewGame){
        pref.putBoolean("isNewGame", isNewGame);
        pref.flush();
    }
}
