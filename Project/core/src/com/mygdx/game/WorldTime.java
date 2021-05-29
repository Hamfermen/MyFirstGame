package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;

public class WorldTime {
    private int hours = 0;
    private float minutes = 0;

    public WorldTime(String time){
        String h = time.substring(0, 2);
        hours = Integer.parseInt(h);
        String m = time.substring(2, 4);
        minutes = Float.parseFloat(m);
    }

    public void TimeUpdate(){
        minutes += 5f / 300f;
        if (minutes >= 60f) {
            hours++;
            minutes = 0;
        }
        if (hours >= 24) {
            hours = 0;
            minutes = 0;
        }
    }

    public int getHours(){
        return hours;
    }

    public int getMinutes(){
        return ((int) minutes);
    }

   /*private int getMinutes(String minutes){
        int m = 0;
        switch (minutes.charAt(1)){
            case '0':
                m += 0;
                break;
            case '1':
                m += 1;
                break;
            case '2':
                m += 2;
                break;
            case '3':
                m += 3;
                break;
            case '4':
                m += 4;
                break;
            case '5':
                m += 5;
                break;
            case '6':
                m += 6;
                break;
            case '7':
                m += 7;
                break;
            case '8':
                m += 8;
                break;
            case '9':
                m += 9;
                break;
        }
        switch (minutes.charAt(1)){
            case '0':
                m += 0;
                break;
            case '1':
                m += 1;
                break;
            case '2':
                m += 2;
                break;
            case '3':
                m += 3;
                break;
            case '4':
                m += 4;
                break;
            case '5':
                m += 5;
                break;
            case '6':
                m += 6;
                break;
            case '7':
                m += 7;
                break;
            case '8':
                m += 8;
                break;
            case '9':
                m += 9;
                break;
        }
        return m;
    }


    private int getHours(String hours){
        int h = 0;
        switch (hours.charAt(0)){
            case '0':
                h = 0 * 10;
                break;
            case '1':
                h = 0 * 10;
                break;
            case '2':
                h = 0 * 10;
                break;
        }
        switch (hours.charAt(1)){
            case '0':
                h += 0;
                break;
            case '1':
                h += 1;
                break;
            case '2':
                h += 2;
                break;
            case '3':
                h += 3;
                break;
            case '4':
                h += 4;
                break;
            case '5':
                h += 5;
                break;
            case '6':
                h += 6;
                break;
            case '7':
                h += 7;
                break;
            case '8':
                h += 8;
                break;
            case '9':
                h += 9;
                break;
        }
        return h;
    }*/
}
