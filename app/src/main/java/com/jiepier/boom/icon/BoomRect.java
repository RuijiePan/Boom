package com.jiepier.boom.icon;

/**
 * Created by panruijiesx on 2016/11/24.
 */

public class BoomRect {

    private int x;
    private int y;
    private int height;
    private int width;

    public int getX() {
        return x;
    }

    public BoomRect setX(int x) {
        this.x = x;
        return this;
    }

    public int getY() {
        return y;
    }

    public BoomRect setY(int y) {
        this.y = y;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public BoomRect setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public BoomRect setWidth(int width) {
        this.width = width;
        return this;
    }

    public BoomRect(int x, int y, int height, int width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public BoomRect(){}
}
