package net.obviam.starassault.screens;

import java.util.Timer;
import java.util.TimerTask;

import net.obviam.starassault.controller.BobController;
import net.obviam.starassault.model.World;
import net.obviam.starassault.view.WorldRenderer;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;

public class GameScreen implements Screen, InputProcessor {

    private World world;
    private WorldRenderer renderer;
    private BobController controller;

    private int width, height;
    private Timer timer;
    protected boolean rightPressed;
    protected boolean leftPressed;
    private boolean bothPressed;

    @Override
    public void show() {
        world = new World();
        renderer = new WorldRenderer(world, false);
        controller = new BobController(world);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        Gdx.input.isTouched();

        controller.update(delta);
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }

    // * InputProcessor methods ***************************//

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.LEFT)
            controller.leftPressed();
        if (keycode == Keys.RIGHT)
            controller.rightPressed();
        if (keycode == Keys.Z)
            controller.jumpPressed();
        if (keycode == Keys.X)
            controller.firePressed();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.LEFT)
            controller.leftReleased();
        if (keycode == Keys.RIGHT)
            controller.rightReleased();
        if (keycode == Keys.Z)
            controller.jumpReleased();
        if (keycode == Keys.X)
            controller.fireReleased();
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
            return false;
        }

        System.out.println("millis: " + System.currentTimeMillis());
        System.out.println("touchDown -- x: " + x + ", y: " + y + ", pointer: " + pointer);

        //        float worldX = (float) x * (WorldRenderer.CAMERA_WIDTH / (float) width);
        //        float worldY = (float) y * (WorldRenderer.CAMERA_HEIGHT / (float) height) * -1 + WorldRenderer.CAMERA_HEIGHT;
        //        int simpleWorldX = (int) Math.floor(worldX);
        //        int simpleWorldY = (int) Math.floor(worldY);

        if (pointer == 1) {
            timer.cancel();
            controller.jumpPressed();
            bothPressed = true;
        }
        if (x < width / 2 && y > height / 2) {
            leftPressed = true;
            if (bothPressed == false) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        controller.leftPressed();
                    }
                }, 50L);
            }
        } else if (x > width / 2 && y > height / 2) {
            rightPressed = true;
            if (bothPressed == false) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        controller.rightPressed();
                    }
                }, 50L);
            }
        }

        //        if (x < width / 2 && y < height / 2) {
        //            controller.jumpPressed();
        //        }
        //        if (x > width / 2 && y < height / 2) {
        //            controller.jumpPressed();
        //        }

        //        Vector2 leftButtonPosition = world.getControls().getLeftButton().getPosition();
        //        Vector2 rightButtonPosition = world.getControls().getRightButton().getPosition();
        //        if (simpleWorldX == leftButtonPosition.x && simpleWorldY == leftButtonPosition.y) {
        //            controller.leftPressed();
        //        } else if (simpleWorldX == rightButtonPosition.x && simpleWorldY == rightButtonPosition.y) {
        //            controller.rightPressed();
        //        } else {
        //            controller.jumpPressed();
        //        }

        return true;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if (!Gdx.app.getType().equals(ApplicationType.Android)) {
            return false;
        }

        System.out.println("millis: " + System.currentTimeMillis());
        System.out.println("touchUp -- x: " + x + ", y: " + y + ", pointer: " + pointer);

        //        float worldX = (float) x * (WorldRenderer.CAMERA_WIDTH / (float) width);
        //        float worldY = (float) y * (WorldRenderer.CAMERA_HEIGHT / (float) height) * -1 + WorldRenderer.CAMERA_HEIGHT;
        //        int simpleWorldX = (int) Math.floor(worldX);
        //        int simpleWorldY = (int) Math.floor(worldY);

        if (x < width / 2 && y > height / 2) {
            leftPressed = false;
            controller.leftReleased();
        } else if (x > width / 2 && y > height / 2) {
            rightPressed = false;
            controller.rightReleased();
        }

        if (bothPressed) {
            controller.jumpReleased();
            bothPressed = false;
            if (leftPressed) {
                controller.leftPressed();
            } else if (rightPressed) {
                controller.rightPressed();
            }
        }

        //        if (x < width / 2 && y < height / 2) {
        //            controller.jumpReleased();
        //        }
        //        if (x > width / 2 && y < height / 2) {
        //            controller.jumpReleased();
        //        }

        //        Vector2 leftButtonPosition = world.getControls().getLeftButton().getPosition();
        //        Vector2 rightButtonPosition = world.getControls().getRightButton().getPosition();
        //        if (simpleWorldX == leftButtonPosition.x && simpleWorldY == leftButtonPosition.y) {
        //            controller.leftReleased();
        //        } else if (simpleWorldX == rightButtonPosition.x && simpleWorldY == rightButtonPosition.y) {
        //            controller.rightReleased();
        //        } else {
        //            controller.jumpReleased();
        //        }

        return true;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return true;
    }
}