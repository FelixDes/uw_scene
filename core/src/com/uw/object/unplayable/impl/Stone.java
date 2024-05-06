package com.uw.object.unplayable.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.uw.object.unplayable.BasicGltfObject;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;

import static com.uw.ConstantsKt.STONE;

public class Stone extends BasicGltfObject {
    private Stone(Vector3 position, GLTFLoader loader) {
        super(position, loader);
    }

    public static Stone of(Vector3 position) {
        GLTFLoader loader = new GLTFLoader();
        return new Stone(position, loader);
    }

    @Override
    public FileHandle getModelFile() {
        return Gdx.files.internal(STONE);
    }
}
    