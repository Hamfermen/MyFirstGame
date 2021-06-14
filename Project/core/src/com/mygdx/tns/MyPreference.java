package com.mygdx.tns;

import com.badlogic.gdx.Gdx;

public class MyPreference {
    public static com.badlogic.gdx.Preferences pref;

    public static com.badlogic.gdx.Preferences[] enemiesPos;

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

    public static int getLevel_number() {
        return pref.getInteger("levelnumber", 0);
    }

    public static void setLevel_number(int level_number) {
        pref.putInteger("levelnumber", level_number);
        pref.flush();
    }

    public static boolean isNewgame() {
        return pref.getBoolean("newgame", false);
    }

    public static void setNewgame(boolean newgame) {
        pref.putBoolean("newgame", newgame);
        pref.flush();
    }

    public static int getDialogPos() {
        return pref.getInteger("dialogPos", 0);
    }

    public static void setDialogPos(int dialogPos) {
        pref.putInteger("dialogPos", dialogPos);
        pref.flush();
    }

    public static String getTime() {
        return pref.getString("time", "1700");
    }

    public static void setTime(String time) {
        pref.putString("time", time);
        pref.flush();
    }

    public static int getScore(){return pref.getInteger("score", 0);}

    public static void setScore(int Score){
        pref.putInteger("score", Score);
        pref.flush();
    }

    public static float getPositionX(){return pref.getFloat("positionX", 3);}

    public static void setPositionX(float position){
        pref.putFloat("positionX", position);
        pref.flush();
    }

    public static float getPositionY(){return pref.getFloat("positionY", 3);}

    public static void setPositionY(float position){
        pref.putFloat("positionY", position);
        pref.flush();
    }

    public static int getHealth(){return pref.getInteger("health", 1);}

    public static void setHealth(int health){
        pref.putInteger("health", health);
        pref.flush();
    }

    public static String getItemNumber(){
        return pref.getString("ItemsNumbers");
    }

    public static void setItemNumber(String itemsNumber){
        pref.putString("ItemsNumbers", itemsNumber);
        pref.flush();
    }

    public static String getEnemyNumber(){
        return pref.getString("EnemiesNumbers");
    }

    public static void setEnemyNumber(String enemiesNumber){
        pref.putString("EnemiesNumbers", enemiesNumber);
        pref.flush();
    }

    public static void setEnemiesSize(int enemiesSize){
        pref.putInteger("EnemiesSize", enemiesSize);
        pref.flush();
    }

    public static int getEnemiesSize(){
        return pref.getInteger("EnemiesSize", 0);
    }

    public static void setEnemiesPos(int size){
        enemiesPos = new com.badlogic.gdx.Preferences[size];
        for (int i = 0; i < size; i++){
            enemiesPos[i] = Gdx.app.getPreferences("enemiesPos" + Integer.toString(i + 1));
        }
    }

    public static float getCameraPositionX(){return pref.getFloat("cameraPositionX", 10f);}

    public static void setCameraPositionX(float position){
        pref.putFloat("cameraPositionX", position);
        pref.flush();
    }

    public static float getCameraPositionY(){return pref.getFloat("cameraPositionY", 10f);}

    public static void setCameraPositionY(float position){
        pref.putFloat("cameraPositionY", position);
        pref.flush();
    }

    public static boolean getIsNewPreference(){return pref.getBoolean("isNewPreference", true);}

    public static void setIsNewPreference(boolean isNewPreference){
        pref.putBoolean("isNewPreference", isNewPreference);
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
