package com.uw.object.unplayable;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.uw.RenderUpdatable;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class BasicObject implements RenderUpdatable {
    private final Scene scene;

    public BasicObject(Vector3 position, SceneAsset sceneAsset) {
        this(new Matrix4(position, new Quaternion(), new Vector3(1,1 ,1)), sceneAsset);
    }

    public BasicObject(Matrix4 transform, SceneAsset sceneAsset) {
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
    