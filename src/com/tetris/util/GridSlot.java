package com.tetris.util;

import java.awt.*;

public class GridSlot {

    public GridSlot(Vector2 pos){
        this.pos = pos;
    }



    private Color color = Color.white;

    public GridSlot setColor(Color color){
        this.color = color;
        return this;
    }

    public Color getColor(){
        return this.color;
    }


    private Vector2 pos;

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void move(Vector2 factor){
        this.pos.x += factor.x;
        this.pos.y += factor.y;
    }
}
