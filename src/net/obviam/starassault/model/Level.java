package net.obviam.starassault.model;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class Level {

    private int width;
    private int height;
    private Block[][] blocks;
    private BlueBlock blueBlock;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public void setBlocks(Block[][] blocks) {
        this.blocks = blocks;
    }

    public BlueBlock getBlueBlock() {
        return blueBlock;
    }

    public Level() {
        //        loadDemoLevel();
        loadVerticalLevel();
    }

    public Block get(int x, int y) {
        return blocks[x][y];
    }

    private void loadDemoLevel() {
        width = 100;
        height = 7;
        blocks = new Block[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                blocks[col][row] = null;
            }
        }

        Random random = new Random();
        boolean[] hasBlocks = { true, true };
        for (int col = 0; col < width; col++) {
            //            blocks[col][0] = new Block(new Vector2(col, 0));
            blocks[col][6] = new Block(new Vector2(col, 6));

            int groundHeight = random.nextInt(3);
            for (int row = 0; row < groundHeight && col > 2; row++) {
                blocks[col][row] = new Block(new Vector2(col, row));
            }

            if (col > 2) {
                hasBlocks[0] = blocks[col - 2][0] == null ? false : true;
                hasBlocks[1] = blocks[col - 1][0] == null ? false : true;

                if (hasBlocks[0] == false && hasBlocks[1] == false) {
                    if (blocks[col][0] == null) {
                        blocks[col][0] = new Block(new Vector2(col, 0));
                    }
                }
            }
        }
        blocks[0][1] = new Block(new Vector2(0, 1));
        blocks[0][2] = new Block(new Vector2(0, 2));
        blocks[0][3] = new Block(new Vector2(0, 3));
        blocks[0][4] = new Block(new Vector2(0, 4));
        blocks[0][5] = new Block(new Vector2(0, 5));

        blocks[0][0] = new Block(new Vector2(0, 0));
        blocks[1][0] = new Block(new Vector2(1, 0));
        blocks[2][0] = new Block(new Vector2(2, 0));
    }

    private void loadVerticalLevel() {
        width = 50;
        height = 50;
        blocks = new Block[width][height];
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                blocks[col][row] = null;
            }
        }

        Random random = new Random();
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                int frequency = random.nextInt(10) + 10;
                if (row == 0 && col < 5) {
                    blocks[col][row] = new Block(new Vector2(col, row));
                } else {
                    if (row % 2 == 0) {
                        int divisor = frequency;
                        int step = width / divisor;
                        if (col % step != 0) {
                            blocks[col][row] = new Block(new Vector2(col, row));
                        }
                    }
                }
            }
        }
        blocks[2][1] = null;
        blocks[2][2] = null;
        blocks[2][3] = null;
        blocks[2][4] = null;

        int blueBlockX = random.nextInt(30) + 20;
        int blueBlockY = random.nextInt(30) + 20;
        blueBlock = new BlueBlock(new Vector2(blueBlockY, blueBlockX));
        blocks[blueBlockY][blueBlockX] = blueBlock;
    }
}