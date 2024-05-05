package com.uw.object.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.uw.PlaceableObject;
import com.uw.RenderUpdatable;
import com.uw.ViewAttachable;

public class BodilessPlayer extends PlaceableObject implements RenderUpdatable, ViewAttachable {
    // CAMERA
    private final PerspectiveCamera camera;
    private final FirstPersonCameraController inputProcessor;

    public static final float FOV = 90;
    public static final float NEAR = 1;
    public static final float FAR = 200;
    public static float playerHeight = 1;

    // MOVEMENT
    public float boostedSpeedMultiplier = 1.5f;
    public float forwardSpeed = 20f;
    public float sideSpeed = 10f;

    public BodilessPlayer(Vector3 position) {
        super(new Vector3(position.x, position.y + playerHeight, position.z));

        camera = new PerspectiveCamera(FOV, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(this.position);
        camera.near = NEAR;
        camera.far = FAR;

        inputProcessor = new FirstPersonCameraController(camera);
        Gdx.input.setInputProcessor(inputProcessor);
    }

    @Override
    public void update(float deltaTime) {
        var computedForwardSpeed = forwardSpeed * deltaTime;
        var computedSideSpeed = sideSpeed * deltaTime;
        var rotationVector = new Vector3(
                camera.direction.x,
                0,
                camera.direction.z
        );
        var rotationRotatedVector = new Vector3(
                -camera.direction.z,
                0,
                camera.direction.x
        );


        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            computedForwardSpeed *= boostedSpeedMultiplier;
            computedSideSpeed *= boostedSpeedMultiplier;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.mulAdd(rotationVector, computedForwardSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.mulAdd(rotationVector.cpy().scl(-1), computedForwardSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.mulAdd(rotationRotatedVector.cpy().scl(-1), computedSideSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.mulAdd(rotationRotatedVector, computedSideSpeed);
        }

        camera.position.set(position);
        inputProcessor.update();
    }

    public FirstPersonCameraController getInputProcessor() {
        return inputProcessor;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }
}
