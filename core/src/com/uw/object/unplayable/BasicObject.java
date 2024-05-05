package com.uw.object.unplayable;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.uw.PlaceableObject;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class BasicObject extends PlaceableObject {
    private final Scene scene;

    public BasicObject(Vector3 position, SceneAsset sceneAsset) {
        this(position, new Matrix4(), sceneAsset);
    }

    public BasicObject(Vector3 position, Matrix4 transform, SceneAsset sceneAsset) {
        super(position);
        scene = new Scene(sceneAsset.scene);
        scene.modelInstance.transform.set(transform);
    }

    @Override
    public void update(float delta) {

    }

    public Scene getScene() {
        return scene;
    }
}
    