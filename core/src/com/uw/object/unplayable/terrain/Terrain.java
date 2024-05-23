package com.uw.object.unplayable.terrain;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.uw.object.unplayable.BasicObject;
import com.uw.service.bounding.ContainsInYProjection;

import static com.uw.service.collision.strategy.CollisionPolicyStrategy.OVER;

public abstract class Terrain extends BasicObject implements ContainsInYProjection {

    protected Terrain(Vector3 position, FileHandle modelFile) {
        super(position, modelFile, OVER);
    }

    public abstract float getHeight(Vector2 position);
}
