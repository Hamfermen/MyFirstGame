package com.mygdx.tns.Screens.Levels;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.tns.InteractiveItem.All_Npc.Npc;

public class Levels_Storage {
    public String levelName;
    public boolean haveEnemies;
    public String npcName;
    public Npc npc;

    public Levels_Storage(String levelName, boolean haveEnemies, String npcName){
        this.levelName = levelName;
        this.haveEnemies = haveEnemies;
        this.npcName = npcName;
    }
}
