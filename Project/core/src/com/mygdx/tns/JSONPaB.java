package com.mygdx.tns;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;

public class JSONPaB<Class> {

    private String fileName;
    private Class aClass;
    private Gson json;
    private FileHandle file;
    private Object class_j;

    public JSONPaB(String fileName, Class aClass, Object class_j) {

        json = new Gson();

        file = Gdx.files.local(fileName);

        this.class_j = class_j;

        this.aClass = aClass;

        this.fileName = fileName;
    }

    public Class parse() {

        try {

             aClass = json.fromJson( file.readString(), (java.lang.Class<Class>) class_j);

            return aClass;
        } catch (Exception e) {
            System.out.println("Parsing error " + e.toString());
        }
        return null;
    }

    public void build(Class json_o) {
        try {
            file.delete();
            file.writeString(json.toJson(json_o), true);
        }catch (Exception e){
            System.out.println("Build error " + e.toString());
        }

    }
}
