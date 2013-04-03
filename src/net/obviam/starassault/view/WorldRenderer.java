package net.obviam.starassault.view;

import net.obviam.starassault.model.Block;
import net.obviam.starassault.model.BlueBlock;
import net.obviam.starassault.model.Bob;
import net.obviam.starassault.model.Bob.State;
import net.obviam.starassault.model.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class WorldRenderer {

    public static final float CAMERA_WIDTH = 10f;
    public static final float CAMERA_HEIGHT = 7f;
    private static final float RUNNING_FRAME_DURATION = 0.06f;

    private World world;
    private OrthographicCamera cam;

    /** for debug rendering **/
    ShapeRenderer debugRenderer = new ShapeRenderer();

    /** Textures **/
    private TextureRegion bobIdleLeft;
    private TextureRegion bobIdleRight;
    private TextureRegion blockTexture;
    private TextureRegion blockBlueTexture;
    private TextureRegion bobFrame;
    private TextureRegion bobJumpLeft;
    private TextureRegion bobFallLeft;
    private TextureRegion bobJumpRight;
    private TextureRegion bobFallRight;

    /** Animations **/
    private Animation walkLeftAnimation;
    private Animation walkRightAnimation;

    private SpriteBatch spriteBatch;
    private boolean debug = false;
    private int width;
    private int height;

    public void setSize(int w, int h) {
        this.width = w;
        this.height = h;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public WorldRenderer(World world, boolean debug) {
        this.world = world;
        this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);

        this.cam.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);

        this.cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
        this.cam.update();
        this.debug = debug;
        spriteBatch = new SpriteBatch();
        loadTextures();
    }

    private void loadTextures() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("images/textures/textures.pack"));
        bobIdleLeft = atlas.findRegion("bob-01");
        bobIdleRight = new TextureRegion(bobIdleLeft);
        bobIdleRight.flip(true, false);
        blockTexture = atlas.findRegion("block");
        blockBlueTexture = atlas.findRegion("block_blue");
        TextureRegion[] walkLeftFrames = new TextureRegion[5];
        for (int i = 0; i < 5; i++) {
            walkLeftFrames[i] = atlas.findRegion("bob-0" + (i + 2));
        }
        walkLeftAnimation = new Animation(RUNNING_FRAME_DURATION, walkLeftFrames);

        TextureRegion[] walkRightFrames = new TextureRegion[5];

        for (int i = 0; i < 5; i++) {
            walkRightFrames[i] = new TextureRegion(walkLeftFrames[i]);
            walkRightFrames[i].flip(true, false);
        }
        walkRightAnimation = new Animation(RUNNING_FRAME_DURATION, walkRightFrames);
        bobJumpLeft = atlas.findRegion("bob-up");
        bobJumpRight = new TextureRegion(bobJumpLeft);
        bobJumpRight.flip(true, false);
        bobFallLeft = atlas.findRegion("bob-down");
        bobFallRight = new TextureRegion(bobFallLeft);
        bobFallRight.flip(true, false);
    }

    public void render() {
        moveCamera(world.getBob().getPosition().x, world.getBob().getPosition().y);
        spriteBatch.setProjectionMatrix(cam.combined);
        spriteBatch.begin();
        drawBlocks();
        drawBob();
        spriteBatch.end();
        drawButtons();
        if (debug) {
            drawCollisionBlocks();
            drawDebug();
        }
    }

    private void drawBlocks() {
        for (Block block : world.getDrawableBlocks((int) CAMERA_WIDTH, (int) CAMERA_HEIGHT)) {
            if (block instanceof BlueBlock == true) {
                spriteBatch.draw(blockBlueTexture, block.getPosition().x, block.getPosition().y, Block.SIZE, Block.SIZE);
            } else {
                spriteBatch.draw(blockTexture, block.getPosition().x, block.getPosition().y, Block.SIZE, Block.SIZE);
            }
        }
    }

    private void drawBob() {
        Bob bob = world.getBob();
        bobFrame = bob.isFacingLeft() ? bobIdleLeft : bobIdleRight;
        if (bob.getState().equals(State.WALKING)) {
            bobFrame = bob.isFacingLeft() ? walkLeftAnimation.getKeyFrame(bob.getStateTime(), true) : walkRightAnimation.getKeyFrame(bob.getStateTime(), true);
        } else if (bob.getState().equals(State.JUMPING)) {
            if (bob.getVelocity().y > 0) {
                bobFrame = bob.isFacingLeft() ? bobJumpLeft : bobJumpRight;
            } else {
                bobFrame = bob.isFacingLeft() ? bobFallLeft : bobFallRight;
            }
        }
        spriteBatch.draw(bobFrame, bob.getPosition().x, bob.getPosition().y, Bob.SIZE, Bob.SIZE);
    }

    private void drawButtons() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);

        Block leftButton = world.getControls().getLeftButton();
        Rectangle rect = leftButton.getBounds();
        debugRenderer.setColor(new Color(1, 0, 0, 1));
        debugRenderer.rect(cam.position.x + (CAMERA_WIDTH / 2 - 2), cam.position.y - (CAMERA_HEIGHT / 2), rect.width, rect.height);

        Block rightButton = world.getControls().getRightButton();
        Rectangle rect2 = rightButton.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(cam.position.x + (CAMERA_WIDTH / 2 - 1), cam.position.y - (CAMERA_HEIGHT / 2), rect2.width, rect2.height);

        debugRenderer.end();
    }

    private void drawDebug() {
        // render blocks
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Line);
        for (Block block : world.getDrawableBlocks((int) CAMERA_WIDTH, (int) CAMERA_HEIGHT)) {
            Rectangle rect = block.getBounds();
            debugRenderer.setColor(new Color(1, 0, 0, 1));
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        // render Bob
        Bob bob = world.getBob();
        Rectangle rect = bob.getBounds();
        debugRenderer.setColor(new Color(0, 1, 0, 1));
        debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        debugRenderer.end();
    }

    private void drawCollisionBlocks() {
        debugRenderer.setProjectionMatrix(cam.combined);
        debugRenderer.begin(ShapeType.Filled);
        debugRenderer.setColor(new Color(1, 1, 1, 1));
        for (Rectangle rect : world.getCollisionRects()) {
            debugRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        debugRenderer.end();

    }

    public void moveCamera(float x, float y) {
        x = x < (CAMERA_WIDTH / 2f) ? CAMERA_WIDTH / 2f : x;
        y = y < (CAMERA_HEIGHT / 2f) ? CAMERA_HEIGHT / 2f : y;
        cam.position.set(x, y, 0);
        cam.update();
        if (y < 0) {
            cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);
            cam.update();
        }
    }
}