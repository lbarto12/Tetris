package com.tetris.layout;

import com.tetris.Tetris;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private JFrame window;
    public SidePanel(){
        this.window = (JFrame) SwingUtilities.getRoot(this);
        this.init();
    }

    private JLabel scoreLab = new JLabel();
    private final JLabel scoreTitle = new JLabel("SCORE:", SwingConstants.CENTER);
    public JLabel score = new JLabel("", SwingConstants.CENTER);

    private JLabel linesLab = new JLabel();
    private final JLabel linesTitle = new JLabel("LINES:", SwingConstants.CENTER);
    public JLabel lines = new JLabel("", SwingConstants.CENTER);

    private JLabel levelLab = new JLabel();
    private final JLabel levelTitle = new JLabel("LEVEL:", SwingConstants.CENTER);
    public JLabel level = new JLabel("", SwingConstants.CENTER);

    public JLabel nextPiece = new JLabel("NEXT PIECE", SwingConstants.CENTER);

    private void init(){
        this.setOpaque(true);
        this.setBackground(Color.gray);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 5));

        this.setPreferredSize(new Dimension(100, 0));
        this.setLayout(new GridLayout(6, 1));

        this.scoreLab.setLayout(new GridLayout(2, 1));
        this.scoreLab.add(scoreTitle);
        this.scoreLab.add(score);
        this.add(scoreLab);

        this.linesLab.setLayout(new GridLayout(2, 1));
        this.linesLab.add(linesTitle);
        this.linesLab.add(lines);
        this.add(linesLab);

        this.levelLab.setLayout(new GridLayout(2, 1));
        this.levelLab.add(levelTitle);
        this.levelLab.add(level);
        this.add(levelLab);

        this.add(nextPiece);

        this.createUpdateThread();

    }

    private void createUpdateThread(){
        new Thread(() -> {
            while (true){

                this.score.setText(String.valueOf(Tetris.score));
                this.level.setText(String.valueOf(Tetris.level));
                this.lines.setText(String.valueOf(Tetris.lines));


                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
