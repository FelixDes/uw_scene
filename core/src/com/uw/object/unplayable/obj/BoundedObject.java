package com.uw.object.unplayable.obj;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.uw.object.unplayable.BasicObject;
import com.uw.service.bounding.Boundary;
import com.uw.service.bounding.BoundingBoxAdapter;
import com.uw.service.bounding.policy.*;
import com.uw.service.collision.strategy.CollisionPolicyStrategy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BoundedObject extends BasicObject {
    protected final Set<Boundary> solidBoundingBoxes;
    protected final Set<Boundary> emptyBoundingBoxes;

    protected BoundedObject(
            Vector3 position,
            FileHandle modelFile,
            CollisionPolicyStrategy collisionPolicyStrategy,
            BoundingPolicy boundingPolicy
    ) {
        this(
                new Matrix4(position, new Quaternion(), new Vector3(1, 1, 1)),
                modelFile,
                collisionPolicyStrategy,
                boundingPolicy
        );
    }

    protected BoundedObject(
            Matrix4 transform,
            FileHandle modelFile,
            CollisionPolicyStrategy collisionPolicyStrategy,
            BoundingPolicy boundingPolicy
    ) {
        super(transform, modelFile, collisionPolicyStrategy);

        var mi = scene.modelInstance;

        Set<Boundary> _solidBoundingBoxes;
        Set<Boundary> _emptyBoundingBoxes;

        switch (boundingPolicy) {
            case AutoFill ignored -> {
                _solidBoundingBoxes = Set.of(new BoundingBoxAdapter(mi.calculateBoundingBox(new BoundingBox())));
                _emptyBoundingBoxes = Set.of();
            }
            case ManualEmpty manualEmpty -> {
                _solidBoundingBoxes = Set.of(new BoundingBoxAdapter(mi.calculateBoundingBox(new BoundingBox())));
                _emptyBoundingBoxes = manualEmpty.empty();
            }
            case AddSolidManual addSolidManual -> {
                _solidBoundingBoxes = new HashSet<>();
                _solidBoundingBoxes.add(new BoundingBoxAdapter(mi.calculateBoundingBox(new BoundingBox())));
                _solidBoundingBoxes.addAll(addSolidManual.addSolid());
                _emptyBoundingBoxes = addSolidManual.empty();
            }
            case FullManual fullManual -> {
                _solidBoundingBoxes = fullManual.solid();
                _emptyBoundingBoxes = fullManual.empty();
            }
        }

        solidBoundingBoxes = _solidBoundingBoxes.stream().map(it -> it.mul(mi.transform)).collect(Collectors.toSet());
        emptyBoundingBoxes = _emptyBoundingBoxes.stream().map(it -> it.mul(mi.transform)).collect(Collectors.toSet());
    }


    public Set<Boundary> getSolidBoundingBoxes() {
        return solidBoundingBoxes;
    }

    public Set<Boundary> getEmptyBoundingBoxes() {
        return emptyBoundingBoxes;
    }
}
