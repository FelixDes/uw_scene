package com.uw.object.unplayable;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;
import com.uw.RenderUpdatable;
import com.uw.service.boundingbox.AddSolidManual;
import com.uw.service.boundingbox.AutoFill;
import com.uw.service.boundingbox.BoundingBoxPolicy;
import com.uw.service.boundingbox.ManualEmpty;
import com.uw.service.collision.CollisionPolicyStrategy;
import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BasicObject implements RenderUpdatable, Disposable {
    protected final CollisionPolicyStrategy collisionPolicyStrategy;
    protected final GLTFLoader loader;
    protected final Scene scene;
    protected final SceneAsset sceneAsset;
    protected final Set<BoundingBox> solidBoundingBoxes;
    protected final Set<BoundingBox> emptyBoundingBoxes;

    protected BasicObject(
            Vector3 position,
            FileHandle modelFile,
            CollisionPolicyStrategy collisionPolicyStrategy,
            BoundingBoxPolicy boundingBoxPolicy
    ) {
        this(
                new Matrix4(position, new Quaternion(), new Vector3(1, 1, 1)),
                modelFile,
                collisionPolicyStrategy,
                boundingBoxPolicy
        );
    }

    protected BasicObject(
            Matrix4 transform,
            FileHandle modelFile,
            CollisionPolicyStrategy collisionPolicyStrategy,
            BoundingBoxPolicy boundingBoxPolicy
    ) {
        this.collisionPolicyStrategy = collisionPolicyStrategy;
        this.loader = new GLTFLoader();
        sceneAsset = loader.load(modelFile);
        scene = new Scene(sceneAsset.scene);
        scene.modelInstance.transform.set(transform);

        var mi = scene.modelInstance;
        switch (boundingBoxPolicy) {
            case AutoFill ignored -> {
                solidBoundingBoxes = Set.of(mi.calculateBoundingBox(new BoundingBox()).mul(mi.transform));
                emptyBoundingBoxes = Set.of();
            }
            case ManualEmpty manualEmpty -> {
                solidBoundingBoxes = Set.of(mi.calculateBoundingBox(new BoundingBox()).mul(mi.transform));
                emptyBoundingBoxes = manualEmpty.empty().stream().map(it -> it.mul(mi.transform)).collect(Collectors.toSet());
            }
            case AddSolidManual addSolidManual -> {
                solidBoundingBoxes = new HashSet<>();
                solidBoundingBoxes.add(mi.calculateBoundingBox(new BoundingBox()).mul(mi.transform));
                for (var solid : addSolidManual.addSolid()) {
                    solidBoundingBoxes.add(solid.mul(mi.transform));
                }
                emptyBoundingBoxes = addSolidManual.empty().stream().map(it -> it.mul(mi.transform)).collect(Collectors.toSet());
            }
        }
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

    public Set<BoundingBox> getSolidBoundingBoxes() {
        return solidBoundingBoxes;
    }

    public Set<BoundingBox> getEmptyBoundingBoxes() {
        return emptyBoundingBoxes;
    }
}
    