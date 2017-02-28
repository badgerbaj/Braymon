package com.apsu.bjordan.braymon;

/**
 * Created by bjordan on 2/28/2017.
 */

public class Color {

    private int dark;
    private int bright;
    private int sound;
    private int colorId;
    private int ib;

    public Color() {
        this.dark = 0;
        this.bright = 0;
        this.sound = 0;
        this.colorId = 0;
        this.ib = 0;
    }

    public int getDark() {
        return dark;
    }

    public void setDark(int dark) {
        this.dark = dark;
    }

    public int getBright() {
        return bright;
    }

    public void setBright(int bright) {
        this.bright = bright;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public int getIb() {
        return ib;
    }

    public void setIb(int ib) {
        this.ib = ib;
    }
}
