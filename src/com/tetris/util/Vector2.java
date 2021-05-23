package com.tetris.util;

public class Vector2 {
    public int x, y;

    public Vector2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 copy){
        this.x = copy.x;
        this.y = copy.y;
    }
}
