package com.example.lab7;


import java.util.ArrayList;

public class PostWeather {
    public Coord coord;
    public ArrayList<Weather> weather = new ArrayList<Weather>();
    public String base;
    public Main main;
    public Wind wind;
    public Cloouds cloouds;
    public float dt;
    public Sys sys;
    public float  timezone;
    public int id;
    public String name;
    public int code;

    public Coord getCoord() {
        return coord;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public String getBase() {
        return base;
    }

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public Cloouds getCloouds() {
        return cloouds;
    }

    public float getDt() {
        return dt;
    }

    public Sys getSys() {
        return sys;
    }

    public float getTimezone() {
        return timezone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
