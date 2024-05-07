package com.uw.object.unplayable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector3;

public abstract class Terrain extends BasicGltfObject {
    protected final Pixmap heightMap;
    protected Terrain(Vector3 position, FileHandle modelFile, Pixmap heightMap) {
        super(position, modelFile);
        this.heightMap = heightMap;
    }

    public abstract float getHeight(float x, float z);
}
