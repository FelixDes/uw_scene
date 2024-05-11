package com.uw.object.unplayable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;

import static com.uw.service.collision.CollisionPolicyStrategy.OVER;

public abstract class Terrain extends BasicObject {
    protected final Pixmap heightMap;

    protected Terrain(Vector3 position, FileHandle modelFile, Pixmap heightMap) {
        super(position, modelFile, OVER);
        this.heightMap = heightMap;
    }

    public abstract float getHeight(float x, float z);
}
