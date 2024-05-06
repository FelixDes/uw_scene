package com.uw.object.unplayable.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.uw.object.unplayable.Terrain;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;


public class SandTerrain extends Terrain {
    protected SandTerrain(Vector3 position, GLTFLoader loader) {
        super(position, loader);
//        ModelBuilder mb = new ModelBuilder();
//        mb.begin();
//        mb.part("terrain", field.mesh, GL20.GL_TRIANGLES, new Material());
//        new ModelInstance()
//
//        System.out.println(this.sceneAsset.meshes);
    }

    @Override
    public float getHeight(float x, float z) {
        return 0;
    }

    @Override
    public FileHandle getModelFile() {
        return Gdx.files.internal("3d/terrain/terrain.gltf");
    }

    public static SandTerrain of(Vector3 position) {
        GLTFLoader loader = new GLTFLoader();
        return new SandTerrain(position, loader);
    }
}
    