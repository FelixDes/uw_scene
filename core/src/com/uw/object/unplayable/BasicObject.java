package com.uw.object.unplayable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.uw.domain.Updatable;
import com.uw.service.collision.strategy.CollisionPolicyStrategy;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public abstract class BasicObject implements Updatable, Disposable {
    protected final CollisionPolicyStrategy collisionPolicyStrategy;
    protected final GLTFLoader loader;
    protected final Scene scene;
    protected final SceneAsset sceneAsset;

    protected BasicObject(
            Vector3 position,
            FileHandle modelFile,
            CollisionPolicyStrategy collisionPolicyStrategy
    ) {
        this(
                new Matrix4(position, new Quaternion(), new Vector3(1, 1, 1)),
                modelFile,
                collisionPolicyStrategy
        );
    }

    protected BasicObject(
            Matrix4 transform,
            FileHandle modelFile,
            CollisionPolicyStrategy collisionPolicyStrategy
    ) {
        this.collisionPolicyStrategy = collisionPolicyStrategy;
        this.loader = new GLTFLoader();
        sceneAsset = loader.load(modelFile);
        scene = new Scene(sceneAsset.scene);
        scene.modelInstance.transform.set(transform);
    }

    public Scene getScene() {
        return scene;
    }

    public CollisionPolicyStrategy getCollisionPolicyStrategy() {
        return collisionPolicyStrategy;
    }

    @Override
    public void dispose() {
        loader.dispose();
    }

    @Override
    public void update(float delta) {
    }
}
    