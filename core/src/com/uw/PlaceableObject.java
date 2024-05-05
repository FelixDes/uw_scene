package com.uw;

import com.badlogic.gdx.math.Vector3;

public abstract class PlaceableObject implements RenderUpdatable {
    // MOVEMENT
    protected final Vector3 position;

    protected PlaceableObject(Vector3 position) {
        this.position = position;
    }
}
