package com.uw.object.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import com.uw.RenderUpdatable;
import com.uw.domain.Position;
import com.uw.service.WorldInteractionResolverService;

public class BodilessPlayer extends InputAdapter implements RenderUpdatable  {
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
    public final Position position;
    public float boostedSpeedMultiplier = 1.5f;
    public float forwardSpeed = 20f;
    public float sideSpeed = 10f;

    private final IntIntMap keys = new IntIntMap();

    // PROCESSING

    private final WorldInteractionResolverService moveService;

    public BodilessPlayer(Vector3 position, WorldInteractionResolverService moveService) {
        this.position = new Position(new Vector3(position.x, position.y + playerHeight, position.z));

        camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(this.position.getPos());
        camera.near = NEAR;
        camera.far = FAR;

        this.moveService = moveService;
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

        Vector3 shift = new Vector3(0,0,0);
        if (keys.containsKey(Input.Keys.W)) {
            shift.mulAdd(rotationVector, computedForwardSpeed);
        }
        if (keys.containsKey(Input.Keys.S)) {
            shift.mulAdd(rotationVector.cpy().scl(-1), computedForwardSpeed);
        }
        if (keys.containsKey(Input.Keys.A)) {
            shift.mulAdd(rotationRotatedVector.cpy().scl(-1), computedSideSpeed);
        }
        if (keys.containsKey(Input.Keys.D)) {
            shift.mulAdd(rotationRotatedVector, computedSideSpeed);
        }

        moveService.performMove(position, shift);

        camera.position.set(position.getPos());
        camera.update(true);
    }
}
