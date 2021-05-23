package com.tetris;

import com.tetris.layout.SidePanel;
import com.tetris.util.Grid;
import com.tetris.util.GridSlot;
import com.tetris.util.Shape;
import com.tetris.util.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Tetris extends JPanel implements KeyListener {
    private final JFrame window = new JFrame();

    public Tetris(){
        this.init();
    }

    private Grid grid = new Grid();

    private void init(){
        this.window.setTitle("Tetris");
        this.window.setSize(new Dimension(454, 695));
        this.window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.window.add(this);
        this.window.setResizable(true);

        window.addKeyListener(this);

        this.setLayout(new BorderLayout());

        this.add(this.grid, BorderLayout.CENTER);
        this.add(new SidePanel(), BorderLayout.EAST);

        this.runGame();

        this.window.setVisible(true);
    }

    public static int delay = 400;
    public static int level = 1;
    public static int score = 0;
    public static int lines = 0;

    // When lines update
    public static void updateStats(){
        lines++;

        if (lines % 10 == 0 && lines != 0){
            level++;
            delay -= 20;
        }

        score += 100 * level;
    }

    public static boolean rightp = false, leftp = false, downp = false, spacep = false;

    public static int pieceSpeedDelay = 100;
    public static int holdCount = 0;

    public static boolean newPiece = false;

    public static boolean gameOverVar = false;


    private void runGame(){

        // Move Thread
        new Thread(() -> {

            grid.addShape(
                    Shape.randomShape()
            );


            while (!gameOverVar) {
                this.repaint();
                grid.updatePiece();

                if (grid.getCurrentShape() != null){
                    grid.moveCurrentShape(new Vector2(0, 1));
                }

                grid.updateLines();

                try { Thread.sleep(downp ? delay / 4 : delay); } catch (InterruptedException e) {e.printStackTrace(); }
            }

        }).start();

        new Thread(() -> {
            while (!gameOverVar){

                if (spacep){
                    newPiece = true;
                    while (newPiece){
                        this.repaint();
                        grid.updatePiece();

                        if (grid.getCurrentShape() != null){
                            grid.moveCurrentShape(new Vector2(0, 1));
                        }

                        grid.updateLines();
                    }
                    spacep = false;
                }

                try {
                    Thread.sleep(1);
                } catch (Exception ignored){}

                if (gameOverVar) {
                    this.gameOver();
                    break;
                }

            }

        }).start();

        new Thread(() -> {

            while (!gameOverVar){
                if (grid.getCurrentShape() != null && pieceSpeedDelay == 0){
                    if (leftp){
                        grid.moveCurrentShape(new Vector2(-1, 0));
                        pieceSpeedDelay = 100;
                    }
                    if (rightp) {
                        grid.moveCurrentShape(new Vector2(1, 0));
                        pieceSpeedDelay = 100;
                    }
                }

                while ((leftp || rightp) && !gameOverVar){
                    grid.updatePiece();

                    if (grid.getCurrentShape() != null && pieceSpeedDelay == 0){
                        if (leftp){
                            pieceSpeedDelay = 30;
                            grid.moveCurrentShape(new Vector2(-1, 0));
                        }
                        if (rightp) {
                            pieceSpeedDelay = 30;
                            grid.moveCurrentShape(new Vector2(1, 0));
                        }


                    }

                    if (pieceSpeedDelay > 0) pieceSpeedDelay--;

                    this.repaint();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                pieceSpeedDelay = 0;


                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void gameOver() {
        this.remove(this.grid);

        var over = new JLabel("Game Over", SwingConstants.CENTER);
        over.setFont(new Font("Calibri", Font.PLAIN, 50));
        this.add(over, BorderLayout.CENTER);
        this.window.setPreferredSize(new Dimension(
                454, 695
        ));

        this.window.pack();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        var ev = e.getExtendedKeyCode();

        if (ev == KeyEvent.VK_LEFT) leftp = true;
        if (ev == KeyEvent.VK_RIGHT) rightp = true;
        if (ev == KeyEvent.VK_DOWN) downp = true;
        if (ev == KeyEvent.VK_SPACE) spacep = true;

        if (ev == KeyEvent.VK_Z) this.grid.rotateCurrentShapeLeft();
        if (ev == KeyEvent.VK_X) this.grid.rotateCurrentShapeRight();
        this.repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        var ev = e.getExtendedKeyCode();

        if (ev == KeyEvent.VK_LEFT) leftp = false;
        if (ev == KeyEvent.VK_RIGHT) rightp = false;
        if (ev == KeyEvent.VK_DOWN) downp = false;
        if (ev == KeyEvent.VK_SPACE) spacep = false;

    }
}
