package com.uw.object.unplayable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.uw.RenderUpdatable;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public abstract class BasicGltfObject implements RenderUpdatable, Disposable {
    private final GLTFLoader loader;
    protected final Scene scene;
    protected final SceneAsset sceneAsset;

    protected BasicGltfObject(Vector3 position, GLTFLoader loader) {
        this(
                new Matrix4(position, new Quaternion(), new Vector3(1, 1, 1)),
                loader
        );
    }

    protected BasicGltfObject(Matrix4 transform, GLTFLoader loader) {
        this.loader = loader;
        sceneAsset = loader.load(getModelFile());
        scene = new Scene(sceneAsset.scene);
        scene.modelInstance.transform.set(transform);
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void dispose() {
        loader.dispose();
    }

    @Override
    public void update(float delta) {
    }

    public abstract FileHandle getModelFile();
}
    