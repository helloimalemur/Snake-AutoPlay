package net.koonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 100;
    final int[] x = new int[(GAME_UNITS)];
    final int[] y = new int[(GAME_UNITS)];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction;
    ArrayList<Character> directions = new ArrayList<>();
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        directions.add(0,'U');
        directions.add(1,'D');
        directions.add(2,'L');
        directions.add(3,'R');
        this.setPreferredSize(new Dimension(600,600));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            randomDirection();
            System.out.println("Heading: " + direction);
            if (!checkStep()) {move();}
            //move();
            checkApple();
            checkCollision();
        } else {
            timer.stop();

        }
        repaint();

    }

    public void startGame() {
        applesEaten = 0;
        bodyParts = 6;
        newApple();
        direction = 'R';
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    @Override //https://www.bogotobogo.com/Java/tutorials/javagraphics3.php
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE); i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.blue);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

            }
        } else {
            gameOver(g);
        }

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());


    }
    public void newApple() {
        //https://youtu.be/bI6e6qjJ8JQ?t=1194
        //appleX = random.nextInt((SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE);
        //appleY = random.nextInt((SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE);
        //why does type casting effect outcome of the math here?
        appleX = random.nextInt((int) (SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
        System.out.println(appleX);
        System.out.println(appleY);
    }
    public void checkApple() {
        //check if head touches apple
        if (x[0] == appleX && y[0] == appleY) {
            applesEaten += 1;
            System.out.println("Apples Eaten: " + applesEaten);
            newApple();
            bodyParts += 1;
        }
    }

    public void checkCollision() {
        //check if head collided with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                System.out.println("!!!hit body part");

            }
        }
        //check if head touches left border
        if (x[0] < 0) {
            running = false;
            System.out.println("!!! left border");
        }
        //check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
            System.out.println("!!! right border");
        }
        //check if head touches top border
        if (y[0] < 0) {
            running = false;
            System.out.println("!!! top border");
        }
        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
            System.out.println("!!! bottom border");
        }
    }

    public void randomDirection() {
        int ran = random.nextInt(100);
        if (ran%3==0) {
            char td = directions.get(random.nextInt(directions.size()));
            if (!(td == direction)) {

                    if (!(direction == 'U' && td == 'D')) {if (!checkStep()) {direction = td;}}
                    if (!(direction == 'L' && td == 'R')) {if (!checkStep()) {direction = td;}}
                    if (!(direction == 'R' && td == 'L')) {if (!checkStep()) {direction = td;}}
                    if (!(direction == 'D' && td == 'U')) {if (!checkStep()) {direction = td;}}

            }
        }
    }

    public boolean checkStep() {
        //touches body

            if ((direction=='L')) {
                for (int i = bodyParts; i > 0; i--) {
                    if ((x[0]-1 == x[i]) && (y[0] == y[i])) {
                        return true;
                    }
                }

            }
            if ((direction=='R')) {
                for (int i = bodyParts; i > 0; i--) {
                    if ((x[0]+1 == x[i]) && (y[0] == y[i])) {
                        return true;
                    }
                }

            }
            if ((direction=='U')) {
                for (int i = bodyParts; i > 0; i--) {
                    if ((x[0] == x[i]) && (y[0]-1 == y[i])) {
                        return true;
                    }
                }

            }
            if ((direction=='D')) {
                for (int i = bodyParts; i > 0; i--) {
                    if ((x[0] == x[i]) && (y[0]+1 == y[i])) {
                        return true;
                    }
                }

            }

            //if ((x[0] == x[i]) && (y[0] == y[i])) {
            //    return true;
            //}

        //check if head touches left border
        if (x[0] < (UNIT_SIZE)) {
            if ((direction=='L')) {
                direction = 'U';
                return true;
            }
        }
        //check if head touches right border
        if ((x[0] > SCREEN_WIDTH-(UNIT_SIZE*2))) { //working
            if ((direction=='R')) {
                direction = 'D';
                return true;
            }
        }
        //check if head touches top border
        if (y[0] < (UNIT_SIZE)) {
            if ((direction=='U')) {
                direction = 'R';
                return true;
            }
        }
        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT-(UNIT_SIZE*2)) {//working
            if ((direction=='D')) {
                direction = 'L';
                return true;
            }
        }
        return false;
    }

    public void move() {
        checkStep();

        for(int i=bodyParts;i>0;i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    public void gameOver(Graphics g) {
        //game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
        g.setColor(Color.red);

        //ending score
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten))/2, g.getFont().getSize());
    }

    public class myKeyAdapter extends KeyAdapter implements KeyListener {
        @Override
        public void keyPressed(KeyEvent d) {
            //direction up
            if (d.getKeyChar() == 'w') {
                if (direction != 'D') {
                    direction='U';
                }
            }
            //direction
            if (d.getKeyChar() == 'a') {
                if (direction != 'R') {
                    direction = 'L';
                }
            }
            if (d.getKeyChar() == 's') {
                if (direction != 'U') {
                    direction = 'D';
                }
            }
            if (d.getKeyChar() == 'd') {
                if (direction != 'L') {
                    direction = 'R';
                }

            }



            if (d.getKeyChar() == 'r') {
                x[0] = random.nextInt((int) (SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
                y[0] = random.nextInt((int) (SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
                startGame();
            }
            if (d.getKeyChar() == 'z') {
                System.exit(0);
            }

        }
    }
}

