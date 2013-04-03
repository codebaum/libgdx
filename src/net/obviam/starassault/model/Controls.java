package net.obviam.starassault.model;

import com.badlogic.gdx.math.Vector2;

public class Controls {

    Block leftButton;
    Block rightButton;

    public Controls() {
        leftButton = new Block(new Vector2(8, 0));
        rightButton = new Block(new Vector2(9, 0));
    }

    public Block getLeftButton() {
        return leftButton;
    }

    public Block getRightButton() {
        return rightButton;
    }
}
