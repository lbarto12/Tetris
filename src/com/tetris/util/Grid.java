package com.tetris.util;

import com.tetris.Tetris;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;

public class Grid extends JPanel {

    public Grid(){
        this.init();
    }

    private void init(){
        this.setBackground(Color.darkGray);
    }

    public static Vector<Shape> shapes = new Vector<>();

    private Shape currentShape;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        var yStep = Math.round((float)this.getHeight() / 20.f);
        var xStep = Math.round((float)this.getWidth() / 10.f);

        for (int i = 1; i < 20; ++i){
            // Vertical
            g.drawLine(
                    0,
                    i * yStep,
                    this.getWidth(),
                    i * yStep
            );
        }

        for (int i = 1; i < 10; ++i){
            // horizontal
            var step = Math.round((float)this.getWidth() / 10.f);
            g.drawLine(
                    i * xStep,
                    0,
                    i * xStep,
                    this.getHeight()
            );
        }

        // !Grid

        // Slots
        for (var i : shapes){
            g.setColor(i.getColor());
            for (var j : i){
                g.fillRect(
                        xStep * j.getPos().x + 1,
                        yStep * j.getPos().y + 1,
                        xStep - 1,
                        yStep - 1
                );
            }
        }

        for (var i : this.currentShape){
            g.setColor(i.getColor());
            g.fillRect(
                    xStep * i.getPos().x + 1,
                    yStep * i.getPos().y + 1,
                    xStep - 1,
                    yStep - 1
            );
        }


    }

    public void addCell(Shape shape){
        shapes.add(shape);
    }

    public void addShape(Shape shape){
        Tetris.newPiece = false;
        if (this.currentShape != null)
            shapes.add(this.currentShape);
        this.currentShape = shape;

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        for (var i : shapes){
            if (this.currentShape.overlaps(i))
                Tetris.gameOverVar = true;
        }

        Tetris.pieceSpeedDelay = 0;
    }

    public void moveCurrentShape(Vector2 factor){
        this.currentShape.move(factor);
    }

    public Shape getCurrentShape(){
        return this.currentShape;
    }

    public void updatePiece(){
        for (var i : this.currentShape){
            if (i.getPos().y >= 19){
                this.addShape(Shape.randomShape());
                return;
            }

        }
        for (var j : new Vector<>(shapes)) {
            if (j.shouldLock(currentShape)){
                this.addShape(Shape.randomShape());
                return;
            }
        }

    }

    public boolean outOfBounds(Shape shape){
        for (var i : shape){
            if (i.getPos().x < 0 || i.getPos().x > 9 || i.getPos().y > 19)
                return true;
        }
        return false;
    }

    public void rotateCurrentShapeLeft(){
        if (this.currentShape.getCenter() == null){
            return;
        }

        Vector2 center = new Vector2(this.currentShape.getCenter().getPos().x, this.currentShape.getCenter().getPos().y);

        var testShape = new Shape(this.currentShape);

        for (var i : testShape){
            Vector2 temp = new Vector2(i.getPos());
            i.setPos(new Vector2(
                    temp.y + center.x - center.y,
                    center.x + center.y - temp.x
            ));
        }
        if (!this.outOfBounds(testShape)){
            for (var i : shapes){
                if (i.overlaps(testShape)) return;
            }

            for (var i : this.currentShape){
                Vector2 temp = new Vector2(i.getPos());
                i.setPos(new Vector2(
                        temp.y + center.x - center.y,
                        center.x + center.y - temp.x
                ));
            }

        }
    }

    public void rotateCurrentShapeRight(){
        if (this.currentShape.getCenter() == null){
            return;
        }

        Vector2 center = new Vector2(this.currentShape.getCenter().getPos().x, this.currentShape.getCenter().getPos().y);

        var testShape = new Shape(this.currentShape);

        for (var i : testShape){
            Vector2 temp = new Vector2(i.getPos());
            i.setPos(new Vector2(
                    center.x + center.y - temp.y,
                    temp.x + center.y - center.x
            ));
        }
        if (!this.outOfBounds(testShape)){
            for (var i : shapes){
                if (i.overlaps(testShape)) return;
            }
            for (var i : this.currentShape){
                Vector2 temp = new Vector2(i.getPos());
                i.setPos(new Vector2(
                        center.x + center.y - temp.y,
                        temp.x + center.y - center.x
                ));
            }
        }

    }

    public void updateLines(){

        var nums = new int[20];
        for (var i : nums) i = 0;

        for (var i : shapes){
            for (var j : i){
                nums[j.getPos().y]++;
            }
        }

        ArrayList<GridSlot> toDelete = new ArrayList<>();

        for (int i = 0; i < nums.length; ++i){
            if (nums[i] == 10){

                for (var j : shapes){
                    for (var k : j){
                        if (k.getPos().y == i)
                            toDelete.add(k);
                    }
                    j.removeAll(toDelete);
                }

            }
        }

        var all = new ArrayList<Integer>();

        for (int i = 0; i < nums.length; ++i){
            if (nums[i] == 10) all.add(i);
        }

        for (var n : all){
            Tetris.updateStats();
            for (var i : shapes){
                for (var j : i){
                    if (j.getPos().y < n){
                        j.getPos().y ++;
                    }
                }
            }
        }
    }

}
