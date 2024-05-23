package com.uw.object.unplayable.terrain;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public abstract class BoundedTerrain extends Terrain {
    protected final BoundingBox bb;

    protected BoundedTerrain(Vector3 position, FileHandle modelFile) {
        super(position, modelFile);
        bb = scene.modelInstance.calculateBoundingBox(new BoundingBox()).mul(scene.modelInstance.transform);
    }
}
