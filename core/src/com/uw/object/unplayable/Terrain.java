package com.uw.object.unplayable;

import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;

public abstract class Terrain extends BasicGltfObject {

    protected Terrain(Vector3 position, GLTFLoader loader) {
        super(position, loader);
    }

    public abstract float getHeight(float x, float z);
}
