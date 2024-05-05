package com.uw.object.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import com.uw.RenderUpdatable;

public class BodilessPlayer implements RenderUpdatable, InputProcessor {
    // CAMERA
    private final PerspectiveCamera camera;

    public static final float FOV = 90;
    public static final float NEAR = 1;
    public static final float FAR = 200;
    public static float playerHeight = 1;
    private static final float SENSITIVITY = 0.2f;
    private Vector3 rotationVector = new Vector3(0, 0, 1);
    private Vector3 rotationRotatedVector = new Vector3(-1, 0 , 0);
    private final Vector3 tmp = new Vector3();

    // MOVEMENT
    public final Vector3 position;
    public float boostedSpeedMultiplier = 1.5f;
    public float forwardSpeed = 20f;
    public float sideSpeed = 10f;

    private final IntIntMap keys = new IntIntMap();

    public BodilessPlayer(Vector3 position) {
        this.position = new Vector3(position.x, position.y + playerHeight, position.z);

        camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(this.position);
        camera.near = NEAR;
        camera.far = FAR;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        float deltaX = -Gdx.input.getDeltaX() * SENSITIVITY;
        float deltaY = -Gdx.input.getDeltaY() * SENSITIVITY;
        camera.direction.rotate(camera.up, deltaX);
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(tmp, deltaY);

        rotationVector = new Vector3(
                camera.direction.x,
                0,
                camera.direction.z
        ).nor();
        rotationRotatedVector = new Vector3(
                -camera.direction.z,
                0,
                camera.direction.x
        ).nor();
        return true;
    }

    @Override
    public void update(float deltaTime) {
        var computedForwardSpeed = forwardSpeed * deltaTime;
        var computedSideSpeed = sideSpeed * deltaTime;

        if (keys.containsKey(Input.Keys.SHIFT_LEFT)) {
            computedForwardSpeed *= boostedSpeedMultiplier;
            computedSideSpeed *= boostedSpeedMultiplier;
        }

        if (keys.containsKey(Input.Keys.W)) {
            position.mulAdd(rotationVector, computedForwardSpeed);
        }
        if (keys.containsKey(Input.Keys.S)) {
            position.mulAdd(rotationVector.cpy().scl(-1), computedForwardSpeed);
        }
        if (keys.containsKey(Input.Keys.A)) {
            position.mulAdd(rotationRotatedVector.cpy().scl(-1), computedSideSpeed);
        }
        if (keys.containsKey(Input.Keys.D)) {
            position.mulAdd(rotationRotatedVector, computedSideSpeed);
        }

        camera.position.set(position);
        camera.update(true);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
