package com.tetris.util;

import com.tetris.Tetris;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class Shape extends Vector<GridSlot> {
    public Shape(GridSlot... cells){
        this.addAll(Arrays.asList(cells));
        this.color = cells[0].getColor();
    }

    public Shape(Shape copy){
        for (var i : copy){
            this.add(new GridSlot(new Vector2(i.getPos())));
        }
        this.color = copy.getColor();
        this.center = copy.getCenter();
    }

    private GridSlot center;

    public Shape setCenter(int index){
        this.center = this.get(index);
        return this;
    }

    public GridSlot getCenter() {
        return center;
    }

    private Color color;

    public Color getColor() {
        return color;
    }

    public void move(Vector2 factor){

        Shape temp = new Shape(this);
        for (var i : temp)
            i.move(factor);

        if (temp.minX() < 0 || temp.maxX() > 9) return;

        for (var i : Grid.shapes){
            if (temp.overlaps(i)){
                Tetris.pieceSpeedDelay = 0;
                return;
            }
        }

        for (var i : this)
            i.move(factor);
    }


    public int minX(){
        int min = this.get(0).getPos().x;
        for (var i : this){
            if (i.getPos().x < min) min = i.getPos().x;
        }
        return min;
    }

    public int maxX(){
        int max = this.get(0).getPos().x;
        for (var i : this){
            if (i.getPos().x > max) max = i.getPos().x;
        }
        return max;
    }

    public boolean shouldLock(Shape shape){
        for (var i : shape){
            for (var j : this){
                if (i.getPos().x == j.getPos().x && i.getPos().y == j.getPos().y - 1){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean overlaps(Shape shape){
        for (var i : shape){
            for (var j : this){
                if (i.getPos().x == j.getPos().x && i.getPos().y == j.getPos().y)
                    return true;
            }
        }
        return false;
    }


    public static Shape randomShape(){
        return switch ((int)(Math.random() * 6)){
            // LINE
            case 0 -> new Shape(
                    new GridSlot(new Vector2(4, 0)).setColor(Color.CYAN),
                    new GridSlot(new Vector2(5, 0)).setColor(Color.CYAN),
                    new GridSlot(new Vector2(6, 0)).setColor(Color.CYAN),
                    new GridSlot(new Vector2(7, 0)).setColor(Color.CYAN)
            ).setCenter(1);
            // SQUARE
            case 1 -> new Shape(
                    new GridSlot(new Vector2(5, 0)).setColor(Color.YELLOW),
                    new GridSlot(new Vector2(6, 0)).setColor(Color.YELLOW),
                    new GridSlot(new Vector2(5, 1)).setColor(Color.YELLOW),
                    new GridSlot(new Vector2(6, 1)).setColor(Color.YELLOW)
            );
            // T
            case 2 -> new Shape(
                    new GridSlot(new Vector2(5, 0)).setColor(Color.MAGENTA),
                    new GridSlot(new Vector2(6, 0)).setColor(Color.MAGENTA),
                    new GridSlot(new Vector2(7, 0)).setColor(Color.MAGENTA),
                    new GridSlot(new Vector2(6, 1)).setColor(Color.MAGENTA)
            ).setCenter(1);
            // L
            case 3 -> new Shape(
                    new GridSlot(new Vector2(5, 0)).setColor(Color.ORANGE),
                    new GridSlot(new Vector2(6, 0)).setColor(Color.ORANGE),
                    new GridSlot(new Vector2(7, 0)).setColor(Color.ORANGE),
                    new GridSlot(new Vector2(5, 1)).setColor(Color.ORANGE)
            ).setCenter(1);
            // J
            case 4 -> new Shape(
                    new GridSlot(new Vector2(5, 0)).setColor(Color.BLUE),
                    new GridSlot(new Vector2(6, 0)).setColor(Color.BLUE),
                    new GridSlot(new Vector2(7, 0)).setColor(Color.BLUE),
                    new GridSlot(new Vector2(7, 1)).setColor(Color.BLUE)
            ).setCenter(1);
            // S
            case 5 -> new Shape(
                    new GridSlot(new Vector2(5, 1)).setColor(Color.GREEN),
                    new GridSlot(new Vector2(6, 1)).setColor(Color.GREEN),
                    new GridSlot(new Vector2(6, 0)).setColor(Color.GREEN),
                    new GridSlot(new Vector2(7, 0)).setColor(Color.GREEN)
            ).setCenter(2);
            // Z
            case 6 -> new Shape(
                    new GridSlot(new Vector2(5, 0)).setColor(Color.RED),
                    new GridSlot(new Vector2(6, 0)).setColor(Color.RED),
                    new GridSlot(new Vector2(6, 1)).setColor(Color.RED),
                    new GridSlot(new Vector2(7, 1)).setColor(Color.RED)
            ).setCenter(2);

            default -> throw new IllegalStateException("Unexpected value: " + (int) Math.random() * 6);
        };
    }
}
